package com.example.trabalho2.dados;

import android.provider.BaseColumns;

public final class TarefaContract {

    public static final class TarefaDados implements BaseColumns {
        public static final String TABLE_NAME = "tarefa";
        public static final String COLUMN_TITULO = "titulo";
        public static final String COLUMN_DESCRICAO = "descricao";
        public static final String COLUMN_DIFICULDADE = "dificuldade";
        public static final String COLUMN_ESTADO = "estado";
        public static final String COLUMN_DATA_LIMITE = "data_limite";
        public static final String COLUMN_DATA_ATUALIZACAO = "data_atualizacao";
        public static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT)",TABLE_NAME, _ID, COLUMN_TITULO, COLUMN_DESCRICAO,COLUMN_DIFICULDADE, COLUMN_ESTADO);
        public static final String DROP_TABLE = String.format("DROP TABLE %s",TABLE_NAME);
    }
}