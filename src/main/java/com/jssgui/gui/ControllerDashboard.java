package com.jssgui.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.control.ContentDisplay.LEFT;

public class ControllerDashboard implements Initializable {
    @FXML
    private VBox vboxMail;

    @FXML
    private Button buttonMessageNew;
    @FXML
    private Button buttonMessageReply;

    @FXML
    private Label labelMessageContent;
    @FXML
    private Label labelFrom;
    @FXML
    private Label labelTo;

    @FXML
    private Separator dividerInbox;

    UserInstance inst = UserInstance.getInstance();
    public void initialize(URL location, ResourceBundle resources){
        Connection connection = DBConnection.getConnection(); //create connection to dd
        PreparedStatement preparedStatement = null; //used to query
        ResultSet resultSet = null; //holds the results
        PreparedStatement preparedStatementSender = null;
        ResultSet resultSetSender = null;

        Button button;
        List<Button> mailList = new ArrayList<>(); //list to hold buttons

        int user_id;
        String userFName;
        String userLName;
        String userEmail;
        if (inst.getType() == 1){
            user_id = inst.getSeek().getUserID();
            userFName = inst.getSeek().getFirstName();
            userLName = inst.getSeek().getLastName();
            userEmail = inst.getSeek().getEmail();
        }
        else{
            user_id = inst.getRec().getUserID();
            userFName = inst.getRec().getFirstName();
            userLName = inst.getRec().getLastName();
            userEmail = inst.getRec().getEmail();
        } // not needed for search

        try{ //query with keyword, join with key_id to user or job_post table, user ids to connect keyword table -> user_skill/job_skill -> user/job_post
            preparedStatement = connection.prepareStatement("SELECT * FROM message m LEFT JOIN message_inbox mi ON mi.message_id = m.message_id  WHERE mi.user_id = ? AND m.message_type_id = ? ORDER BY m.message_id DESC "); //LEFT JOIN user u ON u.user_id = mi.user_id
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, 1); // parameters for the ? in query, should be a keyword
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){ //while there are matches
                int sender_id = resultSet.getInt("m_sender_id");
                String m_content = resultSet.getString("m_content");

                preparedStatementSender = connection.prepareStatement("SELECT * FROM user u WHERE user_id = ?");
                preparedStatementSender.setInt(1, sender_id);
                resultSetSender = preparedStatementSender.executeQuery(); //second resultset preparedstatement not needed

                while (resultSetSender.next()){ //second while not needed
                    String senderFName = resultSetSender.getString("fname");
                    String senderLName = resultSetSender.getString("lname"); //save values from columns
                    String senderEmail = resultSetSender.getString("email");
                    String mailPreview = senderFName + " " + senderLName + "\n<" + senderEmail + ">\n" + resultSet.getString("m_content").substring(0, Math.min(100, resultSet.getString("m_content").length()));
                        //concatenate something for text on button user name/job name, skills etc
                    ImageView imageView = new ImageView(getClass().getResource("/icons/user-profile-avatar-icon-134114304.png").toExternalForm()); //icon for button can ignore
                    imageView.setFitWidth(51);
                    imageView.setFitHeight(59);
                    imageView.setPreserveRatio(true);// image related settings can ignore

                    button = new Button(mailPreview); // create button with button text as argument
                    button.setWrapText(true); //wrap text
                    button.setGraphic(imageView);
                    button.setContentDisplay(LEFT);
                    button.setGraphicTextGap(15); //icon related settings
                    button.setPrefSize(328, 91); //button size
                    button.getStyleClass().add("button2"); //set button style from style.css
                    button.setAlignment(Pos.BASELINE_LEFT); //alignment of text/icon
                    button.setOnAction(new EventHandler<ActionEvent>() { //button function
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            labelFrom.setText("From: " + senderFName + " " + senderLName + " <" + senderEmail + ">");
                            labelTo.setText("To: " + userFName + " " + userLName + " <" + userEmail + ">");
                            labelMessageContent.setText(m_content);

                            buttonMessageReply.setVisible(true);
                            dividerInbox.setPrefWidth(788);
                        }
                    });
                    mailList.add(button); //add to list
                }
            }

            vboxMail.getChildren().clear(); //clears any buttons from before from vbox
            vboxMail.getChildren().addAll(mailList); //adds buttons to the vbox
        }
        catch(SQLException e){ //look at jobscontroller for a version with one preparedstatement
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
            if (resultSetSender != null){
                try{
                    resultSetSender.close();
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
            if (preparedStatementSender != null){
                try{
                    preparedStatementSender.close();
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

        buttonMessageNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stg = new Stage();
                stg.initModality(Modality.APPLICATION_MODAL);
                //stg.initStyle(StageStyle.UNDECORATED);

                try{
                    stg.setResizable(false);
                    Parent root = FXMLLoader.load(getClass().getResource("Message.fxml"));
                    Scene scene = new Scene(root);
                    stg.setScene(scene);
                    stg.show();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

        buttonMessageReply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Pattern p = Pattern.compile("<(.*?)>");
                Matcher m = p.matcher(labelFrom.getText());
                if (m.find()){
                    Stage stg = new Stage();
                    stg.initModality(Modality.APPLICATION_MODAL);
                    //stg.initStyle(StageStyle.UNDECORATED);
                    try{
                        stg.setResizable(false);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Message.fxml"));
                        Scene scene = new Scene(loader.load());
                        stg.setScene(scene);
                        ControllerMessage controllerMessage = loader.getController();
                        controllerMessage.setMessageType(1);
                        controllerMessage.setTfTo(m.group(1));
                        stg.show();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
