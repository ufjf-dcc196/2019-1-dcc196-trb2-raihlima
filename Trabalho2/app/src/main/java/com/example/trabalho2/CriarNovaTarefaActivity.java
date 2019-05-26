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

import com.example.trabalho2.classes.Mascara;
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
    private ContentValues values;
    private TarefaDBHelper helper;
    private SQLiteDatabase db;
    private Cursor c;


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

        dataLimite.addTextChangedListener(Mascara.insert("##/##/####", dataLimite));

        helper = new TarefaDBHelper(getApplicationContext());
        db = helper.getWritableDatabase();
        values = new ContentValues();

        String [] campos = {
                TarefaContract.TarefaDados._ID,
                TarefaContract.TarefaDados.COLUMN_TITULO,
                TarefaContract.TarefaDados.COLUMN_DESCRICAO,
                TarefaContract.TarefaDados.COLUMN_DIFICULDADE
        };

        c = db.query(TarefaContract.TarefaDados.TABLE_NAME, campos, null, null, null,null, null);

        criarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaPreenchimento()) {
                    RadioButton radioButton = (RadioButton) estado.findViewById(estado.getCheckedRadioButtonId());
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    String [] campos = {
                            TarefaContract.TarefaDados._ID,
                            TarefaContract.TarefaDados.COLUMN_TITULO,
                            TarefaContract.TarefaDados.COLUMN_DESCRICAO,
                            TarefaContract.TarefaDados.COLUMN_DIFICULDADE,
                            TarefaContract.TarefaDados.COLUMN_ESTADO
                    };

                    values.put(TarefaContract.TarefaDados.COLUMN_TITULO,titulo.getText().toString());
                    values.put(TarefaContract.TarefaDados.COLUMN_DESCRICAO,descricao.getText().toString());
                    values.put(TarefaContract.TarefaDados.COLUMN_DIFICULDADE,(int) dificuldade.getRating());
                    values.put(TarefaContract.TarefaDados.COLUMN_ESTADO, radioButton.getText().toString());
                    long novoID = db.insert(TarefaContract.TarefaDados.TABLE_NAME,null,values);
                    Toast.makeText(CriarNovaTarefaActivity.this,"Nova Tarefa criada com o id: " + novoID,Toast.LENGTH_SHORT).show();


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
