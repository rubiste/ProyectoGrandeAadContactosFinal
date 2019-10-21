package com.example.proyectograndeaadcontactos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadActivity extends AppCompatActivity {

    File fTest;


    Button btReadFile;
    TextView tvReadFile, tvDisplayContacts;
    EditText etReadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        initComponents();
    }

    private void initComponents() {
        btReadFile = findViewById(R.id.btReadFile);
        tvReadFile = findViewById(R.id.tvReadFile);
        etReadFile = findViewById(R.id.etReadFile);
        tvDisplayContacts = findViewById(R.id.tvDisplayContacts);

        initEvents();
    }

    private void initEvents() {
        btReadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etReadFile.getText().toString().length() != 0){
                    if(comprobarFicheroInternal()){
                        tvDisplayContacts.setText("");
                        tvReadFile.setTextColor(getResources().getColor(R.color.blueWarning));
                        tvReadFile.setText(R.string.encuentraInterno);
                        //HACER EL HISTORIAL DE FICHEROS GUARDADOS
                        leerFicheroInterno();
                    }else if(comprobarFicheroPrivate()){
                        tvDisplayContacts.setText("");
                        tvReadFile.setTextColor(getResources().getColor(R.color.blueWarning));
                        tvReadFile.setText(R.string.encuentraPrivado);
                        leerFicheroPrivado();
                    }else{
                        tvDisplayContacts.setText("");
                        tvReadFile.setTextColor(getResources().getColor(R.color.red));
                        tvReadFile.setText(R.string.readFileWARNING);
                    }
                }
            }
        });
    }

    private boolean comprobarFicheroPrivate() {
        String ficheroBuscar =etReadFile.getText().toString()+".csv";
        fTest = new File(getExternalFilesDir(null), ficheroBuscar);
        if (fTest.exists()){
            return true;
        }else{
            return false;
        }
    }

    private boolean comprobarFicheroInternal(){
        String ficheroBuscar = etReadFile.getText().toString()+".csv";
        fTest = new File(getFilesDir(),ficheroBuscar);
        if (fTest.exists()){
            return true;
        }else{
            return false;
        }
    }

    private void leerFicheroPrivado() {
        String fileName=etReadFile.getText().toString()+".csv";
        File f = new File(getExternalFilesDir(null), fileName);
        try{
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();

            while(linea != null){
                tvDisplayContacts.setText(tvDisplayContacts.getText()+"\n"+linea);
                linea=br.readLine();
            }

            br.close();fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void leerFicheroInterno() {
        String fileName=etReadFile.getText().toString()+".csv";
        File f = new File(getFilesDir(), fileName);
        try{
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();

            while(linea != null){
                tvDisplayContacts.setText(tvDisplayContacts.getText()+"\n"+linea);
                linea=br.readLine();
            }

            br.close();fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            tvDisplayContacts.setText(e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
