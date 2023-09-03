package com.justkeepfaith.finestexotic;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class registrati extends AppCompatActivity {

    EditText rgsterfirst, rgstermiddle, rgsterlast, rgsteremail, rgsterphone, rgsterpin, rgsterconfirm;
    Button rgsterdob, rgsterbutton;
    DatePickerDialog datePickerDialog;
    SharedPreferences sharedPreferences;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrati);

        rgsterfirst = findViewById(R.id.rgsterfirst);
        rgstermiddle = findViewById(R.id.rgstermiddle);
        rgsterlast = findViewById(R.id.rgsterlast);
        rgsteremail = findViewById(R.id.rgsteremail);
        rgsterphone = findViewById(R.id.rgsterphone);
        rgsterpin = findViewById(R.id.rgsterpin);
        rgsterconfirm = findViewById(R.id.rgsterconfirm);
        rgsterdob = findViewById(R.id.rgsterdob);
        rgsterbutton = findViewById(R.id.rgsterbutton);


        initDatePicker();
        rgsterdob.setText(getTodaysDate());

        if (!Conectivity.isConnectingToInternet(registrati.this)) {
            Toast.makeText(registrati.this, "Per favore controlla la tua connessione Internet", Toast.LENGTH_SHORT).show();
        }

        sharedPreferences = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);

        rgsterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String first = rgsterfirst.getText().toString();
                String middle = rgstermiddle.getText().toString();
                String last = rgsterlast.getText().toString();
                String email = rgsteremail.getText().toString();
                String phone = rgsterphone.getText().toString();
                String pin = rgsterpin.getText().toString();
                String confirm = rgsterconfirm.getText().toString();

                if (first.isEmpty()){
                    rgsterfirst.setError("Inserisci il nome");
                    return;
                }
                if (middle.isEmpty()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("middle_name");
                    editor.commit();
                }
                if (last.isEmpty()){
                    rgsterlast.setError("Inserisci il nome");
                    return;
                }
                if (email.isEmpty()){
                    rgsteremail.setError("Inserisci il tuo indirizzo email");
                    Toast.makeText(registrati.this, "Inserisci il tuo indirizzo email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    rgsteremail.setError("Inserisci un'e-mail valida");
                    rgsteremail.requestFocus();
                    return;
                }
                if (phone.isEmpty()){
                    rgsterphone.setError("Inserisci il numero di telefono");
                    Toast.makeText(registrati.this, "Inserisci il numero di telefono", Toast.LENGTH_SHORT).show();
                }
                if (phone.length() < 8){
                    rgsterphone.setError("Troppo corta");
                    Toast.makeText(registrati.this, "Troppo corta", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.length() > 13){
                    rgsterphone.setError("Troppo lungo");
                    Toast.makeText(registrati.this, "Troppo lungo", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pin.isEmpty()){
                    rgsterpin.setError("Inserisci il SPILLO");
                    Toast.makeText(registrati.this, "Inserisci il SPILLO", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pin.length() < 4){
                    rgsterpin.setError("Troppo corta");
                    Toast.makeText(registrati.this, "Troppo corta", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (confirm.isEmpty()){
                    rgsterconfirm.setError("Confermare SPILLO");
                    Toast.makeText(registrati.this, "Confermare SPILLO", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!confirm.equals(pin)){
                    rgsterpin.setError("I SPILLO non corrispondono");
                    rgsterconfirm.setError("I SPILLO non corrispondono");
                    Toast.makeText(registrati.this, "I SPILLO non corrispondono", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder dialog = new AlertDialog.Builder(registrati.this);
                dialog.setTitle("Sei sicuro?");
                dialog.setMessage("Le informazioni fornite non possono essere modificate dopo la registrazione.");
                dialog.setPositiveButton("Procedere", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("first_name", first);
                        editor.putString("middle_name", middle);
                        editor.putString("last_name", last);
                        editor.putString("Email", email);
                        editor.putString("phone_number", phone);
                        editor.putString("PIN", pin);

                        Integer logged_in = 2;
                        editor.putInt("logged", logged_in);

                        editor.commit();

                        progressDialog = new ProgressDialog(registrati.this);
                        progressDialog.setTitle("Attendere prego");
                        progressDialog.setMessage("Creazione dell'account...");
                        progressDialog.setProgressStyle(0);
                        progressDialog.setMax(100);
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(6000);
                                    Intent intent = new Intent(registrati.this, accesso.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                            }
                        }).start();

                    }
                });
                dialog.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });
    }
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int current = cal.get(YEAR);
        int age = 16;

        int year = current - age;
        int month = cal.get(MONTH);
        month = month + 1;
        int day = cal.get(DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                rgsterdob.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int current = cal.get(YEAR);
        int age = 16;

        int year = current - age;
        int month = cal.get(MONTH);
        month = month + 1;
        int day = cal.get(DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + ", " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "GENNAIO";
        if(month == 2)
            return "FEBBRAIO";
        if(month == 3)
            return "MARZO";
        if(month == 4)
            return "APRILE";
        if(month == 5)
            return "MAGGIO";
        if(month == 6)
            return "GIUGNO";
        if(month == 7)
            return "LUGLIO";
        if(month == 8)
            return "AGOSTO";
        if(month == 9)
            return "SETTEMBRE";
        if(month == 10)
            return "OTTOBRE";
        if(month == 11)
            return "NOVEMBRE";
        if(month == 12)
            return "DICEMBRE";

        //default should never happen
        return "AGOSTO";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}