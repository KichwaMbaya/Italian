package com.justkeepfaith.finestexotic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class profilo extends AppCompatActivity {

    TextView user_details, bankaccount;
    Button editbank, deleteaccount;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        user_details = findViewById(R.id.user_details);
        bankaccount = (TextView) findViewById(R.id.bankaccount);
        editbank = findViewById(R.id.editbank);
        deleteaccount = findViewById(R.id.deleteaccount);

        sharedPreferences = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);

        String string = sharedPreferences.getString("first_name", "");
        String string2 = sharedPreferences.getString("middle_name", "");
        String string3 = sharedPreferences.getString("last_name", "");
        String string4 = sharedPreferences.getString("Email", "");
        String string5 = sharedPreferences.getString("phone_number", "");
        String string6 = sharedPreferences.getString("occupation", "");
        String string7 = sharedPreferences.getString("workplace", "");
        String string8 = sharedPreferences.getString("income", "");
        String string9 = sharedPreferences.getString("IDno", "");
        String string10 = sharedPreferences.getString("keen_name", "");
        String string11 = sharedPreferences.getString("relationship", "");
        String string12 = sharedPreferences.getString("keen_phone", "");

        String Routing = sharedPreferences.getString("r_number", "");
        String Account_number = sharedPreferences.getString("a_number", "");

        user_details.setText("\n1. Nome: "+ string + " " + string2 + " " + string3 +
                "\n\n2. E-mail: " + string4 +
                "\n\n3. Telefono: " + string5 +
                "\n\n4. Occupazione: " + string6 +
                "\n\n5. Posto di lavoro: " + string7 +
                "\n\n6. Codice Fiscale: " + string9 +
                "\n\n7. Garante: " + string10 +
                "\n\n8. Relazione: " + string11 +
                "\n\n9. Telefono: " +string12 + "\n");


        bankaccount.setText(Account_number);

        editbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profilo.this, bancarie_coordinate.class);
                startActivity(intent);
            }
        });

        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(profilo.this);
                dialog.setTitle("Sei sicuro?");
                dialog.setMessage("L'eliminazione di questo account comporterà la completa rimozione dei tuoi dati dal sistema." +
                        "Questa azione non può essere annullata e non potrai più accedere all'app. \n\n" +
                        "Tieni presente che se hai risparmiato del denaro sul tuo conto, ti verrà rimborsato entro 5 giorni lavorativi.");
                dialog.setPositiveButton("Eliminare", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (!Conectivity.isConnectingToInternet(profilo.this)) {
                            Toast.makeText(profilo.this, "Per favore controlla la tua connessione Internet", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();

                        Toast.makeText(profilo.this, "Conto cancellato", Toast.LENGTH_SHORT).show();

                        android.os.Process.killProcess(android.os.Process.myPid());

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
}