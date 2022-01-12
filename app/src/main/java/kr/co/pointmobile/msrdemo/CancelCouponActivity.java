package kr.co.pointmobile.msrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kr.co.pointmobile.msrdemo.models.CardAuthResult;
import kr.co.pointmobile.msrdemo.models.Coupon;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitFactory;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelCouponActivity extends AppCompatActivity {
    public static String serial = android.os.Build.SERIAL;
    public String coupon_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_coupon);

        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button Button_use = findViewById(R.id.Button_cancel);
        Button_use.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  EditText get_card_no = (EditText) findViewById(R.id.coupon_no);
                  coupon_no = get_card_no.getText().toString();

                  // TODO: 2. http 요청하기 (쿠폰취소)
                  // FIXME: 여기다가 serial 넣고, 쿠폰번호가져와서 넣으세요
                  RetrofitService networkService = RetrofitFactory.create();
                  networkService.cancelCoupon(serial, coupon_no).enqueue(new Callback<Coupon>() {
                      @Override
                      public void onResponse(Call<Coupon> call, Response<Coupon> response) {
                          if (response.isSuccessful()) {
                              if (response.body().result_cd.equals("0000")) {
                                  // 카드 승인이 정상적으로 이루어진 경우
                                  // TODO: 여기서 영수증 출력하면 됨 (res_ 는 리턴값들)
                                  String res_result_cd = response.body().result_cd;
                                  String res_result_msg = response.body().result_msg;

                                  Toast.makeText(getApplicationContext(), response.body().result_msg, Toast.LENGTH_SHORT).show();
                                  //----- 결과값 출력 팝
                                  String valuesMessage = String.format("결과코드: %s(0000:정상처리)\n결과메세지: %s",
                                          res_result_cd, res_result_msg);
                                  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CancelCouponActivity.this);
                                  builder.setMessage(valuesMessage).setTitle("취소 완료");
                                  builder.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialog, int id) {

                                          Toast.makeText(getApplicationContext(), "쿠폰 사용 취소 성", Toast.LENGTH_SHORT).show();

                                      }
                                  });
                                  android.app.AlertDialog dialog = builder.create();
                                  dialog.show();
                                  // -----
                              } else {
                                  // TODO: 카드 승인이 실패한 경우
                                  Toast.makeText(getApplicationContext(), response.body().result_msg, Toast.LENGTH_SHORT).show();
                              }
                          }
                      }
                      @Override
                      public void onFailure(Call<Coupon> call, Throwable t) {
                          Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
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
}
