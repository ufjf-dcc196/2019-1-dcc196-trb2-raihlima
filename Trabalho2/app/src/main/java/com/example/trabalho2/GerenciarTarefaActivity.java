package com.example.trabalho2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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

public class GerenciarTarefaActivity extends AppCompatActivity {

    private int index;

    private EditText titulo;
    private EditText descricao;
    private EditText dataLimite;
    private RatingBar dificuldade;
    private RadioGroup estado;
    private RadioButton b1;
    private RadioButton b2;
    private RadioButton b3;
    private RadioButton b4;
    private Button editar;
    private Button excluir;

    //Banco de Dados
    private Cursor cursor;
    private ContentValues values;
    private TarefaDBHelper helper;
    private SQLiteDatabase db;
    private Cursor c;

    //Estado da Activity
    //True para Editar
    private boolean editavel = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_tarefa);

        titulo = (EditText) findViewById(R.id.tituloTxtGerenciarTarefa);
        descricao = (EditText) findViewById(R.id.descricaoTxtGerenciarTarefa);
        dataLimite = (EditText) findViewById(R.id.dataLimteTxtGerenciarTarefa);
        dificuldade = (RatingBar) findViewById(R.id.ratingBarGerenciarTarefa);
        estado = (RadioGroup) findViewById(R.id.estadoRadioGroupGerenciarTarefa);
        editar = (Button) findViewById(R.id.editarGerenciarTarefaButton);
        excluir = (Button) findViewById(R.id.excluirTarefaButton);
        b1 = (RadioButton) findViewById(R.id.radioButtonGerenciarTarefa);
        b2 = (RadioButton) findViewById(R.id.radioButton2GerenciarTarefa);
        b3 = (RadioButton) findViewById(R.id.radioButton3GerenciarTarefa);
        b4 = (RadioButton) findViewById(R.id.radioButton4GerenciarTarefa);

        dataLimite.addTextChangedListener(Mascara.insert("##/##/####", dataLimite));

        helper = new TarefaDBHelper(getApplicationContext());
        db = helper.getWritableDatabase();
        values = new ContentValues();

        String [] campos = {
                TarefaContract.TarefaDados._ID,
                TarefaContract.TarefaDados.COLUMN_TITULO,
                TarefaContract.TarefaDados.COLUMN_DESCRICAO,
                TarefaContract.TarefaDados.COLUMN_DIFICULDADE,
                TarefaContract.TarefaDados.COLUMN_ESTADO
        };

        cursor = db.query(TarefaContract.TarefaDados.TABLE_NAME, campos, null, null, null,null, null);


        preencheDados(getIntent().getBundleExtra("info"));
        alteraEstado(false);

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editavel){
                    alteraEstado(false);
                    editavel=false;
                    alteraNomeBotoes();
                    alteraRegistro();
                    Toast.makeText(GerenciarTarefaActivity.this,"Tarefa Alterada",Toast.LENGTH_SHORT).show();
                } else {
                    alteraEstado(true);
                    editavel=true;
                    alteraNomeBotoes();
                }
            }
        });

        excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editavel){
                    alteraEstado(false);
                    editavel=false;
                    alteraNomeBotoes();
                } else {
                    exibirAlertaExclusao();
                    /*
                    removeRegistro();
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
                    */
                }
            }
        });
    }

    public void exibirAlertaExclusao(){
        AlertDialog.Builder mensagem = new AlertDialog.Builder(this);
        mensagem.setTitle("Alerta");
        mensagem.setMessage("Tem certeza que deseja excluir a tarefa?");
        mensagem.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeRegistro();
                setResult(Activity.RESULT_OK, new Intent());
                Toast.makeText(GerenciarTarefaActivity.this,"Tarefa excluida", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        mensagem.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mensagem.show();
    }

    private void alteraEstado(Boolean valor){
        titulo.setEnabled(valor);
        descricao.setEnabled(valor);
        dataLimite.setEnabled(valor);
        dificuldade.setEnabled(valor);
        estado.setEnabled(valor);
        b1.setEnabled(valor);
        b2.setEnabled(valor);
        b3.setEnabled(valor);
        b4.setEnabled(valor);
    }

    private void preencheDados(Bundle bundle){
        index = bundle.getInt("index");
        cursor.moveToPosition(index);
        titulo.setText(this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_TITULO)));
        descricao.setText(this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DESCRICAO)));
        dificuldade.setRating(this.cursor.getInt(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DIFICULDADE)));
        String aux = this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_ESTADO));
        if(aux.equals("A fazer")){
            estado.check(R.id.radioButtonGerenciarTarefa);
        } else if(aux.equals("Em Execução")){
            estado.check(R.id.radioButton2GerenciarTarefa);
        } else if(aux.equals("Bloqueada")){
            estado.check(R.id.radioButton3GerenciarTarefa);
        } else if(aux.equals("Concluída")){
            estado.check(R.id.radioButton4GerenciarTarefa);
        }
    }

    private void alteraNomeBotoes(){
        if(editavel==true){
            editar.setText("Confirmar");
            excluir.setText("Cancelar");

        } else{
            editar.setText("Editar Tarefa");
            excluir.setText("Excluir Tarefa");
        }
    }

    private void alteraRegistro(){
        String where = "_ID" + "=" + cursor.getString((cursor.getColumnIndex(TarefaContract.TarefaDados._ID)));
        RadioButton radioButton = (RadioButton) estado.findViewById(estado.getCheckedRadioButtonId());
        values = new ContentValues();
        values.put(TarefaContract.TarefaDados.COLUMN_TITULO,titulo.getText().toString());
        values.put(TarefaContract.TarefaDados.COLUMN_DESCRICAO,descricao.getText().toString());
        values.put(TarefaContract.TarefaDados.COLUMN_DIFICULDADE,(int) dificuldade.getRating());
        values.put(TarefaContract.TarefaDados.COLUMN_ESTADO, radioButton.getText().toString());

        db.update(TarefaContract.TarefaDados.TABLE_NAME,values,where,null);
        db.close();
    }

    private void removeRegistro(){
        String where = "_ID" + "=" + cursor.getString((cursor.getColumnIndex(TarefaContract.TarefaDados._ID)));
        db.delete(TarefaContract.TarefaDados.TABLE_NAME,where,null);
        db.close();
    }
}
