package kr.co.pointmobile.msrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class passwordActivity extends AppCompatActivity {
    private int counter;
    private String inputPassword;
    private ImageView aster1;
    private ImageView aster2;
    private ImageView aster3;
    private ImageView aster4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

         aster1 = (ImageView)findViewById(R.id.aster1);
         aster2 = (ImageView)findViewById(R.id.aster2);
         aster3 = (ImageView)findViewById(R.id.aster3);
         aster4 = (ImageView)findViewById(R.id.aster4);
        counter = 0;
        inputPassword="";
    }



    //키패드를 누른경우
    public void clickPad(View v){
        TextView target = (TextView) v;
        String targetText = target.getText().toString(); // 키패드의 텍스트값

        // 숫자를 누를겨우
        if(!targetText.equals("확인") && !targetText.equals("X")){
            inputPassword +=targetText;
            if(counter >= 4){
                counter =4;
            }else if(counter < 0){
                counter = 0;
            }else{
                counter +=1;
            }

            if(counter == 1){
                aster1.setVisibility(View.VISIBLE);
            }
            if(counter == 2){
                aster2.setVisibility(View.VISIBLE);
            }
            if(counter == 3){
                aster3.setVisibility(View.VISIBLE);
            }
            if(counter == 4){
                aster4.setVisibility(View.VISIBLE);
            }

        }else if(targetText.equals("X")){
            if(inputPassword.length() >0){
                StringBuffer sb= new StringBuffer(inputPassword);
                sb.deleteCharAt(inputPassword.length()-1);
                inputPassword = new String(sb);
            }
            if(counter == 1){
                aster1.setVisibility(View.INVISIBLE);
            }
            if(counter == 2){
                aster2.setVisibility(View.INVISIBLE);
            }
            if(counter == 3){
                aster3.setVisibility(View.INVISIBLE);
            }
            if(counter == 4) {
                aster4.setVisibility(View.INVISIBLE);
            }

            if(counter > 4){
                counter =4;
            }else if(counter <= 0){
                counter = 0;
            }else{
                counter -=1;
            }
        }else if(targetText.equals("확인")){
            //확인을 누를 경우
            if(inputPassword.equals("8952")){
                Intent intent = new Intent(v.getContext(), AdminCancelActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            }
        }
        Log.d("inputPassword---",inputPassword);

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