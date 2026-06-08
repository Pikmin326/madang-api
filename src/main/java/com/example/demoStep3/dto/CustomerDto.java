package com.example.demoStep3.dto;

//package kr.co.hanbit.demo123456.dto;

public class CustomerDto {
    private int custid;
    private String name;
    private String address;

    public CustomerDto(int id, String name, String address) {
        this.custid = id;
        this.name = name;
        this.address = address;
    }

    // Getter
    public int getCustid() {
        return custid;
    }
    public String getAddress() { return address; }
    public String getName() {
        return name;
    }
}
