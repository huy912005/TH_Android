package com.example.recycerlview;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import api.ApiService;
import api.RetrofitClient;
import model.Building;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBuilding extends AppCompatActivity {
    private EditText edt1, edt2, edt3, edt4, edt5;
    private Button btnCreate, btnBack;
    private static final String TAG = "CreateBuildingApp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_building);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.createbuilding), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btn2);
        btnBack.setOnClickListener(v -> {
            // Quay lại MainActivity
            finish();
        });

        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        edt3 = findViewById(R.id.edt3);
        edt4 = findViewById(R.id.edt4);
        edt5 = findViewById(R.id.edt5);

        btnCreate = findViewById(R.id.btn1);
        btnCreate.setOnClickListener(v -> {
            createBuilding();
        });
    }
    public void createBuilding() {
        String districtId =edt1.getText().toString();
        String name = edt2.getText().toString();
        String street = edt3.getText().toString();
        String ward = edt4.getText().toString();
        String basementStr = edt5.getText().toString();
        if(name.isEmpty() || street.isEmpty() || ward.isEmpty() || basementStr.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        int numberOfBasement =0;
        try {
            numberOfBasement = Integer.parseInt(basementStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Số tầng không hợp lệ!", Toast.LENGTH_SHORT).show();
            return;
        }
        Building building = new Building();
        building.setDistrictId(Long.parseLong(districtId));
        building.setName(name);
        building.setStreet(street);
        building.setWard(ward);
        building.setNumberOfBasement(numberOfBasement);

        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<Building> call = api.createBuilding(building);
        call.enqueue(new Callback<Building>(){

            @Override
            public void onResponse(Call<Building> call, Response<Building> response) {
                if(response.isSuccessful() && response.body() != null ) {
                    Toast.makeText(CreateBuilding.this, "Tạo thành công", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Create successfull : " + response.body());
                    setResult(RESULT_OK);
                    finish();
                }
                else {
                    Toast.makeText(CreateBuilding.this, "Tạo thất bại", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    Log.e(TAG, "Lỗi phản hồi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Building> call, Throwable t) {
                Toast.makeText(CreateBuilding.this, "Lỗi kết nối API: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Lỗi API: " + t.getMessage());
            }
        });
    }
}