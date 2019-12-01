package com.mridx.accountsmanage.lib;

public class NameExtractor {

    public NameExtractor() {
    }

    public String getLastName(String fullname) {
        String lastName = fullname.substring(fullname.lastIndexOf(" ")+1);
        return lastName;
    }

}
