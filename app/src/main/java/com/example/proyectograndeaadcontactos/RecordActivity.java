package com.example.proyectograndeaadcontactos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RecordActivity extends AppCompatActivity {

    File fRecord;

    private final String NOMBRE_FICHERO_RECORD = "ficheroRecords.txt";

    Button btDeleteRecord;
    TextView tvRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Intent i = getIntent();
        initComponents();
    }

    private void initComponents() {
        btDeleteRecord = findViewById(R.id.btDeleteRecord);
        tvRecord = findViewById(R.id.tvRecord);
        displayRecords();
        initEvents();
    }

    private void initEvents() {
        btDeleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecords();
            }
        });
    }

    private void deleteRecords() {
        fRecord = new File(getFilesDir(),NOMBRE_FICHERO_RECORD);
        try{
            FileWriter fw = new FileWriter(fRecord,false);
            fw.write("");
            fw.flush();
            fw.close();
            displayRecords();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayRecords() {
        fRecord = new File(getFilesDir(),NOMBRE_FICHERO_RECORD);
        String cad ="------------------------------------------------------------------------";
        try{
            FileReader fr = new FileReader(fRecord);
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            tvRecord.setText("");
            while (linea != null){
                tvRecord.setText(tvRecord.getText()+"\n"+linea+"\n"+cad+"\n");
                linea = br.readLine();
            }
            br.close();fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
