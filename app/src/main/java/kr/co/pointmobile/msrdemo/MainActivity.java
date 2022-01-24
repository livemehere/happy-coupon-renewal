package kr.co.pointmobile.msrdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }



        Button button1 = (Button) findViewById(R.id.btnMenu1);
        Button button2 = (Button) findViewById(R.id.btnMenu2);
//        Button button3 = (Button) findViewById(R.id.btnMenu3);
        Button button4 = (Button) findViewById(R.id.btnMenu4);
        Button button5 = (Button) findViewById(R.id.btnMenu5);
//        Button button6 = (Button) findViewById(R.id.btnMenu6);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MsrDemoActivity.class);
//                Intent intent = new Intent(v.getContext(), MoneyActivity.class);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"버튼이 눌러졌습니다~~.",Toast.LENGTH_SHORT).show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UseHappyCouponActivity.class);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"버튼이 눌러졌습니다~~.",Toast.LENGTH_SHORT).show();
            }
        });
//        button3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), SaveCashActivity.class);
//                startActivity(intent);
////                Toast.makeText(getApplicationContext(),"버튼이 눌러졌습니다~~.",Toast.LENGTH_SHORT).show();
//            }
//        });
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HistoryActivity.class);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"버튼이 눌러졌습니다~~.",Toast.LENGTH_SHORT).show();
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), passwordActivity.class);
                startActivity(intent);
            }
        });
//        button6.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), dotActivity.class);
//                startActivity(intent);
////                Toast.makeText(getApplicationContext(),"버튼이 눌러졌습니다~~.",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

}