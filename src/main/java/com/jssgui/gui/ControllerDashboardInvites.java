package com.jssgui.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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

public class ControllerDashboardInvites implements Initializable {
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
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatementSender = null;
        ResultSet resultSetSender = null;

        Button button;
        List<Button> mailList = new ArrayList<>();

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
        }

        try{
            preparedStatement = connection.prepareStatement("SELECT * FROM message m LEFT JOIN message_inbox mi ON mi.message_id = m.message_id  WHERE mi.user_id = ? AND m.message_type_id = ? ORDER BY m.message_id DESC ");
            preparedStatement.setInt(1, user_id);
            preparedStatement.setInt(2, 2);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int sender_id = resultSet.getInt("m_sender_id");
                String m_content = resultSet.getString("m_content");

                preparedStatementSender = connection.prepareStatement("SELECT * FROM user u WHERE user_id = ?");
                preparedStatementSender.setInt(1, sender_id);
                resultSetSender = preparedStatementSender.executeQuery();

                while (resultSetSender.next()){
                    String senderFName = resultSetSender.getString("fname");
                    String senderLName = resultSetSender.getString("lname");
                    String senderEmail = resultSetSender.getString("email");
                    String mailPreview = senderFName + " " + senderLName + "\n<" + senderEmail + ">\n" + resultSet.getString("m_content").substring(0, Math.min(100, resultSet.getString("m_content").length()));

                    ImageView imageView = new ImageView(getClass().getResource("/icons/user-profile-avatar-icon-134114304.png").toExternalForm());
                    imageView.setFitWidth(51);
                    imageView.setFitHeight(59);
                    imageView.setPreserveRatio(true);

                    button = new Button(mailPreview);
                    button.setWrapText(true);
                    button.setGraphic(imageView);
                    button.setContentDisplay(LEFT);
                    button.setGraphicTextGap(15);
                    button.setPrefSize(328, 91);
                    button.getStyleClass().add("button2");
                    button.setAlignment(Pos.BASELINE_LEFT);
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            labelFrom.setText("From: " + senderFName + " " + senderLName + " <" + senderEmail + ">");
                            labelTo.setText("To: " + userFName + " " + userLName + " <" + userEmail + ">");
                            labelMessageContent.setText(m_content);

                            buttonMessageReply.setVisible(true);
                            dividerInbox.setPrefWidth(788);
                        }
                    });
                    mailList.add(button);
                }
            }

            vboxMail.getChildren().clear();
            vboxMail.getChildren().addAll(mailList);
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
