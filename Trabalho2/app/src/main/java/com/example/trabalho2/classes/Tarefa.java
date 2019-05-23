package com.example.trabalho2.classes;

import java.util.ArrayList;
import java.util.List;

public class Tarefa {
    private String titulo;
    private String descricao;
    private int dificuldade;
    private String dataLimite;
    private String dataAtualizacao;
    private List<String> tags;

    public Tarefa() {
        this.tags = new ArrayList<>();
    }

    public Tarefa(String titulo, String descricao, int dificuldade, String dataLimite, String dataAtualizacao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dificuldade = dificuldade;
        this.dataLimite = dataLimite;
        this.dataAtualizacao = dataAtualizacao;
        this.tags = new ArrayList<>();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(int dificuldade) {
        this.dificuldade = dificuldade;
    }

    public String getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(String dataLimite) {
        this.dataLimite = dataLimite;
    }

    public String getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(String dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public void addTag(String tag){
        this.tags.add(tag);
    }
}
