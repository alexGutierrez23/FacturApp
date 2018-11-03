package com.example.usuario.testingbd;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 21/12/2017.
 */

public class InvoiceDetails implements Parcelable{

    private String origenFactura = null;
    private String destinoFactura = null;
    private String concepto = null;
    private String additionalInformation = null;

    private double iva;
    private double precioFactura;
    private double precioSinIva;

    private static double IMPUESTOS = 1.10;
    private int cantidad;
    private int elemFactura;
    private int id;

    public InvoiceDetails(Parcel in) {

    }
    public InvoiceDetails(){

    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(origenFactura);
        dest.writeString(destinoFactura);
        dest.writeString(concepto);
        dest.writeString(additionalInformation);
        dest.writeDouble(iva);
        dest.writeDouble(precioFactura);
        dest.writeDouble(precioSinIva);
        dest.writeDouble(IMPUESTOS);
        dest.writeInt(cantidad);
    }

    public static final Creator<InvoiceDetails> CREATOR = new Creator<InvoiceDetails>() {

        @Override
        public InvoiceDetails[] newArray(int size) {
            return new InvoiceDetails[size];
        }

        @Override
        public InvoiceDetails createFromParcel(Parcel source) {
            return new InvoiceDetails(source);
        }
    };

    public int getElemFactura() {
        return elemFactura;
    }

    public void setElemFactura(int elemFactura) {
        this.elemFactura = elemFactura;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getOrigenFactura() {
        return origenFactura;
    }

    public void setOrigenFactura(String origenFactura) {
        this.origenFactura = origenFactura;
    }

    public String getDestinoFactura() {
        return destinoFactura;
    }

    public void setDestinoFactura(String destinoFactura) {
        this.destinoFactura = destinoFactura;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getPrecioFactura() {
        return precioFactura;
    }

    public void setPrecioFactura(double precioFactura) {
        this.precioFactura = precioFactura;
    }

    public double getPrecioSinIva() {
        return precioSinIva;
    }

    public void setPrecioSinIva(double precioSinIva) {
        this.precioSinIva = precioSinIva;
    }

    public double getIMPUESTOS() {
        return IMPUESTOS;
    }

    public void setIMPUESTOS(double IMPUESTOS) {
        this.IMPUESTOS = IMPUESTOS;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
