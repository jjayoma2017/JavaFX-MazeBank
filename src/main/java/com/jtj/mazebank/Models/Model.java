package com.jtj.mazebank.Models;

import com.jtj.mazebank.Views.AccountType;
import com.jtj.mazebank.Views.ViewFactory;

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

    private Model(){
        this.viewFactory = new ViewFactory(); // Singleton
        this.databaseDriver = new DatabaseDriver();
        // Client Section
        this.clientLoginSuccessFlag = false;
        this.adminLoginSuccessFlag = false;
        this.client = new Client("","","",null,null,null);
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
                this.clientLoginSuccessFlag = true;
            }
        }
        catch (Exception e){

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

}
