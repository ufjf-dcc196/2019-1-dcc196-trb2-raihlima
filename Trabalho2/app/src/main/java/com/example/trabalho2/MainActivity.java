package com.example.trabalho2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;

import com.example.trabalho2.adapter.TarefaDadosAdapter;
import com.example.trabalho2.classes.Tarefa;
import com.example.trabalho2.dados.TarefaContract;
import com.example.trabalho2.dados.TarefaDBHelper;

public class MainActivity extends AppCompatActivity {

    private Button criarTarefa;
    private RecyclerView rv;
    private TarefaDadosAdapter tarefaDadosAdapter;

    //Banco de Dados
    private Cursor cursor;
    private ContentValues contentValues;
    private SQLiteDatabase sqLiteDatabase;
    private TarefaDBHelper tarefaDBHelper;

    public static final int CRIAR_NOVA_TAREFA = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Banco de dados
        tarefaDBHelper = new TarefaDBHelper(getApplicationContext());
        sqLiteDatabase = tarefaDBHelper.getWritableDatabase();
        contentValues = new ContentValues();

        //preencheCursor();

        /*
        rv = findViewById(R.id.rvTarefas);
        tarefaDadosAdapter = new TarefaDadosAdapter(cursor);
        rv.setAdapter(tarefaDadosAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        */

        criarTarefa = (Button) findViewById(R.id.novaTarefaButton);
        criarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CriarNovaTarefaActivity.class);
                startActivityForResult(intent,CRIAR_NOVA_TAREFA);
            }
        });
/*
        tarefaDadosAdapter.setOnTarefaDadosClickListener(new TarefaDadosAdapter.OnTarefaDadosClickListener() {
            @Override
            public void onTarefaDadosClick(View v, int position) {
                Toast.makeText(MainActivity.this,Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode== CRIAR_NOVA_TAREFA){
                Bundle bundle = data.getBundleExtra("info");
                alterarLista();
                //alterarInfo();
            }
        }
    }

    private void preencheCursor(){
        String [] campos = {
                TarefaContract.TarefaDados._ID,
                TarefaContract.TarefaDados.COLUNM_TITULO,
                TarefaContract.TarefaDados.COLUNM_DESCRICAO,
                TarefaContract.TarefaDados.COLUMN_DIFICULDADE,
                TarefaContract.TarefaDados.COLUMN_ESTADO
                //TarefaContract.TarefaDados.COLUMN_DATA_LIMITE,
                //TarefaContract.TarefaDados.COLUMN_DATA_ATUALIZACAO

        };
        //cursor =  sqLiteDatabase.query(TarefaContract.TarefaDados.TABLE_NAME,campos,null,null,null,null,null);

    }

    private void alterarLista(){
        preencheCursor();
        //tarefaDadosAdapter.alteraDados(cursor);

    }
}
