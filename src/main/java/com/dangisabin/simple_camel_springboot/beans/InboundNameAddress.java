package com.dangisabin.simple_camel_springboot.beans;

import lombok.Data;

@Data
public class InboundNameAddress {
    private String name;
    private String houseNumber;
    private String city;
    private String province;
    private String postalCode;
}
