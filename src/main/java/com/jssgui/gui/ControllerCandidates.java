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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.control.ContentDisplay.LEFT;

public class ControllerCandidates implements Initializable{
    @FXML
    private VBox vboxApplicants;
    @FXML
    private BorderPane borderCandidates;

    @FXML
    private Button buttonApplications;
    @FXML
    private Button buttonInvite;
    @FXML
    private Button buttonReject;

    @FXML
    private Label labelAppAddress;
    @FXML
    private Label labelAppCity;
    @FXML
    private Label labelAppContact;
    @FXML
    private Label labelAppCountry;
    @FXML
    private Label labelAppDob;
    @FXML
    private Label labelAppEdu;
    @FXML
    private Label labelAppEmail;
    @FXML
    private Label labelAppGrad;
    @FXML
    private Label labelAppJob;
    @FXML
    private Label labelAppName;
    @FXML
    private Label labelAppName1;
    @FXML
    private Label labelAppPost;
    @FXML
    private Label labelAppSkills;
    @FXML
    private Label labelAppState;
    @FXML
    private Label labelAppUsername;

    @FXML
    private Label labelAppAddressHead;
    @FXML
    private Label labelAppCityHead;
    @FXML
    private Label labelAppContactHead;
    @FXML
    private Label labelAppCountryHead;
    @FXML
    private Label labelAppDobHead;
    @FXML
    private Label labelAppEduHead;
    @FXML
    private Label labelAppEmailHead;
    @FXML
    private Label labelAppGradHead;
    @FXML
    private Label labelAppNameHead;
    @FXML
    private Label labelAppPostHead;
    @FXML
    private Label labelAppSkillsHead;
    @FXML
    private Label labelAppStateHead;
    @FXML
    private Label labelAppUsernameHead;

    @FXML
    private Separator dividerApp;

