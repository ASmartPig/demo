package com.example.demo.enums;

public enum CEMS {
    CEMS_in_NOX("CEMS_in_NOX"),
    CEMS_in_SO2("CEMS_in_SO2"),
    CEMS_in_flux("CEMS_in_flux"),
    CEMS_in_O2("CEMS_in_flux"),
    CEMS_in_temp("CEMS_in_flux"), Null;

    private String str;

    CEMS() {
    }

    public static CEMS valueOfStr(String str) {
        for (CEMS obj : CEMS.values()) {
            if (java.util.Objects.equals(obj.str, str)) {
                return obj;
            }
        }
        return Null;
    }


    CEMS(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}
