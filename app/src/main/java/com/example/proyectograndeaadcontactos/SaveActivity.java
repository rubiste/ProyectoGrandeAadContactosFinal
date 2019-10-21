package com.example.proyectograndeaadcontactos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveActivity extends AppCompatActivity {

    private File fGuardado, fRecord;

    private final String NOMBRE_FICHERO_RECORD = "ficheroRecords.txt";
    private final String NOMBRE_FICHERO_GUARDADO="ficheroNombreGuardado.txt";
    private String etIntroducirValues, fileName;

    private Button btSave;
    private TextView tvWarning, tvIntroducir;
    private EditText etIntroducir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        etIntroducirValues = i.getStringExtra("etIntroducir");
        initComponents();
        initFichero();
    }

    private void initFichero() {
        SharedPreferences shSwitch = PreferenceManager.getDefaultSharedPreferences(SaveActivity.this);
        boolean estadoSwitchNombre =shSwitch.getBoolean("guardarTitulo",false);
        if (estadoSwitchNombre) {
            String linea = "";
            fGuardado = new File(getFilesDir(), NOMBRE_FICHERO_GUARDADO);
            try {
                FileReader fr = new FileReader(fGuardado);
                BufferedReader br = new BufferedReader(fr);
                linea = br.readLine();
                br.close();
                fr.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!linea.isEmpty()) {
                etIntroducir.setText(linea);
            }
        }
    }

    private void initComponents() {
        btSave = findViewById(R.id.btSave);
        tvWarning = findViewById(R.id.tvWarning);
        tvIntroducir = findViewById(R.id.tvIntroducir);
        etIntroducir = findViewById(R.id.etIntroducir);

        initEvents();
    }

    private void initEvents() {
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etIntroducir.length() != 0) {
                    saveContacts();
                }
            }
        });
    }

    private void saveRecord(String recordFileName){
        fRecord = new File (getFilesDir(), NOMBRE_FICHERO_RECORD);
        try{
            FileWriter fw = new FileWriter(fRecord, true);
            fw.write(recordFileName+"\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFileInternalStorage(){
        fileName = etIntroducir.getText().toString()+".csv"; //Cogemos nombre que ha escrito en el et
        File file = new File (getFilesDir(),fileName);

        try {
            FileWriter fw = new FileWriter(file, false);
            fw.write(etIntroducirValues);
            Toast.makeText(this, "Guardado en"+getFilesDir()+ "/"+fileName, Toast.LENGTH_LONG).show();
            fw.flush();
            fw.close();
            fileName = etIntroducir.getText().toString();
            saveRecord(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveContacts() {
        SharedPreferences shSwitch = PreferenceManager.getDefaultSharedPreferences(SaveActivity.this);
        SharedPreferences shChooseStorage = PreferenceManager.getDefaultSharedPreferences(SaveActivity.this);
        boolean estadoSwitch = shSwitch.getBoolean("guardarFiles", false);
        boolean estadoSwitchNombre =shSwitch.getBoolean("guardarTitulo",false);
        String storageChoose = shChooseStorage.getString("private_internal","Private");

        if (!estadoSwitch){
            tvWarning.setText(R.string.tvActivate);
        }else{
            tvWarning.setTextColor(getResources().getColor(R.color.blueWarning));
            if(storageChoose.compareTo("Private") == 0){
                 if(checkWritePermissions()){
                     saveFilePrivateStorage();
                 }
            }else if (storageChoose.compareTo("Internal") == 0){
                tvWarning.setText(R.string.tvActivate3);
                saveFileInternalStorage();
            }

            if(estadoSwitchNombre){
                fGuardado = new File(getFilesDir(), NOMBRE_FICHERO_GUARDADO);
                try{
                    FileWriter fw = new FileWriter(fGuardado, false);
                    fw.write(etIntroducir.getText().toString());
                    fw.flush();
                    fw.close();
                }catch(IOException e){
                    e.getMessage();
                }
            }
        }
    }

    private void saveFilePrivateStorage(){
        fileName = etIntroducir.getText().toString().trim()+".csv";
        File f = new File(getExternalFilesDir(null),fileName);
        Log.v("zxyxz", f.getAbsolutePath());

        try {
            FileWriter fw = new FileWriter(f);
            fw.write(etIntroducirValues);
            Toast.makeText(this, "Guardado en"+getFilesDir()+ "/"+fileName, Toast.LENGTH_LONG).show();

            fw.flush();
            fw.close();
            fileName=etIntroducir.getText().toString();
            saveRecord(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean checkWritePermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            Toast toast= Toast.makeText(getApplicationContext(),R.string.aceptarPermisos2
                    ,Toast.LENGTH_LONG);
            return false;
        }else {
            return true;
        }
    }
}
