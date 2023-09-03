package com.justkeepfaith.finestexotic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class politica_sulla_riservatezza extends AppCompatActivity {

    Button okbuttonp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_sulla_riservatezza);

        okbuttonp1 = findViewById(R.id.okbuttonp1);

        okbuttonp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}