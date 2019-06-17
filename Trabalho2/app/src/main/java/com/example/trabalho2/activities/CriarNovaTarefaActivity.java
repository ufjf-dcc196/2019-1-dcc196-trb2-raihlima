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
import android.widget.Toast;

import com.example.trabalho2.R;
import com.example.trabalho2.adapter.EtiquetaCriarTarefaAdapter;
import com.example.trabalho2.adapter.TarefaDadosAdapter;
import com.example.trabalho2.dados.TarefaContract;
import com.example.trabalho2.dados.TarefaDBHelper;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class CriarNovaTarefaActivity extends AppCompatActivity {
    private EditText titulo;
    private EditText descricao;
    private EditText dataLimite;
    private RatingBar dificuldade;
    private RadioGroup estado;
    private Button criarTarefa;
    private RecyclerView recyclerView;
    private EtiquetaCriarTarefaAdapter etiquetaCriarTarefaAdapter;

    //Banco de Dados
    private Cursor cursor;
    private ContentValues values;
    private TarefaDBHelper helper;
    private SQLiteDatabase db;
    private Cursor cursorEtiqueta;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    //Auxiliar de etiqueta
    private ArrayList <Long> idEtiquetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_nova_tarefa);

        setTitle("Cadastrar Nova Tarefa");

        titulo = (EditText) findViewById(R.id.tituloTxtNovaTarefa);
        descricao = (EditText) findViewById(R.id.descricaoTxtNovaTarefa);
        dataLimite = (EditText) findViewById(R.id.dataLimteTxtNovaTarefa);
        dificuldade = (RatingBar) findViewById(R.id.ratingBar);
        estado = (RadioGroup) findViewById(R.id.estadoRadioGroup);
        criarTarefa = (Button) findViewById(R.id.confirmarNovaTarefaButton);
        idEtiquetas = new ArrayList<>();

        //dataLimite.addTextChangedListener(Mascara.insert("##/##/####", dataLimite));

        helper = new TarefaDBHelper(getApplicationContext());
        db = helper.getWritableDatabase();
        values = new ContentValues();

        String [] campos = TarefaContract.TABELA_TAREFA;

        cursor = db.query(TarefaContract.TarefaDados.TABLE_NAME, campos, null, null, null,null, null);
        cursorEtiqueta = db.query(TarefaContract.EtiquetaDados.TABLE_NAME, TarefaContract.TABELA_ETIQUETA, null, null, null,null, null);

        if(cursorEtiqueta.getCount()==0){
            ContentValues values = new ContentValues();
            values.put(TarefaContract.EtiquetaDados.COLUMN_NOME,"Nova Etiqueta");
            db.insert(TarefaContract.EtiquetaDados.TABLE_NAME,null,values);
            cursorEtiqueta = db.query(TarefaContract.EtiquetaDados.TABLE_NAME, TarefaContract.TABELA_ETIQUETA, null, null, null,null, null);
        }

        recyclerView = findViewById(R.id.rvSelecionarEtiqueta);
        etiquetaCriarTarefaAdapter = new EtiquetaCriarTarefaAdapter(cursorEtiqueta);
        recyclerView.setAdapter(etiquetaCriarTarefaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                if(month<9){
                    dataLimite.setText(dayOfMonth+"/0"+(month+1)+"/"+year);
                } else {
                    dataLimite.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                }

            }
        };

        criarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaPreenchimento()) {
                    RadioButton radioButton = (RadioButton) estado.findViewById(estado.getCheckedRadioButtonId());
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();

                    String [] campos = TarefaContract.TABELA_TAREFA;

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
                    long novoID = db.insert(TarefaContract.TarefaDados.TABLE_NAME,null,values);
                    Toast.makeText(CriarNovaTarefaActivity.this,"Nova Tarefa criada com o id: " + novoID,Toast.LENGTH_SHORT).show();

                    ContentValues tarefaEtiquetaValues;
                    for(int i=0;i<idEtiquetas.size();i++){
                        tarefaEtiquetaValues = new ContentValues();
                        tarefaEtiquetaValues.put(TarefaContract.TarefaEtiquetaDados.COLUMN_ID_TAREFA,novoID);
                        tarefaEtiquetaValues.put(TarefaContract.TarefaEtiquetaDados.COLUMN_ID_ETIQUETA,idEtiquetas.get(i));
                        db.insert(TarefaContract.TarefaEtiquetaDados.TABLE_NAME,null, tarefaEtiquetaValues);
                    }

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(CriarNovaTarefaActivity.this);
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
                            Toast.makeText(CriarNovaTarefaActivity.this,"Etiqueta Criada", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CriarNovaTarefaActivity.this, "Teste Long Click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEtiquetaCriarTarefaLongClick(View v, int position, final long id) {
                if (position < (etiquetaCriarTarefaAdapter.getItemCount() - 1)) {
                    AlertDialog.Builder mensagem = new AlertDialog.Builder(CriarNovaTarefaActivity.this);
                    mensagem.setTitle("Alerta");
                    mensagem.setMessage("Tem certeza que deseja excluir a etiqueta?");
                    mensagem.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeRegistro(id);
                            Toast.makeText(CriarNovaTarefaActivity.this, "Etiqueta excluida", Toast.LENGTH_SHORT).show();
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

    private void removeRegistro(long id){
        cursorEtiqueta.moveToFirst();
        while(cursorEtiqueta.isAfterLast()==false){
            if(cursorEtiqueta.getLong(cursorEtiqueta.getColumnIndex(TarefaContract.TarefaDados._ID))==id){
                break;
            }
            cursorEtiqueta.moveToNext();
        }
        String where = "_ID" + "=" + cursorEtiqueta.getString((cursorEtiqueta.getColumnIndex(TarefaContract.EtiquetaDados._ID)));
        String where2 = "id_etiqueta" + cursorEtiqueta.getString((cursorEtiqueta.getColumnIndex(TarefaContract.EtiquetaDados._ID)));
        db.delete(TarefaContract.EtiquetaDados.TABLE_NAME,where,null);
        db.delete(TarefaContract.TarefaEtiquetaDados.TABLE_NAME, where2,null);
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

        DatePickerDialog dateDialog= new DatePickerDialog(CriarNovaTarefaActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener,ano,mes,dia);
        dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateDialog.show();
    }

    private boolean verificaPreenchimento(){
        if(titulo.getText().toString().equals("")){
            return false;
        } else if(descricao.getText().toString().equals("")){
            return false;
        } else if(dataLimite.getText().toString().equals("")){
            return false;
        } else if(idEtiquetas.size()==0){
            return false;
        }
        return true;
    }
}
