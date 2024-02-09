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
        return queryRecords("Select * FROM Clients where PayeeAddress='" + pAddress +"' AND  Password='" +password + "' ");
    }

    /*
    * Admin Section
    * */
    public ResultSet getAdminData(String username, String password){
        return queryRecords("Select * FROM Admins where Username='" + username +"' AND  Password='" +password + "' ");
    }

    public void createClient(String fName, String lName, String pAddress, String password, LocalDate date){
        executeUpdate("INSERT INTO " +
                "Clients (FirstName, LastName, PayeeAddress, Password, Date) " +
                "VALUES ('" + fName + "' , '" + lName + "', '" + pAddress + "','" + password + "','" + date.toString() + "')");
    }

    public void createCheckingAccount(String owner, String number, double tLimit, double balance){
        executeUpdate("INSERT INTO " +
                "CheckingAccounts (Owner, AccountNumber, TransactionLimit, Balance) " +
                "VALUES ('" + owner + "' , '" + number + "', '" + tLimit + "','" + balance + "')");
    }

    public void createSavingsAccount(String owner, String number, double wLimit, double balance){
        executeUpdate("INSERT INTO " +
                "SavingsAccounts (Owner, AccountNumber, WithdrawalLimit, Balance) " +
                "VALUES ('" + owner + "' , '" + number + "', '" + wLimit + "','" + balance + "')");
    }

    /*
    * Utility Methods
    * */
    public int getLastClientsId(){
        ResultSet resultSet = null;
        int id = 0;
        try{
            resultSet = queryRecords("Select * FROM sqlite_sequence where name='Clients';");
             id = resultSet.getInt("seq");
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return  id;
    }

    public ResultSet getAllClientsData(){
        return queryRecords("Select * from Clients;");
    }

    public ResultSet getCheckingAccountData(String pAddress){
       return queryRecords("Select * from CheckingAccounts where Owner='" +pAddress+ "'; ");
    }

    public ResultSet getSavingsAccountData(String pAddress){
        return queryRecords("Select * from SavingsAccounts where Owner='" +pAddress+ "'; ");
    }

    public ResultSet searchClient(String pAddress){
        return queryRecords("Select * from Clients where PayeeAddress='" +pAddress+ "'; ");
    }

    public void DepositSavings(String pAddress, double amount){
        executeUpdate("Update SavingsAccounts SET Balance='" + amount + "' WHERE Owner = '" + pAddress + "'  ");
    }

    public ResultSet queryRecords(String queryStr){
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = this.conn.createStatement();
            resultSet = statement.executeQuery(queryStr);
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        return  resultSet;
    }

    public void executeUpdate(String queryStr){
        Statement statement;
        try{
            statement = this.conn.createStatement();
            statement.executeUpdate(queryStr);
        }
        catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }
}
