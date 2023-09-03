package com.justkeepfaith.finestexotic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class bancarie_coordinate extends AppCompatActivity {

    EditText bankAccount, confirmbankAccount;
    Button bankSave;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bancarie_coordinate);

        bankAccount = (EditText) findViewById(R.id.bankAccount);
        confirmbankAccount = (EditText) findViewById(R.id.confirmbankAccount);

        bankSave = findViewById(R.id.bankSave);

        sharedPreferences = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String Routing = sharedPreferences.getString("r_number", "");
        String Account_number = sharedPreferences.getString("a_number", "");

        bankAccount.setHint(Account_number);

        bankSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userbankinfo();
            }
        });
    }
    private void userbankinfo() {

        String ubankAccount = bankAccount.getText().toString().trim();
        String uconfirmbankAccount = confirmbankAccount.getText().toString().trim();

        if (ubankAccount.isEmpty()) {
            bankAccount.setError("Inserisci l'IBAN");
            return;
        }
        if (ubankAccount.length() > 35) {
            bankAccount.setError("IBAN non valido");
            return;
        }
        if (ubankAccount.length() < 10) {
            bankAccount.setError("IBAN non valido");
            return;
        }
        if (uconfirmbankAccount.isEmpty()) {
            confirmbankAccount.setError("Conferma l'IBAN");
            return;
        }
        if (!uconfirmbankAccount.equals(ubankAccount)) {
            confirmbankAccount.setError("I campi non corrispondono");
            bankAccount.setError("I campi non corrispondono");
            return;
        }
        if (!Conectivity.isConnectingToInternet(bancarie_coordinate.this)) {
            Toast.makeText(bancarie_coordinate.this, "Per favore controlla la tua connessione Internet", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("a_number", ubankAccount);
        editor.commit();

        progressDialog = new ProgressDialog(bancarie_coordinate.this);
        progressDialog.setMessage("Sincronizzazione...");
        progressDialog.setProgressStyle(0);
        progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);//7000
                    Intent intent = new Intent(bancarie_coordinate.this, profilo.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }).start();
    }
}