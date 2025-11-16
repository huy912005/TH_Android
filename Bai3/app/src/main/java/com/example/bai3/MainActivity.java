package com.example.bai3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editNumber;
    private Button btnCheck;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNumber = findViewById(R.id.edit_number);
        btnCheck = findViewById(R.id.btn_check);
        txtResult = findViewById(R.id.txt_result);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editNumber.getText().toString().trim();
                if (input.isEmpty()) {
                    txtResult.setText("Vui lòng nhập một số!");
                    return;
                }

                int num = Integer.parseInt(input);
                if (isPrime(num)) {
                    txtResult.setText(num + " là số nguyên tố");
                } else {
                    txtResult.setText(num + " không phải là số nguyên tố");
                }
            }
        });
    }
    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}
