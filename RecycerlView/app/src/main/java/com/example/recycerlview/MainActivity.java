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

import java.util.List;

import api.ApiService;
import api.RetrofitClient;
import model.Building;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText edt1, edt2, edt3, edt4, edt5;
    private Button btnBack, btnNext,btnCreate,btnDelete;

    // 1. Khai báo biến để lưu trữ danh sách và chỉ mục hiện tại
    private List<Building> buildingList;
    private int currentIndex = 0;
    private static final String TAG = "BuildingApp";
    private static final int CREATE_BUILDING_REQUEST_CODE = 1;

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

        // Ánh xạ View
        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        edt3 = findViewById(R.id.edt3);
        edt4 = findViewById(R.id.edt4);
        edt5 = findViewById(R.id.edt5);
        btnBack = findViewById(R.id.btn1); // back
        btnNext = findViewById(R.id.btn2); // next
        btnCreate=findViewById(R.id.btnCreate);
        btnDelete=findViewById(R.id.btnDelete);

        // Ẩn nút Back và Next ban đầu
        btnBack.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);

        // Gọi API
        fetchData();

        // 2. Thiết lập sự kiện click cho nút NEXT
        btnNext.setOnClickListener(v -> {
            if (buildingList != null && currentIndex < buildingList.size() - 1) {
                currentIndex++;
                displayBuilding(currentIndex);
            }
        });

        // 3. Thiết lập sự kiện click cho nút BACK
        btnBack.setOnClickListener(v -> {
            if (buildingList != null && currentIndex > 0) {
                currentIndex--;
                displayBuilding(currentIndex);
            }
        });

        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this,CreateBuilding.class);
            startActivityForResult(intent, CREATE_BUILDING_REQUEST_CODE);
        });

        btnDelete.setOnClickListener(v -> {
            if(buildingList!=null && currentIndex>=0 && currentIndex<buildingList.size()) {
                Building b = buildingList.get(currentIndex);
                deleteBuilding(b.getId());
            }else{
                Toast.makeText(this, "Không có dữ liệu để xóa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // TRONG MainActivity.java, thêm phương thức này
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra xem kết quả có phải từ CreateBuilding Activity (mã 1) hay không
        if (requestCode == CREATE_BUILDING_REQUEST_CODE) {
            // Kiểm tra xem kết quả trả về có phải là RESULT_OK (Tạo thành công) hay không
            if (resultCode == RESULT_OK) {
                // TẢI LẠI DỮ LIỆU TỪ SERVER
                fetchData();
                Toast.makeText(this, "Danh sách đã được cập nhật!", Toast.LENGTH_SHORT).show();

                // Đảm bảo sau khi tải lại, giao diện hiển thị từ đầu danh sách (index 0)
                currentIndex = 0;
            }
        }
    }

    // Hàm gọi API
    private void fetchData() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Building>> listBuildings = api.getAllBuildings();
        listBuildings.enqueue(new Callback<List<Building>>(){
            @Override
            public void onResponse(Call<List<Building>> call, Response<List<Building>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Lưu trữ danh sách vào biến toàn cục
                    buildingList = response.body();
                    currentIndex = 0;

                    // Hiển thị phần tử đầu tiên
                    displayBuilding(currentIndex);

                    // Hiện nút Next (nếu có hơn 1 phần tử) và Back
                    if (buildingList.size() > 1) {
                        btnNext.setVisibility(View.VISIBLE);
                        btnBack.setVisibility(View.VISIBLE);
                    } else {
                        btnNext.setVisibility(View.GONE);
                        btnBack.setVisibility(View.GONE);
                    }

                    Log.d(TAG, "Dữ liệu đã hiển thị thành công!");
                } else {
                    Log.e(TAG, "Không có dữ liệu hoặc lỗi phản hồi");
                    Toast.makeText(MainActivity.this, "Không có dữ liệu tòa nhà.", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Building>> call, Throwable t) {
                Log.e(TAG,"Lỗi api : "+t.getMessage());
                Toast.makeText(MainActivity.this, "Lỗi kết nối API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // 4. Hàm hiển thị dữ liệu lên giao diện
    private void displayBuilding(int index) {
        if (buildingList != null && index >= 0 && index < buildingList.size()) {
            Building b = buildingList.get(index);
            edt1.setText(String.valueOf(b.getId()));
            edt2.setText(b.getName());
            edt3.setText(b.getStreet());
            edt4.setText(b.getWard());
            edt5.setText(String.valueOf(b.getNumberOfBasement()));

            // Cập nhật trạng thái nút Back/Next
            btnBack.setEnabled(currentIndex > 0);
            btnNext.setEnabled(currentIndex < buildingList.size() - 1);

            // (Không bắt buộc) Thông báo cho người dùng biết đang ở phần tử nào
            // getSupportActionBar().setTitle("Building " + (currentIndex + 1) + "/" + buildingList.size());
        }
    }

    private void deleteBuilding(int id){
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = api.deleteBuilding(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Xoa thanh cong!");
                    fetchData();
                }else{
                    Toast.makeText(MainActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối khi xóa: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Delete API error: " + t.getMessage());
            }
        });
    }
}