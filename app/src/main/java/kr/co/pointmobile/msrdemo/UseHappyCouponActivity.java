package kr.co.pointmobile.msrdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    private TextView get_coupon_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_happy_coupon);
        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        get_coupon_no= (TextView) findViewById(R.id.coupon_no);
        coupon_no="";


    }
    // 홈 버튼 클릭시 홈화면으로 이동
    public void navToHome(View v){
        finish();
    }
    // 종료 버튼 시 모든 Activity 종료
    public void finishApp(View v){
        finishAffinity();
    }

    //키패드를 누른경우
    public void clickPad(View v){
        TextView target = (TextView) v;
        String targetText = target.getText().toString(); // 키패드의 텍스트값

        // 숫자를 누를겨우
        if(!targetText.equals("확인") && !targetText.equals("X")){
            coupon_no +=targetText;

        }else if(targetText.equals("X")){
            if(coupon_no.length() >0){
                StringBuffer sb= new StringBuffer(coupon_no);
                sb.deleteCharAt(coupon_no.length()-1);
                coupon_no = new String(sb);
            }


        }else if(targetText.equals("확인")){
            //확인을 누를 경우
            if(coupon_no.length()>0){
                // 취소 요청 및 프린트
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

            }else{
//                Toast.makeText(getApplicationContext(), "가격을 적어주세요", Toast.LENGTH_SHORT).show();
            }
        }
        get_coupon_no.setText(coupon_no);


    }

}