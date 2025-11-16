package com.example.vidu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText et;
    private Button btn_click;
    private TextView tv;
    private ProgressBar pg;
    RatingBar mRatingBar;

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
        et=findViewById(R.id.et);
        tv=findViewById(R.id.tv);
        btn_click=findViewById(R.id.btn_click);
        pg=findViewById(R.id.pb);
        pg.setVisibility(View.GONE);

        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(100); // đặt max vì max 100
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    int progress = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(progress);
                        }
                    });
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        btn_click.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                et.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
                btn_click.setVisibility(View.GONE);
                pg.setVisibility(View.VISIBLE);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String input =et.getText().toString().trim();
                        if(chekName(input)==false){
                            tv.setText("Nhập sai tên rồi");
                            et.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.VISIBLE);
                            btn_click.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            tv.setText("Good job!");
                            tv.setVisibility(View.VISIBLE);
                            Intent intent = new Intent(MainActivity.this, Screen2.class);
                            startActivity(intent);
                        }
                        pg.setVisibility(View.GONE);
                    }
                },2000);
            }
        });
        //radio button
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton1) {
                    Toast toast = Toast.makeText(MainActivity.this, "Bạn chọn 1", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.END, 50, 100);
                    toast.show();
                } else if (checkedId == R.id.radioButton2) {
                    Toast toast = Toast.makeText(MainActivity.this, "Bạn chọn 2", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
                    toast.show();
                }
            }
        });

        // RatingBar hình ngôi sao
        mRatingBar = findViewById(R.id.ratingBar);
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String rate=String.valueOf(ratingBar.getRating());
                tv.setText(rate);
                Toast.makeText(MainActivity.this, "User Rating is :"+rate, Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean chekName(String name){
        if(name.equals("Pham Minh Huy"))
            return true;
        return false;
    }
}