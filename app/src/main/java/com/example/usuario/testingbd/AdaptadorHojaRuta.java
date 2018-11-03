package com.example.usuario.testingbd;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Usuario on 11/02/2018.
 */

public class AdaptadorHojaRuta extends RecyclerView.Adapter<AdaptadorHojaRuta.ViewHolder>{
    private sqlite.model.HojaRuta[] hojaRutaSet = null;
    private AdaptadorHojaRutaInterface adaptadorHojaRutaInterface;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView numHojaRuta;
        public TextView pasajerosHojaRuta;
        public ImageView confirmHojaRuta;
        public ImageView warningHojaRuta;
        public CardView cardView;
        public ImageButton btnVer;
        public ImageButton btnEmail;
        public ImageButton btnBorrar;
        public ImageButton btnEditar;
        public ImageButton btnImprimir;
        public CardView editarArrendatarioCardView;
        public CardView editarParadasCardView;
        public CardView borrarArrendatarioCardView;

        public ViewHolder(View v) {
            super(v);
            numHojaRuta = (TextView) v.findViewById(R.id.numHojaRuta);
            pasajerosHojaRuta = (TextView) v.findViewById(R.id.pasajerosHojaRuta);
            confirmHojaRuta = (ImageView) v.findViewById(R.id.hojaRutaConfirm);
            warningHojaRuta = (ImageView) v.findViewById(R.id.hojaRutaIncomplete);
            cardView = (CardView) v.findViewById(R.id.cardViewHojaRuta);
            btnVer = (ImageButton) v.findViewById(R.id.verBtnItemHojaRuta);
            btnEmail = (ImageButton) v.findViewById(R.id.enviarBtnItemHojaRuta);
            btnBorrar = (ImageButton) v.findViewById(R.id.borrarBtnItemHojaRuta);
            btnEditar = (ImageButton) v.findViewById(R.id.editBtnItemHojaRuta);
            btnImprimir = (ImageButton) v.findViewById(R.id.printBtnItemHojaRuta);
            editarArrendatarioCardView = (CardView) v.findViewById(R.id.cardViewEditarArrendatario);
            editarParadasCardView = (CardView) v.findViewById(R.id.cardViewEditarStops);
            borrarArrendatarioCardView = (CardView) v.findViewById(R.id.cardViewBorrarArrendatario);
        }
    }
    public AdaptadorHojaRuta(sqlite.model.HojaRuta[] hojaRutaSet) {
        this.hojaRutaSet = hojaRutaSet;
    }

    public AdaptadorHojaRuta() {}

    @Override
    public int getItemCount() {
        if(hojaRutaSet == null){
            return 0;
        }
        else{
            return hojaRutaSet.length;
        }
    }

    @Override
    public AdaptadorHojaRuta.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_hojaruta, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int pos) {
        viewHolder.numHojaRuta.setText(hojaRutaSet[pos].getHjr_alias());
        if(hojaRutaSet[pos].getHjr_date_fin().isEmpty() || hojaRutaSet[pos].getHjr_date_fin() == null){
            viewHolder.confirmHojaRuta.setVisibility(View.GONE);
            viewHolder.warningHojaRuta.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.confirmHojaRuta.setVisibility(View.VISIBLE);
            viewHolder.warningHojaRuta.setVisibility(View.GONE);
        }
        //viewHolder.pasajerosHojaRuta.setText(hojaRutaSet[pos].getHjr_pasajeros());

        viewHolder.cardView.setTag(pos);  // Position in the recyclerview list as a tag

  /*      viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog(viewHolder.itemView);
                Toast.makeText(view.getContext(),
                        "card tocada, pos: " + view.getTag().toString() + ", " + ticketSet[Integer.parseInt(view.getTag().toString())].getTck_numTickRed(),
                        Toast.LENGTH_SHORT).show();
            }
        });
*/
        viewHolder.btnVer.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorHojaRutaInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int hjr_id = hojaRutaSet[pos].getHjr_id();
                    String hjrId = String.valueOf(hjr_id);
                    adaptadorHojaRutaInterface.verHojaRutaPdf(pos, hjrId);
                }
            }
        });

        viewHolder.btnEmail.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorHojaRutaInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int hjr_id = hojaRutaSet[pos].getHjr_id();
                    String hjrId = String.valueOf(hjr_id);
                    adaptadorHojaRutaInterface.enviarHojaRuta(pos, hjrId, view);
                }
            }
        });

        viewHolder.btnEditar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(adaptadorHojaRutaInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int hjr_id = hojaRutaSet[pos].getHjr_id();
                    String hjrId = String.valueOf(hjr_id);
                    adaptadorHojaRutaInterface.editarHojaRuta(pos, hjrId, view);
                }
            }
        });

        viewHolder.btnImprimir.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorHojaRutaInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int hjr_id = hojaRutaSet[pos].getHjr_id();
                    String hjrId = String.valueOf(hjr_id);
                    adaptadorHojaRutaInterface.imprimirHojaRuta(pos, hjrId, view);
                }
            }
        });

        viewHolder.btnBorrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(adaptadorHojaRutaInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int hjr_id = hojaRutaSet[pos].getHjr_id();
                    String hjrId = String.valueOf(hjr_id);
                    adaptadorHojaRutaInterface.borrarHojaRuta(pos, hjrId, view);
                }
            }
        });

        viewHolder.editarArrendatarioCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adaptadorHojaRutaInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    String ard_id = hojaRutaSet[pos].getHjr_ard_id();
                    String con_id = hojaRutaSet[pos].getHjr_con_id();
                    int hrj_id = hojaRutaSet[pos].getHjr_id();
                    adaptadorHojaRutaInterface.editarArrendatario(pos, String.valueOf(hrj_id), ard_id, con_id, view);
                }
            }
        });

        viewHolder.editarParadasCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adaptadorHojaRutaInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int hjr_id = hojaRutaSet[pos].getHjr_id();
                    String hjrId = String.valueOf(hjr_id);
                    adaptadorHojaRutaInterface.editarParadas(pos, hjrId, view);
                }
            }
        });

        viewHolder.borrarArrendatarioCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adaptadorHojaRutaInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    String ard_id = hojaRutaSet[pos].getHjr_ard_id();
                    String con_id = hojaRutaSet[pos].getHjr_con_id();
                    adaptadorHojaRutaInterface.borrarArrendatario(pos, ard_id, con_id);
                }
            }
        });
    }

    public void setAdaptadorHojaRutaInterface(AdaptadorHojaRutaInterface adaptadorHojaRutaInterface){
        this.adaptadorHojaRutaInterface = adaptadorHojaRutaInterface;
    }

    public interface AdaptadorHojaRutaInterface {
        void verHojaRutaPdf(int position, String hjrId);
        void enviarHojaRuta(int position, String hjrId, View view);
        void editarHojaRuta(int position, String hjrId, View view);
        void borrarHojaRuta(int position, String hjrId, View view);
        void imprimirHojaRuta(int position, String hjrId, View view);
        void editarArrendatario(int position, String hrjId, String ardId, String conId, View view);
        void editarParadas(int position, String hjrId, View view);
        void borrarArrendatario(int position, String ardId, String conId);
    }
}
