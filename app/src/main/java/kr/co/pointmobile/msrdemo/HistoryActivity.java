package kr.co.pointmobile.msrdemo;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        WebView WebView_history = (WebView) findViewById(R.id.WebView_history);
        WebView_history.setWebViewClient(new WebViewClient());
        WebView_history.getSettings().setJavaScriptEnabled(true);
        WebView_history.loadUrl("https://www.naver.com/");
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