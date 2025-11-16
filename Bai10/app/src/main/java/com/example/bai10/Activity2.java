package com.example.bai10;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class Activity2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_layout);

        EditText receiveValueEdit = findViewById(R.id.value_receive);
        Button callReceiverButton = findViewById(R.id.call_button);

        // Nhận dữ liệu kiểu String
        String receiveValue = getIntent().getStringExtra("value");
        if (receiveValue == null) receiveValue = "";
        receiveValueEdit.setText(receiveValue);

        callReceiverButton.setOnClickListener(v -> {
            // Lấy nội dung mới nhập ở EditText
            String newValue = receiveValueEdit.getText().toString().trim();

            // Gửi broadcast sang Receiver
            Intent i = new Intent(Activity2.this, Receiver.class);
            i.putExtra("new_value", newValue);
            sendBroadcast(i);
        });
    }
}
