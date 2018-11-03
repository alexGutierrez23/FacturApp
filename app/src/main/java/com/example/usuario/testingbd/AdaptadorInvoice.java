package com.example.usuario.testingbd;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import sqlite.model.Invoice;

/**
 * Created by Usuario on 11/02/2018.
 */

public class AdaptadorInvoice extends RecyclerView.Adapter<AdaptadorInvoice.ViewHolder>{
    private Invoice[] invoiceSet = null;
    private AdaptadorInvoiceInterface adaptadorInvoiceInterface;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // Campos respectivos de un item
        public TextView numInvoice;
        public TextView dateInvoice;
        public CardView cardView;
        public ImageButton btnVer;
        public ImageButton btnEmail;
        public ImageButton btnBorrar;


        public ViewHolder(View v) {
            super(v);
            numInvoice = (TextView) v.findViewById(R.id.numInvoice);
            dateInvoice = (TextView) v.findViewById(R.id.dateInvoice);
            cardView = (CardView) v.findViewById(R.id.cardViewInvoices);
            btnVer = (ImageButton) v.findViewById(R.id.verBtnItemInvoice);
            btnEmail = (ImageButton) v.findViewById(R.id.enviarBtnItemInvoice);
            btnBorrar = (ImageButton) v.findViewById(R.id.borrarBtnItemInvoice);
        }
    }

    public AdaptadorInvoice(Invoice[] invoiceSet) {
        this.invoiceSet = invoiceSet;
    }

    public AdaptadorInvoice() {
    }

    //Buscar manyana metodo getItem
 /*   public Invoice getItem(int position){
        return dataSet[position];
    }
*/
    @Override
    public int getItemCount() {
        return invoiceSet.length;
    }

    @Override
    public AdaptadorInvoice.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_invoices, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int pos) {
        viewHolder.numInvoice.setText(invoiceSet[pos].getInv_numFactRed());
        viewHolder.dateInvoice.setText(invoiceSet[pos].getInv_date());
        viewHolder.cardView.setTag(pos);  // Position in the recyclerview list as a tag
/*
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog(viewHolder.itemView);
                Toast.makeText(view.getContext(),
                        "card tocada, pos: " + view.getTag().toString() + ", "
                                + invoiceSet[Integer.parseInt(view.getTag().toString())].getInv_numFactRed(),
                        Toast.LENGTH_SHORT).show();
            }
        });
*/
        viewHolder.btnVer.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorInvoiceInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int inv_id = invoiceSet[pos].getInv_id();
                    String invId = String.valueOf(inv_id);
                    adaptadorInvoiceInterface.verFacturaPdf(pos, invId);
                }
                /*Toast.makeText(view.getContext(),
                        "Button tocado, pos: " + viewHolder.cardView.getTag() + ", "
                        + invoiceSet[Integer.parseInt(viewHolder.cardView.getTag().toString())].getInv_numFactRed(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        viewHolder.btnEmail.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorInvoiceInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int inv_id = invoiceSet[pos].getInv_id();
                    String invId = String.valueOf(inv_id);
                    adaptadorInvoiceInterface.enviarFactura(pos, invId, view);
                }
                /*Toast.makeText(view.getContext(),
                        "Button tocado, pos: " + viewHolder.cardView.getTag() + ", "
                        + invoiceSet[Integer.parseInt(viewHolder.cardView.getTag().toString())].getInv_numFactRed(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        viewHolder.btnBorrar.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorInvoiceInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int inv_id = invoiceSet[pos].getInv_id();
                    String invId = String.valueOf(inv_id);
                    adaptadorInvoiceInterface.borrarFactura(pos, invId, view);
                }
                /*Toast.makeText(view.getContext(),
                        "Button tocado, pos: " + viewHolder.cardView.getTag() + ", "
                        + invoiceSet[Integer.parseInt(viewHolder.cardView.getTag().toString())].getInv_numFactRed(),
                        Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    public void setAdaptadorInvoiceInterface(AdaptadorInvoiceInterface adaptadorInvoiceInterface){
        this.adaptadorInvoiceInterface = adaptadorInvoiceInterface;
    }

    public interface AdaptadorInvoiceInterface{
        void verFacturaPdf(int position, String invId);
        void enviarFactura(int position, String invId, View view);
        void borrarFactura(int position, String tckId, View view);
    }
}
