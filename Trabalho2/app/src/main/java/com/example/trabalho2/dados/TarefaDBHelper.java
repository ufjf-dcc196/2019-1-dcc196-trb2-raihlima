package com.example.trabalho2.dados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TarefaDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "Tarefas.db";

    public TarefaDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TarefaContract.TarefaDados.CREATE_TABLE);
        db.execSQL(TarefaContract.EtiquetaDados.CREATE_TABLE);
        db.execSQL(TarefaContract.TarefaEtiquetaDados.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TarefaContract.TarefaDados.DROP_TABLE);
        db.execSQL(TarefaContract.EtiquetaDados.DROP_TABLE);
        db.execSQL(TarefaContract.TarefaEtiquetaDados.DROP_TABLE);
        onCreate(db);
    }
}