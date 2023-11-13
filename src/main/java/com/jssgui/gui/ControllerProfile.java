package com.jssgui.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerProfile implements Initializable{
    @FXML
    private Button buttonCompany;
    @FXML
    private Button buttonUpdate;

    @FXML
    private ScrollPane scrollProfile;

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
    private TextField tfState;
    @FXML
    private TextField tfPost;
    @FXML
    private TextField tfCountry;
    @FXML
    private TextField tfEdu;
    @FXML
    private TextField tfGraduation;
    @FXML
    private TextField tfSkills;

    @FXML
    private Label labelPassMatch;
    @FXML
    private Label labelUpdate;

    @FXML
    private ControllerDash controllerDash;

    @FXML
    private ComboBox comboState;

    UserInstance inst = UserInstance.getInstance();
    public void initialize(URL location, ResourceBundle resources){

        String[] states = {"Australian Capital Territory", "New South Wales", "Northern Territory", "Queensland", "South Australia", "Tasmania", "Victoria", "Western Australia"};

        for (String inputStates : states) {
            comboState.getItems().add(inputStates);
        }

        tfGraduation.disableProperty().bind(tfEdu.textProperty().isEmpty());

        tfGraduation.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (newValue.intValue() > 4) {
                    tfGraduation.setText(tfGraduation.getText().substring(0,4));
                }
            }
        });

        tfGraduation.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!(newValue == null) && !newValue.matches("\\d")) {
                    tfGraduation.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        if (inst.getType() == 1){
            buttonCompany.setVisible(false);

            tfFirst.setText(inst.getSeek().getFirstName());
            tfLast.setText(inst.getSeek().getLastName());
            tfUser.setText(inst.getSeek().getUsername());
            tfPass.setText(inst.getSeek().getPassword());
            tfPassConfirm.setText(inst.getSeek().getPassword());
            tfEmail.setText(inst.getSeek().getEmail());
            tfDob.setText(String.format("%1$td-%1$tm-%1$tY", inst.getSeek().getDateOfBirth()));
            tfContact.setText(inst.getSeek().getPhoneNo());
            tfAddress.setText(inst.getSeek().getAddress());
            tfCity.setText(inst.getSeek().getCity());
            comboState.getSelectionModel().select(inst.getSeek().getState());
            tfPost.setText(inst.getSeek().getPost());
            tfCountry.setText(inst.getSeek().getCountry());
            tfEdu.setText(inst.getSeek().getEducation());
            tfGraduation.setText(inst.getSeek().getGraduation());
            tfSkills.setText(inst.getSeek().getSkills());
        }
        else{
            buttonCompany.setVisible(true);

            buttonCompany.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        BorderPane dash = (BorderPane) scrollProfile.getParent().getScene().getRoot();
                        dash.setCenter(FXMLLoader.load(Utility.class.getResource("ProfileCompany.fxml")));
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            });

            tfFirst.setText(inst.getRec().getFirstName());
            tfLast.setText(inst.getRec().getLastName());
            tfUser.setText(inst.getRec().getUsername());
            tfPass.setText(inst.getRec().getPassword());
            tfPassConfirm.setText(inst.getRec().getPassword());
            tfEmail.setText(inst.getRec().getEmail());
            tfDob.setText(String.format("%1$td-%1$tm-%1$tY", inst.getRec().getDateOfBirth()));
            tfContact.setText(inst.getRec().getPhoneNo());
            tfAddress.setText(inst.getRec().getAddress());
            tfCity.setText(inst.getRec().getCity());
            comboState.getSelectionModel().select(inst.getRec().getState());
