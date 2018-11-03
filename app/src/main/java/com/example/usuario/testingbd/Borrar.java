package com.example.usuario.testingbd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import Constants.Constants;
import appImplementations.CommonMethodsImplementation;
import appInterfaces.CommonMethods;
import sqlite.data.DatabaseHelper;
import sqlite.model.Invoice;
import sqlite.model.Presupuesto;
import sqlite.model.Ticket;
import sqlite.repo.InvoiceDML;
import sqlite.repo.LastFileDML;
import sqlite.repo.PresupuestoDML;
import sqlite.repo.TicketDML;

public class Borrar extends AppCompatActivity {

    private String tckId = null;
    private String invId = null;
    private String preId = null;

    private TicketDML tck = null;
    private Ticket t = null;
    private boolean lastTicket = false;

    private InvoiceDML inv = null;
    private Invoice i = null;
    private boolean lastInvoice;

    private PresupuestoDML pre = null;
    private Presupuesto p = null;
    private boolean lastPresupuesto;

    CommonMethods cm = new CommonMethodsImplementation();
    Constants c = new Constants();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar);

        android.support.v7.app.ActionBar act = getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);
        act.setTitle(R.string.title_activity_borrar);
        act.setIcon(R.mipmap.ic_launcher);
        act.setLogo(R.mipmap.ic_launcher);
        act.setDisplayUseLogoEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MenuPrincipal.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private boolean getLastTicket(){
        boolean lastTicket = false;
        tck = new TicketDML(this);
        LastFileDML lfiDml = new LastFileDML(this);
        tckId = cm.getLastTicket(this);
        if(tckId == null){
            lastTicket = false;
        }
        else if(tckId.equals("-1")){
            lastTicket = false;
        }
        else{
            t = tck.getTicketById(new DatabaseHelper(this), tckId, this);
            lastTicket = true;
        }
        return lastTicket;
    }

    public void deleteTicketDialog(View v){
        lastTicket = getLastTicket();
        if(lastTicket){
            final AlertDialog.Builder builder = new AlertDialog.Builder(Borrar.this);
            builder.setTitle(getString(R.string.dialog_borrarTicketTitulo) + " " + t.getTck_number());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.dialog_borrarTicket)
                    .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            tckId = cm.borrarFile(c.TICKET_TYPE, Borrar.this);
                            tck.borrarTicket(new DatabaseHelper(Borrar.this), tckId, Borrar.this);

                            String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                                    + c.NOMBRE_CARPETA_APP + File.separator + c.GENERADOS_TICKETS
                                    + File.separator + t.getTck_numTickRed() + ".pdf";
                            cm.borrarPdf(nombre_completo);
                            showConfirmationMessage(c.TICKET_TYPE);
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            Toast.makeText(this, R.string.borrarTicketError, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getLastInvoice(){
        boolean lastInvoice = false;
        inv = new InvoiceDML(this);
        LastFileDML lfiDml = new LastFileDML(this);
        invId = cm.getLastInvoice(this);
        if(invId == null){
            lastInvoice = false;
        }
        else if(invId.equals("-1")){
            lastInvoice = false;
        }
        else{
            i = inv.getInvoiceById(new DatabaseHelper(this), Integer.parseInt(invId), this);
            lastInvoice = true;
        }
        return lastInvoice;
    }

    public void deleteInvoiceDialog(View v){
        lastInvoice = getLastInvoice();
        if(lastInvoice){
            final AlertDialog.Builder builder = new AlertDialog.Builder(Borrar.this);
            builder.setTitle(getString(R.string.dialog_borrarFacturaTitulo) + " " + i.getInv_number());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.dialog_borrarFactura)
                    .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            invId = cm.borrarFile(c.FACTURA_TYPE, Borrar.this);
                            inv.borrarInvoice(new DatabaseHelper(Borrar.this), invId, Borrar.this);

                            String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                                    + c.NOMBRE_CARPETA_APP + File.separator + c.GENERADOS_FACTURAS
                                    + File.separator + i.getInv_numFactRed() + ".pdf";
                            cm.borrarPdf(nombre_completo);
                            showConfirmationMessage(c.FACTURA_TYPE);
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            Toast.makeText(this, R.string.borrarInvoiceError, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getLastPresupuesto(){
        boolean lastPresupuesto = false;
        pre = new PresupuestoDML(this);
        LastFileDML lfiDml = new LastFileDML(this);
        preId = cm.getLastPresupuesto(this);
        if(preId == null){
            lastPresupuesto = false;
        }
        else if(preId.equals("-1")){
            lastPresupuesto = false;
        }
        else{
            p = pre.getPresupuestoById(new DatabaseHelper(this), Integer.parseInt(preId), this);
            lastPresupuesto = true;
        }
        return lastPresupuesto;
    }

    public void deletePresupuestoDialog(View v){
        lastPresupuesto = getLastPresupuesto();
        if(lastPresupuesto){
            final AlertDialog.Builder builder = new AlertDialog.Builder(Borrar.this);
            builder.setTitle(getString(R.string.dialog_borrarPresupuestoTitulo) + " " + p.getPre_number());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(R.string.dialog_borrarPresupuesto)
                    .setPositiveButton(R.string.dialog_si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            preId = cm.borrarFile(c.PRESUPUESTO_TYPE, Borrar.this);
                            pre.borrarPresupuesto(new DatabaseHelper(Borrar.this), preId, Borrar.this);

                            String nombre_completo = Environment.getExternalStorageDirectory() + File.separator
                                    + c.NOMBRE_CARPETA_APP + File.separator + c.GENERADOS_PRESUPUESTOS
                                    + File.separator + p.getPre_numPresupuestoRed() + ".pdf";
                            cm.borrarPdf(nombre_completo);
                            showConfirmationMessage(c.PRESUPUESTO_TYPE);
                        }
                    })
                    .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            Toast.makeText(this, R.string.borrarPresupuestoError, Toast.LENGTH_SHORT).show();
        }
    }

    private void showConfirmationMessage(String type) {
        finish();
        if (type.equals(c.TICKET_TYPE)) {
            Toast.makeText(this, t.getTck_numTickRed() + R.string.confirmacionBorrar, Toast.LENGTH_LONG).show();
        }
        else if(type.equals(c.FACTURA_TYPE)){
            Toast.makeText(this, i.getInv_numFactRed() + R.string.confirmacionBorrar, Toast.LENGTH_LONG).show();
        }
        else if(type.equals(c.PRESUPUESTO_TYPE)){
            Toast.makeText(this, p.getPre_numPresupuestoRed() + R.string.confirmacionBorrar, Toast.LENGTH_LONG).show();
        }
    }
}

