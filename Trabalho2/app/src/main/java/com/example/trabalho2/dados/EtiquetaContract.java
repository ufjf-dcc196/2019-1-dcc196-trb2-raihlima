package com.example.trabalho2.dados;

import android.provider.BaseColumns;

public class EtiquetaContract {
    public final String TABELA_ETIQUETA[]={
            EtiquetaDados._ID,
            EtiquetaDados.COLUMN_TITULO
    };

    public static final class EtiquetaDados implements BaseColumns {
        public static final String TABLE_NAME = "etiqueta";
        public static final String COLUMN_TITULO = "nome";
        public static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)", TABLE_NAME, _ID, COLUMN_TITULO);
        public static final String DROP_TABLE = String.format("DROP TABLE %s", TABLE_NAME);
    }
}
