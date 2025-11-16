package api;

import java.util.List;

import model.Building;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/building") // Endpoint trả JSON ở server
    Call<List<Building>> getAllBuildings();

    @POST("api/building")
    Call<Building> createBuilding(@Body Building building);

    @DELETE("api/building/{id}")
    Call<Void> deleteBuilding(@Path("id") int id);
}
