package kr.co.pointmobile.msrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import device.sdk.print.Printer;
import device.sdk.print.ReceiptPrint;
import device.sdk.print.utils.ExFormat;
import kr.co.pointmobile.msrdemo.models.CardAuthResult;
import kr.co.pointmobile.msrdemo.models.CardCancelResult;
import kr.co.pointmobile.msrdemo.models.Coupon;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitFactory;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static device.sdk.print.Printer.BMP_PRINT_COMMON_SUCCESS;
import static device.sdk.print.Printer.ERROR_NO_FONT_LIBRARY;
import static device.sdk.print.ReceiptPrint.ALIGN_CENTER;
import static device.sdk.print.ReceiptPrint.PRESET_W30H100;
import static device.sdk.print.ReceiptPrint.PRESET_W40H100;

public class CancelCardActivity extends AppCompatActivity {
    public static String serial = android.os.Build.SERIAL;

    //TODO: print
    private ReceiptPrint receipt2;
    private Printer printer;

    public String coupon_no;
//    public String res_result_cd;
//    public String res_result_msg;
//    public String res_tot_amt;
//    public String res_transeq;
//    public String res_app_date;
//    public String res_card_no;
//    public String res_expire_date;
//    public String res_install_period;
//    public String res_auth_no;
//    public String res_coupon_no;
//    public String res_iss_cd;
//    public String res_iss_nm;
    public String res_transeq;
    public String res_result_cd;
    public String res_result_msg;
    public String res_app_date;
    public String res_app_dt;
    public String res_app_tm;
    public String res_tot_amt;
    public String res_order_no;
    public String res_card_no;
    public String res_isuse;
    public String res_status;
    public String res_payment;
    public String res_regdate;
    public String res_regtime;
    public String res_agentid;
    public String res_memberid;
    public String res_parentid;
    public String res_origindate;
    public String res_origintime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_card);

        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        receipt2 = ((BaseApplication)getApplication()).getReceiptPrint();
        printer = ((BaseApplication)getApplication()).getPrinter();

        Button Button_use = findViewById(R.id.Button_use);
        Button_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText get_card_no = (EditText)findViewById(R.id.coupon_no);
                coupon_no = get_card_no.getText().toString();

                // TODO: 2. http 요청하기 (쿠폰취소)
                // FIXME: 여기다가 serial 넣고, 쿠폰번호가져와서 넣으세요
                RetrofitService networkService = RetrofitFactory.create();


                networkService.cancelCard(serial,coupon_no).enqueue(new Callback<CardCancelResult>() {
                    @Override
                    public void onResponse(Call<CardCancelResult> call, Response<CardCancelResult> response) {
                        if(response.isSuccessful()){
                            if(response.body().result_cd .equals("0000")){
                                // 카드 승인이 정상적으로 이루어진 경우
                                // TODO: 여기서 영수증 출력하면 됨 (res_ 는 리턴값들)
//                                res_result_cd = response.body().result_cd;
//                                res_result_msg = response.body().result_msg;
//                                res_tot_amt = response.body().tot_amt;
//                                res_card_no = response.body().card_no;
//                                res_expire_date = response.body().expire_date;
//                                res_install_period = response.body().install_period;
//                                res_transeq = response.body().transeq;
//                                res_auth_no = response.body().auth_no;
//                                res_coupon_no = response.body().coupon_no;
//                                res_app_date = response.body().app_date;
//                                res_iss_cd = response.body().iss_cd;
//                                res_iss_nm = response.body().iss_nm;

                                res_transeq = response.body().transeq;
                                res_result_cd = response.body().result_cd;
                                res_result_msg = response.body().result_msg;
                                res_app_date = response.body().app_date;
                                res_app_dt = response.body().app_dt;
                                res_app_tm = response.body().app_tm;
                                res_tot_amt = response.body().tot_amt;
                                res_order_no = response.body().order_no;
                                res_card_no = response.body().card_no;
                                res_isuse = response.body().isuse;
                                res_status = response.body().status;
                                res_payment = response.body().payment;
                                res_regdate = response.body().regdate;
                                res_regtime = response.body().regtime;
                                res_agentid = response.body().agentid;
                                res_memberid = response.body().memberid;
                                res_parentid = response.body().parentid;
                                res_origindate = response.body().origindate;
                                res_origintime = response.body().origintime;

                                makeReceiptCancel(); //TODO: 영수증 틀 만들기


                                Toast.makeText(getApplicationContext(), response.body().result_msg, Toast.LENGTH_SHORT).show();
                                //----- 결과값 출력 팝
                                String valuesMessage = String.format("결과코드: %s\n결과메세지: %s\n결제일련번호: %s\n취소일자: %s\n취소금액: %s",
                                        res_result_cd,res_result_msg,res_transeq,res_app_date,res_tot_amt);
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CancelCardActivity.this);
                                builder.setMessage(valuesMessage).setTitle("영수증을 출력하시겠습니까?");
                                builder.setPositiveButton("출력", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        //TODO: 영수증출력하기
                                        if (receipt2 != null) {
                                            int ret = printer.setGrayValue(3);
                                            if (ret == BMP_PRINT_COMMON_SUCCESS || ret == ERROR_NO_FONT_LIBRARY) {
                                                ret = printer.print(receipt2);
                                            }
                                        }
                                        Toast.makeText(getApplicationContext(), "영수증을 출력합니다", Toast.LENGTH_SHORT).show();

                                    }
                                });

                                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        Toast.makeText(getApplicationContext(), "영수증을 출력하지 않습니다", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                                android.app.AlertDialog dialog = builder.create();
                                dialog.show();
                                // -----
                            }else{
                                // TODO: 카드 승인이 실패한 경우
                                Toast.makeText(getApplicationContext(), response.body().result_msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CardCancelResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("http실패",t.toString());
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

    private void makeReceiptCancel() {
        try {

            receipt2.setPreset(PRESET_W30H100);
            receipt2.addTextAlign("신용취소\n", ALIGN_CENTER);
            receipt2.setPreset(PRESET_W40H100);
            receipt2.addText(ExFormat.format("%-20s%20s\n", "거 래 일 시 :", res_app_date));
            receipt2.addText(ExFormat.format("%-20s%20s\n", "카 드 번 호 :", res_card_no));
//            receipt2.addText(ExFormat.format("%-20s%20s\n", "카 드 종 류 :", res_issuer_nm));
            receipt2.addText(ExFormat.format("%-20s%20s\n", "유 효 기 간 :", "**/**"));
            receipt2.addText(ExFormat.format("%-20s%20s\n", "거 래 유 형 :", "신용취소"));
//            receipt2.addText(ExFormat.format("%-20s%20s\n", "할 부 개 월 :", res_install_period));
            receipt2.addTextLine("----------------------------------------");
            receipt2.addText(ExFormat.format("%-20s%18s원\n", "공 급 가 액 :", res_tot_amt)); //FIXME
            receipt2.addText(ExFormat.format("%-20s%20s\n", "부  가  세  :", "0원")); //FIXME
            receipt2.addText(ExFormat.format("%-20s%18s원\n", "합       계 :", res_tot_amt));
            receipt2.addTextLine("----------------------------------------");
            receipt2.addText(ExFormat.format("%-20s%20s\n", "승 인 번 호 :", res_card_no));
            receipt2.addTextLine("----------------------------------------");
            receipt2.addText(ExFormat.format("%-20s%20s\n", "가 맹 점 명 :", "해피쿠폰점")); //FIXME
            receipt2.addText(ExFormat.format("%-20s%20s\n", "대 표 자 명 :", "홍길동")); //FIXME
            receipt2.addText(ExFormat.format("%-20s%20s\n", "사 업 자 NO :", "000-00-0000")); //FIXME
            receipt2.addText(ExFormat.format("%-20s%20s\n", "문 의 전 화 :", "010-0000-0000")); //FIXME
            receipt2.addText("경기 머머시 머머구 머머로00번 길 00(머머동) 0층\n\n"); //FIXME
            receipt2.addText("[결제대행사]\n");
            receipt2.addText("(주)온오프코리아\n");
            receipt2.addText(ExFormat.format("%s %s\n\n", "사업자번호  :", "636-88-00753"));
            receipt2.addText("[쿠폰발행사]\n");
            receipt2.addText("(주)해피페이\n");
            receipt2.addText(ExFormat.format("%s %s\n", "사업자번호  :", "140-81-49182"));
            receipt2.addText(ExFormat.format("%s %s\n\n\n", "문 의 전 화 :", "1600-8952"));
            receipt2.addText(ExFormat.format("%-20s%20s\n", "", "취소상품내역"));
            receipt2.addTextLine("----------------------------------------");
            receipt2.addTextAlign("HAPPY COUPON\n", ALIGN_CENTER);
            receipt2.addTextLine("----------------------------------------");
            receipt2.addText(ExFormat.format("%-20s%18s원\n", "쿠 폰 금 액 :", res_tot_amt));
            receipt2.addText(ExFormat.format("%-20s%20s\n", "쿠 폰 번 호  :", coupon_no));
            receipt2.addTextLine("----------------------------------------");
            receipt2.addText("http://m.happycoupon.co.kr\n");
            receipt2.addText("에서 사용하신 쿠폰번호로 \n");
            receipt2.addText("해피캐시를 적립하신 후\n");
            receipt2.addText("리커버샵(www.recovershop.co.kr\n");
            receipt2.addText("에서 편리하게 사용하세요\n");
            receipt2.addText("(서명/SIGNATURE)\n");

        } catch (ReceiptPrint.BitmapOutOfRangeException e) {
            e.printStackTrace();
        }
    }
}