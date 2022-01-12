package kr.co.pointmobile.msrdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.pointmobile.msrdemo.models.Coupon;
import kr.co.pointmobile.msrdemo.models.Post;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitFactory;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UseHappyCouponActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_happy_coupon);
        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // 쿠폰사용하기 버튼
        Button Button_use = findViewById(R.id.Button_use);
        Button_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: 2. http 요청하기 (쿠폰사용)
                // FIXME: 여기다가 serial 넣고, 쿠폰번호가져와서 넣으세요
                RetrofitService networkService = RetrofitFactory.create();
                networkService.useCoupon("123123123","000").enqueue(new Callback<Coupon>() {
                    @Override
                    public void onResponse(Call<Coupon> call, Response<Coupon> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getApplicationContext(), response.body().id, Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), call.request().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Coupon> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "http연결 실패", Toast.LENGTH_SHORT).show();
                    }
                });
                //사용완료가되면

            }
        });
    }
    // 홈 버튼 클릭시 홈화면으로 이동
    public void navToHome(View v){
        finish();
    }
    // 종료 버튼 시 모든 Activity 종료
    public void finishApp(View v){
        finishAffinity();
    }

    // 쿠폰사용 버튼
    public void  btnUseCoupon(){

    }
}