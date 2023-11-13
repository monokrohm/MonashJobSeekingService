package com.jssgui.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Utility {

    public static void changeScene(ActionEvent event, String fxml) {
        try {
            Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent pane = FXMLLoader.load(Utility.class.getResource(fxml));
            stg.getScene().setRoot(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        UserInstance inst = UserInstance.getInstance();

        try{
            preparedStatement = connection.prepareStatement("SELECT user_id, user_type_id, username, password, fname, lname, email, dob, contact, address, city, state, post, country, education, graduation FROM user WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery(); //resultSet = selected columns

            if (!resultSet.isBeforeFirst()){ //empty
                //System.out.println("event = " + event + ", wrong username = " + username + ",  wrong password = " + password);
            }
            else{
                while(resultSet.next()){
                    int user_id = resultSet.getInt("user_id");
                    int typeID = resultSet.getInt("user_type_id");
                    String dbPassword = resultSet.getString("password");
                    String fname = resultSet.getString("fname");
                    String lname = resultSet.getString("lname");
                    String email = resultSet.getString("email");
                    Date dob = resultSet.getDate("dob");
                    String contact = resultSet.getString("contact");
                    String address = resultSet.getString("address");
                    String city = resultSet.getString("city");
                    String state = resultSet.getString("state");
                    String post = resultSet.getString("post");
                    String country = resultSet.getString("country");
                    String education = resultSet.getString("education");
                    String graduation = resultSet.getString("graduation");

                    if (dbPassword.equals(password)){
                        preparedStatement = connection.prepareStatement("SELECT k.key_id, k.keyword FROM keyword k LEFT JOIN user_skill us ON us.key_id = k.key_id LEFT JOIN user u ON u.user_id = us.user_id WHERE us.user_id = ?");
                        preparedStatement.setInt(1, user_id);
                        resultSet = preparedStatement.executeQuery();

                        String skillList = "";
                        while(resultSet.next()){
                            skillList += resultSet.getString("keyword");
                            if (!resultSet.isLast())
                                skillList += ", ";
                        }

                        if (typeID == 1){
                            Seeker u = new Seeker();
                            inst.setSeek(u);
                            inst.setType(typeID);

                            u.setUserID(user_id);
                            u.setUserTypeID(typeID);
                            u.setUsername(username);
                            u.setPassword(dbPassword);
                            u.setFirstName(fname);
                            u.setLastName(lname);
                            u.setEmail(email);
                            u.setDateOfBirth(dob);
                            u.setPhoneNo(contact);
                            u.setAddress(address);
                            u.setCity(city);
                            u.setState(state);
                            u.setPost(post);
                            u.setCountry(country);
                            u.setEducation(education);
                            u.setGraduation(graduation);
                            u.setSkills(skillList.toUpperCase());

                            Utility.changeScene(event,"DashJS.fxml");
                        }
                        else{
                            Recruiter u = new Recruiter();
                            inst.setRec(u);
                            inst.setType(typeID);

                            preparedStatement = connection.prepareStatement("SELECT c.company_id, c.c_name, c.c_email, c.c_contact, c.c_address, c.c_city, c.c_state, c.c_post, c.c_country FROM company c LEFT JOIN company_involvement ci ON ci.company_id = c.company_id LEFT JOIN user u ON u.user_id = ci.user_id WHERE ci.user_id = ?");
                            preparedStatement.setInt(1, user_id);
                            resultSet = preparedStatement.executeQuery();

                            while(resultSet.next()) {
                                int company_id = resultSet.getInt("company_id");
                                String c_name = resultSet.getString("c_name");
                                String c_email = resultSet.getString("c_email");
                                String c_contact = resultSet.getString("c_contact");
                                String c_address = resultSet.getString("c_address");
                                String c_city = resultSet.getString("c_city");
                                String c_state = resultSet.getString("c_state");
                                String c_post = resultSet.getString("c_post");
                                String c_country = resultSet.getString("c_country");

                                u.setUserID(user_id);
                                u.setUserTypeID(typeID);
                                u.setUsername(username);
                                u.setPassword(dbPassword);
                                u.setFirstName(fname);
                                u.setLastName(lname);
                                u.setEmail(email);
                                u.setDateOfBirth(dob);
                                u.setPhoneNo(contact);
                                u.setAddress(address);
                                u.setCity(city);
                                u.setState(state);
                                u.setPost(post);
                                u.setCountry(country);
                                u.setEducation(education);
                                u.setGraduation(graduation);
                                u.setSkills(skillList.toUpperCase());

                                u.setCompanyID(company_id);
                                u.setCompanyName(c_name);
                                u.setCompanyEmail(c_email);
                                u.setCompanyPhoneNo(c_contact);
                                u.setCompanyAddress(c_address);
                                u.setCompanyCity(c_city);
                                u.setCompanyState(c_state);
                                u.setCompanyPost(c_post);
                                u.setCompanyCountry(c_country);
                            }

                            Utility.changeScene(event, "DashR.fxml");
                        }
                    }
                    else{
                        //System.out.println("event = " + event + ", wrong username = " + username + ",  wrong password = " + password);
                    }
                }
            }
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

    public static void signUp(ActionEvent event, String fxml, int typeID, String username, String password, String fname, String lname, String email, Date dob, String contact, String address, String city, String state, String post, String country, String education, String graduation, String skills, String c_name, String c_email, String c_contact, String c_address, String c_city, String c_state, String c_post, String c_country) {
        Connection connection = DBConnection.getConnection();
        PreparedStatement psInsert = null;
        PreparedStatement psCheck = null;
        ResultSet resultSet = null;

        UserInstance inst = UserInstance.getInstance();

        try {
            psCheck = connection.prepareStatement("SELECT * FROM user WHERE username = ?");
            psCheck.setString(1, username);
            resultSet = psCheck.executeQuery();

            if (resultSet.isBeforeFirst()){
                //user exists prompt
            }
            else{
                psInsert = connection.prepareStatement("INSERT INTO user (username, password, fname, lname, email, dob, contact, address, city, state, post, country, education, graduation, user_type_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, fname);
                psInsert.setString(4, lname);
                psInsert.setString(5, email);
                psInsert.setDate(6, dob);
                psInsert.setString(7, contact);
                psInsert.setString(8, address);
                psInsert.setString(9, city);
                psInsert.setString(10, state);
                psInsert.setString(11, post);
                psInsert.setString(12, country);
                if (!education.isEmpty())
                    psInsert.setString(13, education);
                else
                    psInsert.setNull(13, Types.VARCHAR);
                if (!graduation.isEmpty())
                    psInsert.setString(14, graduation);
                else
                    psInsert.setNull(14, Types.VARCHAR);
                psInsert.setInt(15, typeID);
                psInsert.executeUpdate();

                psCheck = connection.prepareStatement("SELECT user_id FROM user WHERE username = ?");
                psCheck.setString(1, username);
                resultSet = psCheck.executeQuery();
                int user_id = 0;
                while(resultSet.next()){
                    user_id = resultSet.getInt("user_id");
                }

                //skills into new table
                int key_id = 0;
                if (!skills.isEmpty()){
                    String skillsTrim = skills.trim();
                    String[] skillsArray = skillsTrim.split(",");

                    for (int i = 0; i < skillsArray.length; i++){
                        skillsTrim = skillsArray[i].trim();
                        skillsArray[i] = skillsTrim;
                    }

                    for (int i = 0; i < skillsArray.length; i++){
                        psCheck = connection.prepareStatement("SELECT key_id FROM keyword WHERE keyword = ?");
                        psCheck.setString(1, skillsArray[i]);
                        resultSet = psCheck.executeQuery();

                        if (resultSet.isBeforeFirst()){
                            while(resultSet.next()){
                                key_id = resultSet.getInt("key_id");
                            }
                            psInsert = connection.prepareStatement("INSERT INTO user_skill (user_id, key_id) VALUES (?,?)");
                            psInsert.setInt(1, user_id);
                            psInsert.setInt(2, key_id);
                            psInsert.executeUpdate();
                        }
                        else {
                            psInsert = connection.prepareStatement("INSERT INTO keyword (keyword) VALUES (?)");
                            psInsert.setString(1, skillsArray[i]);
                            psInsert.executeUpdate();

                            psInsert = connection.prepareStatement("INSERT INTO user_skill (user_id, key_id) VALUES (?,LAST_INSERT_ID())");
                            psInsert.setInt(1, user_id);
                            psInsert.executeUpdate();
                        }
                    }
                }

                int company_id = 0;
                if (typeID == 2){
                    psCheck = connection.prepareStatement("SELECT company_id FROM company WHERE c_name = ?");
                    psCheck.setString(1, c_name);
                    resultSet = psCheck.executeQuery();

                    if (resultSet.isBeforeFirst()){ //If company exists, connect user to company else create new company entry
                        while(resultSet.next()){
                            company_id = resultSet.getInt("company_id");
                        }
                        psInsert = connection.prepareStatement("INSERT INTO company_involvement (user_id, company_id) VALUES (?,?)");
                        psInsert.setInt(1, user_id);
                        psInsert.setInt(2, company_id);
                        psInsert.executeUpdate();
                    }
                    else{
                        psInsert = connection.prepareStatement("INSERT INTO company (c_name, c_email, c_contact, c_address, c_city, c_state, c_post, c_country) VALUES (?,?,?,?,?,?,?,?)");
                        psInsert.setString(1, c_name);
                        psInsert.setString(2, c_email);
                        psInsert.setString(3, c_contact);
                        psInsert.setString(4, c_address);
                        psInsert.setString(5, c_city);
                        psInsert.setString(6, c_state);
                        psInsert.setString(7, c_post);
                        psInsert.setString(8, c_country);
                        psInsert.executeUpdate();

                        psInsert = connection.prepareStatement("INSERT INTO company_involvement (user_id, company_id) VALUES (?,LAST_INSERT_ID())");
                        psInsert.setInt(1, user_id);
                        psInsert.executeUpdate();

                        psCheck = connection.prepareStatement("SELECT company_id FROM company_involvement WHERE user_id = ?");
                        psCheck.setInt(1, user_id);
                        resultSet = psCheck.executeQuery();

                        while(resultSet.next()){
                            company_id = resultSet.getInt("company_id");
                        }
                    }
                }

                if (typeID == 1){
                    Seeker u = new Seeker();
                    inst.setSeek(u);
                    inst.setType(typeID);

                    u.setUserID(user_id);
                    u.setUserTypeID(typeID);
                    u.setUsername(username);
                    u.setPassword(password);
                    u.setFirstName(fname);
                    u.setLastName(lname);
                    u.setEmail(email);
                    u.setDateOfBirth(dob);
                    u.setPhoneNo(contact);
                    u.setAddress(address);
                    u.setCity(city);
                    u.setState(state);
                    u.setPost(post);
                    u.setCountry(country);
                    u.setEducation(education);
                    u.setGraduation(graduation);
                    u.setSkills(skills.toUpperCase());
                }
                else{
                    Recruiter u = new Recruiter();
                    inst.setRec(u);
                    inst.setType(typeID);

                    u.setUserID(user_id);
                    u.setUserTypeID(typeID);
                    u.setUsername(username);
                    u.setPassword(password);
                    u.setFirstName(fname);
                    u.setLastName(lname);
                    u.setEmail(email);
                    u.setDateOfBirth(dob);
                    u.setPhoneNo(contact);
                    u.setAddress(address);
                    u.setCity(city);
                    u.setState(state);
                    u.setPost(post);
                    u.setCountry(country);
                    u.setEducation(education);
                    u.setGraduation(graduation);
                    u.setSkills(skills.toUpperCase());

                    u.setCompanyID(company_id);
                    u.setCompanyName(c_name);
                    u.setCompanyEmail(c_email);
                    u.setCompanyPhoneNo(c_contact);
                    u.setCompanyAddress(c_address);
                    u.setCompanyCity(c_city);
                    u.setCompanyState(c_state);
                    u.setCompanyPost(c_post);
                    u.setCompanyCountry(c_country);
                }

                Utility.changeScene(event, fxml);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(resultSet != null){
                try{
                    resultSet.close();
                }
                catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if (psCheck != null){
                try{
                    psCheck.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if (psInsert != null){
                try{
                    psInsert.close();
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
