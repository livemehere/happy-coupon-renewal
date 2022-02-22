package kr.co.pointmobile.msrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import kr.co.pointmobile.msrdemo.models.Coupon;
import kr.co.pointmobile.msrdemo.models.Marget;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitFactory;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MargetInfoActivity extends AppCompatActivity {
//    public static String serial = android.os.Build.SERIAL;
    public static String serial = android.os.Build.SERIAL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marget_info);

        SharedPreferences sharedPreferences = getSharedPreferences("marget", Context.MODE_PRIVATE);

        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        try {
            RetrofitService networkService = RetrofitFactory.create();
            networkService.getMargetInfo(serial).enqueue(new Callback<Marget>() {
                @Override
                public void onResponse(Call<Marget> call, Response<Marget> response) {

                    EditText get_store_name = (EditText)findViewById(R.id.store_name);
                    EditText get_store_companyid = (EditText)findViewById(R.id.store_companyid);
                    EditText get_store_ceoname = (EditText)findViewById(R.id.store_ceoname);
                    EditText get_store_phone = (EditText)findViewById(R.id.store_phone);
                    EditText get_store_address = (EditText)findViewById(R.id.store_address);
                    EditText get_serial = (EditText)findViewById(R.id.store_serial);


                    if(response.isSuccessful()){
                        get_serial.setText(serial);
                        get_store_name.setText(response.body().name);
                        get_store_companyid.setText(response.body().companyid);
                        get_store_ceoname.setText(response.body().ceoname);
                        get_store_phone.setText(response.body().phone);
                        get_store_address.setText(response.body().address);

//                        Log.d("data1",response.body().result_cd);
//                        Log.d("data1",response.body().name);
//                        Log.d("data1",response.body().companyid);
//                        Log.d("data1",response.body().ceoname);
//                        Log.d("data1",response.body().phone);
//                        Log.d("data1",response.body().address);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("name",response.body().name);
                        editor.putString("companyid",response.body().companyid);
                        editor.putString("ceoname",response.body().ceoname);
                        editor.putString("phone",response.body().phone);
                        editor.putString("address",response.body().address);
                        editor.commit();

                    }else{
//                        Toast.makeText(getApplicationContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Marget> call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e) {
            Log.w("에러", "가맹점 정보 불로오는 중 에러발생 ", e);
        }


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
