package com.jssgui.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerDash implements Initializable {
    @FXML
    private BorderPane borderMain;

    @FXML
    private Button buttonDash;
    @FXML
    private Button buttonProfile;
    @FXML
    private Button buttonJobs;
    @FXML
    private Button buttonCandidates;
    @FXML
    private Button buttonSettings;
    @FXML
    private Button buttonChat;
    @FXML
    private Button buttonInbox;
    @FXML
    private Button buttonTopProfile;
    @FXML
    private Button buttonSearch;
    @FXML
    private Button buttonLogout;

    @FXML
    private Button buttonSearchSeekers;

    @FXML
    private Label labelHeader;
    @FXML
    private Label labelName;

    @FXML
    private ControllerProfile controllerProfile;


    @FXML
    private ComboBox<String> comboCategory;

    @FXML
    private ComboBox<String> comboLocation;

    @FXML
    private ComboBox<String> comboSalary;

    @FXML
    private TextField searchBar;

    @FXML
    private String searchQuery;

    @FXML
    private String selectedCategory;

    @FXML
    private String selectedLocation;

    @FXML
    private int selectedSalary;

    @FXML
    private RadioButton radioButtonJob;

    @FXML
    private RadioButton radioButtonSeeker;

    private String searchMode;


    UserInstance inst = UserInstance.getInstance();

    public ControllerDash(){

    }

    public void initialize(URL location, ResourceBundle resources){
        if (inst.getType()==1){
            labelName.setText(inst.getSeek().getFirstName() + " " + inst.getSeek().getLastName());
        }
        else{
            labelName.setText(inst.getRec().getFirstName() + " " + inst.getRec().getLastName());
        }

        try {
            Parent centerPane = FXMLLoader.load(Utility.class.getResource("Dashboard.fxml"));
            borderMain.setCenter(centerPane);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        searchMode = "Job";
        populateCategory();
        populateLocation();
        populateSalary();

    }

    public void dashPress(ActionEvent event) {
        if (event.getSource() == buttonDash || event.getSource() == buttonInbox){
            labelHeader.setText(buttonDash.getText());
            try {
                Parent centerPane = FXMLLoader.load(Utility.class.getResource("Dashboard.fxml"));
                borderMain.setCenter(centerPane);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else if (event.getSource() == buttonChat){
            labelHeader.setText(buttonDash.getText());
            try {
                Parent centerPane = FXMLLoader.load(Utility.class.getResource("DashboardInvites.fxml"));
                borderMain.setCenter(centerPane);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else if (event.getSource() == buttonProfile || event.getSource() == buttonTopProfile){
            labelHeader.setText(buttonProfile.getText());
            try {
                Parent centerPane = FXMLLoader.load(Utility.class.getResource("Profile.fxml"));
                borderMain.setCenter(centerPane);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else if (event.getSource() == buttonJobs){
            labelHeader.setText(buttonJobs.getText());
            try {
                if (inst.getType()==1){
                    Parent centerPane = FXMLLoader.load(Utility.class.getResource("JobBookmarks.fxml"));
                    borderMain.setCenter(centerPane);
                }
                else{
                    Parent centerPane = FXMLLoader.load(Utility.class.getResource("Jobs.fxml"));
                    borderMain.setCenter(centerPane);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else if (event.getSource() == buttonCandidates){
            labelHeader.setText(buttonCandidates.getText());
            try {
                if (inst.getType()==1){
                    Parent centerPane = FXMLLoader.load(Utility.class.getResource("Applications.fxml"));
                    borderMain.setCenter(centerPane);
                }
                else{
                    Parent centerPane = FXMLLoader.load(Utility.class.getResource("Candidates.fxml"));
                    borderMain.setCenter(centerPane);
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else if (event.getSource() == buttonSettings){
            labelHeader.setText(buttonSettings.getText());
            try {
                Parent centerPane = FXMLLoader.load(Utility.class.getResource("Settings.fxml"));
                borderMain.setCenter(centerPane);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else if (event.getSource() == buttonSearch) {

            searchQuery = searchBar.getText();

            if ( searchMode.equals("Job")) {

                handleCategory(event);
                handleLocation(event);
                handleSalary(event);

                labelHeader.setText("Search");
                try {

                    ControllerSearch sc = new ControllerSearch();
                    sc.setSearchInput(searchQuery);
                    sc.setSearchLocationInput(selectedLocation);
                    sc.setSearchCategoryInput(selectedCategory);
                    sc.setSearchSalaryInput(selectedSalary);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setController(sc);
                    loader.setLocation(getClass().getResource("Search.fxml"));
                    Parent centerPane = loader.load();
                    borderMain.setCenter(centerPane);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }

            else if (searchMode.equals("Seeker")) {

                handleLocation(event);


                labelHeader.setText("Search");
                try {


                    ControllerSearchSeekers ssc = new ControllerSearchSeekers();
                    ssc.setSearchInput(searchQuery);
                    ssc.setSearchLocationInput(selectedLocation);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setController(ssc);
                    loader.setLocation(getClass().getResource("SearchSeekers.fxml"));
                    Parent centerPane = loader.load();
                    borderMain.setCenter(centerPane);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }


        }
        else if (event.getSource() == radioButtonJob | event.getSource()  == radioButtonSeeker) {

            if (radioButtonJob.isSelected()) {
                comboCategory.setVisible(true);
                comboSalary.setVisible(true);
                searchMode = "Job";
            }
            else if (radioButtonSeeker.isSelected()) {
                comboCategory.setVisible(false);
                comboSalary.setVisible(false);
                searchMode = "Seeker";
            }
        }
        else{
            Utility.changeScene(event,"Login.fxml");
        }
    }

    public void populateCategory() {

        comboCategory.getItems().add(null);

        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            preparedStatement = connection.prepareStatement("SELECT DISTINCT cat_name FROM category c");
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                comboCategory.getItems().add(resultSet.getString("cat_name"));
            }
            preparedStatement.close();
            resultSet.close();
        }
        catch (SQLException e) {
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
            if (preparedStatement != null){
                try{
                    preparedStatement.close();
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

    }

    public void populateLocation() {

        comboLocation.getItems().add(null);

        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        if (searchMode.equals("Job")) {
            try {
                preparedStatement = connection.prepareStatement("SELECT DISTINCT j_location FROM job_post jp");
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    comboLocation.getItems().add(resultSet.getString("j_location"));
                }
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
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
                if (preparedStatement != null){
                    try{
                        preparedStatement.close();
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
        }
        else if (searchMode.equals("Seeker")) {
            try {
                preparedStatement = connection.prepareStatement("SELECT DISTINCT state FROM user u");
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    comboLocation.getItems().add(resultSet.getString("state"));
                }
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
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
                if (preparedStatement != null){
                    try{
                        preparedStatement.close();
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
        }


    }

    public void populateSalary() {


        String[] salaryRanges = {"Salary Range", "$30,000+","$40,000+", "$50,000+", "$60,000+", "$70,000+", "$80,000+", "$100,000+", "$120,000+", "$150,000+"};

        try{
            for (String salary : salaryRanges) {
                comboSalary.getItems().add(salary);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleCategory(ActionEvent event) {
        selectedCategory = comboCategory.getSelectionModel().getSelectedItem();
    }

    public void handleLocation(ActionEvent event) {
        selectedLocation = comboLocation.getSelectionModel().getSelectedItem();
    }

    public void handleSalary(ActionEvent event) {

        if(comboSalary.getSelectionModel().getSelectedItem() == null) {
            selectedSalary = 0;
        }
        else if (comboSalary.getSelectionModel().getSelectedItem().equals("Salary Range")) {
            selectedSalary = 0;
        }
        else {
            selectedSalary = Integer.parseInt(comboSalary.getSelectionModel().getSelectedItem().replaceAll("(\\$|\\,|\\+)", ""));
        }
    }





}
