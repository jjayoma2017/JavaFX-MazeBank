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

    public ResultSet getTransactions(String pAddress, int limit){
        return queryRecords("Select * FROM transactions where Sender='" + pAddress +"' OR  Receiver='" +pAddress + "' LIMIT '" + limit + "' ");
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

    // Method returns savings account balance
    public double getSavingsAccountBalance(String pAddress) throws SQLException {
        ResultSet resultSet = queryRecords("Select * from SavingsAccounts where owner='" +pAddress+ "'; ");
        return resultSet.getDouble("Balance");
    }

    // Method to either add or subtract from balance
    public void updateBalance(String pAddress, double amount, String operation) throws SQLException {
        ResultSet resultSet = queryRecords("Select * from SavingsAccounts where owner='" +pAddress+ "'; ");
        double newBalance;
        if(operation.equals("ADD")){
            newBalance = resultSet.getDouble("Balance") + amount;
            executeUpdate("Update SavingsAccounts SET Balance='" + newBalance + "' WHERE Owner = '" + pAddress + "'  ");
        }
        else {
            if(resultSet.getDouble("Balance") >= amount){
                newBalance = resultSet.getDouble("Balance") - amount;
                executeUpdate("Update SavingsAccounts SET Balance='" + newBalance + "' WHERE Owner = '" + pAddress + "'  ");
            }
        }
    }

    // Creates and records new transaction
    public void newTransaction(String sender, String receiver, double amount, String message){
        LocalDate date = LocalDate.now();
        executeUpdate("INSERT INTO " +
                "Transactions (Sender, Receiver, Amount, Date, Message) " +
                "VALUES ('" + sender + "' , '" + receiver + "', '" + amount + "','" + date + "','" + message + "')");
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
