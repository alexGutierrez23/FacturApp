package com.example.usuario.testingbd;

import java.io.Serializable;

/**
 * Created by Usuario on 22/01/2018.
 */

public class CustomerTesting implements Serializable {
    private String email;
    private String name;
    private String nif;
    private AddressTesting address;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public AddressTesting getAddress() {
        return address;
    }

    public void setAddress(AddressTesting address) {
        this.address = address;
    }
}
