package com.jssgui.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.control.ContentDisplay.LEFT;

public class ControllerSearch implements Initializable {
    @FXML
    private ImageView imageBook;

    @FXML
    private ControllerDash controllerDash;

    @FXML
    private VBox vboxSearch;

    @FXML
    private Button buttonApply;

    @FXML
    private ToggleButton buttonBook;

    /*@FXML
    private Label labelJobName;

    @FXML
    private Label labelCompanyName;

    @FXML
    private Label labelLocation;

    @FXML
    private Label labelSalary;

    @FXML
    private Label labelDescription;*/

    @FXML
    private Separator dividerSearch;

    //**********************************************************************************
    @FXML
    private Label labelJobSalary;
    @FXML
    private Label labelJobSkills;
    @FXML
    private Label labelJobLocation;
    @FXML
    private Label labelJobCompany;

    @FXML
    private Label labelJobCreate;

    @FXML
    private Label labelJobExpiry;
    @FXML
    private TextArea tfJobContent;
    @FXML
    private TextField tfJobCompany;
    @FXML
    private TextField tfJobSalary;
    @FXML
    private TextField tfJobSkills;
    @FXML
    private TextField tfJobLocation;
    @FXML
    private TextField tfJobTitle;

    @FXML
    private TextField tfJobCreate;

    @FXML
    private TextField tfJobExpiry;

    @FXML
    private Separator dividerJobs;
    @FXML
    private Separator dividerJobs1;
    @FXML
    private Separator dividerJobsContent;

    @FXML
    private String searchInput;

    @FXML
    private String searchLocationInput;

    @FXML
    private String searchCategoryInput;

    @FXML
    private int searchSalaryInput;


    public ControllerSearch() {

    }

    UserInstance inst = UserInstance.getInstance();
    @Override
    //job search
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatementSkills = null;
        ResultSet resultSetSkills = null;

        int user_id;
        if (inst.getType() == 1)
            user_id = inst.getSeek().getUserID();
        else
            user_id = inst.getRec().getUserID();


        Button button;
        List<Button> postedJobList = new ArrayList<>();


        String searchText = searchInput;

        String[] keywordArray = searchText.split("\\s{2,8}|[,.?;'!@#%^&*/-]+"); //split string on when there are 2 to 8 spaces between characters

        ArrayList<String> keywordArrayList = new ArrayList<String>(
                Arrays.asList(keywordArray)
        );



        String searchLocation = searchLocationInput;
        String searchCategory = searchCategoryInput;
        int searchSalary  = searchSalaryInput;


        List<Integer> existingResults = new ArrayList<>();

