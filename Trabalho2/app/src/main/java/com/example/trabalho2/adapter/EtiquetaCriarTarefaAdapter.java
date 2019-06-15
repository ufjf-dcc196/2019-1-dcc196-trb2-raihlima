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
    private Cursor cursorPreenchido;
    private EtiquetaCriarTarefaAdapter.OnEtiquetaCriarTarefaClickListener listener;

    public EtiquetaCriarTarefaAdapter(){
    }

    public EtiquetaCriarTarefaAdapter(Cursor cursor) {
        this.cursor = cursor;
        this.cursorPreenchido=null;
    }

    public EtiquetaCriarTarefaAdapter(Cursor cursor, Cursor cursorPreenchido) {
        this.cursor = cursor;
        this.cursorPreenchido = cursorPreenchido;
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
        if(this.cursorPreenchido==null) {
            cursor.moveToPosition(index);
            String nome = this.cursor.getString(cursor.getColumnIndex(TarefaContract.EtiquetaDados.COLUMN_NOME));
            viewHolder.id = cursor.getLong(cursor.getColumnIndex(TarefaContract.TarefaDados._ID));
            viewHolder.nomeEtiqueta.setText(nome);
        } else {
            cursor.moveToPosition(index);
            String nome = this.cursor.getString(cursor.getColumnIndex(TarefaContract.EtiquetaDados.COLUMN_NOME));
            viewHolder.id = cursor.getLong(cursor.getColumnIndex(TarefaContract.TarefaDados._ID));
            viewHolder.nomeEtiqueta.setText(nome);
            //viewHolder.nomeEtiqueta.setEnabled(false);
            cursorPreenchido.moveToFirst();
            for(int i=0;i<cursorPreenchido.getCount();i++){
                if(cursor.getLong(cursor.getColumnIndex(TarefaContract.EtiquetaDados._ID))==cursorPreenchido.getLong(cursorPreenchido.getColumnIndex(TarefaContract.EtiquetaDados._ID))){
                    viewHolder.nomeEtiqueta.setChecked(true);

                }
                cursorPreenchido.moveToNext();
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        protected CheckBox nomeEtiqueta;
        protected TextView posicao;
        private long id;

        public long getId(){
            return this.id;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeEtiqueta = (CheckBox) itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        alteraEstado();
                        listener.onEtiquetaCriarTarefaClick(v, position, id);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        listener.onEtiquetaCriarTarefaLongClick(v, position, id);
                    }
                    return true;
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


        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                listener.onEtiquetaCriarTarefaLongClick(v,position);
            }
            return false;
        }

        public void alteraEstado(){

            if(!nomeEtiqueta.isChecked()){
                nomeEtiqueta.setChecked(true);
            } else {
                nomeEtiqueta.setChecked(false);
            }
        }
    }

    public interface OnEtiquetaCriarTarefaClickListener {
        public void onEtiquetaCriarTarefaClick(View v, int position);
        public void onEtiquetaCriarTarefaClick(View v, int position, long id);
        public void onEtiquetaCriarTarefaLongClick (View v, int position);
        public void onEtiquetaCriarTarefaLongClick (View v, int position, long id);
    }
}
