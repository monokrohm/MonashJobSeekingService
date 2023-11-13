package com.jssgui.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static java.sql.Date.valueOf;

public class ControllerSignup implements Initializable{
    @FXML
    private Button buttonSubmit;
    @FXML
    private Button buttonCancel;

    @FXML
    private ScrollPane scrollSign;

    @FXML
    private TextField tfFirst;
    @FXML
    private TextField tfLast;
    @FXML
    private TextField tfUser;
    @FXML
    private TextField tfPass;
    @FXML
    private TextField tfPassConfirm;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfDob;
    @FXML
    private TextField tfContact;
    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfCity;
    @FXML
    private TextField tfPost;
    @FXML
    private TextField tfCountry;
    @FXML
    private TextField tfEdu;
    @FXML
    private TextField tfCompletion;
    @FXML
    private TextField tfSkills;

    @FXML
    private TextField tfCompany;
    @FXML
    private TextField tfCompEmail;
    @FXML
    private TextField tfCompNo;
    @FXML
    private TextField tfCompAddress;
    @FXML
    private TextField tfCompCity;
    @FXML
    private TextField tfCompPost;
    @FXML
    private TextField tfCompCountry;

    @FXML
    private Label userError;
    @FXML
    private Label blankError;

    @FXML
    private ComboBox comboState;

    @FXML
    private ComboBox comboCompState;

    @FXML
    private DatePicker datePickerDob;

    UserInstance inst = UserInstance.getInstance();

    public void initialize(URL location, ResourceBundle resources){



        String[] states = {"Australian Capital Territory", "New South Wales", "Northern Territory", "Queensland", "South Australia", "Tasmania", "Victoria", "Western Australia"};


        User user = inst.getUser();

        if (user.getUserTypeID() == 1){
            for (String inputStates : states) {
                comboState.getItems().add(inputStates);

            }
        }
        else{
            for (String inputStates : states) {
                comboState.getItems().add(inputStates);
                comboCompState.getItems().add(inputStates);
            }
            comboCompState.getSelectionModel().selectFirst();
        }

        comboState.getSelectionModel().selectFirst();
        datePickerDob.setValue(LocalDate.now());


        tfCompletion.disableProperty().bind(tfEdu.textProperty().isEmpty());

        tfCompletion.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (newValue.intValue() > 4) {
                    tfCompletion.setText(tfCompletion.getText().substring(0,4));
                }

            }
        });

        tfCompletion.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d")) {
                    tfCompletion.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        if (user.getUserTypeID() == 1){
            buttonSubmit.disableProperty().bind(tfFirst.textProperty().isEmpty().or(tfLast.textProperty().isEmpty()).or(tfUser.textProperty().isEmpty()).or(tfPass.textProperty().isEmpty()).or(tfPassConfirm.textProperty().isEmpty()).or(tfEmail.textProperty().isEmpty()).or(tfContact.textProperty().isEmpty()).or(tfAddress.textProperty().isEmpty()).or(tfCity.textProperty().isEmpty()).or(tfPost.textProperty().isEmpty()).or(tfCountry.textProperty().isEmpty()));
        }
        else{
            buttonSubmit.disableProperty().bind(tfFirst.textProperty().isEmpty().or(tfLast.textProperty().isEmpty()).or(tfUser.textProperty().isEmpty()).or(tfPass.textProperty().isEmpty()).or(tfPassConfirm.textProperty().isEmpty()).or(tfEmail.textProperty().isEmpty()).or(tfContact.textProperty().isEmpty()).or(tfAddress.textProperty().isEmpty()).or(tfCity.textProperty().isEmpty()).or(tfPost.textProperty().isEmpty()).or(tfCountry.textProperty().isEmpty()).or(tfCompany.textProperty().isEmpty()).or(tfCompEmail.textProperty().isEmpty()).or(tfCompNo.textProperty().isEmpty()).or(tfCompAddress.textProperty().isEmpty()).or(tfCompCity.textProperty().isEmpty()).or(tfCompPost.textProperty().isEmpty()).or(tfCompCountry.textProperty().isEmpty()));
        }



        buttonSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tfPass.getText().isEmpty() && tfPass.getText().equals(tfPassConfirm.getText())) {
                    if (user.getUserTypeID() == 1){
                        Utility.signUp(event, "DashJS.fxml", user.getUserTypeID(), tfUser.getText(), tfPass.getText(), tfFirst.getText(), tfLast.getText(), tfEmail.getText(), valueOf(datePickerDob.getValue()), tfContact.getText(), tfAddress.getText(), tfCity.getText(), comboState.getSelectionModel().getSelectedItem().toString(), tfPost.getText(), tfCountry.getText(), tfEdu.getText(), tfCompletion.getText(), tfSkills.getText(), null, null, null, null, null, null, null, null);
                    }
                    else{
                        Utility.signUp(event, "DashR.fxml", user.getUserTypeID(), tfUser.getText(), tfPass.getText(), tfFirst.getText(), tfLast.getText(), tfEmail.getText(), valueOf(datePickerDob.getValue()), tfContact.getText(), tfAddress.getText(), tfCity.getText(), comboState.getSelectionModel().getSelectedItem().toString(), tfPost.getText(), tfCountry.getText(), tfEdu.getText(), tfCompletion.getText(), tfSkills.getText(), tfCompany.getText(), tfCompEmail.getText(), tfCompNo.getText(), tfCompAddress.getText(), tfCompCity.getText(), comboCompState.getSelectionModel().getSelectedItem().toString(), tfCompPost.getText(), tfCompCountry.getText());
                    }
                }
                else {
                    scrollSign.setVvalue(0);
                    blankError.setText("Passwords Do Not Match. Please Try Again");
                }
            }
        });

        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utility.changeScene(event,"Login.fxml");
            }

        });
    }
    //userError.setText("Username Already Taken. Please Try Again.");
}
