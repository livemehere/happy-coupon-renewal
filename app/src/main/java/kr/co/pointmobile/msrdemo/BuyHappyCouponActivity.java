package kr.co.pointmobile.msrdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class BuyHappyCouponActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_happy_coupon);
        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // 결제버튼
        Button Button_buy = findViewById(R.id.Button_buy);
        Button_buy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 1. 단말기번호, 카드번호, 유효기간, 할부, 금액 값 가져와서 세팅하기
                String serial = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                EditText get_expire_date = (EditText)findViewById(R.id.expire_date);
                Spinner get_install_period = (Spinner)findViewById(R.id.install_period_spinner);
                EditText get_tot_amt = (EditText)findViewById(R.id.tot_amt);

                String expire_date = get_expire_date.getText().toString(); //유효기간
                String install_period = get_install_period.getSelectedItem().toString(); //할부
                String tot_amt = get_tot_amt.getText().toString();//금액

                String valuesMessage = String.format("단말기번호: %s\n카드번호: %s\n유효기간: %s\n할부: %s (개월)\n금액: %s (원)",serial,"카드번호",expire_date,install_period,tot_amt);
                AlertDialog.Builder builder = new AlertDialog.Builder(BuyHappyCouponActivity.this);
                builder.setMessage(valuesMessage).setTitle("아래의 정보로 결제합니다");
                builder.setPositiveButton("결제", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // 0. 카드 긁기 대기 창띄우기 + 카드 긁을때까지 대기(취소버튼있음)
                        // 0.5 . 카드를 긁으면! 아래 실행
                        // 1. 단말기번호, 카드번호, 유효기간, 할부, 금액 값 가져오기
                        // 2. http 요청하기
                        // 3. 결과값받아와서 성공이면 성공 tost 띄우고, 프린트하기
                        Toast.makeText(getApplicationContext(), "결제되었습니다", Toast.LENGTH_SHORT).show();
                        // + 결제가 완료되면 홈으로
                        finish();
                        // 4. 실패하거나, 카드긁기 취소하면 실패 tost 띄우기

                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Toast.makeText(getApplicationContext(), "취소되었습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        //Spinner객체 생성

        final Spinner spinner_field = (Spinner)findViewById(R.id.install_period_spinner);
        //1번에서 생성한 field.xml의 item을 String 배열로 가져오기
        String[] str = getResources().getStringArray(R.array.install_period_array);
        //2번에서 생성한 spinner_item.xml과 str을 인자로 어댑터 생성.
        final ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,R.layout.install_spinner_item,str);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_field.setAdapter(adapter);

        //spinner 이벤트 리스너
        spinner_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner_field.getSelectedItemPosition() > 0){
                    //선택된 항목
                    Log.v("알림",spinner_field.getSelectedItem().toString()+ "is selected");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
