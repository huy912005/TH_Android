package com.example.bai7;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bai7.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by webmaster@dotnet.vn on 10/11/2017.
 */
public class MainActivity extends AppCompatActivity {

    private Spinner spnCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spnCategory = findViewById(R.id.spnCategory);

        // Danh sách item
        List<String> list = new ArrayList<>();
        list.add("Java");
        list.add("Android");
        list.add("PHP");
        list.add("C#");
        list.add("ASP.NET");

        // Adapter – cầu nối giữa dữ liệu và Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                list
        );

        // Kiểu danh sách xổ xuống (có chấm tròn - single choice)
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        // Gắn adapter vào Spinner
        spnCategory.setAdapter(adapter);

        // Bắt sự kiện khi chọn item
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        MainActivity.this,
                        spnCategory.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT
                ).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Trường hợp không chọn gì
            }
        });
    }
}
