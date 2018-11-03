package com.example.usuario.testingbd;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import sqlite.model.Presupuesto;

/**
 * Created by Usuario on 11/02/2018.
 */

public class AdaptadorPresupuesto extends RecyclerView.Adapter<AdaptadorPresupuesto.ViewHolder>{
    private Presupuesto[] presupuestoSet = null;
    private AdaptadorPresupuestoInterface adaptadorPresupuestoInterface;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView numPresupuesto;
        public TextView datePresupuesto;
        public CardView cardView;
        public ImageButton btnVer;
        public ImageButton btnEmail;
        public ImageButton btnBorrar;

        public ViewHolder(View v) {
            super(v);
            numPresupuesto = (TextView) v.findViewById(R.id.numPresupuesto);
            datePresupuesto = (TextView) v.findViewById(R.id.datePresupuesto);
            cardView = (CardView) v.findViewById(R.id.cardViewPresupuestos);
            btnVer = (ImageButton) v.findViewById(R.id.verBtnItemPresupuesto);
            btnEmail = (ImageButton) v.findViewById(R.id.enviarBtnItemPresupuesto);
            btnBorrar = (ImageButton) v.findViewById(R.id.borrarBtnItemPresupuesto);
        }
    }
    public AdaptadorPresupuesto(Presupuesto[] presupuestoSet) {
        this.presupuestoSet = presupuestoSet;
    }

    public AdaptadorPresupuesto() {}

    @Override
    public int getItemCount() {
        return presupuestoSet.length;
    }

    @Override
    public AdaptadorPresupuesto.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_presupuestos, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int pos) {
        viewHolder.numPresupuesto.setText(presupuestoSet[pos].getPre_numPresupuestoRed());
        viewHolder.datePresupuesto.setText(presupuestoSet[pos].getPre_date());

        viewHolder.cardView.setTag(pos);  // Position in the recyclerview list as a tag

  /*      viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog(viewHolder.itemView);
                Toast.makeText(view.getContext(),
                        "card tocada, pos: " + view.getTag().toString() + ", "
                                + presupuestoSet[Integer.parseInt(view.getTag().toString())].getPre_numPresupuestoRed(),
                        Toast.LENGTH_SHORT).show();
            }
        });
*/
        viewHolder.btnVer.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorPresupuestoInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int pre_id = presupuestoSet[pos].getPre_id();
                    String preId = String.valueOf(pre_id);
                    adaptadorPresupuestoInterface.verPresupuestoPdf(pos, preId);
                }
            }
        });

        viewHolder.btnEmail.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorPresupuestoInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int pre_id = presupuestoSet[pos].getPre_id();
                    String preId = String.valueOf(pre_id);
                    adaptadorPresupuestoInterface.enviarPresupuesto(pos, preId, view);
                }
            }
        });

        viewHolder.btnBorrar.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorPresupuestoInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int pre_id = presupuestoSet[pos].getPre_id();
                    String preId = String.valueOf(pre_id);
                    adaptadorPresupuestoInterface.borrarPresupuesto(pos, preId, view);
                }
            }
        });
    }

    public void setAdaptadorPresupuestoInterface(AdaptadorPresupuestoInterface adaptadorPresupuestoInterface){
        this.adaptadorPresupuestoInterface = adaptadorPresupuestoInterface;
    }

    public interface AdaptadorPresupuestoInterface {
        void verPresupuestoPdf(int position, String preId);
        void enviarPresupuesto(int position, String preId, View view);
        void borrarPresupuesto(int position, String preId, View view);
    }
}