//            tfState.setText(inst.getRec().getState());
            tfPost.setText(inst.getRec().getPost());
            tfCountry.setText(inst.getRec().getCountry());
            tfEdu.setText(inst.getRec().getEducation());
            tfGraduation.setText(inst.getRec().getGraduation());
            tfSkills.setText(inst.getRec().getSkills());
        }

        buttonUpdate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String column;
                String value;

                if (inst.getType() == 1){
                    if (!tfFirst.getText().equals(inst.getSeek().getFirstName())){
                        column = "fname";
                        value = tfFirst.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getSeek().setFirstName(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfLast.getText().equals(inst.getSeek().getLastName())){
                        column = "lname";
                        value = tfLast.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getSeek().setLastName(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfPass.getText().equals(inst.getSeek().getPassword())){
                        column = "password";
                        value = tfPass.getText();
                        if (tfPassConfirm.getText().equals(tfPass.getText())){
                            UtilityUpdate.profileUp(column, value, tfUser.getText());

                            inst.getSeek().setPassword(value);
                            scrollProfile.setVvalue(0);
                            labelPassMatch.setText("");
                            labelUpdate.setText("Updated Successfully.");
                        }
                        else{
                            scrollProfile.setVvalue(0);
                            labelPassMatch.setText("Passwords Do Not Match. Please Try Again");
                        }
                    }
                    if (!tfEmail.getText().equals(inst.getSeek().getEmail())){
                        column = "email";
                        value = tfEmail.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getSeek().setEmail(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfContact.getText().equals(inst.getSeek().getPhoneNo())){
                        column = "contact";
                        value = tfContact.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getSeek().setPhoneNo(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfAddress.getText().equals(inst.getSeek().getAddress())){
                        column = "address";
                        value = tfAddress.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getSeek().setAddress(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfCity.getText().equals(inst.getSeek().getCity())){
                        column = "city";
                        value = tfCity.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getSeek().setCity(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!comboState.getSelectionModel().getSelectedItem().equals(inst.getSeek().getState())){
                        column = "state";
                        value = comboState.getSelectionModel().getSelectedItem().toString();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getSeek().setState(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfPost.getText().equals(inst.getSeek().getPost())){
                        column = "post";
                        value = tfPost.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getSeek().setPost(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfCountry.getText().equals(inst.getSeek().getCountry())){
                        column = "country";
                        value = tfCountry.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getSeek().setCountry(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!(tfEdu.getText() == null) && !tfEdu.getText().equals(inst.getSeek().getEducation())){
                        column = "education";
                        value = tfEdu.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getSeek().setEducation(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!(tfGraduation.getText() == null) && !tfGraduation.getText().equals(inst.getSeek().getGraduation())){
                        column = "graduation";
                        value = tfGraduation.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getSeek().setGraduation(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!(tfSkills.getText() == null) && !tfSkills.getText().equals(inst.getSeek().getSkills())){
                        value = tfSkills.getText();
                        UtilityUpdate.skillUp(inst.getSeek().getUserID(), value);

                        inst.getSeek().setSkills(value.toUpperCase());
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                }
                else{
                    if (!tfFirst.getText().equals(inst.getRec().getFirstName())){
                        column = "fname";
                        value = tfFirst.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getRec().setFirstName(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfLast.getText().equals(inst.getRec().getLastName())){
                        column = "lname";
                        value = tfLast.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getRec().setLastName(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfPass.getText().equals(inst.getRec().getPassword())){
                        column = "password";
                        value = tfPass.getText();
                        if (tfPassConfirm.getText().equals(tfPass.getText())){
                            UtilityUpdate.profileUp(column, value, tfUser.getText());

                            inst.getRec().setPassword(value);
                            scrollProfile.setVvalue(0);
                            labelPassMatch.setText("");
                            labelUpdate.setText("Updated Successfully.");
                        }
                        else{
                            scrollProfile.setVvalue(0);
                            labelPassMatch.setText("Passwords Do Not Match. Please Try Again");
                        }
                    }
                    if (!tfEmail.getText().equals(inst.getRec().getEmail())){
                        column = "email";
                        value = tfEmail.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getRec().setEmail(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfContact.getText().equals(inst.getRec().getPhoneNo())){
                        column = "contact";
                        value = tfContact.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getRec().setPhoneNo(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfAddress.getText().equals(inst.getRec().getAddress())){
                        column = "address";
                        value = tfAddress.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getRec().setAddress(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfCity.getText().equals(inst.getRec().getCity())){
                        column = "city";
                        value = tfCity.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getRec().setCity(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!comboState.getSelectionModel().getSelectedItem().equals(inst.getRec().getState())){
                        column = "state";
                        value = comboState.getSelectionModel().getSelectedItem().toString();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getRec().setState(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfPost.getText().equals(inst.getRec().getPost())){
                        column = "post";
                        value = tfPost.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getRec().setPost(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!tfCountry.getText().equals(inst.getRec().getCountry())){
                        column = "country";
                        value = tfCountry.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getRec().setCountry(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!(tfEdu.getText() == null) && !tfEdu.getText().equals(inst.getRec().getEducation())){
                        column = "education";
                        value = tfEdu.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getRec().setEducation(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!(tfGraduation.getText() == null) && !tfGraduation.getText().equals(inst.getRec().getGraduation())){
                        column = "graduation";
                        value = tfGraduation.getText();
                        UtilityUpdate.profileUp(column, value, tfUser.getText());

                        inst.getRec().setGraduation(value);
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                    if (!(tfSkills.getText() == null) && !tfSkills.getText().equals(inst.getRec().getSkills())){
                        value = tfSkills.getText();
                        UtilityUpdate.skillUp(inst.getRec().getUserID(), value);

                        inst.getRec().setSkills(value.toUpperCase());
                        scrollProfile.setVvalue(0);
                        labelUpdate.setText("Updated Successfully.");
                    }
                }
            }
        });
    }
}
