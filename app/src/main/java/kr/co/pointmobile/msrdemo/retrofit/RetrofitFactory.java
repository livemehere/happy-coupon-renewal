package kr.co.pointmobile.msrdemo.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    private static String BASE_URL = "http://192.168.1.32:3000/";
//    private static String BASE_URL = "http://hc.happycoupon.co.kr/";

    public static RetrofitService create(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RetrofitService.class);
    }
}
