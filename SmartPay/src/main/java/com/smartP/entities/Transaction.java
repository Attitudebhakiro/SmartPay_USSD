package com.smartP.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private long id;
    private String type;
    private String currency;
    private String packageName;
    private double price;
    private double amount;
    private double profit;
    private LocalDateTime purchaseDate;
    private  String expiryDate;
    private String contactFrom;
    private String contactTo;

}
