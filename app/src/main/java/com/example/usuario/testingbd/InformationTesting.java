package com.example.usuario.testingbd;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Usuario on 19/01/2018.
 */

public class InformationTesting implements Serializable {

    private String additionalInfo;
    private String vat_price;
    private String no_vat_price;
    private String quantity;
    private String item;
    private String vat;
    private HashMap<String, String> tripFactura;

    public String getadditionalInfo() {
        return additionalInfo;
    }

    public void setadditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getvat_price() {
        return vat_price;
    }

    public void setvat_price(String vat_price) {
        this.vat_price = vat_price;
    }

    public String getno_vat_price() {
        return no_vat_price;
    }

    public void setno_vat_price(String no_vat_price) {
        this.no_vat_price = no_vat_price;
    }

    public String getquantity() {
        return quantity;
    }

    public void setquantity(String quantity) {
        this.quantity = quantity;
    }

    public String getitem() {
        return item;
    }

    public void setitem(String item) {
        this.item = item;
    }

    public String getvat() {
        return vat;
    }

    public void setvat(String vat) {
        this.vat = vat;
    }

    public HashMap<String, String> getTripFactura() {
        return tripFactura;
    }

    public void setTripFactura(HashMap<String, String> tripFactura) {
        this.tripFactura = tripFactura;
    }
}
