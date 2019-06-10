package com.example.trabalho2.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.trabalho2.R;
import com.example.trabalho2.dados.TarefaContract;

public class EtiquetaCriarTarefaAdapter extends RecyclerView.Adapter <EtiquetaCriarTarefaAdapter.ViewHolder>{
    private Cursor cursor;
    private EtiquetaCriarTarefaAdapter.OnEtiquetaCriarTarefaClickListener listener;

    public EtiquetaCriarTarefaAdapter(){
    }

    public EtiquetaCriarTarefaAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public void alteraDados(Cursor cursor){
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public void setOnEtiquetaCriarTarefaClickListener(EtiquetaCriarTarefaAdapter.OnEtiquetaCriarTarefaClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public EtiquetaCriarTarefaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View linha = inflater.inflate(R.layout.selecionar_etiqueta_layout, viewGroup, false);
        EtiquetaCriarTarefaAdapter.ViewHolder vh = new EtiquetaCriarTarefaAdapter.ViewHolder(linha);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull EtiquetaCriarTarefaAdapter.ViewHolder viewHolder, int index) {
        cursor.moveToPosition(index);
        String nome = this.cursor.getString(cursor.getColumnIndex(TarefaContract.EtiquetaDados.COLUMN_NOME));
        String posicao = Integer.toString(index);

        viewHolder.nomeEtiqueta.setText(nome);
        viewHolder.posicao.setText(posicao);
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox nomeEtiqueta;
        TextView posicao;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeEtiqueta = (CheckBox) itemView.findViewById(R.id.checkBox);
            posicao = (TextView) itemView.findViewById(R.id.txtPosicaoTeste);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener!=null){
                        listener.onEtiquetaCriarTarefaClick(v, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                listener.onEtiquetaCriarTarefaClick(v,position);
            }
        }


    }

    public interface OnEtiquetaCriarTarefaClickListener {
        public void onEtiquetaCriarTarefaClick(View v, int position);
    }
}