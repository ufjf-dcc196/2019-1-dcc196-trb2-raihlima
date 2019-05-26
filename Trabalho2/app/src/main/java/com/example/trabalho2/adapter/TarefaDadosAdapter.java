package com.example.trabalho2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trabalho2.R;
import com.example.trabalho2.dados.TarefaContract;


public class TarefaDadosAdapter extends RecyclerView.Adapter <TarefaDadosAdapter.ViewHolder>{

    private Cursor cursor;
    private TarefaDadosAdapter.OnTarefaDadosClickListener listener;

    public TarefaDadosAdapter(){

    }

    public TarefaDadosAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void alteraDados(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public void setOnTarefaDadosClickListener(TarefaDadosAdapter.OnTarefaDadosClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public TarefaDadosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View linha = inflater.inflate(R.layout.tarefas_layout, viewGroup, false);
        TarefaDadosAdapter.ViewHolder vh = new TarefaDadosAdapter.ViewHolder(linha);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaDadosAdapter.ViewHolder viewHolder, int index) {
        cursor.moveToPosition(index);
        String titulo = this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_TITULO));
        String dificuldade = this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DIFICULDADE));
        String estado = this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_ESTADO));

        viewHolder.titulo.setText(titulo);
        viewHolder.dificuldade.setText(dificuldade);
        viewHolder.estado.setText(estado);
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titulo;
        TextView dificuldade;
        TextView estado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = (TextView) itemView.findViewById(R.id.tituloTxtTarefaLayout);
            dificuldade = (TextView) itemView.findViewById(R.id.dificuldadeTxtTarefaLayout);
            estado = (TextView) itemView.findViewById(R.id.estadoTxtTarefaLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.onTarefaDadosClick(v, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                listener.onTarefaDadosClick(v,position);
            }
        }
    }

    public interface OnTarefaDadosClickListener {
        public void onTarefaDadosClick(View v, int position);
    }
}
