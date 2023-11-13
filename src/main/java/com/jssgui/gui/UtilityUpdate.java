package com.jssgui.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilityUpdate {
    //UserInstance inst = UserInstance.getInstance();

    public static void candidateUp(String appUsername, int user_id, String jobTitle, Boolean pending, Boolean approve){
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement("UPDATE job_activity ja LEFT JOIN `user` u ON u.user_id = ja.user_id LEFT JOIN job_post jp ON jp.job_id = ja.job_id LEFT JOIN company_post cp ON cp.job_id = jp.job_id LEFT JOIN company c ON c.company_id = cp.company_id LEFT JOIN company_involvement ci ON ci.company_id = c.company_id SET ja.ja_pending = ?, ja.ja_approval = ? WHERE u.username = ? AND ci.user_id = ? AND jp.j_title = ?");
            preparedStatement.setBoolean(1, pending);
            preparedStatement.setBoolean(2, approve);

            preparedStatement.setString(3, appUsername);
            preparedStatement.setInt(4, user_id);
            preparedStatement.setString(5, jobTitle);
            preparedStatement.executeUpdate();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
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

    public static void companyUp(String column, String value, int c_ID){
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            switch(column){
                case "c_name":
                    preparedStatement = connection.prepareStatement("UPDATE company SET c_name = ? WHERE company_id = ?");
                    break;
                case "c_email":
                    preparedStatement = connection.prepareStatement("UPDATE company SET c_email = ? WHERE company_id = ?");
                    break;
                case "c_contact":
                    preparedStatement = connection.prepareStatement("UPDATE company SET c_contact = ? WHERE company_id = ?");
                    break;
                case "c_address":
                    preparedStatement = connection.prepareStatement("UPDATE company SET c_address = ? WHERE company_id = ?");
                    break;
                case "c_city":
                    preparedStatement = connection.prepareStatement("UPDATE company SET c_city = ? WHERE company_id = ?");
                    break;
                case "c_state":
                    preparedStatement = connection.prepareStatement("UPDATE company SET c_state = ? WHERE company_id = ?");
                    break;
                case "c_post":
                    preparedStatement = connection.prepareStatement("UPDATE company SET c_post = ? WHERE company_id = ?");
                    break;
                case "c_country":
                    preparedStatement = connection.prepareStatement("UPDATE company SET c_country = ? WHERE company_id = ?");
                    break;
            }
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, c_ID);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally {
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

    public static void jobUp(String j_title, String j_location, String j_salary, String j_content, int user_id, String oldTitle){
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement("UPDATE job_post j LEFT JOIN company_post cp ON cp.job_id = j.job_id LEFT JOIN company c ON c.company_id = cp.company_id LEFT JOIN company_involvement ci ON ci.company_id = c.company_id SET j_title = ?, j_location = ?, j_salary = ?, j_content = ? WHERE ci.user_id = ? AND j.j_title = ?");
            preparedStatement.setString(1, j_title);
            preparedStatement.setString(2, j_location);
            preparedStatement.setInt(3, Integer.parseInt(j_salary));
            preparedStatement.setString(4, j_content);
            preparedStatement.setInt(5, user_id);
            preparedStatement.setString(6, oldTitle);
            preparedStatement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally {
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

    public static void jobVisibilityUp(int user_id, String jobTitle, Boolean jobPublished){
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement("UPDATE job_post j LEFT JOIN company_post cp ON cp.job_id = j.job_id LEFT JOIN company c ON c.company_id = cp.company_id LEFT JOIN company_involvement ci ON ci.company_id = c.company_id SET j_visible = ? WHERE ci.user_id = ? AND j.j_title = ?"); //LEFT JOIN user u ON u.user_id = ci.user_id
            preparedStatement.setBoolean(1, jobPublished);
            preparedStatement.setInt(2, user_id);
            preparedStatement.setString(3, jobTitle);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
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

    public static void profileUp(String column, String value, String username){
        Connection connection = DBConnection.getConnection();
        PreparedStatement preparedStatement = null;

        try{
            switch(column){
                case "fname":
                    preparedStatement = connection.prepareStatement("UPDATE user SET fname = ? WHERE username = ?");
                    break;
                case "lname":
                    preparedStatement = connection.prepareStatement("UPDATE user SET lname = ? WHERE username = ?");
                    break;
                case "password":
                    preparedStatement = connection.prepareStatement("UPDATE user SET password = ? WHERE username = ?");
                    break;
                case "email":
                    preparedStatement = connection.prepareStatement("UPDATE user SET email = ? WHERE username = ?");
                    break;
                case "contact":
                    preparedStatement = connection.prepareStatement("UPDATE user SET contact = ? WHERE username = ?");
                    break;
                case "address":
                    preparedStatement = connection.prepareStatement("UPDATE user SET address = ? WHERE username = ?");
                    break;
                case "city":
                    preparedStatement = connection.prepareStatement("UPDATE user SET city = ? WHERE username = ?");
                    break;
                case "state":
                    preparedStatement = connection.prepareStatement("UPDATE user SET state = ? WHERE username = ?");
                    break;
                case "post":
                    preparedStatement = connection.prepareStatement("UPDATE user SET post = ? WHERE username = ?");
                    break;
                case "country":
                    preparedStatement = connection.prepareStatement("UPDATE user SET country = ? WHERE username = ?");
                    break;
                case "education":
                    preparedStatement = connection.prepareStatement("UPDATE user SET education = ? WHERE username = ?");
                    break;
                case "graduation":
                    preparedStatement = connection.prepareStatement("UPDATE user SET graduation = ? WHERE username = ?");
                    break;
            }
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        finally {
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

    public static void skillUp(int user_id, String skills){
        Connection connection = DBConnection.getConnection();
        PreparedStatement psInsert = null;
        PreparedStatement psCheck = null;
        ResultSet resultSet = null;

        int key_id = 0;
        String skillsTrim = skills.trim();
        String[] skillsArray = skillsTrim.split(",");

        for (int i = 0; i < skillsArray.length; i++){
            skillsTrim = skillsArray[i].trim();
            skillsArray[i] = skillsTrim;
        }

        try{
            psCheck = connection.prepareStatement("DELETE FROM user_skill WHERE user_id = ?");
            psCheck.setInt(1, user_id);
            psCheck.executeUpdate();

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
        catch(SQLException e){
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

    public static void skillJobUp(int user_id, String skills, String jobTitle){
        Connection connection = DBConnection.getConnection();
        PreparedStatement psInsert = null;
        PreparedStatement psCheck = null;
        ResultSet resultSet = null;

        int key_id = 0;
        String skillsTrim = skills.trim();
        String[] skillsArray = skillsTrim.split(",");

        for (int i = 0; i < skillsArray.length; i++){
            skillsTrim = skillsArray[i].trim();
            skillsArray[i] = skillsTrim;
        }

        try{
            psCheck = connection.prepareStatement("SELECT j.job_id FROM job_post j LEFT JOIN company_post cp ON cp.job_id = j.job_id LEFT JOIN company c ON c.company_id = cp.company_id LEFT JOIN company_involvement ci ON ci.company_id = c.company_id WHERE ci.user_id = ? AND j.j_title = ?");
            psCheck.setInt(1, user_id);
            psCheck.setString(2, jobTitle);
            resultSet = psCheck.executeQuery();

            while(resultSet.next()){
                int job_id = resultSet.getInt("job_id");

                psCheck = connection.prepareStatement("DELETE FROM job_skill WHERE job_id = ?");
                psCheck.setInt(1, job_id);
                psCheck.executeUpdate();

                for (int i = 0; i < skillsArray.length; i++){
                    psCheck = connection.prepareStatement("SELECT key_id FROM keyword WHERE keyword = ?");
                    psCheck.setString(1, skillsArray[i]);
                    resultSet = psCheck.executeQuery();

                    if (resultSet.isBeforeFirst()){
                        while(resultSet.next()){
                            key_id = resultSet.getInt("key_id");
                        }
                        psInsert = connection.prepareStatement("INSERT INTO job_skill (job_id, key_id) VALUES (?,?)");
                        psInsert.setInt(1, job_id);
                        psInsert.setInt(2, key_id);
                        psInsert.executeUpdate();
                    }
                    else {
                        psInsert = connection.prepareStatement("INSERT INTO keyword (keyword) VALUES (?)");
                        psInsert.setString(1, skillsArray[i]);
                        psInsert.executeUpdate();

                        psInsert = connection.prepareStatement("INSERT INTO user_skill (user_id, key_id) VALUES (?,LAST_INSERT_ID())");
                        psInsert.setInt(1, job_id);
                        psInsert.executeUpdate();
                    }
                }
            }
        }
        catch(SQLException e){
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
