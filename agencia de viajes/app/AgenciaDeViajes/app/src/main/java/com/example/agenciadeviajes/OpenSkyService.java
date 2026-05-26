package com.example.agenciadeviajes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenSkyService {
    // Pedimos coordenadas: lamin, lomin, lamax, lomax
    @GET("states/all")
    Call<OpenSkyResponse> getAreaStates(
            @Query("lamin") double lamin,
            @Query("lomin") double lomin,
            @Query("lamax") double lamax,
            @Query("lomax") double lomax
    );
}
