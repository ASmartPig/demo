package com.example.demo.enums;

public enum CEMS {
    CEMS_in_NOX("input.CEMS_in_NOX"),
    CEMS_in_SO2("input.CEMS_in_SO2"),
    CEMS_in_flux("input.CEMS_in_flux"),
    CEMS_in_O2("input.CEMS_in_O2"),
    CEMS_in_temp("input.CEMS_in_temp");

    private String str;

    public String getStr() {
        return str;
    }


    CEMS(String str) {
        this.str = str;
    }
}
