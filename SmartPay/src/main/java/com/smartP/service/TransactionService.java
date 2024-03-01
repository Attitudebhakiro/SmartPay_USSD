package com.smartP.service;

import com.mysql.cj.jdbc.result.UpdatableResultSet;
import com.smartP.entities.Package;

import java.awt.event.PaintEvent;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public final class TransactionService {
    private static final Map<String, Package> packages = new HashMap<>();
    private String url = "jdbc:mysql://localhost:3306/smartPay";
    private String username = "root";
    private String password = "Bacca@27";

    String saveTsql = "INSERT INTO transaction (type, currency, packageName, price, amount ,profit,purchaseDate, expiryDate, contactFrom, contactTo) VALUES(?,?,?,?,?,?,?,?,?,?)";
    String psql = "INSERT INTO package (name,type ,currency, price,amount, purchaseDate, expiryDate, contact) VALUES(?,?,?,?,?,?,?,?)";
    String checksql = "SELECT name FROM seller WHERE contact = ?";
    String checkPackagesql = "SELECT amount FROM package WHERE contact = ? AND name = ? ";
    String deductPackagesql = "UPDATE package SET amount = ? WHERE contact = ? AND name = ? ";
    //get user input
    public  String transaction(String name, String type, String currency, double price){
      try{
          Scanner scanner = new Scanner(System.in);
          System.out.println("Enter Your Number");
          String sender = scanner.next();

          System.out.println("Enter Recipient Number");
          String receiver = scanner.next();
          System.out.println("Enter Amount for "+name);
          Double amount = scanner.nextDouble();

          Double profit = 0.05* amount;
          LocalDateTime today = LocalDateTime.now();
          Period days = Period.ofDays(180);
          LocalDateTime expiry = today.plus(days);
          String expiryDate = expiry.toString();

          String purchaseDate = String.valueOf(LocalDateTime.now());

          return new TransactionService().saveTransaction(sender,receiver,name,type,currency,price, amount, profit,purchaseDate, expiryDate);
      }catch (Exception e){
          System.out.println(e.getMessage());
          return "Invalid Input";
      }

    }
  // save transaction
    public String saveTransaction(String sender, String receiver, String pname, String type, String currency, Double price,Double amount, Double profit, String purchaseDate, String expiryDate ){
     try(Connection con =DriverManager.getConnection(url,username,password);
         PreparedStatement checkSender = con.prepareStatement(checksql);
         PreparedStatement checkReceiver = con.prepareStatement(checksql);
         PreparedStatement savePackage = con.prepareStatement(psql);
         PreparedStatement saveTransaction = con.prepareStatement(saveTsql);
         PreparedStatement checkPackage = con.prepareStatement(checkPackagesql);
         PreparedStatement deductPackage = con.prepareStatement(deductPackagesql)
     ){
         //check if contact is registered
         checkSender.setString(1, sender);
         ResultSet rs1 = checkSender.executeQuery();
              // rs1.next();
             // System.out.println("rs1 "+rs1.getString("name"));
         checkReceiver.setString(1,receiver);
         ResultSet rs2 = checkReceiver.executeQuery();
           //  rs2.next();
         if(rs1.next() && rs2.next() && rs1 != null && rs2 != null){
             //check if package is sufficient
             checkPackage.setString(1, sender);
             checkPackage.setString(2,pname);
             ResultSet rs3 = checkPackage.executeQuery();

             if(rs3.next() && rs3 != null){
                 double balance = rs3.getDouble("amount");
                 double newAmount = balance - amount;
                 if(newAmount>=0 ) {
                     //deduct from sender
                     deductPackage.setDouble(1,newAmount);
                     deductPackage.setString(2,sender);
                     deductPackage.setString(3,pname);
                     int pdeducted = deductPackage.executeUpdate();
                     //save package
                     savePackage.setString(1, pname);
                     savePackage.setString(2, type);
                     savePackage.setString(3, currency);
                     savePackage.setDouble(4, price);
                     savePackage.setDouble(5, amount);
                     savePackage.setString(6, purchaseDate);
                     savePackage.setString(7, expiryDate);
                     savePackage.setString(8, receiver);
                     int psaved = savePackage.executeUpdate();

                     //save transaction
                     saveTransaction.setString(1, type);
                     saveTransaction.setString(2, currency);
                     saveTransaction.setString(3, pname);
                     saveTransaction.setDouble(4, price);
                     saveTransaction.setDouble(5, amount);
                     saveTransaction.setDouble(6, profit);
                     saveTransaction.setString(7,purchaseDate);
                     saveTransaction.setString(8, expiryDate);
                     saveTransaction.setString(9, sender);
                     saveTransaction.setString(10, receiver);
                     int tsaved = saveTransaction.executeUpdate();
                     if(pdeducted ==1 && psaved==1 && tsaved ==1){
                         return "Transfer of "+currency+" "+ amount+ " "+ pname+ " was successful";
                     }else{
                         return  "Network Failed";
                     }
                 }else{
                     return "Insufficient balance";
                 }
             }else{
                 return "Insufficient balance";
             }


         }else{
            return "Both numbers must be registered";
         }
     } catch (SQLException e) {
         System.out.println(e.getMessage());
         return  "Network Failed";
     }
    }

  //  @PostConstruct
    public void initialize() {
        Package airtimeUSD = new Package();
        airtimeUSD.setName("USD_AIRTIME");
        airtimeUSD.setType("AIRTIME");
        airtimeUSD.setCurrency("USD");
        airtimeUSD.setPrice(1);

        Package airtimeZWL = new Package();
        airtimeZWL.setName("ZWL_AIRTIME");
        airtimeZWL.setType("AIRTIME");
        airtimeZWL.setCurrency("ZWL");
        airtimeZWL.setPrice(1);

        packages.put(airtimeUSD.getName(), airtimeUSD);
        packages.put(airtimeZWL.getName(), airtimeZWL);

    }
    public Package getPackages(String name){
        return  packages.get(name);

    }

}
