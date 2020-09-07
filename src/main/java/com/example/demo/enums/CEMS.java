package com.example.demo.enums;

public enum CEMS {
    CEMS_in_NOX("CEMS_in_NOX"),
    CEMS_in_SO2("CEMS_in_SO2"),
    CEMS_in_flux("CEMS_in_flux"),
    CEMS_in_O2("CEMS_in_flux"),
    CEMS_in_temp("CEMS_in_flux");

    private String str;

    public String getStr() {
        return str;
    }


    CEMS(String str) {
        this.str = str;
    }
}
