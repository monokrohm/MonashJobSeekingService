package com.jssgui.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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

public class ControllerApplications implements Initializable {
    @FXML
    private VBox vboxJobs;
    @FXML
    private BorderPane borderApp;

    @FXML
    private Button buttonCandidates;

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
    private Separator dividerJobs1;
    @FXML
    private Separator dividerJobsContent;

    UserInstance inst = UserInstance.getInstance();

    public void initialize(URL location, ResourceBundle resources) {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement psCheck = null;
        ResultSet resultSetCheck = null;
        PreparedStatement preparedStatementSkills = null;
        ResultSet resultSetSkills = null;

        Button button;
        List<Button> jobsList = new ArrayList<>();

        int user_id;
        if (inst.getType() == 1) {
            user_id = inst.getSeek().getUserID();
            buttonCandidates.setVisible(false);
        }
        else {
            user_id = inst.getRec().getUserID();
            buttonCandidates.setVisible(true);
        }

        try{
            preparedStatement = connection.prepareStatement("SELECT * FROM job_post j LEFT JOIN job_activity ja ON ja.job_id = j.job_id WHERE ja.user_id = ? AND j.j_visible = 1 ORDER BY ja.apply_date DESC ");
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

                psCheck = connection.prepareStatement("SELECT * FROM job_activity WHERE user_id = ? AND job_id = ?");
                psCheck.setInt(1, user_id);
                psCheck.setInt(2, job_id);
                resultSetCheck = psCheck.executeQuery();

                Boolean ja_pending = null;
                Boolean ja_approval = null;
                String pending = "";
                while(resultSetCheck.next()){
                    ja_pending = resultSetCheck.getBoolean("ja_pending");
                    ja_approval = resultSetCheck.getBoolean("ja_approval");
                }

                String jobPreview = "";
                if (ja_pending) {
                    jobPreview = jobTitle.substring(0, Math.min(34, jobTitle.length())) + "\n" + jobCompany + "\n" + jobContent.substring(0, Math.min(100, jobContent.length()));
                }
                else{
                    if (ja_approval)
                        jobPreview = jobTitle.substring(0, Math.min(34, jobTitle.length())) + "\n" + jobCompany + "\n" + "<Invitation Received>";
                    else
                        jobPreview = jobTitle.substring(0, Math.min(34, jobTitle.length())) + "\n" + jobCompany + "\n" + "<Rejected>";;
                }

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

                        dividerJobs1.setVisible(true);
                        dividerJobsContent.setVisible(true);
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
            if (resultSetCheck != null){
                try{
                    resultSetCheck.close();
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
            if (psCheck != null){
                try{
                    psCheck.close();
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

        buttonCandidates.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    BorderPane dash = (BorderPane) borderApp.getParent().getScene().getRoot();
                    dash.setCenter(FXMLLoader.load(Utility.class.getResource("Candidates.fxml")));
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
