package com.example.trabalho2.activities;

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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.trabalho2.R;
import com.example.trabalho2.adapter.TarefaDadosAdapter;
import com.example.trabalho2.dados.TarefaContract;
import com.example.trabalho2.dados.TarefaDBHelper;


public class MainActivity extends AppCompatActivity {

    private Button criarNovaTarefa;
    private RecyclerView recyclerView;
    private Cursor cursor;
    private ContentValues values;
    private TarefaDBHelper helper;
    private SQLiteDatabase dataBase;
    private TarefaDadosAdapter tarefaDadosAdapter;
    private SearchView busca;

    public static final int CRIAR_NOVA_TAREFA = 1;
    public static final int EXIBIR_TAREFA = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        criarNovaTarefa = (Button) findViewById(R.id.button);

        helper = new TarefaDBHelper(getApplicationContext());
        dataBase = helper.getWritableDatabase();
        values = new ContentValues();


        preencheCursor();

        recyclerView = findViewById(R.id.rvTarefas);
        tarefaDadosAdapter = new TarefaDadosAdapter(cursor);
        recyclerView.setAdapter(tarefaDadosAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        busca = (SearchView) findViewById(R.id.searchTeste);

        criarNovaTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CriarNovaTarefaActivity.class);
                startActivityForResult(intent, CRIAR_NOVA_TAREFA);
            }
        });



        tarefaDadosAdapter.setOnTarefaDadosClickListener(new TarefaDadosAdapter.OnTarefaDadosClickListener() {
            @Override
            public void onTarefaDadosClick(View v, int position) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MainActivity.this, GerenciarTarefaActivity.class);
                cursor.moveToPosition(position);
                long index = cursor.getLong(cursor.getColumnIndex(TarefaContract.TarefaDados._ID));
                bundle.putLong("id",index);
                intent.putExtra("info",bundle);
                startActivityForResult(intent, EXIBIR_TAREFA);
            }
        });

        busca.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cursorBusca(query);
                if(cursor.getCount()>0){
                    String info = "Encontrado " + cursor.getCount() + " resultado(s).";
                    Toast.makeText(MainActivity.this, info,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Que pena, não encontramos a Etiqueta solicidada.",Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==EXIBIR_TAREFA){
            atualizaDados();
        }
        if(resultCode == Activity.RESULT_OK){
            if(requestCode== CRIAR_NOVA_TAREFA){
                Bundle bundle = data.getBundleExtra("info");
                atualizaDados();
            }
        }
    }

    private void preencheCursor(){
        String [] campos = TarefaContract.TABELA_TAREFA;
        cursor = dataBase.query(TarefaContract.TarefaDados.TABLE_NAME, campos, null, null, null,null, TarefaContract.TarefaDados.COLUMN_ESTADO + " ASC");
        //tarefaDadosAdapter.alteraDados(cursor);
    }

    private void cursorBusca(String  busca){
        String where = "SELECT t.* FROM " + TarefaContract.TarefaDados.TABLE_NAME + " as t , " + TarefaContract.EtiquetaDados.TABLE_NAME + " as e, " +
                TarefaContract.TarefaEtiquetaDados.TABLE_NAME + " as te WHERE e." + TarefaContract.EtiquetaDados.COLUMN_NOME + " LIKE " + "'"+busca+"'"+ " AND e." +
                TarefaContract.EtiquetaDados._ID + " = te." + TarefaContract.TarefaEtiquetaDados.COLUMN_ID_ETIQUETA + " AND te." + TarefaContract.TarefaEtiquetaDados.COLUMN_ID_TAREFA + " = t." + TarefaContract.TarefaDados._ID + " ORDER BY " + TarefaContract.TarefaDados.COLUMN_ESTADO + " ASC";

       cursor = dataBase.rawQuery(where,null);
       tarefaDadosAdapter.alteraDados(cursor);
    }

    private void atualizaDados(){
        preencheCursor();
        tarefaDadosAdapter.alteraDados(cursor);
    }
}

