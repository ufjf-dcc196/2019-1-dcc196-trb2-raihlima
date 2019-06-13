package com.example.trabalho2.dados;

import android.provider.BaseColumns;

public final class TarefaContract {

    public static final String[] TABELA_TAREFA = {
            TarefaContract.TarefaDados._ID,
            TarefaContract.TarefaDados.COLUMN_TITULO,
            TarefaContract.TarefaDados.COLUMN_DESCRICAO,
            TarefaContract.TarefaDados.COLUMN_DIFICULDADE,
            TarefaContract.TarefaDados.COLUMN_ESTADO,
            TarefaDados.COLUMN_DATA_LIMITE,
            TarefaDados.COLUMN_DATA_ATUALIZACAO
    };

    public static final class TarefaDados implements BaseColumns {
        public static final String TABLE_NAME = "tarefa";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_DIFICULDADE = "dificuldade";
        public static final String COLUMN_ESTADO = "estado";
        public static final String COLUMN_DATA_LIMITE = "data_limite";
        public static final String COLUMN_DATA_ATUALIZACAO = "data_atualizacao";
        public static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TIMESTAMP, %s TIMESTAMP)", TABLE_NAME, _ID, COLUMN_TITULO, COLUMN_DESCRICAO, COLUMN_DIFICULDADE, COLUMN_ESTADO, COLUMN_DATA_LIMITE, COLUMN_DATA_ATUALIZACAO);
        public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    }

    public static final String[] TABELA_ETIQUETA ={
            EtiquetaDados._ID,
            EtiquetaDados.COLUMN_NOME
    };

    public static final class EtiquetaDados implements BaseColumns {
        public static final String TABLE_NAME = "etiqueta";
        public static final String COLUMN_NOME = "nome";
        public static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)", TABLE_NAME, _ID, COLUMN_NOME);
        public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    }

    public static final String[] TABELA_TAREFA_ETIQUETA ={
            TarefaEtiquetaDados.COLUMN_ID_TAREFA,
            TarefaEtiquetaDados.COLUMN_ID_ETIQUETA,
    };

    public static final class TarefaEtiquetaDados implements BaseColumns{
        public static final String TABLE_NAME = "tarefa_etiqueta";
        public static final String COLUMN_ID_TAREFA = "id_tarefa";
        public static final String COLUMN_ID_ETIQUETA = "id_etiqueta";
        public static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER, %s INTEGER, FOREIGN KEY(%s) REFERENCES %s(%s), FOREIGN KEY(%s) REFERENCES %s(%s))", TABLE_NAME, COLUMN_ID_TAREFA, COLUMN_ID_ETIQUETA, COLUMN_ID_TAREFA, TarefaDados.TABLE_NAME, TarefaDados._ID, COLUMN_ID_ETIQUETA, EtiquetaDados.TABLE_NAME, EtiquetaDados._ID);
        public static final String DROP_TABLE = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
    }
}