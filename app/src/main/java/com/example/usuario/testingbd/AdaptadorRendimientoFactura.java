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

public class AdaptadorRendimientoFactura extends RecyclerView.Adapter<AdaptadorRendimientoFactura.ViewHolder>{
    private Invoice[] facturaSet = null;
    private AdaptadorRendimientoFacturaInterface adaptadorRendimientoFacturaInterface;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView numRdtoFactura;
        public TextView dateRdtoFactura;
        public CardView cardView;
        public ImageButton btnVer;
        public ImageButton btnEmail;


        public ViewHolder(View v) {
            super(v);
            numRdtoFactura = (TextView) v.findViewById(R.id.numRdtoFactura);
            dateRdtoFactura = (TextView) v.findViewById(R.id.dateRdtoFactura);
            cardView = (CardView) v.findViewById(R.id.cardViewRdtoFacturas);
            btnVer = (ImageButton) v.findViewById(R.id.verBtnItemRdtoFactura);
            btnEmail = (ImageButton) v.findViewById(R.id.enviarBtnItemRdtoFactura);
        }
    }
    public AdaptadorRendimientoFactura(Invoice[] facturaSet) {
        this.facturaSet = facturaSet;
    }

    public AdaptadorRendimientoFactura() {}

    @Override
    public int getItemCount() {
        return facturaSet.length;
    }

    @Override
    public AdaptadorRendimientoFactura.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_rendimiento_facturas, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int pos) {
        viewHolder.numRdtoFactura.setText(facturaSet[pos].getInv_numFactRed());
        viewHolder.dateRdtoFactura.setText(facturaSet[pos].getInv_date());

        viewHolder.cardView.setTag(pos);  // Position in the recyclerview list as a tag
/*
         viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog(viewHolder.itemView);
                Toast.makeText(view.getContext(),
                        "card tocada, pos: " + view.getTag().toString() + ", " + facturaSet[Integer.parseInt(view.getTag().toString())].getInv_numFactRed(),
                        Toast.LENGTH_SHORT).show();
            }
        });
*/
        viewHolder.btnVer.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorRendimientoFacturaInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int inv_id = facturaSet[pos].getInv_id();
                    String invId = String.valueOf(inv_id);
                    adaptadorRendimientoFacturaInterface.verRdtoFacturaPdf(pos, invId);
                }
            }
        });

        viewHolder.btnEmail.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorRendimientoFacturaInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int inv_id = facturaSet[pos].getInv_id();
                    String invId = String.valueOf(inv_id);
                    adaptadorRendimientoFacturaInterface.enviarRdtoFactura(pos, invId, view);
                }
            }
        });
    }

    public void setAdaptadorRendimientoFacturaInterface(AdaptadorRendimientoFacturaInterface adaptadorRendimientoFacturaInterface){
        this.adaptadorRendimientoFacturaInterface = adaptadorRendimientoFacturaInterface;
    }

    public interface AdaptadorRendimientoFacturaInterface {
        void verRdtoFacturaPdf(int position, String tckId);
        void enviarRdtoFactura(int position, String tckId, View view);
    }
}
