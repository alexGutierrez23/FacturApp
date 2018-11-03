package com.example.usuario.testingbd;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import sqlite.model.Arrendatario;
import sqlite.model.Contrato;
import sqlite.model.HojaRuta;

/**
 * Created by Usuario on 11/02/2018.
 */

public class AdaptadorContrato extends RecyclerView.Adapter<AdaptadorContrato.ViewHolder>{
    private Contrato[] contratoSet = null;
    private AdaptadorContratoInterface adaptadorContratoInterface;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView arrendatarioContrato, arrendatarioTitle;
        public TextView arrendadorContrato, arrendadorTitle;
        public TextView numContrato, numContratoTitle;
        public CardView cardView;
        public ImageButton btnBorrar;
        public ImageButton btnEditar;

        public ViewHolder(View v) {
            super(v);
            //aliasContrato = (TextView) v.findViewById(R.id.aliasContrato);
            arrendadorContrato = (TextView) v.findViewById(R.id.arrendadorContrato);
            arrendatarioContrato = (TextView) v.findViewById(R.id.arrendatarioContrato);
            numContrato = (TextView) v.findViewById(R.id.numContrato);
            numContratoTitle = (TextView) v.findViewById(R.id.numContratoTitle);
            arrendatarioTitle = (TextView) v.findViewById(R.id.arrendatarioTitle);
            arrendadorTitle = (TextView) v.findViewById(R.id.arrendadorTitle);

            cardView = (CardView) v.findViewById(R.id.cardViewContratos);
            btnBorrar = (ImageButton) v.findViewById(R.id.borrarBtnItemContrato);
            btnEditar = (ImageButton) v.findViewById(R.id.editBtnItemContrato);
        }
    }
    public AdaptadorContrato(Contrato[] contratoSet) {
        this.contratoSet = contratoSet;
    }

    public AdaptadorContrato() {}

    /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id){
                String data= spinner.getItemAtPosition(pos).toString();
                Toast.makeText(adapterView.getContext(), data, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/

    @Override
    public int getItemCount() {
        if(contratoSet != null){
            return contratoSet.length;
        }
        else{
            return 0;
        }
    }

    @Override
    public AdaptadorContrato.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_contrato, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int pos) {
        boolean esTdaArrendador = adaptadorContratoInterface.esTdaArrendador(pos, String.valueOf(contratoSet[pos].getCon_isTda_arrendador()));
        if(esTdaArrendador){
            ArrayList<HojaRuta> hojasRuta = adaptadorContratoInterface.getHojasRuta(pos, String.valueOf(contratoSet[pos].getCon_id()));
            // No pasa nada por tener varias hojas de ruta:
            // el arrendatario va a ser siempre el mismo cuando el arrendador es el taxi driver
            if(hojasRuta != null && hojasRuta.size() > 0){
                for(HojaRuta hr : hojasRuta){
                    Arrendatario arrendatario = adaptadorContratoInterface.getArrendatario(pos, hr.getHjr_ard_id());
                    String nombreArrendatario = arrendatario.getArd_name();
                    String numContrato = arrendatario.getArd_number();
                    viewHolder.numContrato.setText(numContrato);
                    viewHolder.arrendatarioContrato.setText(nombreArrendatario);
                    viewHolder.arrendadorContrato.setText(contratoSet[pos].getCon_arrendador());
                    viewHolder.numContratoTitle.setVisibility(View.VISIBLE);
                    viewHolder.arrendatarioTitle.setVisibility(View.VISIBLE);
                    break;
                }
            }
            else{
                viewHolder.numContrato.setText(null);
                viewHolder.numContratoTitle.setVisibility(View.GONE);
                viewHolder.arrendatarioTitle.setVisibility(View.GONE);
                viewHolder.arrendatarioContrato.setText(null);
                viewHolder.arrendadorContrato.setText(contratoSet[pos].getCon_alias().toUpperCase());
            }
        }
        else{
            viewHolder.arrendatarioContrato.setText(null);
            viewHolder.arrendatarioTitle.setText(null);
            viewHolder.arrendadorContrato.setText(contratoSet[pos].getCon_arrendador());
            viewHolder.numContrato.setText(contratoSet[pos].getCon_number());
        }
        //Tengo que unir arrendatario con contrato! poner en arrendatario el id de contrato !!
        viewHolder.cardView.setTag(pos);  // Position in the recyclerview list as a tag

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog(viewHolder.itemView);
                String posString = viewHolder.cardView.getTag().toString();
                int pos = Integer.parseInt(posString);
                int con_id = contratoSet[pos].getCon_id();
                String conId = String.valueOf(con_id);
                adaptadorContratoInterface.verHojasRuta(pos, conId);
             /*   Toast.makeText(view.getContext(),
                        "card tocada, pos: " + view.getTag().toString() + ", " + ticketSet[Integer.parseInt(view.getTag().toString())].getTck_numTickRed(),
                        Toast.LENGTH_SHORT).show();
                        */
            }
        });

        viewHolder.btnEditar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(adaptadorContratoInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int con_id = contratoSet[pos].getCon_id();
                    String conId = String.valueOf(con_id);
                    adaptadorContratoInterface.editarContrato(pos, conId, view);
                }
            }
        });

        viewHolder.btnBorrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(adaptadorContratoInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int con_id = contratoSet[pos].getCon_id();
                    String conId = String.valueOf(con_id);
                    adaptadorContratoInterface.borrarContrato(pos, conId, view);
                }
            }
        });
    }

    public void setAdaptadorContratoInterface(AdaptadorContratoInterface adaptadorContratoInterface){
        this.adaptadorContratoInterface = adaptadorContratoInterface;
    }

    public interface AdaptadorContratoInterface {
        void verHojasRuta(int position, String conId);
        void editarContrato(int position, String conId, View view);
        void borrarContrato(int position, String conId, View view); //Al borrar el contrato hay que borrar todas las hojas de ruta
        ArrayList<HojaRuta> getHojasRuta(int position, String conId);
        boolean esTdaArrendador(int position, String esArrendador);
        Arrendatario getArrendatario(int position, String arrendatarioId);
    }
}
