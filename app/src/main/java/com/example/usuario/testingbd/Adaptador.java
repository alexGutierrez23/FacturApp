package com.example.usuario.testingbd;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import sqlite.model.Ticket;

/**
 * Created by Usuario on 11/02/2018.
 */

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder>{
    private Ticket[] ticketSet = null;
    private AdaptadorTicketInterface adaptadorTicketInterface;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView numTicket;
        public TextView dateTicket;
        public CardView cardView;
        public ImageButton btnVer;
        public ImageButton btnEmail;
        public ImageButton btnBorrar;

        public ViewHolder(View v) {
            super(v);
            numTicket = (TextView) v.findViewById(R.id.numTicket);
            dateTicket = (TextView) v.findViewById(R.id.dateTicket);
            cardView = (CardView) v.findViewById(R.id.cardViewTickets);
            btnVer = (ImageButton) v.findViewById(R.id.verBtnItemTicket);
            btnEmail = (ImageButton) v.findViewById(R.id.enviarBtnItemTicket);
            btnBorrar = (ImageButton) v.findViewById(R.id.borrarBtnItemTicket);
        }
    }
    public Adaptador(Ticket[] ticketSet) {
        this.ticketSet = ticketSet;
    }

    public Adaptador() {}

    @Override
    public int getItemCount() {
        return ticketSet.length;
    }

    @Override
    public Adaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_tickets, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int pos) {
        viewHolder.numTicket.setText(ticketSet[pos].getTck_numTickRed());
        viewHolder.dateTicket.setText(ticketSet[pos].getTck_date());

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
                if(adaptadorTicketInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int tck_id = ticketSet[pos].getTck_id();
                    String tckId = String.valueOf(tck_id);
                    adaptadorTicketInterface.verTicketPdf(pos, tckId);
                }
            }
        });

        viewHolder.btnEmail.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorTicketInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int tck_id = ticketSet[pos].getTck_id();
                    String tckId = String.valueOf(tck_id);
                    adaptadorTicketInterface.enviarTicket(pos, tckId, view);
                }
            }
        });

        viewHolder.btnBorrar.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorTicketInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int tck_id = ticketSet[pos].getTck_id();
                    String tckId = String.valueOf(tck_id);
                    adaptadorTicketInterface.borrarTicket(pos, tckId, view);
                }
            }
        });
    }

    public void setAdaptadorTicketInterface(AdaptadorTicketInterface adaptadorTicketInterface){
        this.adaptadorTicketInterface = adaptadorTicketInterface;
    }

    public interface AdaptadorTicketInterface {
        void verTicketPdf(int position, String tckId);
        void enviarTicket(int position, String tckId, View view);
        void borrarTicket(int position, String tckId, View view);
    }
}
