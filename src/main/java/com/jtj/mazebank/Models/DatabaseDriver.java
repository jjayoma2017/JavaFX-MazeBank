package com.jtj.mazebank.Models;

import java.sql.*;

public class DatabaseDriver {
    private Connection conn;

    public  DatabaseDriver(){
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:mazebank.db");
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /*
    *  Client Section
    * */
    public ResultSet getClientData(String pAddress, String password){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("Select * FROM Clients where PayeeAddress='" + pAddress +"' AND  Password='" +password + "' ");
        }
        catch (SQLException e){
            throw  new RuntimeException(e);
        }
        return  resultSet;
    }

    /*
    * Admin Section
    * */
    public ResultSet getAdminData(String username, String password){
        Statement statement;
        ResultSet resultSet = null;
        try{
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("Select * FROM Admins where Username='" + username +"' AND  Password='" +password + "' ");
        }
        catch (SQLException e){
            throw  new RuntimeException(e);
        }
        return  resultSet;
    }
    /*
    * Utility Methods
    * */
}
