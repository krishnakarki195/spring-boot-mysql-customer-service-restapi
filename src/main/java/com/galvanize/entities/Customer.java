package com.galvanize.entities;

import javax.persistence.*;

@Entity(name = "customers")
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name")
    String first;

    @Column(name = "last_name")
    String last;

    @Column(length = 50)
    String address;

    @Column(length = 50)
    String city;

    @Column(length = 2)
    String state;

    @Column(length = 5)
    String zip;

    @Column(unique = true)
    String telephone;

    public Customer() {
    }

    public Customer(String first, String last, String address, String city, String state, String zip) {
        this.first = first;
        this.last = last;
        this.address = address;
        this.city = city;
        this.state = state.toUpperCase();
        this.zip = zip;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", first='" + first + '\'' +
                ", last='" + last + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state.toUpperCase();
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
