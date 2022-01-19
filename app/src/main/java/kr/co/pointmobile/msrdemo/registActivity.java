package kr.co.pointmobile.msrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class registActivity extends AppCompatActivity {

    private EditText store_business_number;
    private EditText store_serial;
    private EditText store_eEB;
    private EditText store_password;
    private EditText store_password_check;

    public static String serial = android.os.Build.SERIAL;
    private String business_number;
    private String eEB;
    private String password;
    private String password_check;




    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("device", Context.MODE_PRIVATE);
        // element 가져오기
        store_business_number = findViewById(R.id.store_business_number);
        store_eEB = findViewById(R.id.store_eEB);
        store_password = findViewById(R.id.store_password);
        store_password_check = findViewById(R.id.store_password_check);
        store_serial = findViewById(R.id.store_serial);

        //값 가져오기
        business_number = sharedPreferences.getString("business_number","");
        eEB = sharedPreferences.getString("eEB","210100237");
        password = sharedPreferences.getString("password","");
        password_check = sharedPreferences.getString("password_check","");

        Log.d("1",business_number);
        Log.d("2",eEB);
        Log.d("3",password);
        Log.d("4",password_check);

        //값 세팅하기
        store_serial.setText(serial);
        store_business_number.setText(business_number);
        store_eEB.setText(eEB);
        store_password.setText(password);
        store_password_check.setText(password_check);





        // 등록하기 버튼
        Button btnRegist = (Button) findViewById(R.id.btnRegist);
        btnRegist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                business_number = store_business_number.getText().toString();
                eEB = store_eEB.getText().toString();
                password = store_password.getText().toString();
                password_check = store_password_check.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("business_number",business_number);
                editor.putString("eEB",eEB);
                editor.putString("password",password);
                editor.putString("password_check",password_check);
                editor.commit();

//                Toast.makeText(getApplicationContext(), "등록되었습니다", Toast.LENGTH_SHORT).show();
                finish();
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