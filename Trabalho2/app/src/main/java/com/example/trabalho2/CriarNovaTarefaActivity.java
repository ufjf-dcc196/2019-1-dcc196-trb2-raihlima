package com.example.trabalho2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.trabalho2.dados.TarefaContract;
import com.example.trabalho2.dados.TarefaDBHelper;

public class CriarNovaTarefaActivity extends AppCompatActivity {

    private EditText titulo;
    private EditText descricao;
    private EditText dataLimite;
    private RatingBar dificuldade;
    private RadioGroup estado;
    private Button criarTarefa;

    //Banco de Dados
    private Cursor cursor;
    private ContentValues contentValues;
    private SQLiteDatabase sqLiteDatabase;
    private TarefaDBHelper tarefaDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_nova_tarefa);

        titulo = (EditText) findViewById(R.id.tituloTxtNovaTarefa);
        descricao = (EditText) findViewById(R.id.descricaoTxtNovaTarefa);
        dataLimite = (EditText) findViewById(R.id.dataLimteTxtNovaTarefa);
        dificuldade = (RatingBar) findViewById(R.id.ratingBar);
        estado = (RadioGroup) findViewById(R.id.estadoRadioGroup);
        criarTarefa = (Button) findViewById(R.id.confirmarNovaTarefaButton);

        tarefaDBHelper = new TarefaDBHelper(getApplicationContext());
        sqLiteDatabase = tarefaDBHelper.getWritableDatabase();
        contentValues = new ContentValues();

        criarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaPreenchimento()) {
                    RadioButton radioButton = (RadioButton) estado.findViewById(estado.getCheckedRadioButtonId());
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    contentValues.put(TarefaContract.TarefaDados.COLUNM_TITULO,"jAO");//titulo.getText().toString());
                    //contentValues.put(TarefaContract.TarefaDados.COLUNM_DESCRICAO,descricao.getText().toString());
                    //contentValues.put(TarefaContract.TarefaDados.COLUMN_DIFICULDADE, (int) dificuldade.getRating());
                    //contentValues.put(TarefaContract.TarefaDados.COLUMN_ESTADO, radioButton.getText().toString());
                    long novoId = sqLiteDatabase.insert(TarefaContract.TarefaDados.TABLE_NAME, null, contentValues);


                    Toast.makeText(CriarNovaTarefaActivity.this,"Novo id: " +novoId ,Toast.LENGTH_SHORT).show();


                    bundle.putString("titulo",titulo.getText().toString());
                    bundle.putString("descricao", descricao.getText().toString());
                    bundle.putString("dataLimite",dataLimite.getText().toString());
                    bundle.putInt("dificuldade", (int) dificuldade.getRating());
                    bundle.putString("estado",radioButton.getText().toString());

                    intent.putExtra("info", bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(CriarNovaTarefaActivity.this, "Preencha todos os dados!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean verificaPreenchimento(){
        if(titulo.getText().toString().equals("")){
            return false;
        } else if(descricao.getText().toString().equals("")){
            return false;
        } else if(dataLimite.getText().toString().equals("")){
            return false;
        } return true;
    }
}
