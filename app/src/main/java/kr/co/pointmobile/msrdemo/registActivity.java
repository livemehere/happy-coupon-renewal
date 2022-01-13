package kr.co.pointmobile.msrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class registActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnRegist = (Button) findViewById(R.id.btnRegist);

        //환경설정
        btnRegist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "등록되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 홈 버튼 클릭시 홈화면으로 이동
    public void navToHome(View v){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    // 종료 버튼 시 모든 Activity 종료
    public void finishApp(View v){
        finishAffinity();
    }
}