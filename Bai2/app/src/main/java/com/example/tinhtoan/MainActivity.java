package com.example.tinhtoan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editNum1 = findViewById(R.id.edit_num1);
        EditText editNum2 = findViewById(R.id.edit_num2);
        Button btnCalculate = findViewById(R.id.btn_calculate);
        TextView tvResult = findViewById(R.id.tv_result);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double num1 = Double.parseDouble(editNum1.getText().toString());
                    double num2 = Double.parseDouble(editNum2.getText().toString());

                    double tong = num1 + num2;
                    double hieu = num1 - num2;
                    double tich = num1 * num2;
                    String thuong;
                    if (num2 != 0) {
                        thuong = String.valueOf(num1 / num2);
                    } else {
                        thuong = "Không thể chia cho 0";
                    }

                    String result = "Tổng là: " + tong
                            + "\nHiệu là: " + hieu
                            + "\nTích là: " + tich
                            + "\nThương là: " + thuong;

                    tvResult.setText(result);

                } catch (Exception e) {
                    tvResult.setText("Vui lòng nhập số hợp lệ!");
                }
            }
        });
    }
}
