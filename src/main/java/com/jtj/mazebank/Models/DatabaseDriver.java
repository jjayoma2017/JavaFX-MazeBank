package com.jtj.mazebank.Models;

import java.sql.*;
import java.time.LocalDate;

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

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date){
        Statement statement;
        try{
            statement = this.conn.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "Clients (FirstName, LastName, PayeeAddress, Password, Date) " +
                    "VALUES ('" + fName + "' , '" + lName + "', '" + pAddress + "','" + password + "','" + date.toString() + "')");
        }
        catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance){
        Statement statement;
        try{
            statement = this.conn.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance) " +
                    "VALUES ('" + owner + "' , '" + number + "', '" + tLimit + "','" + balance + "')");
        }
        catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance){
        Statement statement;
        try{
            statement = this.conn.createStatement();
            statement.executeUpdate("INSERT INTO " +
                    "SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance) " +
                    "VALUES ('" + owner + "' , '" + number + "', '" + wLimit + "','" + balance + "')");
        }
        catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    /*
    * Utility Methods
    * */
    public int getLastClientsId(){
        Statement statement;
        ResultSet resultSet = null;
        int  id=0;
        try{
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery("Select * FROM sqlite_sequence where name='Clients';");
            id = resultSet.getInt("seq");
        }
        catch (SQLException e){
            throw  new RuntimeException(e);
        }
        return  id;
    }
}
