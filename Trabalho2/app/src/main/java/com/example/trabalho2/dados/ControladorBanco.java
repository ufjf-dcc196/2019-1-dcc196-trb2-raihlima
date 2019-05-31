package com.example.trabalho2.dados;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.trabalho2.classes.Tarefa;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ControladorBanco {

    public static Tarefa retornaTarefaOrdenado(int index, Context c){
        Tarefa tarefa = new Tarefa();

        TarefaDBHelper helper = new TarefaDBHelper(c.getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String [] campos = TarefaContract.TABELA_TAREFA;
        Cursor cursor = db.query(TarefaContract.TarefaDados.TABLE_NAME, campos, null, null, null,null, TarefaContract.TarefaDados.COLUMN_ESTADO + " ASC");

        cursor.moveToPosition(index);

        tarefa.setId(cursor.getLong(cursor.getColumnIndex(TarefaContract.TarefaDados._ID)));
        tarefa.setTitulo(cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_TITULO)));
        tarefa.setDescricao(cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DESCRICAO)));
        tarefa.setDificuldade(cursor.getInt(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DIFICULDADE)));

        Timestamp ts = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DATA_LIMITE)));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataL = dateFormat.format(ts);

        tarefa.setDataLimite(dataL);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss" );
        ts = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DATA_ATUALIZACAO)));
        String dataA = dateFormat.format(ts);

        tarefa.setDataAtualizacao("Ultima atualização: " + dataA);
        tarefa.setEstado(cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_ESTADO)));

        return tarefa;
    }


}
