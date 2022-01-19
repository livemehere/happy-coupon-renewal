package kr.co.pointmobile.msrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class MoneyActivity extends AppCompatActivity {
    private String serial;
    private String card_no;
    private String expire_date;
    private TextView totalTextView;
    private String totalStr;
    private int counter;
    private DecimalFormat myFormatter;
    private String moneyFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        myFormatter = new DecimalFormat("###,###");



        totalTextView = findViewById(R.id.total);
        counter=0;
        totalStr="";
        // 시리얼,카드번호,유효기간 받아오기
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serial = extras.getString("serial");
            card_no = extras.getString("card_no");
            expire_date = extras.getString("expire_date");
        }



    }

    //키패드를 누른경우
    public void clickPad(View v){
        TextView target = (TextView) v;
        String targetText = target.getText().toString(); // 키패드의 텍스트값

        // 숫자를 누를겨우
        if(!targetText.equals("확인") && !targetText.equals("X")){
            totalStr +=targetText;
            counter+=1;
            moneyFormat = myFormatter.format(Long.parseLong(totalStr));
            totalTextView.setText(moneyFormat);

        }else if(targetText.equals("X")){


            if(totalStr.length() >0){
                StringBuffer sb= new StringBuffer(totalStr);
                sb.deleteCharAt(totalStr.length()-1);
                totalStr = new String(sb);
                counter-=1;

                if(!totalStr.equals("")){
                    moneyFormat = myFormatter.format(Long.parseLong(totalStr));
                    totalTextView.setText(moneyFormat);
                }else{
                    totalTextView.setText("");
                }
            }



        }else if(targetText.equals("확인")){
            //확인을 누를 경우

            if(totalStr.length()>0){
                Intent intent = new Intent(v.getContext(), HalbuActivity.class);
                intent.putExtra("serial",serial);
                intent.putExtra("card_no",card_no);
                intent.putExtra("expire_date",expire_date);
                intent.putExtra("tot_amt",totalStr);
                startActivity(intent);
            }else{
//                Toast.makeText(getApplicationContext(), "가격을 적어주세요", Toast.LENGTH_SHORT).show();
            }
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