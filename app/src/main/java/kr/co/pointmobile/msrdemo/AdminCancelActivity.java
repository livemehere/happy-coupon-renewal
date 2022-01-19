package kr.co.pointmobile.msrdemo;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminCancelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cancel);
        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button button1 = (Button) findViewById(R.id.btnMenu1);
        Button button2 = (Button) findViewById(R.id.btnMenu2);
        Button button3 = (Button) findViewById(R.id.btnMenu3);
        Button button4 = (Button) findViewById(R.id.btnMenu4);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CancelCardActivity.class);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"버튼이 눌러졌습니다~~.",Toast.LENGTH_SHORT).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CancelCouponActivity.class);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"버튼이 눌러졌습니다~~.",Toast.LENGTH_SHORT).show();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MargetInfoActivity.class);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"버튼이 눌러졌습니다~~.",Toast.LENGTH_SHORT).show();
            }
        });
//        button4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                startActivity(new Intent(Settings.ACTION_SETTINGS));
//            }
//        });

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