        //iterate through each word in the search input
        for (String keyword: keywordArrayList) {
            keyword = keyword.strip();

            try {
                //creating list of parameters to look up in SQL query based on search input + combobox selections
                List<String> parameterList = new ArrayList<>();
                String queryCondition ="";

                if ( !keyword.equals("")) {
                    parameterList.add("k.keyword = '" + keyword + "'");
                }

                if (searchLocation != null) {
                    parameterList.add("jp.j_location = '" + searchLocation + "'");
                }

                if (searchCategory != null) {
                    parameterList.add("c.cat_name = '" + searchCategory + "'");
                }

                if ( searchSalary != 0) {
                    parameterList.add("jp.j_salary >= " + searchSalary);
                }

                queryCondition = String.join(" AND ", parameterList);

                if (queryCondition.isBlank()) {
                    queryCondition = "1=1"; //includes everything
                }


                preparedStatement = connection.prepareStatement("SELECT * FROM job_post jp \n" +
                        "LEFT JOIN job_skill js USING (job_id) \n" +
                        "LEFT JOIN keyword k USING (key_id)\n" +
                        "LEFT JOIN job_category jc USING (job_id)\n" +
                        "LEFT JOIN category c USING (category_id)\n" +
                        "WHERE " + queryCondition + "\n" +
//                        "GROUP BY js.job_id \n" +
                        "ORDER BY jp.j_created DESC");
//                preparedStatement.setString(1, keyword);
//                preparedStatement.setString(2, searchLocation);
//                preparedStatement.setString(3, searchCategory);
//                preparedStatement.setInt(4, searchSalary);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int job_id = resultSet.getInt("job_id");
                    String jobSkill = resultSet.getString("keyword");
                    String jobTitle = resultSet.getString("j_title");
                    String jobCompany = resultSet.getString("j_company");
                    String jobLocation = resultSet.getString("j_location");
                    String jobCategory = resultSet.getString("cat_name");
                    int jobSalary = resultSet.getInt("j_salary");
                    String jobContent = resultSet.getString("j_content");
                    Date jobCreate = resultSet.getDate("j_created");
                    Date jobExpiry = resultSet.getDate("j_expiry");
                    int jobVisibility = resultSet.getInt("j_visible");


                    //calculate total search inputs
                    double totalSearchInputs = keywordArrayList.size();
                    if(searchCategory != null) {
                        totalSearchInputs++;
                    }

                    if (jobVisibility == 0) {
                        continue;
                    }

                    if (searchLocation != null) {
                        totalSearchInputs++;
                    }

                    if (searchSalary != 0) {
                        totalSearchInputs++;
                    }

                    //calculate matches
                    double matchCount = 0;

                    if(jobCategory.equals(searchCategory)) {
                        matchCount++;
                    }
                    if(jobLocation.equals(searchLocation)) {
                        matchCount++;
                    }
                    if((jobSalary >= searchSalary) && (searchSalary != 0)) {
                        matchCount++;
                    }

                    //move on to next loop if job_id has already been included in existing search results
                    if (existingResults.contains(job_id)) {
                        continue;
                    }
                    else {
                        existingResults.add(job_id);
                    }


                    preparedStatementSkills = connection.prepareStatement("SELECT k.keyword FROM keyword k LEFT JOIN job_skill js ON js.key_id = k.key_id WHERE js.job_id = ?");
                    preparedStatementSkills.setInt(1, job_id);
                    resultSetSkills = preparedStatementSkills.executeQuery();
                    String jobSkills = "";


                    while(resultSetSkills.next()){
                        jobSkills += resultSetSkills.getString("keyword");
                        for (String matchingSkill : keywordArrayList) {
                            if (matchingSkill.strip().toLowerCase().equals(resultSetSkills.getString(("keyword")).toLowerCase())) {
                                matchCount++;
                            }
                        }

                        if (!resultSetSkills.isLast())
                            jobSkills += ", ";
                    }

                    //match is calculated as a percentage of matching search inputs (all keywords entered) and combo selections out of the total number of search inputs
                    double matchPercentage = (matchCount/totalSearchInputs)*100;

                    String jobPreview = jobTitle.substring(0, Math.min(34, jobTitle.length())) + "\n" + jobCompany + "\n" + "Match: " + (int)matchPercentage + "%";


                    button = new Button(jobPreview);
                    button.setWrapText(true);
                    button.setContentDisplay(LEFT);
                    button.setGraphicTextGap(15); //icon related settings
                    button.setPrefSize(328, 91); //button size
                    button.getStyleClass().add("button2"); //set button style from style.css
                    button.setAlignment(Pos.BASELINE_LEFT); //alignment of text/icon
                    String jobSkillsList = jobSkills;

                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            tfJobTitle.setText(jobTitle);
                            tfJobCompany.setText(jobCompany);
                            tfJobLocation.setText(jobLocation);
                            tfJobSalary.setText(String.valueOf(jobSalary));
                            tfJobContent.setText(jobContent);
                            tfJobSkills.setText(jobSkillsList.toUpperCase());
                            tfJobCreate.setText(String.format("%1$td-%1$tm-%1$tY", jobCreate));
                            tfJobExpiry.setText(String.format("%1$td-%1$tm-%1$tY", jobExpiry));

                            labelJobLocation.setVisible(true);
                            labelJobSalary.setVisible(true);
                            labelJobSkills.setVisible(true);
                            labelJobExpiry.setVisible(true);
                            labelJobCreate.setVisible(true);

                            tfJobTitle.setVisible(true);
                            tfJobContent.setVisible(true);
                            tfJobCompany.setVisible(true);
                            tfJobLocation.setVisible(true);
                            tfJobSalary.setVisible(true);
                            tfJobContent.setVisible(true);
                            tfJobSkills.setVisible(true);
                            tfJobCreate.setVisible(true);
                            tfJobExpiry.setVisible(true);


                            buttonApply.setVisible(true);
                            if (inst.getType() == 1)
                                buttonBook.setVisible(true);
                            else
                                buttonBook.setVisible(false);
                            dividerJobs.setPrefWidth(788);
                            dividerJobs1.setVisible(true);
                            dividerJobsContent.setVisible(true);

                            Connection connection = DBConnection.getConnection();
                            PreparedStatement preparedStatement = null;
                            ResultSet resultSet = null;

                            try{
                                preparedStatement = connection.prepareStatement("SELECT job_id FROM job_post WHERE j_title = ? AND j_company = ?");
                                preparedStatement.setString(1, tfJobTitle.getText());
                                preparedStatement.setString(2, tfJobCompany.getText());
                                resultSet = preparedStatement.executeQuery();

                                int job_id = 0;
                                while (resultSet.next())
                                {
                                    job_id = resultSet.getInt("job_id");
                                }

                                preparedStatement = connection.prepareStatement("SELECT * FROM job_activity WHERE user_id = ? AND job_id = ?");
                                preparedStatement.setInt(1, user_id);
                                preparedStatement.setInt(2, job_id);
                                resultSet = preparedStatement.executeQuery();

                                boolean pending = true;
                                boolean approve = true;
                                if (resultSet.isBeforeFirst()){
                                    while(resultSet.next()){
                                        pending = resultSet.getBoolean("ja_pending");
                                        approve = resultSet.getBoolean("ja_approval");
                                    }

                                    if (pending == true && approve == false || pending == false && approve == true){
                                        buttonApply.setDisable(true);
                                    }
                                    else{
                                        buttonApply.setDisable(false);
                                    }
                                }
                                else{
                                    buttonApply.setDisable(false);
                                }

                                preparedStatement = connection.prepareStatement("SELECT * FROM job_bookmark WHERE user_id = ? AND job_id = ?");
                                preparedStatement.setInt(1, user_id);
                                preparedStatement.setInt(2, job_id);
                                resultSet = preparedStatement.executeQuery();

                                if (resultSet.isBeforeFirst()){
                                    ImageView imageView = new ImageView(getClass().getResource("/icons/bookmark1.png").toExternalForm());
                                    imageView.setFitWidth(30);
                                    imageView.setFitHeight(57);
                                    imageView.setPreserveRatio(true);

                                    buttonBook.setGraphic(imageView);
                                    buttonBook.setSelected(true);
                                }
                                else{
                                    ImageView imageView = new ImageView(getClass().getResource("/icons/bookmark.png").toExternalForm());
                                    imageView.setFitWidth(30);
                                    imageView.setFitHeight(57);
                                    imageView.setPreserveRatio(true);

                                    buttonBook.setGraphic(imageView);
                                    buttonBook.setSelected(false);
                                }
                            }
                            catch (SQLException e){
                                e.printStackTrace();
                            }
                            finally {
                                if (resultSet != null){
                                    try{
                                        resultSet.close();
                                    }
                                    catch(SQLException e){
                                        e.printStackTrace();
                                    }
                                }
                                if (preparedStatement != null){
                                    try{
                                        preparedStatement.close();
                                    }
                                    catch (SQLException e){
                                        e.printStackTrace();
                                    }
                                }
                                if (connection != null){
                                    try{
                                        connection.close();
                                    }
                                    catch (SQLException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                    postedJobList.add(button);
                }

                vboxSearch.getChildren().clear();
                vboxSearch.getChildren().addAll(postedJobList);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (resultSet != null){
                    try{
                        resultSet.close();
                    }
                    catch(SQLException e){
                        e.printStackTrace();
                    }
                }
                if (resultSetSkills != null){
                    try{
                        resultSetSkills.close();
                    }
                    catch(SQLException e){
                        e.printStackTrace();
                    }
                }
                if (preparedStatement != null){
                    try{
                        preparedStatement.close();
                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                if (preparedStatementSkills != null){
                    try{
                        preparedStatementSkills.close();
                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                if (connection != null){
                    try{
                        connection.close();
                    }
                    catch (SQLException e){
                        e.printStackTrace();
                    }
                }
            }
        }

        buttonApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Connection connection = DBConnection.getConnection();
                PreparedStatement preparedStatement = null;
                ResultSet resultSet = null;

                try{
                    preparedStatement = connection.prepareStatement("SELECT job_id FROM job_post WHERE j_title = ? AND j_company = ?");
                    preparedStatement.setString(1, tfJobTitle.getText());
                    preparedStatement.setString(2, tfJobCompany.getText());
                    resultSet = preparedStatement.executeQuery();

                    int job_id = 0;
                    while (resultSet.next())
                    {
                        job_id = resultSet.getInt("job_id");
                    }

                    preparedStatement = connection.prepareStatement("INSERT INTO job_activity (user_id, job_id, apply_date, ja_pending, ja_approval) VALUES (?,?,?,?,?)");
                    preparedStatement.setInt(1, user_id);
                    preparedStatement.setInt(2, job_id);
                    preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
                    preparedStatement.setBoolean(4, true);
                    preparedStatement.setBoolean(5, false);
                    preparedStatement.executeUpdate();

                    buttonApply.setDisable(true);
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
                finally {
                    if (resultSet != null){
                        try{
                            resultSet.close();
                        }
                        catch(SQLException e){
                            e.printStackTrace();
                        }
                    }
                    if (preparedStatement != null){
                        try{
                            preparedStatement.close();
                        }
                        catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                    if (connection != null){
                        try{
                            connection.close();
                        }
                        catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        buttonBook.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Connection connection = DBConnection.getConnection();
                PreparedStatement psInsert = null;
                PreparedStatement psCheck = null;
                ResultSet resultSet = null;

                if (buttonBook.isSelected()){
                    ImageView imageView = new ImageView(getClass().getResource("/icons/bookmark1.png").toExternalForm());
                    imageView.setFitWidth(30);
                    imageView.setFitHeight(57);
                    imageView.setPreserveRatio(true);

                    buttonBook.setGraphic(imageView);

                    try{
                        psCheck = connection.prepareStatement("SELECT job_id FROM job_post  WHERE j_title = ? AND j_company = ?");
                        psCheck.setString(1, tfJobTitle.getText());
                        psCheck.setString(2, tfJobCompany.getText());
                        resultSet = psCheck.executeQuery();

                        int job_id = 0;
                        while(resultSet.next()){
                            job_id = resultSet.getInt("job_id");
                        }

                        psInsert = connection.prepareStatement("INSERT INTO job_bookmark (user_id, job_id) VALUES (?,?)");
                        psInsert.setInt(1, user_id);
                        psInsert.setInt(2, job_id);
                        psInsert.executeUpdate();
                    }
                    catch(SQLException e){
                        e.printStackTrace();
                    }
                }
                else{
                    ImageView imageView = new ImageView(getClass().getResource("/icons/bookmark.png").toExternalForm());
                    imageView.setFitWidth(30);
                    imageView.setFitHeight(57);
                    imageView.setPreserveRatio(true);

                    buttonBook.setGraphic(imageView);

                    try{
                        psCheck = connection.prepareStatement("SELECT job_id FROM job_post  WHERE j_title = ? AND j_company = ?");
                        psCheck.setString(1, tfJobTitle.getText());
                        psCheck.setString(2, tfJobCompany.getText());
                        resultSet = psCheck.executeQuery();

                        int job_id = 0;
                        while(resultSet.next()){
                            job_id = resultSet.getInt("job_id");
                        }

                        psInsert = connection.prepareStatement("DELETE FROM job_bookmark WHERE user_id = ? AND job_id = ?");
                        psInsert.setInt(1, user_id);
                        psInsert.setInt(2, job_id);
                        psInsert.executeUpdate();
                    }
                    catch(SQLException e){
                        e.printStackTrace();
                    }
                    finally {
                        if(resultSet != null){
                            try{
                                resultSet.close();
                            }
                            catch(SQLException e){
                                e.printStackTrace();
                            }
                        }
                        if (psCheck != null){
                            try{
                                psCheck.close();
                            }
                            catch (SQLException e){
                                e.printStackTrace();
                            }
                        }
                        if (psInsert != null){
                            try{
                                psInsert.close();
                            }
                            catch (SQLException e){
                                e.printStackTrace();
                            }
                        }
                        if (connection != null){
                            try{
                                connection.close();
                            }
                            catch (SQLException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }

    public void setSearchLocationInput(String searchLocationInput) {
        this.searchLocationInput = searchLocationInput;
    }

    public void setSearchCategoryInput(String searchCategoryInput) {
        this.searchCategoryInput = searchCategoryInput;
    }

    public void setSearchSalaryInput(int searchSalaryInput) {
        this.searchSalaryInput = searchSalaryInput;
    }
}