    UserInstance inst = UserInstance.getInstance();
    public void initialize(URL location, ResourceBundle resources){
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatementApp = null;
        ResultSet resultSetApp = null;
        PreparedStatement preparedStatementSkills = null;
        ResultSet resultSetSkills = null;

        Button button;
        List<Button> appList = new ArrayList<>();

        int user_id;
        if (inst.getType() == 1)
            user_id = inst.getSeek().getUserID();
        else
            user_id = inst.getRec().getUserID();

        try{
            preparedStatement = connection.prepareStatement("SELECT j.job_id, j.j_title FROM job_post j LEFT JOIN company_post cp ON cp.job_id = j.job_id LEFT JOIN company c ON c.company_id = cp.company_id LEFT JOIN company_involvement ci ON ci.company_id = c.company_id  WHERE ci.user_id = ? ORDER BY j.job_id DESC "); //LEFT JOIN user u ON u.user_id = ci.user_id
            preparedStatement.setInt(1, user_id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int job_id = resultSet.getInt("job_id");
                String job_title = resultSet.getString("j_title");

                preparedStatementApp = connection.prepareStatement("SELECT * FROM user u LEFT JOIN job_activity ja ON ja.user_id = u.user_id WHERE ja.job_id = ?");
                preparedStatementApp.setInt(1, job_id);
                resultSetApp = preparedStatementApp.executeQuery();

                while(resultSetApp.next()){
                    int applicant_id = resultSetApp.getInt("user_id");
                    String username = resultSetApp.getString("username");
                    String fname = resultSetApp.getString("fname");
                    String lname = resultSetApp.getString("lname");
                    String email = resultSetApp.getString("email");
                    Date dob = resultSetApp.getDate("dob");
                    String contact = resultSetApp.getString("contact");
                    String address = resultSetApp.getString("address");
                    String city = resultSetApp.getString("city");
                    String state = resultSetApp.getString("state");
                    String post = resultSetApp.getString("post");
                    String country = resultSetApp.getString("country");
                    String education = resultSetApp.getString("education");
                    String graduation = resultSetApp.getString("graduation");

                    preparedStatementSkills = connection.prepareStatement("SELECT k.key_id, k.keyword FROM keyword k LEFT JOIN user_skill us ON us.key_id = k.key_id LEFT JOIN user u ON u.user_id = us.user_id WHERE us.user_id = ?");
                    preparedStatementSkills.setInt(1, applicant_id);
                    resultSetSkills = preparedStatementSkills.executeQuery();

                    String appSkills = "";
                    while(resultSetSkills.next()){
                        appSkills += resultSetSkills.getString("keyword");
                        if (!resultSetSkills.isLast())
                            appSkills += ", ";
                    }

                    preparedStatementApp = connection.prepareStatement("SELECT * FROM job_activity WHERE job_id = ? AND user_id = ?");
                    preparedStatementApp.setInt(1, job_id);
                    preparedStatementApp.setInt(2, applicant_id);
                    resultSetApp = preparedStatementApp.executeQuery();

                    Boolean ja_pending = null;
                    Boolean ja_approval = null;
                    String pending = "";
                    while(resultSetApp.next()){
                        ja_pending = resultSetApp.getBoolean("ja_pending");
                        ja_approval = resultSetApp.getBoolean("ja_approval");
                    }

                    if (ja_pending) {
                        pending = "Pending Review";
                    }
                    else{
                        if (ja_approval)
                            pending = "Invitation Sent";
                        else
                            pending = "Rejected";
                    }

                    String profilePreview = fname + " " + lname + "\n<" + email + ">\n" + pending;

                    ImageView imageView = new ImageView(getClass().getResource("/icons/user-profile-avatar-icon-134114304.png").toExternalForm());
                    imageView.setFitWidth(51);
                    imageView.setFitHeight(59);
                    imageView.setPreserveRatio(true);

                    button = new Button(profilePreview);
                    button.setWrapText(true);
                    button.setGraphic(imageView);
                    button.setContentDisplay(LEFT);
                    button.setGraphicTextGap(15);
                    button.setPrefSize(328, 91);
                    button.getStyleClass().add("button2");
                    button.setAlignment(Pos.BASELINE_LEFT);

                    String appSkillsList = appSkills;
                    button.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            labelAppAddressHead.setVisible(true);
                            labelAppCityHead.setVisible(true);
                            labelAppContactHead.setVisible(true);
                            labelAppCountryHead.setVisible(true);
                            labelAppDobHead.setVisible(true);
                            labelAppEduHead.setVisible(true);
                            labelAppEmailHead.setVisible(true);
                            labelAppGradHead.setVisible(true);
                            labelAppNameHead.setVisible(true);
                            labelAppPostHead.setVisible(true);
                            labelAppSkillsHead.setVisible(true);
                            labelAppStateHead.setVisible(true);
                            labelAppUsernameHead.setVisible(true);

                            labelAppAddress.setText(address);
                            labelAppCity.setText(city);
                            labelAppContact.setText(contact);
                            labelAppCountry.setText(country);
                            labelAppDob.setText(String.format("%1$td-%1$tm-%1$tY", dob));

                            labelAppEmail.setText(email);
                            labelAppName.setText(fname + " " + lname + " <" + email + ">");
                            labelAppName1.setText(fname + " " + lname);
                            labelAppJob.setText("Applied To: " + job_title);
                            labelAppPost.setText(post);
                            labelAppState.setText(state);
                            labelAppUsername.setText("@" + username);

                            if (!(education == null))
                                labelAppEdu.setText(education);
                            else
                                labelAppEdu.setText("");
                            if (!(graduation == null))
                                labelAppGrad.setText(graduation);
                            else
                                labelAppGrad.setText("");
                            if (!(appSkillsList == null))
                                labelAppSkills.setText(appSkillsList.toUpperCase());
                            else
                                labelAppSkills.setText("");

                            dividerApp.setPrefWidth(651);
                            buttonInvite.setVisible(true);
                            buttonReject.setVisible(true);

                            Connection connection2 = DBConnection.getConnection();
                            PreparedStatement preparedStatementApp = null;
                            ResultSet resultSetApp = null;

                            try{
                                preparedStatementApp = connection2.prepareStatement("SELECT * FROM job_activity WHERE job_id = ? AND user_id = ?");
                                preparedStatementApp.setInt(1, job_id);
                                preparedStatementApp.setInt(2, applicant_id);
                                resultSetApp = preparedStatementApp.executeQuery();

                                Boolean ja_pending = null;
                                while(resultSetApp.next()){
                                    ja_pending = resultSetApp.getBoolean("ja_pending");
                                }

                                if(ja_pending){
                                    buttonInvite.setDisable(false);
                                    buttonReject.setDisable(false);
                                }
                                else{
                                    buttonInvite.setDisable(true);
                                    buttonReject.setDisable(true);
                                }
                            }
                            catch(SQLException e){
                                e.printStackTrace();
                            }
                            finally {
                                if (resultSetApp != null){
                                    try{
                                        resultSetApp.close();
                                    }
                                    catch(SQLException e){
                                        e.printStackTrace();
                                    }
                                }
                                if (preparedStatementApp != null){
                                    try{
                                        preparedStatementApp.close();
                                    }
                                    catch (SQLException e){
                                        e.printStackTrace();
                                    }
                                }
                                if (connection2 != null){
                                    try{
                                        connection2.close();
                                    }
                                    catch (SQLException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                    appList.add(button);
                }
            }

            vboxApplicants.getChildren().clear();
            vboxApplicants.getChildren().addAll(appList);
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
            if (resultSetApp != null){
                try{
                    resultSetApp.close();
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
            if (preparedStatementApp != null){
                try{
                    preparedStatementApp.close();
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

        buttonApplications.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    BorderPane dash = (BorderPane) borderCandidates.getParent().getScene().getRoot();
                    dash.setCenter(FXMLLoader.load(Utility.class.getResource("Applications.fxml")));
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

        buttonInvite.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Pattern p = Pattern.compile("<(.*?)>");
                Matcher m = p.matcher(labelAppName.getText());
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
                        controllerMessage.setJobInfo(labelAppJob.getText().replaceAll("Applied To: ", ""));
                        controllerMessage.setUserInfo(labelAppUsername.getText().replaceAll("@", ""));
                        controllerMessage.setMessageType(2);
                        controllerMessage.setTfTo(m.group(1));
                        stg.show();

                        stg.setOnHiding(windowEvent -> {
                            buttonInvite.setDisable(true);
                            buttonReject.setDisable(true);});
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        buttonReject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                UtilityUpdate.candidateUp(labelAppUsername.getText().replaceAll("@", ""), user_id, labelAppJob.getText().replaceAll("Applied To: ", ""), false, false);
                buttonInvite.setDisable(true);
                buttonReject.setDisable(true);
            }
        });
    }
}