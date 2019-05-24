package com.example.trabalho2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;

import com.example.trabalho2.adapter.TarefaAdapter;
import com.example.trabalho2.classes.Tarefa;

public class MainActivity extends AppCompatActivity {

    private Button criarTarefa;
    private RecyclerView rv;
    private TarefaAdapter tarefaAdapter;

    public static final int CRIARNOVATAREFA = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        criarTarefa = (Button) findViewById(R.id.novaTarefaButton);
        rv = findViewById(R.id.rvTarefas);
        tarefaAdapter = new TarefaAdapter();
        rv.setAdapter(tarefaAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        criarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CriarNovaTarefaActivity.class);
                startActivityForResult(intent,CRIARNOVATAREFA);
            }
        });

        tarefaAdapter.setOnTarefaClickListener(new TarefaAdapter.OnTarefaClickListener() {
            @Override
            public void onTarefaClick(View v, int position) {
                Toast.makeText(MainActivity.this,Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode== CRIARNOVATAREFA){
                Bundle bundle = data.getBundleExtra("info");
                alterarLista(bundle);
                //alterarInfo();
            }
        }
    }

    public void alterarLista(Bundle bundle){
        Tarefa tarefa= new Tarefa();
        tarefa.setTitulo(bundle.getString("titulo"));
        tarefa.setDescricao(bundle.getString("descricao"));
        tarefa.setDificuldade(bundle.getInt("dificuldade"));
        tarefa.setDataLimite(bundle.getString("dataLimite"));
        tarefa.setEstado(bundle.getString("estado"));

        tarefaAdapter.alteraDados(tarefa);

    }
}
