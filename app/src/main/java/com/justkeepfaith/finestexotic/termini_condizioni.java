package com.justkeepfaith.finestexotic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class termini_condizioni extends AppCompatActivity {

    Button okbuttont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termini_condizioni);

        okbuttont = findViewById(R.id.okbuttont);

        okbuttont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}