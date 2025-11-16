package com.example.bai11;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    Button button1, button2, button3;
    Button button4, button5, button6;
    Button button7, button8, button9;
    Button button0, buttonStar, buttonClear;
    // THÊM: Khai báo nút gọi
    Button buttonCall;

    TextView numberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Ánh xạ View
        numberView = findViewById(R.id.number_display);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);
        button0 = findViewById(R.id.button0);
        buttonStar = findViewById(R.id.button_star);
        buttonClear = findViewById(R.id.button_clear);
        // THÊM: Ánh xạ nút gọi
        buttonCall = findViewById(R.id.button_call);

        // 2. Gắn sự kiện bấm cho các nút số
        button1.setOnClickListener(appendString("1"));
        button2.setOnClickListener(appendString("2"));
        button3.setOnClickListener(appendString("3"));
        button4.setOnClickListener(appendString("4"));
        button5.setOnClickListener(appendString("5"));
        button6.setOnClickListener(appendString("6"));
        button7.setOnClickListener(appendString("7"));
        button8.setOnClickListener(appendString("8"));
        button9.setOnClickListener(appendString("9"));
        button0.setOnClickListener(appendString("0"));
        buttonStar.setOnClickListener(appendString("*"));

        // Nút Clear
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberView.setText("");
            }
        });

        // THÊM: Xử lý nút CALL
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall();
            }
        });
    }

    // Hàm tạo View.OnClickListener để gắn chuỗi số vào TextView
    public View.OnClickListener appendString(final String number) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberView.append(number);
            }
        };
    }

    // HÀM MỚI: Xử lý chức năng quay số
    private void makeCall() {
        String phoneNumber = numberView.getText().toString().trim();

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo URI với giao thức 'tel:'
        Uri callUri = Uri.parse("tel:" + phoneNumber);

        // Tạo Implicit Intent với hành động ACTION_DIAL
        // ACTION_DIAL sẽ mở ứng dụng quay số (Dialer) của hệ thống
        // và điền sẵn số, người dùng sẽ nhấn CALL lần nữa
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, callUri);

        try {
            startActivity(dialIntent);
        } catch (SecurityException e) {
            // Trường hợp này hiếm khi xảy ra với ACTION_DIAL,
            // nhưng nên xử lý để đề phòng.
            Toast.makeText(this, "Không thể mở ứng dụng quay số. Vui lòng kiểm tra quyền.", Toast.LENGTH_LONG).show();
        }
    }


    // Các hàm Menu vẫn giữ nguyên
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, Menu.FIRST, 0, "Exit")
                .setIcon(android.R.drawable.ic_delete);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == Menu.FIRST) {
            finish(); // Thoát ứng dụng
            return true;
        }
        return false;
    }
}