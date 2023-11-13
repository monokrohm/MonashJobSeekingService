package com.jssgui.gui;

import java.sql.*;
import java.time.LocalDate;

public class UtilityJobs {
    public static void createJob(int poster_id, String j_title, String j_company, String j_location, int j_salary, String j_content, Boolean j_visible, String requiredSkills, String category) {
        Connection connection = DBConnection.getConnection();
        PreparedStatement psInsert = null;
        PreparedStatement psCheck = null;
        ResultSet resultSet = null;

        try {
            psCheck = connection.prepareStatement("SELECT * FROM job_post WHERE j_title = ? AND j_company = ?");
            psCheck.setString(1, j_title);
            psCheck.setString(2, j_company);
            resultSet = psCheck.executeQuery();

            if (resultSet.isBeforeFirst()){
                //error duplicate title for company
            }
            else{
                psInsert = connection.prepareStatement("INSERT INTO job_post (j_poster_id, j_title, j_company, j_location, j_salary, j_content, j_created, j_expiry, j_visible) VALUES (?,?,?,?,?,?,?,?,?)");
                psInsert.setInt(1, poster_id);
                psInsert.setString(2, j_title);
                psInsert.setString(3, j_company);
                psInsert.setString(4, j_location);
                psInsert.setInt(5, j_salary);
                psInsert.setString(6, j_content);
                psInsert.setDate(7, Date.valueOf(LocalDate.now()));
                psInsert.setDate(8, Date.valueOf(LocalDate.now()));
                psInsert.setBoolean(9, j_visible);
                psInsert.executeUpdate();

                psCheck = connection.prepareStatement("SELECT company_id FROM company WHERE c_name = ?");
                psCheck.setString(1, j_company);
                resultSet = psCheck.executeQuery();
                int company_id = 0;

                while(resultSet.next()){
                    company_id = resultSet.getInt("company_id");
                }

                psInsert = connection.prepareStatement("INSERT INTO company_post(job_id, company_id) VALUES (LAST_INSERT_ID(),?)");
                psInsert.setInt(1, company_id);
                psInsert.executeUpdate();

                psCheck = connection.prepareStatement("SELECT job_id FROM company_post WHERE company_id = ?");
                psCheck.setInt(1, company_id);
                resultSet = psCheck.executeQuery();
                int job_id = 0;

                while(resultSet.next()){
                    job_id = resultSet.getInt("job_id");
                }

                int key_id = 0;
                String skillsTrim = requiredSkills.trim();
                String[] skillsArray = skillsTrim.split(",");

                for (int i = 0; i < skillsArray.length; i++){
                    skillsTrim = skillsArray[i].trim();
                    skillsArray[i] = skillsTrim;
                }

                for (int i = 0; i < skillsArray.length; i++){
                    psCheck = connection.prepareStatement("SELECT key_id, keyword FROM keyword WHERE keyword = ?");
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

                        psInsert = connection.prepareStatement("INSERT INTO job_skill (job_id, key_id) VALUES (?,LAST_INSERT_ID())");
                        psInsert.setInt(1, job_id);
                        psInsert.executeUpdate();
                    }
                }

                psCheck = connection.prepareStatement("SELECT * FROM category WHERE cat_name = ?");
                psCheck.setString(1, category);
                resultSet = psCheck.executeQuery();
                int category_id = 0;
                if (resultSet.isBeforeFirst()){
                    while(resultSet.next()){
                        category_id = resultSet.getInt("category_id");
                    }
                    psInsert = connection.prepareStatement("INSERT INTO job_category (job_id, category_id) VALUES (?,?)");
                    psInsert.setInt(1, job_id);
                    psInsert.setInt(2, category_id);
                    psInsert.executeUpdate();
                }
                else {
                    psInsert = connection.prepareStatement("INSERT INTO category (cat_name) VALUES (?)");
                    psInsert.setString(1, category);
                    psInsert.executeUpdate();

                    psInsert = connection.prepareStatement("INSERT INTO job_category (job_id, category_id) VALUES (?,LAST_INSERT_ID())");
                    psInsert.setInt(1, job_id);
                    psInsert.executeUpdate();
                }
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


