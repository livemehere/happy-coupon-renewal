package kr.co.pointmobile.msrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import static device.sdk.print.ReceiptPrint.PRESET_W20H100;
import static device.sdk.print.ReceiptPrint.PRESET_W30H100;
import static device.sdk.print.ReceiptPrint.PRESET_W40H100;

public class CancelCardActivity extends AppCompatActivity {
    public static String serial = android.os.Build.SERIAL;

    //TODO: print
    private ReceiptPrint receipt2;
    private Printer printer;

    public String coupon_no;
    private TextView get_coupon_no;
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

    private String name;
    private String companyid;
    private String ceoname;
    private String phone;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_card);

        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        receipt2 = ((BaseApplication) getApplication()).getReceiptPrint();
        printer = ((BaseApplication) getApplication()).getPrinter();

        SharedPreferences sharedPreferences = getSharedPreferences("marget", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("name", "");
        companyid = sharedPreferences.getString("companyid", "");
        ceoname = sharedPreferences.getString("ceoname", "");
        phone = sharedPreferences.getString("phone", "");
        address = sharedPreferences.getString("address", "");

        get_coupon_no = (TextView) findViewById(R.id.coupon_no);
        coupon_no="";
    }

    // 홈 버튼 클릭시 홈화면으로 이동
    public void navToHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // 종료 버튼 시 모든 Activity 종료
    public void finishApp(View v) {
        finishAffinity();
    }


    private void makeReceiptCancel() {
        try {

            receipt2.setPreset(PRESET_W20H100);
            receipt2.addTextAlign("신용취소\n", ALIGN_CENTER);
            receipt2.setPreset(PRESET_W30H100);
            receipt2.addText(ExFormat.format("%-15s%15s\n", "거 래 일 시 :", res_app_date));
            receipt2.addText(ExFormat.format("%-13s%17s\n", "카 드 번 호 :", res_card_no));
            receipt2.addText(ExFormat.format("%-15s%15s\n", "유 효 기 간 :", "**/**"));
            receipt2.addText(ExFormat.format("%-15s%15s\n", "거 래 유 형 :", "신용취소"));
            receipt2.addTextLine("------------------------------");
            receipt2.addText(ExFormat.format("%-15s%13s원\n", "공 급 가 액 :", res_tot_amt)); //FIXME
            receipt2.addText(ExFormat.format("%-15s%15s\n", "부  가  세  :", "0원")); //FIXME
            receipt2.addText(ExFormat.format("%-15s%13s원\n", "합       계 :", res_tot_amt));
            receipt2.addTextLine("------------------------------");
            receipt2.addText(ExFormat.format("%-15s%15s\n", "승 인 번 호 :", res_card_no));
            receipt2.addTextLine("------------------------------");
            receipt2.addText(ExFormat.format("%-15s%15s\n", "가 맹 점 명 :", name)); //FIXME
            receipt2.addText(ExFormat.format("%-15s%15s\n", "대 표 자 명 :", ceoname)); //FIXME
            receipt2.addText(ExFormat.format("%-15s%15s\n", "사 업 자 NO :", companyid)); //FIXME
            receipt2.addText(ExFormat.format("%-15s%15s\n", "문 의 전 화 :", phone)); //FIXME
            receipt2.addText(ExFormat.format("%s\n\n", address)); //FIXME
            receipt2.addText("[결제대행사]\n");
            receipt2.addText("(주)온오프코리아\n");
            receipt2.addText(ExFormat.format("%s %s\n\n", "사업자번호  :", "636-88-00753"));
            receipt2.addText("[쿠폰발행사]\n");
            receipt2.addText("(주)해피페이\n");
            receipt2.addText(ExFormat.format("%s %s\n", "사업자번호  :", "140-81-49182"));
            receipt2.addText(ExFormat.format("%s %s\n\n\n", "문 의 전 화 :", "1600-8952"));
            receipt2.addText(ExFormat.format("%-15s%15s\n", "", "취소상품내역"));
            receipt2.addTextLine("------------------------------");
            receipt2.addTextAlign("HAPPY COUPON\n", ALIGN_CENTER);
            receipt2.addTextLine("------------------------------");
            receipt2.addText(ExFormat.format("%-15s%13s원\n", "쿠 폰 금 액 :", res_tot_amt));
            receipt2.addText(ExFormat.format("%-15s%15s\n", "쿠 폰 번 호  :", coupon_no));
            receipt2.addTextLine("------------------------------");
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

    //키패드를 누른경우
    public void clickPad(View v) {
        TextView target = (TextView) v;
        String targetText = target.getText().toString(); // 키패드의 텍스트값

        // 숫자를 누를겨우
        if (!targetText.equals("확인") && !targetText.equals("X")) {
            coupon_no += targetText;
            get_coupon_no.setText(coupon_no);

        } else if (targetText.equals("X")) {
            if (coupon_no.length() > 0) {
                StringBuffer sb = new StringBuffer(coupon_no);
                sb.deleteCharAt(coupon_no.length() - 1);
                coupon_no = new String(sb);
                get_coupon_no.setText(coupon_no);
            }


        } else if (targetText.equals("확인")) {
            //확인을 누를 경우
            if (coupon_no.length() > 0) {
                // 취소 요청 및 프린트
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
//

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

//                                Toast.makeText(getApplicationContext(), response.body().result_msg, Toast.LENGTH_SHORT).show();
                                //----- 결과값 출력 팝
                                //TODO: 영수증출력하기
                                if (receipt2 != null) {
                                    int ret = printer.setGrayValue(3);
                                    if (ret == BMP_PRINT_COMMON_SUCCESS || ret == ERROR_NO_FONT_LIBRARY) {
                                        ret = printer.print(receipt2);
                                    }
                                }
                                Intent intent = new Intent(v.getContext(),MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
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



        }

    }
}