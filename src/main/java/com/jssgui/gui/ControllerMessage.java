package com.jssgui.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerMessage implements Initializable {
    @FXML
    private Button buttonMessageCancel;
    @FXML
    private Button buttonMessageSend;

    @FXML
    private TextArea tfMessage;
    @FXML
    private TextField tfTo;

    private int messageType;
    private String jobInfo;
    private String userInfo;

    UserInstance inst = UserInstance.getInstance();

    public void initialize(URL location, ResourceBundle resource){
        buttonMessageSend.disableProperty().bind(tfTo.textProperty().isEmpty().or(tfMessage.textProperty().isEmpty()));

        int user_id;
        if (inst.getType() == 1){
            user_id = inst.getSeek().getUserID();
        }
        else{
            user_id = inst.getRec().getUserID();
        }

        buttonMessageCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ((Stage) buttonMessageCancel.getScene().getWindow()).close();
            }
        });

        buttonMessageSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (messageType == 2)
                    UtilityUpdate.candidateUp(userInfo, user_id, jobInfo, false, true);

                UtilityMessage.sendMessage(tfTo.getText(), user_id, tfMessage.getText(), messageType);
                ((Stage) buttonMessageCancel.getScene().getWindow()).close();
            }
        });
    }

    public void setJobInfo(String jobInfo){
        this.jobInfo = jobInfo;
    }

    public void setMessageType(int messageType){
        this.messageType = messageType;
    }

    public void setTfTo(String messageTo){
        tfTo.setText(messageTo);
    }

    public void setUserInfo(String userInfo){
        this.userInfo = userInfo;
    }
}
