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
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.control.ContentDisplay.LEFT;

public class ControllerSearchSeekers implements Initializable {

    @FXML
    private VBox vboxSeekers;

    @FXML
    private Button buttonInvite;
    @FXML
    private Button buttonMessage;

    @FXML
    private Label labelSeekerAddress;
    @FXML
    private Label labelSeekerCity;
    @FXML
    private Label labelSeekerContact;
    @FXML
    private Label labelSeekerCountry;
    @FXML
    private Label labelSeekerDob;
    @FXML
    private Label labelSeekerEdu;
    @FXML
    private Label labelSeekerEmail;
    @FXML
    private Label labelSeekerGrad;
    @FXML
    private Label labelSeekerJob;
    @FXML
    private Label labelSeekerName;
    @FXML
    private Label labelSeekerName1;
    @FXML
    private Label labelSeekerPost;
    @FXML
    private Label labelSeekerSkills;
    @FXML
    private Label labelSeekerState;
    @FXML
    private Label labelSeekerUsername;

    @FXML
    private Label labelSeekerAddressHead;
    @FXML
    private Label labelSeekerCityHead;
    @FXML
    private Label labelSeekerContactHead;
    @FXML
    private Label labelSeekerCountryHead;
    @FXML
    private Label labelSeekerDobHead;
    @FXML
    private Label labelSeekerEduHead;
    @FXML
    private Label labelSeekerEmailHead;
    @FXML
    private Label labelSeekerGradHead;
    @FXML
    private Label labelSeekerNameHead;
    @FXML
    private Label labelSeekerPostHead;
    @FXML
    private Label labelSeekerSkillsHead;
    @FXML
    private Label labelSeekerStateHead;
    @FXML
    private Label labelSeekerUsernameHead;

    @FXML
    private String searchInput;

    @FXML
    private String searchLocationInput;

    @FXML
    private Separator dividerSeeker;

