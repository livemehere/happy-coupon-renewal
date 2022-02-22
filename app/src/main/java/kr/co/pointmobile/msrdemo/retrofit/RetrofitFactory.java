package kr.co.pointmobile.msrdemo.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
//    private static final String BASE_URL = "http:10.0.2.2:3000/";
    private static final String BASE_URL = "http://hc.happycoupon.co.kr/";

    public static RetrofitService create(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RetrofitService.class);
    }
}
