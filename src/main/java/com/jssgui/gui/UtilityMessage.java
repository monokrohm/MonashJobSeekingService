package com.jssgui.gui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilityMessage {
    public static void sendMessage(String receiverEmail, int sender_id, String content, int messageType){
        Connection connection = DBConnection.getConnection();
        PreparedStatement psCheck = null;
        PreparedStatement psInsert = null;
        ResultSet resultSet = null;

        try{
            psCheck = connection.prepareStatement("SELECT * FROM user WHERE email = ?");
            psCheck.setString(1, receiverEmail);
            resultSet = psCheck.executeQuery();

            if (!resultSet.isBeforeFirst()){
                //user doesnt exist
            }
            else{
                while(resultSet.next()) {
                    int receiver_id = resultSet.getInt("user_id");
                    psInsert = connection.prepareStatement("INSERT  INTO message(message_type_id, m_sender_id, m_content, m_read) VALUES (?,?,?,?)");
                    psInsert.setInt(1, messageType);
                    psInsert.setInt(2, sender_id);
                    if (content == null)
                        psInsert.setString(3, " ");
                    else
                        psInsert.setString(3, content);
                    psInsert.setInt(4, 0);
                    psInsert.executeUpdate();

                    psInsert = connection.prepareStatement("INSERT INTO message_inbox(message_id, user_id) VALUES (LAST_INSERT_ID(),?)");
                    psInsert.setInt(1, receiver_id);
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
}
