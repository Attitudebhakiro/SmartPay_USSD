package com.smartP;

import com.smartP.entities.Seller;
import com.smartP.service.SellerService;
import com.smartP.service.TransactionService;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to SmartPay Solution");
        Main testHelloAge = new Main();

//        testHelloAge.geHelloAge("0774632000");
        // seller admin
        //0774333222
        //0776888445

        //2024-02-26 01:17:09
     SellerService sellerService = new SellerService();

     try {
         Scanner scn = new Scanner(System.in);
         System.out.println("Choose your option");
         System.out.println("1. Register as a Seller");
         System.out.println("2. Airtime USD");
         System.out.println("3. Airtime ZWL");
         System.out.println("4. Data Bundles");
         int opt = scn.nextInt();
         if(opt ==1){
              System.out.println(sellerService.registerSeller());
         }else if(opt==2){
             System.out.println("Send USD_AIRTIME to");
             System.out.println("1. Registered Seller's account");
             System.out.println("2. Airtime Wallet");
             int opt2 = scn.nextInt();
             if(opt2 == 1){
                 System.out.println(new TransactionService().transaction("USD_AIRTIME","AIRTIME","USD", 1));
             } else if (opt2==2) {
                 System.out.println("Enter Recipient Number");
             }else {
                 System.out.println("Invalid Option");
             }

         }else if(opt==3){
             System.out.println("Send ZWL_AIRTIME to");
             System.out.println("1. Registered Seller's account");
             System.out.println("2. Airtime Wallet");
             int opt3 = scn.nextInt();
             if(opt3 == 1){
                 System.out.println(new TransactionService().transaction("ZWL_AIRTIME","AIRTIME","ZWL", 1));
             } else if (opt3==2) {
                 System.out.println("Enter Recipient Number");
             }else {
                 System.out.println("Invalid Option");
             }

         }else{
             System.out.println("Invalid Option");
         }
     }catch (Exception e){
         System.out.println("Invalid Option");
     }
    }

        public void geHelloAge(String contact) {
            String wsURL = "http://localhost:8091/soapWS";
            URL url = null;
            URLConnection connection = null;
            HttpURLConnection httpConn = null;
            String responseString = null;
            String outputString="";
            OutputStream out = null;
            InputStreamReader isr = null;
            BufferedReader in = null;

            String xmlInput =
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hel=\"http://techprimers.com/spring-boot-soap-example\">" +
                            "<soapenv:Header/>" +
                            "<soapenv:Body>" +
                            "<hel:getUserRequest>"+
                            "<hel:contact>"+ contact + "</hel:contact>"+
                            "</hel:getUserRequest>"+
                            "</soapenv:Body>" +
                            "</soapenv:Envelope>";

            try
            {
                url = new URL(wsURL);
                connection = url.openConnection();
                httpConn = (HttpURLConnection) connection;

                byte[] buffer = new byte[xmlInput.length()];
                buffer = xmlInput.getBytes();

                String SOAPAction = "";
                // Set the appropriate HTTP parameters.
                httpConn.setRequestProperty("Content-Length", String
                        .valueOf(buffer.length));

                httpConn.setRequestProperty("Content-Type", "text/xml; charset=iso-8859-1");


                httpConn.setRequestProperty("SOAPAction", SOAPAction);
                httpConn.setRequestMethod("POST");
                httpConn.setDoOutput(true);
                httpConn.setDoInput(true);
                out = httpConn.getOutputStream();
                out.write(buffer);
                out.close();

                // Read the response and write it to standard out.
                isr = new InputStreamReader(httpConn.getInputStream());
                in = new BufferedReader(isr);

                while ((responseString = in.readLine()) != null)
                {
                    outputString = outputString + responseString;
                }
                System.out.println(outputString);
                System.out.println("");

                // Get the response from the web service call
                Document document = parseXmlFile(outputString);
                NodeList nodeLst = document.getElementsByTagName("ns2:airtime");
                String webServiceResponse = nodeLst.item(0).getTextContent();
                System.out.println("The response from the web service call is : " + webServiceResponse);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        private Document parseXmlFile(String in) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(in));
                return (Document) db.parse(is);
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


}