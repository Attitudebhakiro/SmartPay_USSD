package com.smartP.service;

import com.smartP.entities.Seller;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

//import static jdk.internal.net.http.common.Utils.encode;


public final class SellerService {
   // register a seller
 //  BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private String url = "jdbc:mysql://localhost:3306/smartPay";
    private String username = "root";
    private String password = "Bacca@27";
    String sql = "INSERT INTO seller (name, idnumber, address, contact, pin) VALUES(?,?,?,?,?)";
    public  String registerSeller(){
        // taking registration input
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter First Name");
            String fname = sc.next();
            System.out.println("Enter Last Name");
            String lname = sc.next();
            String name = fname+ " " +lname;

            System.out.println("Enter ID Number");
            String idnumber = sc.next();


            System.out.println("Enter Address");
            String address = sc.next();

            System.out.println("Enter Contact");
            String contact = sc.next();

            System.out.println("Enter Pin");
            int pin = sc.nextInt();
            String pn = String.valueOf(pin);
            if( String.valueOf(pin).length() != 4){
                return "Invalid PIN. Please enter 4 digits";
            }else if(name.length()<= 5){
                return  "Invalid Name";
            }else if(idnumber.length() <= 5){
                return "Invalid ID Number";
            }else{
            return new SellerService().register(name,idnumber,address,contact,pn);
               // return "Success";
            }

        }catch (Exception exception){
            //System.out.println(exception.getMessage());
            return "Invalid Input";
        }

    }
    public String register(String name, String idnumber, String address, String contact, String pn) {
        try(Connection con =  DriverManager.getConnection(url,username,password);
           PreparedStatement ps = con.prepareStatement(sql)
        ){
            Seller seller = new Seller();

                 // saving to Db
                ps.setString(1,name);
                ps.setString(2,idnumber);
                ps.setString(3,address);
                ps.setString(4,contact);
                ps.setString(5, pn);
                ps.executeUpdate();
                return "Registration was Successful";

        }

        catch (SQLException sqlexception){
           // System.out.println(sqlexception.getMessage());
            return "Seller already exist";
        }
    }
    public SellerService(){

    }
}
