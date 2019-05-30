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

    private Button b;
    private RecyclerView recyclerView;
    private Cursor c;
    private ContentValues values;
    private TarefaDBHelper helper;
    private SQLiteDatabase db;
    private TarefaDadosAdapter tarefaDadosAdapter;

    public static final int CRIAR_NOVA_TAREFA = 1;
    public static final int EXIBIR_TAREFA = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button) findViewById(R.id.button);

        helper = new TarefaDBHelper(getApplicationContext());
        db = helper.getWritableDatabase();
        values = new ContentValues();

        preencheCursor();

        recyclerView = findViewById(R.id.rvTarefas);
        tarefaDadosAdapter = new TarefaDadosAdapter(c);
        recyclerView.setAdapter(tarefaDadosAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        b.setOnClickListener(new View.OnClickListener() {
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
                bundle.putInt("index",position);
                intent.putExtra("info",bundle);
                startActivityForResult(intent, EXIBIR_TAREFA);
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
        c = db.query(TarefaContract.TarefaDados.TABLE_NAME, campos, null, null, null,null, null);
    }

    private void atualizaDados(){
        preencheCursor();
        tarefaDadosAdapter.alteraDados(c);
    }
}

