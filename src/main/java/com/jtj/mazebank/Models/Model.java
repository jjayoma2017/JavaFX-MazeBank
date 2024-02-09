package com.jtj.mazebank.Models;

import com.jtj.mazebank.Views.AccountType;
import com.jtj.mazebank.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Model {
    private  static Model model;
    private final ViewFactory viewFactory;
    private final  DatabaseDriver databaseDriver;
    // Client Data Section
    private  Client client;
    private  boolean clientLoginSuccessFlag;
    // Admin Data Section
    private  boolean adminLoginSuccessFlag;
    private final ObservableList<Client> clients;

    private Model(){
        this.viewFactory = new ViewFactory(); // Singleton
        this.databaseDriver = new DatabaseDriver();
        // Client Section
        this.clientLoginSuccessFlag = false;
        this.adminLoginSuccessFlag = false;
        this.client = new Client("","","",null,null,null);
        this.clients = FXCollections.observableArrayList();
        // Admin Section
    }

    public  static  synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return  model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public DatabaseDriver getDatabaseDriver() {
        return databaseDriver;
    }


    /*
     * Client Method Section
     * */
    public boolean getClientLoginSuccessFlag() {
        return this.clientLoginSuccessFlag;
    }

    public void setClientLoginSuccessFlag(boolean clientLoginSuccessFlag) {
        this.clientLoginSuccessFlag = clientLoginSuccessFlag;
    }


    public Client getClient() {
        return client;
    }

    public  void  evaluateClientCred(String pAddress, String password){
        CheckingAccount checkingAccount;
        SavingsAccount savingsAccount;
        ResultSet resultSet = databaseDriver.getClientData(pAddress,password);
        try {
            if(resultSet.isBeforeFirst()) { // if cursor in first row or has records/data
                this.client.firstNameProperty().set(resultSet.getString("FirstName"));
                this.client.lastNameProperty().set(resultSet.getString("LastName"));
                this.client.payeeAddressProperty().set(resultSet.getString("PayeeAddress"));
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                this.client.dateCreatedProperty().set(date);
                checkingAccount = getCheckingAccount(pAddress);
                savingsAccount = getSavingsAccount(pAddress);
                this.client.checkingAccountProperty().set(checkingAccount);
                this.client.savingsAccountProperty().set(savingsAccount);
                this.clientLoginSuccessFlag = true;
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    /*
     * Admin Method Section
     * */
    public boolean getAdminLoginSuccessFlag() {
        return adminLoginSuccessFlag;
    }

    public void setAdminLoginSuccessFlag(boolean adminLoginSuccessFlag) {
        this.adminLoginSuccessFlag = adminLoginSuccessFlag;
    }

    public  void evaluateAdminCred(String username, String password){
        ResultSet resultSet = databaseDriver.getAdminData(username,password);
        try {
            if(resultSet.isBeforeFirst()){
                this.adminLoginSuccessFlag = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public void setClients() {
        CheckingAccount checkingAccount;
        SavingsAccount savingsAccount;
        ResultSet resultSet = databaseDriver.getAllClientsData();
        try {
            while (resultSet.next()){
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("LastName");
                String pAddress = resultSet.getString("PayeeAddress");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                checkingAccount = getCheckingAccount(pAddress);
                savingsAccount = getSavingsAccount(pAddress);
                clients.add(new Client(fName,lName,pAddress,checkingAccount,savingsAccount,date));
            }
        }
        catch (SQLException e){
            throw  new RuntimeException(e);
        }

    }

    public CheckingAccount getCheckingAccount(String pAddress){
        CheckingAccount account = null;
        ResultSet resultSet = databaseDriver.getCheckingAccountData(pAddress);
        try {
            String num = resultSet.getString("AccountNumber");
            int tLimit = (int) resultSet.getDouble("TransactionLimit");
            double balance = resultSet.getDouble("Balance");
            account = new CheckingAccount(pAddress,num,balance,tLimit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  account;
    }

    public SavingsAccount getSavingsAccount(String pAddress){
        SavingsAccount account = null;
        ResultSet resultSet = databaseDriver.getSavingsAccountData(pAddress);
        try {
            String num = resultSet.getString("AccountNumber");
            int wLimit = (int) resultSet.getDouble("WithdrawalLimit");
            double balance = resultSet.getDouble("Balance");
            account = new SavingsAccount(pAddress,num,balance,wLimit);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  account;
    }

    public ObservableList<Client> searchClient(String pAddress){
        ObservableList<Client> searchResults = FXCollections.observableArrayList();
        try (ResultSet resultSet = databaseDriver.searchClient(pAddress)) {
            try {
                CheckingAccount checkingAccount = getCheckingAccount(pAddress);
                SavingsAccount savingsAccount = getSavingsAccount(pAddress);
                String fName = resultSet.getString("FirstName");
                String lName = resultSet.getString("LastName");
                String[] dateParts = resultSet.getString("Date").split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                searchResults.add(new Client(fName, lName, pAddress, checkingAccount, savingsAccount, date));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException e){
            throw  new RuntimeException(e);
        }
        return  searchResults;
    }

}
