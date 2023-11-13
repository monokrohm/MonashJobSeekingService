package com.jssgui.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.scene.control.ContentDisplay.LEFT;

public class ControllerJobs implements Initializable {
    @FXML
    private VBox vboxJobs;

    @FXML
    private Button buttonNewJob;
    @FXML
    private Button buttonPublish;
    @FXML
    private Button buttonUpdateJob;

    @FXML
    private Label labelJobSalary;
    @FXML
    private Label labelJobSkills;
    @FXML
    private Label labelJobLocation;
    @FXML
    private Label labelUpdate;
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

    private String currentTitle = "";

    UserInstance inst = UserInstance.getInstance();

    public ControllerJobs(){};

    public void initialize(URL location, ResourceBundle resources) {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatementSkills = null;
        ResultSet resultSetSkills = null;

        Button button;
        List<Button> jobsList = new ArrayList<>();

        int user_id;
        if (inst.getType() == 1)
            user_id = inst.getSeek().getUserID();
        else
            user_id = inst.getRec().getUserID();

        tfJobSalary.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d")) {
                    tfJobSalary.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        try{
            preparedStatement = connection.prepareStatement("SELECT * FROM job_post j LEFT JOIN company_post cp ON cp.job_id = j.job_id LEFT JOIN company c ON c.company_id = cp.company_id LEFT JOIN company_involvement ci ON ci.company_id = c.company_id  WHERE ci.user_id = ? ORDER BY j.job_id DESC "); //LEFT JOIN user u ON u.user_id = ci.user_id
            preparedStatement.setInt(1, user_id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int job_id = resultSet.getInt("job_id");
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

                        buttonUpdateJob.setVisible(true);
                        buttonPublish.setVisible(true);

                        if (jobPublished == true)
                        {
                            buttonPublish.setText("Unpublish");
                            buttonUpdateJob.setDisable(true);
                        }
                        else {
                            buttonPublish.setText("Publish");
                            buttonUpdateJob.setDisable(false);
                        }

                        dividerJobs.setPrefWidth(651);
                        dividerJobs1.setVisible(true);
                        dividerJobsContent.setVisible(true);

                        setCurrentTitle(tfJobTitle.getText());
                    }
                });
                jobsList.add(button);

            }

            vboxJobs.getChildren().clear();
            vboxJobs.getChildren().addAll(jobsList);
        }
        catch(SQLException e){
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

        buttonNewJob.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stg = new Stage();
                stg.initModality(Modality.APPLICATION_MODAL);
                //stg.initStyle(StageStyle.UNDECORATED);
                String companyName = inst.getRec().getCompanyName();
                String jobLocation = inst.getRec().getCompanyState();

                try{
                    stg.setResizable(false);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("JobsCreate.fxml"));
                    Scene scene = new Scene(loader.load());
                    stg.setScene(scene);
                    ControllerJobsCreate controllerJobsCreate = loader.getController();
                    controllerJobsCreate.setTfJobLocation(jobLocation);
                    controllerJobsCreate.setTfCompanyName(companyName);
                    stg.show();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

        buttonPublish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int user_id;
                if (inst.getType() == 1)
                    user_id = inst.getSeek().getUserID();
                else
                    user_id = inst.getRec().getUserID();

                if (buttonPublish.getText().equals("Publish"))
                {
                    UtilityUpdate.jobVisibilityUp(user_id, tfJobTitle.getText(), true);

                    buttonPublish.setText("Unpublish");
                    buttonUpdateJob.setDisable(true);
                }
                else {
                    UtilityUpdate.jobVisibilityUp(user_id, tfJobTitle.getText(), false);

                    buttonPublish.setText("Publish");
                    buttonUpdateJob.setDisable(false);
                }

            }
        });

        buttonUpdateJob.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                UtilityUpdate.jobUp(tfJobTitle.getText(), tfJobLocation.getText(), tfJobSalary.getText(), tfJobContent.getText(), user_id, getCurrentTitle());
                UtilityUpdate.skillJobUp(user_id, tfJobSkills.getText(), getCurrentTitle());//update display text, refresh fxml ?
                tfJobContent.setScrollTop(0);
                labelUpdate.setText("Updated Successfully.");
                setCurrentTitle(tfJobTitle.getText());
            }
        });
    }

    public String getCurrentTitle() {
        return currentTitle;
    }

    public void setCurrentTitle(String currentTitle) {
        this.currentTitle = currentTitle;
    }
}