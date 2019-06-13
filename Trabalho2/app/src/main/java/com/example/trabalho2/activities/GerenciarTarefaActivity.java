package com.example.trabalho2.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalho2.R;
import com.example.trabalho2.adapter.EtiquetaCriarTarefaAdapter;
import com.example.trabalho2.dados.TarefaContract;
import com.example.trabalho2.dados.TarefaDBHelper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GerenciarTarefaActivity extends AppCompatActivity {

    private int index;
    private long id;

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
    private TextView atualizacao;
    private RecyclerView recyclerViewEtiquetas;
    private EtiquetaCriarTarefaAdapter etiquetaCriarTarefaAdapter;
    private ArrayList<Long> idEtiquetas;

    //Banco de Dados
    private Cursor cursor;
    private ContentValues values;
    private TarefaDBHelper helper;
    private SQLiteDatabase db;
    private Cursor c;
    private Cursor cursorEtiqueta;

    //Estado da Activity
    //True para Editar
    private boolean editavel = false;

    private DatePickerDialog.OnDateSetListener mDateSetListener;


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
        atualizacao = (TextView) findViewById(R.id.ultimaAtualizacaoTxt);
        idEtiquetas = new ArrayList<>();


        //dataLimite.addTextChangedListener(Mascara.insert("##/##/####", dataLimite));

        helper = new TarefaDBHelper(getApplicationContext());
        db = helper.getWritableDatabase();
        values = new ContentValues();

        String [] campos = TarefaContract.TABELA_TAREFA;

        cursor = db.query(TarefaContract.TarefaDados.TABLE_NAME, campos, null, null, null,null, null);
        cursorEtiqueta = db.query(TarefaContract.EtiquetaDados.TABLE_NAME, TarefaContract.TABELA_ETIQUETA, null, null, null,null, null);

        preencheDados(getIntent().getBundleExtra("info"));
        alteraEstado(false);

        recyclerViewEtiquetas = findViewById(R.id.rvEtiquetaGerenciar);
        etiquetaCriarTarefaAdapter = new EtiquetaCriarTarefaAdapter(cursorEtiqueta);
        recyclerViewEtiquetas.setAdapter(etiquetaCriarTarefaAdapter);
        recyclerViewEtiquetas.setLayoutManager(new LinearLayoutManager(this));

        dataLimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerarCalendario();
            }
        });

        dataLimite.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==true){
                    gerarCalendario();
                }
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dataLimite.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            }
        };

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editavel){
                    alteraEstado(false);
                    editavel=false;
                    alteraNomeBotoes();
                    alteraRegistro();
                    Toast.makeText(GerenciarTarefaActivity.this,"Tarefa Alterada",Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, new Intent());
                    finish();
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

        etiquetaCriarTarefaAdapter.setOnEtiquetaCriarTarefaClickListener(new EtiquetaCriarTarefaAdapter.OnEtiquetaCriarTarefaClickListener() {
            @Override
            public void onEtiquetaCriarTarefaClick(View v, int position) {

            }

            @Override
            public void onEtiquetaCriarTarefaClick(View v, int position, final long id) {
                gerenciaIdEtiqueta(id);

                if(position==(etiquetaCriarTarefaAdapter.getItemCount()-1)){
                    //Toast.makeText(CriarNovaTarefaActivity.this, "Teste Click Curto", Toast.LENGTH_SHORT).show();

                    //Dialogo de Nova etiqueta
                    AlertDialog.Builder builder = new AlertDialog.Builder(GerenciarTarefaActivity.this);
                    View viewBuilder = getLayoutInflater().inflate(R.layout.nova_etiqueta_dialog_layout,null);
                    final EditText nomeEtiqueta = (EditText) viewBuilder.findViewById(R.id.editNovaEtiqueta);
                    builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String where = "_ID = " + id;
                            ContentValues valuesEtiqueta = new ContentValues();
                            valuesEtiqueta.put(TarefaContract.EtiquetaDados.COLUMN_NOME,nomeEtiqueta.getText().toString());
                            db.update(TarefaContract.EtiquetaDados.TABLE_NAME,valuesEtiqueta,where,null);

                            valuesEtiqueta = new ContentValues();
                            valuesEtiqueta.put(TarefaContract.EtiquetaDados.COLUMN_NOME,"Nova Etiqueta");
                            db.insert(TarefaContract.EtiquetaDados.TABLE_NAME,null,valuesEtiqueta);
                            cursorEtiqueta = db.query(TarefaContract.EtiquetaDados.TABLE_NAME, TarefaContract.TABELA_ETIQUETA, null, null, null,null, null);
                            etiquetaCriarTarefaAdapter.alteraDados(cursorEtiqueta);
                            Toast.makeText(GerenciarTarefaActivity.this,"Etiqueta Criada", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.setTitle("Criar nova Etiqueta");
                    builder.setView(viewBuilder);
                    builder.show();
                }
            }

            @Override
            public void onEtiquetaCriarTarefaLongClick(View v, int position) {
                Toast.makeText(GerenciarTarefaActivity.this, "Teste Long Click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEtiquetaCriarTarefaLongClick(View v, int position, final long id) {
                if (position < (etiquetaCriarTarefaAdapter.getItemCount() - 1)) {
                    AlertDialog.Builder mensagem = new AlertDialog.Builder(GerenciarTarefaActivity.this);
                    mensagem.setTitle("Alerta");
                    mensagem.setMessage("Tem certeza que deseja excluir a etiqueta?");
                    mensagem.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeRegistro(id);
                            Toast.makeText(GerenciarTarefaActivity.this, "Etiqueta excluida", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mensagem.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    mensagem.show();
                }
            }
        });
    }

    private void preencheCheckBox(){
        
    }

    private void removeRegistro(long id){
        cursorEtiqueta.moveToFirst();
        while(cursorEtiqueta.isAfterLast()==false){
            if(cursorEtiqueta.getLong(cursorEtiqueta.getColumnIndex(TarefaContract.TarefaDados._ID))==id){
                break;
            }
            cursorEtiqueta.moveToNext();
        }
        String where = "_ID" + "=" + cursorEtiqueta.getString((cursorEtiqueta.getColumnIndex(TarefaContract.EtiquetaDados._ID)));
        db.delete(TarefaContract.EtiquetaDados.TABLE_NAME,where,null);
        cursorEtiqueta = db.query(TarefaContract.EtiquetaDados.TABLE_NAME, TarefaContract.TABELA_ETIQUETA, null, null, null,null, null);
        etiquetaCriarTarefaAdapter.alteraDados(cursorEtiqueta);

    }

    private void gerenciaIdEtiqueta(long id){
        for(int i=0;i<idEtiquetas.size();i++){
            if(idEtiquetas.get(i)==id){
                idEtiquetas.remove(i);
                return;
            }
        }
        idEtiquetas.add(id);
    }

    private void gerarCalendario(){
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog= new DatePickerDialog(GerenciarTarefaActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,ano,mes,dia);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
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

    //@RequiresApi(api = Build.VERSION_CODES.O)
    private void preencheDados(Bundle bundle){
        index = bundle.getInt("index");
        id = bundle.getLong("id");
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false){
            if(cursor.getInt(cursor.getColumnIndex(TarefaContract.TarefaDados._ID))==id){
                break;
            }
            cursor.moveToNext();
        }
        //cursor.moveToPosition(index);
        titulo.setText(this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_TITULO)));
        descricao.setText(this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DESCRICAO)));
        dificuldade.setRating(this.cursor.getInt(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DIFICULDADE)));

        Timestamp ts = Timestamp.valueOf(this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DATA_LIMITE)));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataL = dateFormat.format(ts);
        //String dataL  = (this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DATA_LIMITE)));

        dataLimite.setText(dataL);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss" );
        ts = Timestamp.valueOf(this.cursor.getString(cursor.getColumnIndex(TarefaContract.TarefaDados.COLUMN_DATA_ATUALIZACAO)));
        String dataA = dateFormat.format(ts);

        atualizacao.setText("Ultima atualização: " + dataA);
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
        String inDate = dataLimite.getText().toString();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Timestamp ts = new Timestamp(((java.util.Date)df.parse(inDate)).getTime());
            values.put(TarefaContract.TarefaDados.COLUMN_DATA_LIMITE, ts.toString());

        } catch (ParseException e) {
            Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());
            values.put(TarefaContract.TarefaDados.COLUMN_DATA_LIMITE, dataDeHoje.toString());
        }

        Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());
        values.put(TarefaContract.TarefaDados.COLUMN_DATA_ATUALIZACAO, dataDeHoje.toString());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss" );
        String dataA = dateFormat.format(dataDeHoje);

        atualizacao.setText("Ultima atualização: " + dataA);

        db.update(TarefaContract.TarefaDados.TABLE_NAME,values,where,null);

        //db.close();
    }

    private void removeRegistro(){
        String where = "_ID" + "=" + cursor.getString((cursor.getColumnIndex(TarefaContract.TarefaDados._ID)));
        db.delete(TarefaContract.TarefaDados.TABLE_NAME,where,null);
        db.close();
    }
}
