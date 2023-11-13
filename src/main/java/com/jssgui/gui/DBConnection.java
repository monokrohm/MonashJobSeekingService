package com.jssgui.gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String url = "jdbc:mysql://localhost:3306/monjs";
    private static String user = "root";
    private static String pw = "sql123";
    private static Connection con;

    public static Connection getConnection(){
        try {
            con = DriverManager.getConnection(url, user, pw);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
}
