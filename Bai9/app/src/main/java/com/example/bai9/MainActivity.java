package com.example.bai9;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvContact = findViewById(R.id.lvContact);
        ArrayList<Contact> arrContact = new ArrayList<>();

        arrContact.add(new Contact("Phạm Minh Huy", "23115053122314", Color.RED));
        arrContact.add(new Contact("Ngô Quang Nhật Hào", "23115053122310", Color.GREEN));
        arrContact.add(new Contact("Nguyễn Huỳnh", "23115053122319", Color.GRAY));
        arrContact.add(new Contact("Trần Văn Thọ Khang", "23115053122320", Color.YELLOW));
        arrContact.add(new Contact("Phạm Nguyễn Hoài Duy", "151250533113", Color.BLACK));
        arrContact.add(new Contact("Đỗ Thiên Giang", "131250532378", Color.BLUE));
        arrContact.add(new Contact("Võ Hữu Hải", "151250533116", Color.CYAN));

        CustomAdapter adapter = new CustomAdapter(this, R.layout.row_listview, arrContact);
        lvContact.setAdapter(adapter);
    }
}