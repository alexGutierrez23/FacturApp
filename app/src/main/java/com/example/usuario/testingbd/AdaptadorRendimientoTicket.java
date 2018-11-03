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

public class AdaptadorRendimientoTicket extends RecyclerView.Adapter<AdaptadorRendimientoTicket.ViewHolder>{
    private Ticket[] ticketSet = null;
    private AdaptadorRendimientoTicketInterface adaptadorRendimientoTicketInterface;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView numRdtoTicket;
        public TextView dateRdtoTicket;
        public CardView cardView;
        public ImageButton btnVer;
        public ImageButton btnEmail;


        public ViewHolder(View v) {
            super(v);
            numRdtoTicket = (TextView) v.findViewById(R.id.numRdtoTicket);
            dateRdtoTicket = (TextView) v.findViewById(R.id.dateRdtoTicket);
            cardView = (CardView) v.findViewById(R.id.cardViewRdtoTickets);
            btnVer = (ImageButton) v.findViewById(R.id.verBtnItemRdtoTicket);
            btnEmail = (ImageButton) v.findViewById(R.id.enviarBtnItemRdtoTicket);
        }
    }
    public AdaptadorRendimientoTicket(Ticket[] ticketSet) {
        this.ticketSet = ticketSet;
    }

    public AdaptadorRendimientoTicket() {}

    @Override
    public int getItemCount() {
        return ticketSet.length;
    }

    @Override
    public AdaptadorRendimientoTicket.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_rendimiento_tickets, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int pos) {
        viewHolder.numRdtoTicket.setText(ticketSet[pos].getTck_numTickRed());
        viewHolder.dateRdtoTicket.setText(ticketSet[pos].getTck_date());

        viewHolder.cardView.setTag(pos);  // Position in the recyclerview list as a tag

      /*   viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
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
                if(adaptadorRendimientoTicketInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int tck_id = ticketSet[pos].getTck_id();
                    String tckId = String.valueOf(tck_id);
                    adaptadorRendimientoTicketInterface.verRdtoTicketPdf(pos, tckId);
                }
            }
        });

        viewHolder.btnEmail.setOnClickListener(new View.OnClickListener(){  //FUNCIONA!!!!!!
            @Override
            public void onClick(View view) {
                if(adaptadorRendimientoTicketInterface != null){
                    String posString = viewHolder.cardView.getTag().toString();
                    int pos = Integer.parseInt(posString);
                    int tck_id = ticketSet[pos].getTck_id();
                    String tckId = String.valueOf(tck_id);
                    adaptadorRendimientoTicketInterface.enviarRdtoTicket(pos, tckId, view);
                }
            }
        });
    }

    public void setAdaptadorRendimientoTicketInterface(AdaptadorRendimientoTicketInterface adaptadorRendimientoTicketInterface){
        this.adaptadorRendimientoTicketInterface = adaptadorRendimientoTicketInterface;
    }

    public interface AdaptadorRendimientoTicketInterface {
        void verRdtoTicketPdf(int position, String tckId);
        void enviarRdtoTicket(int position, String tckId, View view);
    }
}