    UserInstance inst = UserInstance.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources){
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatementSeeker = null;
        ResultSet resultSetSeeker = null;
        PreparedStatement preparedStatementSkills = null;
        ResultSet resultSetSkills = null;

        Button button;
        List<Button> seekerList = new ArrayList<>();

        int user_id;
        if (inst.getType() == 1)
            user_id = inst.getSeek().getUserID();
        else
            user_id = inst.getRec().getUserID();

        String searchText = searchInput;
        String[] keywordArray = searchText.split("\\s{2,8}|[,.?;'!@#%^&*/-]+"); //split string on when there are 2 to 8 spaces between characters

        ArrayList<String> keywordArrayList = new ArrayList<String>(
                Arrays.asList(keywordArray)
        );

        String searchLocation = searchLocationInput;

        List<Integer> existingResults = new ArrayList<>();



        for (String keyword: keywordArrayList) {
            keyword = keyword.strip();

            try {
                    List<String> parameterList = new ArrayList<>();
                    String queryCondition ="";

                    if ( !keyword.equals("")) {
                        parameterList.add("k.keyword = '" + keyword + "'");
                    }

                    if (searchLocation != null) {
                        parameterList.add("u.state = '" + searchLocation + "'");
                    }

                    queryCondition = String.join(" AND ", parameterList);

                    if (queryCondition.isBlank()) {
                        queryCondition = "1=1"; //includes everything
                    }

                    preparedStatementSeeker = connection.prepareStatement("SELECT * FROM user u \n" +
                            "LEFT JOIN user_skill us USING (user_id)\n" +
                            "LEFT JOIN keyword k USING (key_id)\n" +
                            "WHERE " + queryCondition + "\n" +
                            "GROUP BY u.user_id\n" +
                            "ORDER BY u.user_type_id");
//                    preparedStatementSeeker.setString(1, keyword);
//                    preparedStatementSeeker.setString(2,searchLocation);

                    resultSetSeeker = preparedStatementSeeker.executeQuery();



                    while (resultSetSeeker.next()) {
                        int seekerId = resultSetSeeker.getInt("user_id");
                        String username = resultSetSeeker.getString("username");
                        String fname = resultSetSeeker.getString("fname");
                        String lname = resultSetSeeker.getString("lname");
                        String email = resultSetSeeker.getString("email");
                        Date dob = resultSetSeeker.getDate("dob");
                        String contact = resultSetSeeker.getString("contact");
                        String address = resultSetSeeker.getString("address");
                        String city = resultSetSeeker.getString("city");
                        String state = resultSetSeeker.getString("state");
                        String post = resultSetSeeker.getString("post");
                        String country = resultSetSeeker.getString("country");
                        String education = resultSetSeeker.getString("education");
                        String graduation = resultSetSeeker.getString("graduation");
                        String jobSkill = resultSetSeeker.getString("keyword");


                        //calculate total search inputs
                        double totalSearchInputs = keywordArrayList.size();

                        if (searchLocation != null) {
                            totalSearchInputs++;
                        }

                        //calculate matches
                        double matchCount = 0;

                        if(state.equals(searchLocation)) {
                            matchCount++;
                        }


                        if (existingResults.contains(seekerId)) {
                            continue;
                        }
                        else {
                            existingResults.add(seekerId);
                        }

                        preparedStatementSkills = connection.prepareStatement("SELECT k.key_id, k.keyword FROM keyword k LEFT JOIN user_skill us ON us.key_id = k.key_id LEFT JOIN user u ON u.user_id = us.user_id WHERE us.user_id = ?");
                        preparedStatementSkills.setInt(1, seekerId);
                        resultSetSkills = preparedStatementSkills.executeQuery();

                        String seekerSkills = "";
                        while (resultSetSkills.next()) {
                            seekerSkills += resultSetSkills.getString("keyword");
                            for (String matchingSkill : keywordArrayList) {
                                if (matchingSkill.strip().toLowerCase().equals(resultSetSkills.getString(("keyword")).toLowerCase())) {
                                    matchCount++;
                                }
                            }
                            if (!resultSetSkills.isLast())
                                seekerSkills += ", ";
                        }

                        double matchPercentage = (matchCount/totalSearchInputs)*100;

                        String profilePreview = fname + " " + lname + "\n<" + email + "\n" + "Match: " + (int)matchPercentage + "%";

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

                        String seekerSkillsList = seekerSkills;
                        button.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                labelSeekerAddressHead.setVisible(true);
                                labelSeekerCityHead.setVisible(true);
                                labelSeekerContactHead.setVisible(true);
                                labelSeekerCountryHead.setVisible(true);
                                labelSeekerDobHead.setVisible(true);
                                labelSeekerEduHead.setVisible(true);
                                labelSeekerEmailHead.setVisible(true);
                                labelSeekerGradHead.setVisible(true);
                                labelSeekerNameHead.setVisible(true);
                                labelSeekerPostHead.setVisible(true);
                                labelSeekerSkillsHead.setVisible(true);
                                labelSeekerStateHead.setVisible(true);

                                labelSeekerAddress.setText(address);
                                labelSeekerCity.setText(city);
                                labelSeekerContact.setText(contact);
                                labelSeekerCountry.setText(country);
                                labelSeekerDob.setText(String.format("%1$td-%1$tm-%1$tY", dob));

                                labelSeekerEmail.setText(email);
                                labelSeekerName.setText(fname + " " + lname + " <" + email + ">");
                                labelSeekerName1.setText(fname + " " + lname);
                                labelSeekerUsername.setText("@" + username);

                                labelSeekerPost.setText(post);
                                labelSeekerState.setText(state);

                                if (!(education == null))
                                    labelSeekerEdu.setText(education);
                                else
                                    labelSeekerEdu.setText("");
                                if (!(graduation == null))
                                    labelSeekerGrad.setText(graduation);
                                else
                                    labelSeekerGrad.setText("");
                                if (!(seekerSkillsList == null))
                                    labelSeekerSkills.setText(seekerSkillsList.toUpperCase());
                                else
                                    labelSeekerSkills.setText("");

                                dividerSeeker.setPrefWidth(788);
                                buttonMessage.setVisible(true);
                            }
                        });

                        seekerList.add(button);
                    }

                vboxSeekers.getChildren().clear();
                vboxSeekers.getChildren().addAll(seekerList);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (resultSetSeeker != null) {
                    try {
                        resultSetSeeker.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (preparedStatementSeeker != null) {
                    try {
                        preparedStatementSeeker.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (resultSetSkills != null) {
                    try {
                        resultSetSkills.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (preparedStatementSkills != null) {
                    try {
                        preparedStatementSkills.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }
        }


        /*buttonInvite.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Pattern p = Pattern.compile("<(.*?)>");
                Matcher m = p.matcher(labelSeekerName.getText());
                if (m.find()){
                    Stage stg = new Stage();
                    stg.initModality(Modality.APPLICATION_MODAL);
                    //stg.initStyle(StageStyle.UNDECORATED);

                    try{
                        stg.setResizable(false);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Message.fxml"));
                        Scene scene = new Scene(loader.load());
                        stg.setScene(scene);
                        MessageController messageController = loader.getController();
                        messageController.setMessageType(2);
                        messageController.setTfTo(m.group(1));
                        stg.show();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });*/

        buttonMessage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Pattern p = Pattern.compile("<(.*?)>");
                Matcher m = p.matcher(labelSeekerName.getText());
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

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }

    public void setSearchLocationInput(String searchLocationInput) {
        this.searchLocationInput = searchLocationInput;
    }
}