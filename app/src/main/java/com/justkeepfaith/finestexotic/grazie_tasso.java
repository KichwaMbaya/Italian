package com.justkeepfaith.finestexotic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class grazie_tasso extends AppCompatActivity {

    TextView thanksokbutton, no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grazie_tasso);

        thanksokbutton = (TextView) findViewById(R.id.thanksokbutton);
        no = (TextView) findViewById(R.id.no);

        cancelalarm();


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.TEXT", "Per condizioni di prestito rapide e migliori. Segui il link " +
                        "sottostante per scaricare l'app nel Play Store e applicare. Ottieni €50 quando condividi con un amico\n  " +
                        "https://play.google.com/store/apps/details?id=com.justkeepfaith.finestexotic");
                intent.setType("text/plain");
                grazie_tasso.this.startActivity(Intent.createChooser(intent, (CharSequence) null));
            }
        });
        thanksokbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.justkeepfaith.finestexotic"));
                startActivity(intent);
            }
        });

    }
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Dacci un 5 stelle e una recensione").setCancelable(false).setPositiveButton("VALUTARE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                grazie_tasso.this.startActivity(new Intent("android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/details?id=com.justkeepfaith.finestexotic")));
            }
        }).setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
    private void cancelalarm(){

        Intent intent = new Intent(grazie_tasso.this, Notification_Alarm2.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(grazie_tasso.this, 0,
                intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }
}