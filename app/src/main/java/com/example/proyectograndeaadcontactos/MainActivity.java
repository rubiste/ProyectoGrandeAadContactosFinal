package com.example.proyectograndeaadcontactos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.proyectograndeaadcontactos.settings.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.ContactsContract;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvContactos;
    private int permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        askContactPermissions();
        askWritePrivatePermissions();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initComponents();
        initEvents();
    }

    private void initComponents() {
        tvContactos = findViewById(R.id.tvContactos);
    }

    private void espera(int tiempo){
        try {
            Thread.sleep(tiempo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean askContactPermissions() {
        if (!checkPermissionsContactos()){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, permissions);
                espera(5000);
            if (permissions == 0){
                return false;
            }else{
                return true;
            }
        }
        return true;
    }

    private void initEvents() {
        if (askContactPermissions()){
            ArrayList<String> showContactsPhone = recuperarListaTelefonos(showContactsName());
            typeContactsInTextView(showContactsName(), showContactsPhone);
        }else{
            tvContactos.setText(R.string.aceptarPermisos);
        }
    }

    private ArrayList<String> recuperarListaTelefonos(List<Contacto> showContactsName) {
        ArrayList <String> showContactsPhone = new ArrayList<>();
        for (int i= 0; i<showContactsName.size();i++){
            showContactsPhone.add(String.valueOf(showContactsPhone(showContactsName.get(i).getId())));
        }
        return showContactsPhone;
    }

    private void typeContactsInTextView(List<Contacto> showContactsName, List<String> showContactsPhone) {
        for (int i=0; i < showContactsName.size(); i++){
            tvContactos.setText(tvContactos.getText()+""+showContactsName.get(i).toString()
                    +showContactsPhone.get(i)+"\n\n");
        }
    }

    public List<Contacto> showContactsName(){
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ? and " +
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "= ?";
        String argumentos[] = new String[]{"1","1"};
        String orden = ContactsContract.Contacts.DISPLAY_NAME + " collate localized asc";
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        int indiceId = cursor.getColumnIndex(ContactsContract.Contacts._ID);
        int indiceNombre = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        List<Contacto> lista = new ArrayList<>();
        Contacto contacto;
        while(cursor.moveToNext()){
            contacto = new Contacto();
            contacto.setId(cursor.getLong(indiceId));
            contacto.setNombre(cursor.getString(indiceNombre));
            lista.add(contacto);
        }
        return lista;
    }

    public List<String> showContactsPhone(long id){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String proyeccion[] = null;
        String seleccion = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String argumentos[] = new String[]{id+""};
        String orden = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        int indiceNumero = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        List<String> lista = new ArrayList<>();
        String numero;
        while(cursor.moveToNext()){
            numero = cursor.getString(indiceNumero);
            lista.add(numero);
        }
        return lista;
    }

    private boolean checkPermissionsContactos(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            Toast toast= Toast.makeText(getApplicationContext(),R.string.aceptarPermisos
                    ,Toast.LENGTH_LONG);
            return false;
        }else {
            return true;
        }
    }


    private boolean checkPermissionsWritePrivate(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            Toast toast= Toast.makeText(getApplicationContext(),"You have to accept the permissions to use this app"
                    ,Toast.LENGTH_LONG);
            return false;
        }else {
            return true;
        }
    }

    private boolean askWritePrivatePermissions() {
        if (!checkPermissionsWritePrivate()){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissions);
            espera(5000);
            if (permissions == 0){
                return false;
            }else{
                return true;
            }
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i;
        switch(id){
            case R.id.action_settings:
                i = new Intent (MainActivity.this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.guardarContactos:
                i = new Intent(MainActivity.this, SaveActivity.class);
                i.putExtra("etIntroducir",tvContactos.getText().toString());
                startActivity(i);
                break;
            case R.id.leerContactos:
                i = new Intent(MainActivity.this, ReadActivity.class);
                startActivity(i);
                break;
            case R.id.mostrarHistorial:
                i = new Intent (MainActivity.this, RecordActivity.class);
                startActivity(i);
                break;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
