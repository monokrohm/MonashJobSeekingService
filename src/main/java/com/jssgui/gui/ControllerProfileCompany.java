package com.jssgui.gui;

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

public class ControllerProfileCompany implements Initializable {
    @FXML
    private Button buttonProfile;
    @FXML
    private Button buttonUpdateC;

    @FXML
    private ScrollPane scrollProfileC;

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
    private Label labelUpdateC;

    @FXML
    private ComboBox comboCompState;

    UserInstance inst = UserInstance.getInstance();

    public void initialize(URL location, ResourceBundle resources){

        String[] states = {"Australian Capital Territory", "New South Wales", "Northern Territory", "Queensland", "South Australia", "Tasmania", "Victoria", "Western Australia"};

        for (String inputStates : states) {
            comboCompState.getItems().add(inputStates);
        }

        tfCompany.setText(inst.getRec().getCompanyName());
        tfCompEmail.setText(inst.getRec().getCompanyEmail());
        tfCompNo.setText(inst.getRec().getCompanyPhoneNo());
        tfCompAddress.setText(inst.getRec().getCompanyAddress());
        tfCompCity.setText(inst.getRec().getCompanyCity());
        comboCompState.getSelectionModel().select(inst.getRec().getCompanyState());
        tfCompPost.setText(inst.getRec().getCompanyPost());
        tfCompCountry.setText(inst.getRec().getCompanyCountry());

        buttonProfile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    BorderPane dash = (BorderPane) scrollProfileC.getParent().getScene().getRoot();
                    dash.setCenter(FXMLLoader.load(Utility.class.getResource("Profile.fxml")));
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

        buttonUpdateC.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String column;
                String value;

                if (!tfCompany.getText().equals(inst.getRec().getCompanyName())){
                    column = "c_name";
                    value = tfCompany.getText();
                    UtilityUpdate.companyUp(column, value, inst.getRec().getCompanyID());

                    inst.getRec().setCompanyName(value);
                    labelUpdateC.setText("Updated Successfully.");
                }
                if (!tfCompEmail.getText().equals(inst.getRec().getCompanyEmail())){
                    column = "c_email";
                    value = tfCompEmail.getText();
                    UtilityUpdate.companyUp(column, value, inst.getRec().getCompanyID());

                    inst.getRec().setCompanyEmail(value);
                    labelUpdateC.setText("Updated Successfully.");
                }
                if (!tfCompNo.getText().equals(inst.getRec().getCompanyPhoneNo())){
                    column = "c_contact";
                    value = tfCompNo.getText();
                    UtilityUpdate.companyUp(column, value, inst.getRec().getCompanyID());

                    inst.getRec().setCompanyPhoneNo(value);
                    labelUpdateC.setText("Updated Successfully.");
                }
                if (!tfCompAddress.getText().equals(inst.getRec().getCompanyAddress())){
                    column = "c_address";
                    value = tfCompAddress.getText();
                    UtilityUpdate.companyUp(column, value, inst.getRec().getCompanyID());

                    inst.getRec().setCompanyAddress(value);
                    labelUpdateC.setText("Updated Successfully.");
                }
                if (!tfCompCity.getText().equals(inst.getRec().getCompanyCity())){
                    column = "c_city";
                    value = tfCompCity.getText();
                    UtilityUpdate.companyUp(column, value, inst.getRec().getCompanyID());

                    inst.getRec().setCompanyCity(value);
                    labelUpdateC.setText("Updated Successfully.");
                }
                if (!comboCompState.getSelectionModel().getSelectedItem().equals(inst.getRec().getCompanyState())){
                    column = "c_state";
                    value = comboCompState.getSelectionModel().getSelectedItem().toString();
                    UtilityUpdate.companyUp(column, value, inst.getRec().getCompanyID());

                    inst.getRec().setCompanyState(value);
                    labelUpdateC.setText("Updated Successfully.");
                }
                if (!tfCompPost.getText().equals(inst.getRec().getCompanyPost())){
                    column = "c_post";
                    value = tfCompPost.getText();
                    UtilityUpdate.companyUp(column, value, inst.getRec().getCompanyID());

                    inst.getRec().setCompanyPost(value);
                    labelUpdateC.setText("Updated Successfully.");
                }
                if (!tfCompCountry.getText().equals(inst.getRec().getCompanyCountry())){
                    column = "c_country";
                    value = tfCompCountry.getText();
                    UtilityUpdate.companyUp(column, value, inst.getRec().getCompanyID());

                    inst.getRec().setCompanyCountry(value);
                    labelUpdateC.setText("Updated Successfully.");
                }
            }
        });
    }
}
