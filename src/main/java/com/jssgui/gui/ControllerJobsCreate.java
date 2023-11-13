package com.jssgui.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static java.sql.Date.valueOf;

public class ControllerJobsCreate implements Initializable {
    @FXML
    private TextArea tfJobContent;
    @FXML
    private TextField tfJobCategory;
    @FXML
    private TextField tfCompanyName;
    @FXML
    private TextField tfJobTitle;
    @FXML
    private TextField tfJobSalary;
    @FXML
    private TextField tfJobSkills;
    @FXML
    private TextField tfJobLocation;


    @FXML
    private Button buttonJobCancel;
    @FXML
    private Button buttonJobPublish;
    @FXML
    private Button buttonJobSave;

    UserInstance inst = UserInstance.getInstance();

    public void initialize(URL url, ResourceBundle resources) {
        buttonJobPublish.disableProperty().bind(tfJobContent.textProperty().isEmpty().or(tfCompanyName.textProperty().isEmpty().or(tfJobTitle.textProperty().isEmpty().or(tfJobLocation.textProperty().isEmpty().or(tfJobSalary.textProperty().isEmpty().or(tfJobSkills.textProperty().isEmpty()))))));
        buttonJobSave.disableProperty().bind(tfJobContent.textProperty().isEmpty().or(tfCompanyName.textProperty().isEmpty().or(tfJobTitle.textProperty().isEmpty().or(tfJobLocation.textProperty().isEmpty().or(tfJobSalary.textProperty().isEmpty().or(tfJobSkills.textProperty().isEmpty()))))));

        int user_id;
        if (inst.getType() == 1){
            user_id = inst.getSeek().getUserID();
        }
        else{
            user_id = inst.getRec().getUserID();
        }

        tfJobSalary.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d")) {
                    tfJobSalary.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        buttonJobCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ((Stage) buttonJobCancel.getScene().getWindow()).close();
            }
        });

        buttonJobPublish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                UtilityJobs.createJob(user_id, tfJobTitle.getText(), tfCompanyName.getText(), tfJobLocation.getText(), Integer.parseInt(tfJobSalary.getText()), tfJobContent.getText(), true, tfJobSkills.getText(), tfJobCategory.getText());
                ((Stage) buttonJobCancel.getScene().getWindow()).close();
            }
        });

        buttonJobSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                UtilityJobs.createJob(user_id, tfJobTitle.getText(), tfCompanyName.getText(), tfJobLocation.getText(), Integer.parseInt(tfJobSalary.getText()), tfJobContent.getText(), false, tfJobSkills.getText(), tfJobCategory.getText());
                ((Stage) buttonJobCancel.getScene().getWindow()).close();
            }
        });
    }

    public void setTfCompanyName(String companyName){
        tfCompanyName.setText(companyName);
    }
    public void setTfJobLocation(String jobLocation){
        tfJobLocation.setText(jobLocation);
    }
}
