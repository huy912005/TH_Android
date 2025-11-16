package com.example.bai10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Activity1 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1_layout);

        EditText editValue = findViewById(R.id.value_edit);
        Button sendButton = findViewById(R.id.send_button);

        sendButton.setOnClickListener(v -> {
            String valueString = editValue.getText().toString().trim();

            Intent intent = new Intent(Activity1.this, Activity2.class);
            intent.putExtra("value", valueString);
            startActivity(intent);
        });
    }
}
