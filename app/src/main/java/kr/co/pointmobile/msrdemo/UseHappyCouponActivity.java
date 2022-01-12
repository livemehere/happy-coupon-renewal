package kr.co.pointmobile.msrdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kr.co.pointmobile.msrdemo.models.Coupon;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitFactory;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UseHappyCouponActivity extends AppCompatActivity {

    public static String serial = android.os.Build.SERIAL;
    public String coupon_no;

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
                EditText get_card_no = (EditText)findViewById(R.id.coupon_no);
                coupon_no = get_card_no.getText().toString();

                // TODO: 2. http 요청하기 (쿠폰사용)
                // FIXME: 여기다가 serial 넣고, 쿠폰번호가져와서 넣으세요
                RetrofitService networkService = RetrofitFactory.create();
                networkService.useCoupon(serial,coupon_no).enqueue(new Callback<Coupon>() {
                    @Override
                    public void onResponse(Call<Coupon> call, Response<Coupon> response) {

                        if(response.isSuccessful()){
                            if(response.body().result_cd.equals("0000")){
                                Log.d("쿠폰사용결과",response.body().result_cd);
                                Toast.makeText(getApplicationContext(), response.body().result_msg, Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Log.d("쿠폰사용결과",response.body().result_cd);
                                Toast.makeText(getApplicationContext(), response.body().result_msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Coupon> call, Throwable t) {
                        Log.d("쿠폰사용결과",t.toString());
                    }
                });

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