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
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.control.ContentDisplay.LEFT;

public class ControllerJobBookmarks implements Initializable {
    @FXML
    private VBox vboxJobs;

    @FXML
    private Button buttonApply;

    @FXML
    private Label labelJobSalary;
    @FXML
    private Label labelJobSkills;
    @FXML
    private Label labelJobLocation;
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
    private Separator dividerJobs;
    @FXML
    private Separator dividerJobs1;
    @FXML
    private Separator dividerJobsContent;

    UserInstance inst = UserInstance.getInstance();

    public void initialize(URL location, ResourceBundle resources) {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatementID = null;
        ResultSet resultSetID = null;
        PreparedStatement preparedStatementSkills = null;
        ResultSet resultSetSkills = null;

        Button button;
        List<Button> jobsList = new ArrayList<>();

        int user_id;
        if (inst.getType() == 1)
            user_id = inst.getSeek().getUserID();
        else
            user_id = inst.getRec().getUserID();

        try{
            preparedStatementID = connection.prepareStatement("SELECT * FROM job_bookmark WHERE user_id = ? ");
            preparedStatementID.setInt(1, user_id);
            resultSetID = preparedStatementID.executeQuery();

            int job_id = 0;
            while(resultSetID.next()){
                job_id = resultSetID.getInt("job_id");
                preparedStatement = connection.prepareStatement("SELECT * FROM job_post WHERE job_id = ? ORDER BY j_created DESC ");
                preparedStatement.setInt(1, job_id);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()){
                    String jobTitle = resultSet.getString("j_title");
                    String jobCompany = resultSet.getString("j_company");
                    String jobContent = resultSet.getString("j_content");
                    String jobState = resultSet.getString("j_location");
                    int jobSalary = resultSet.getInt("j_salary");
                    boolean jobPublished = resultSet.getBoolean("j_visible");
                    String jobPreview = jobTitle.substring(0, Math.min(34, jobTitle.length())) + "\n" + jobCompany + "\n" + jobContent.substring(0, Math.min(100, jobContent.length()));


                    preparedStatementSkills = connection.prepareStatement("SELECT k.keyword FROM keyword k LEFT JOIN job_skill js ON js.key_id = k.key_id WHERE js.job_id = ?");
                    preparedStatementSkills.setInt(1, job_id);
                    resultSetSkills = preparedStatementSkills.executeQuery();
                    String jobSkills = "";

                    while(resultSetSkills.next()){
                        jobSkills += resultSetSkills.getString("keyword");
                        if (!resultSetSkills.isLast())
                            jobSkills += ", ";
                    }


                    ImageView imageView = new ImageView(getClass().getResource("/icons/user-profile-avatar-icon-134114304.png").toExternalForm());
                    imageView.setFitWidth(51);
                    imageView.setFitHeight(59);
                    imageView.setPreserveRatio(true);

                    button = new Button(jobPreview);
                    button.setWrapText(true);
                    button.setGraphic(imageView);
                    button.setContentDisplay(LEFT);
                    button.setGraphicTextGap(15);
                    button.setPrefSize(328, 91);
                    button.getStyleClass().add("button2");
                    button.setAlignment(Pos.BASELINE_LEFT);
                    String jobSkillsList = jobSkills;
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            tfJobTitle.setText(jobTitle);
                            tfJobCompany.setText(jobCompany);
                            tfJobSalary.setText(String.valueOf(jobSalary));
                            tfJobSkills.setText(jobSkillsList.toUpperCase());
                            tfJobLocation.setText(jobState);
                            tfJobContent.setText(jobContent);

                            tfJobTitle.setVisible(true);
                            tfJobCompany.setVisible(true);
                            tfJobSalary.setVisible(true);
                            tfJobSkills.setVisible(true);
                            tfJobLocation.setVisible(true);

                            labelJobSalary.setVisible(true);
                            labelJobSkills.setVisible(true);
                            labelJobLocation.setVisible(true);

                            buttonApply.setVisible(true);

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
                            }
                            catch (SQLException e){
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
                    jobsList.add(button);
                }
            }
            vboxJobs.getChildren().clear();
            vboxJobs.getChildren().addAll(jobsList);
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
            if(resultSetID != null){
                try{
                    resultSetID.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if (resultSetSkills != null){
                try{
                    resultSetSkills.close();
                }
                catch (SQLException e){
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
            if (preparedStatementID != null){
                try{
                    preparedStatementID.close();
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
                    if(resultSet != null){
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
    }
}
