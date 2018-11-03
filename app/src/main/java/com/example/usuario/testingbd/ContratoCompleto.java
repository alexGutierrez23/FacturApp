package com.example.usuario.testingbd;

/**
 * Created by Usuario on 28/04/2018.
 */

import java.util.ArrayList;

import sqlite.model.Arrendatario;
import sqlite.model.Contrato;
import sqlite.model.HojaRuta;

/**
 * Esta clase tiene como finalidad guardar todos los datos de un contrato.
 * Desde los propios datos del contrato, pasando por el arrendatario, hasta todas las hojas de ruta pertenecientes a ese contrato.
 */
public class ContratoCompleto {
    private Contrato contrato;
    private Arrendatario arrendatario;
    private ArrayList<sqlite.model.HojaRuta> hojasRuta;

    public ContratoCompleto(Contrato contrato, Arrendatario arrendatario, ArrayList<HojaRuta> hojasRuta) {
        this.contrato = contrato;
        this.arrendatario = arrendatario;
        this.hojasRuta = hojasRuta;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Arrendatario getArrendatario() {
        return arrendatario;
    }

    public void setArrendatario(Arrendatario arrendatario) {
        this.arrendatario = arrendatario;
    }

    public ArrayList<HojaRuta> getHojasRuta() {
        return hojasRuta;
    }

    public void setHojasRuta(ArrayList<HojaRuta> hojasRuta) {
        this.hojasRuta = hojasRuta;
    }
}
