package com.smartP.entities;

import jakarta.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "seller", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        }),
        @UniqueConstraint(columnNames = {
                "contact"
        })
})
    public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Pattern(regexp="^[a-zA-Z]*$",message="Name can only have letters")
    @NotNull(message = "Name is required")
    private String name;
    @Pattern(regexp="^[A-Z0-9]*$",message="ID number can only have letters and numbers")
    @NotNull(message = "Id number is required")
    private String idnumber;
    @Pattern(regexp="^[a-zA-Z]*$",message="Address can only have letters")
    @NotNull(message = "Address is required")
    private String address;
    @Column(unique = true, name="contact")
    private String contact;
    @Pattern(regexp="^[0-9]*$",message="Pin can only have numbers")
    @NotNull(message = "PIN is required")
    private String pin;
    public Seller(){}

    public Seller(String name, String idnumber, String address, String contact, String pin) {
        this.name = name;
        this.idnumber = idnumber;
        this.address = address;
        this.contact = contact;
        this.pin = pin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
