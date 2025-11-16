package com.example.bai4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {
    private Button btnClick;
    private EditText eDW;
    private EditText eDH;
    private TextView kq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnClick=findViewById(R.id.btnClick);
        eDW=findViewById(R.id.eDW);
        eDH=findViewById(R.id.eDH);
        kq=findViewById(R.id.kq);
        btnClick.setOnClickListener(view -> {
            if (eDW.getText().toString().isEmpty() ||eDH.getText().toString().isEmpty()) {
                kq.setText("Vui lòng nhập đủ cân nặng và chiều cao!");
                kq.setTextColor(Color.RED);
                return;
            }
            float a =Float.parseFloat(eDW.getText().toString());
            float b =Float.parseFloat(eDH.getText().toString());
            float BMI=a/(b*b);
            if(BMI<18)
                kq.setText("BMI là : "+BMI+"\nXếp loại : Người gầy");
            else
                if(BMI>=18 && BMI<=24.9)
                    kq.setText("BMI là : "+BMI+"\nXếp loại : Người bình thường");
                else
                    if(BMI>=25 && BMI<=29.9)
                        kq.setText("BMI là : "+BMI+"\nXếp loại : Người béo phì loại I");
                    else
                        if(BMI>=30 && BMI<=34.9)
                            kq.setText("BMI là : "+BMI+"\nXếp loại : Người béo phì loại II");
                        else
                            kq.setText("BMI là : "+BMI+"\nXếp loại : Người béo phì loại III");
        });
    }
}