package kr.co.pointmobile.msrdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
                Toast.makeText(getApplicationContext(), "정상적으로 처리되었습니다", Toast.LENGTH_SHORT).show();
                //사용완료가되면
                finish();
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