package com.example.bai8;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;/** * Created by webmaster@dotnet.vn on 10/11/2017. */
public class MainActivity  extends AppCompatActivity {
    private ListView lv;
    private String[] number = {"Ngô Quang Nhật Hào","23115053122310","151250533308","161250533207","151250533113","131250532378"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv= (ListView) findViewById(R.id.lvMaSinhVien);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,number);
        lv.setAdapter(arrayAdapter);    }
}