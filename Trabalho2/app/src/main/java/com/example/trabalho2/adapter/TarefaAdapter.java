package com.example.trabalho2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trabalho2.R;
import com.example.trabalho2.classes.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter <TarefaAdapter.ViewHolder>{
    
    private List <Tarefa> listaTarefas = new ArrayList<>();
    private TarefaAdapter.OnTarefaClickListener listener;

    public TarefaAdapter(){

    }

    public TarefaAdapter(Tarefa tarefa) {
            this.listaTarefas.add(tarefa);
    }

    public void alteraDados(Tarefa tarefa){
        this.listaTarefas.add(tarefa);
        notifyDataSetChanged();
    }

    public void setOnTarefaClickListener(TarefaAdapter.OnTarefaClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public TarefaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View linha = inflater.inflate(R.layout.tarefas_layout, viewGroup, false);
        TarefaAdapter.ViewHolder vh = new TarefaAdapter.ViewHolder(linha);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaAdapter.ViewHolder viewHolder, int i) {
        String titulo = this.listaTarefas.get(i).getTitulo();
        String dificuldade = Integer.toString(this.listaTarefas.get(i).getDificuldade());
        String estado = this.listaTarefas.get(i).getEstado();

        viewHolder.titulo.setText(titulo);
        viewHolder.dificuldade.setText(dificuldade);
        viewHolder.estado.setText(estado);
    }

    @Override
    public int getItemCount() {
        return this.listaTarefas.size();
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
                        listener.onTarefaClick(v, position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION){
                listener.onTarefaClick(v,position);
            }
        }
    }

    public interface OnTarefaClickListener {
        public void onTarefaClick(View v, int position);
    }
}
