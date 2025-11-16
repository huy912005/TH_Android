package com.example.bai1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editName = findViewById(R.id.edit_name);
        EditText editMaSV = findViewById(R.id.edit_masv);
        Button btnShow = findViewById(R.id.btn_show);
        TextView textResult = findViewById(R.id.text_result);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                String masv = editMaSV.getText().toString().trim();

                if (name.isEmpty() || masv.isEmpty()) {
                    textResult.setText("Vui lòng nhập đủ tên và mã sinh viên!");
                } else {
                    textResult.setText("Tên: " + name + "\nMã SV: " + masv);
                }
            }
        });
    }
}
