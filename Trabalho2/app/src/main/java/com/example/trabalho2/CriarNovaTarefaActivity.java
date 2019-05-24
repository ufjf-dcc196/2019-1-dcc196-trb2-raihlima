package com.example.trabalho2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

public class CriarNovaTarefaActivity extends AppCompatActivity {

    private EditText titulo;
    private EditText descricao;
    private EditText dataLimite;
    private RatingBar dificuldade;
    private RadioGroup estado;
    private Button criarTarefa;


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

        criarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaPreenchimento()) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    bundle.putString("titulo",titulo.getText().toString());
                    bundle.putString("descricao", descricao.getText().toString());
                    bundle.putString("dataLimite",dataLimite.getText().toString());
                    bundle.putInt("dificuldade", (int) dificuldade.getRating());
                    RadioButton radioButton = (RadioButton) estado.findViewById(estado.getCheckedRadioButtonId());
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
