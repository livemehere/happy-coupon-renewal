package kr.co.pointmobile.msrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import device.sdk.print.Printer;
import device.sdk.print.ReceiptPrint;
import device.sdk.print.utils.ExFormat;
import kr.co.pointmobile.msrdemo.models.CardAuthResult;
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

public class HalbuActivity extends AppCompatActivity {


    private String serial ;
    private String card_no ;
    private String expire_date ;
    private String tot_amt ;
    private String install_period;


//    print
    private String printBtn;
    private String grayPlusBtn;
    private String grayMinusBtn;
    private String grayValText ="3";
    private boolean checkBoxBoldStyle;

    private Button textMethodBtn;
    private Button presetBtn;
    private Button receiptBtn;
    private Button resetBtn;

    //    private ScrreceiptollView scrollView;
    private ImageView imageView;

    private ProgressDialog inprogressDialog;

    private ReceiptPrint receipt;
    private Printer printer;

    private final int SELECT_TEXT_METHOD = 0;
    private final int SELECT_PRESET = 1;
    private final int SELECT_RECEIPT = 2;
    private final int SELECT_RESET = 3;


    public String res_serial = "";
    public String res_card_no = "";
    public String res_expire_date = "";
    public String res_install_period = "";
    public String res_tot_amt = "";
    public String res_agentid = "";
    public String res_onfftid = "";
    public String res_cert_type = "";
    public String res_card_user_type = "";
    public String res_user_nm = "";
    public String res_user_phone2 = "";
    public String res_order_no = "";
    public String res_transeq = "";
    public String res_result_cd = "";
    public String res_result_msg = "";
    public String res_app_date = "";
    public String res_app_dt = "";
    public String res_app_tm = "";
    public String res_auth_no = "";
    public String res_issuer_nm = "";
    public String res_payment = "";
    public String res_regdate = "";
    public String res_regtime = "";
    public String res_fee = "";
    public String res_coupon_no = "";

    private String name;
    private String companyid;
    private String ceoname;
    private String phone;
    private String address;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halbu);


        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        // 상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // 시리얼,카드번호,유효기간 받아오기
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serial = extras.getString("serial");
            card_no = extras.getString("card_no");
            expire_date = extras.getString("expire_date");
            tot_amt = extras.getString("tot_amt");

        }

        SharedPreferences sharedPreferences = getSharedPreferences("marget", Context.MODE_PRIVATE);

        name = sharedPreferences.getString("name","");
        companyid = sharedPreferences.getString("companyid","");
        ceoname = sharedPreferences.getString("ceoname","");
        phone = sharedPreferences.getString("phone","");
        address = sharedPreferences.getString("address","");


        receipt = ((BaseApplication)getApplication()).getReceiptPrint();
        printer = ((BaseApplication)getApplication()).getPrinter();
    }

    //키패드를 누른경우
    public void clickPad(View v){
        TextView target = (TextView) v;
        String targetText = target.getText().toString(); // 키패드의 텍스트값
        boolean isDirect = false;

        if(targetText.equals("일시불")){
            install_period = "00";
        }else if(targetText.equals("2개월")){
            install_period = "02";
        }else if(targetText.equals("3개월")){
            install_period = "03";
        }else if(targetText.equals("4개월")){
            install_period = "04";
        }else if(targetText.equals("5개월")){
            install_period = "05";
        }else if(targetText.equals("6개월")){
            install_period = "06";
        }else if(targetText.equals("직접입력")){
            isDirect=true;
            Intent intent = new Intent(v.getContext(), DynamicHalbuActivity.class);
            intent.putExtra("serial",serial);
            intent.putExtra("card_no",card_no);
            intent.putExtra("expire_date",expire_date);
            intent.putExtra("tot_amt",tot_amt);
            startActivity(intent);
        }
        button1.setEnabled(false);
        button2.setEnabled(false);
        button3.setEnabled(false);
        button4.setEnabled(false);
        button5.setEnabled(false);
        button6.setEnabled(false);
        button7.setEnabled(false);

        if(!isDirect){
            RetrofitService networkService = RetrofitFactory.create();
            networkService.authCard(serial,card_no,expire_date,install_period,tot_amt).enqueue(new Callback<CardAuthResult>() {
                @Override
                public void onResponse(Call<CardAuthResult> call, Response<CardAuthResult> response) {
                    //  정상 통신 된 경우
                    if(response.isSuccessful()){
                        Log.d("카드승인 결과",response.body().result_cd);
                        if(response.body().result_cd .equals("0000")){
                            // 카드 승인이 정상적으로 이루어진 경우
                            // TODO: 여기서 영수증 출력하면 됨 (res_ 는 리턴값들)
                            res_serial = response.body().serial;
                            res_card_no = response.body().card_no;
                            res_expire_date = response.body().expire_date;
                            res_install_period = response.body().install_period;
                            res_tot_amt = response.body().tot_amt;
                            res_agentid = response.body().agentid;
                            res_onfftid = response.body().onfftid;
                            res_cert_type = response.body().cert_type;
                            res_card_user_type = response.body().card_user_type;
                            res_user_nm = response.body().user_nm;
                            res_user_phone2 = response.body().user_phone2;
                            res_order_no = response.body().order_no;
                            res_transeq = response.body().transeq;
                            res_result_cd = response.body().result_cd;
                            res_result_msg = response.body().result_msg;
                            res_app_date = response.body().app_date;
                            res_app_dt = response.body().app_dt;
                            res_app_tm = response.body().app_tm;
                            res_auth_no = response.body().auth_no;
                            res_issuer_nm = response.body().issuer_nm;
                            res_payment = response.body().payment;
                            res_regdate = response.body().regdate;
                            res_regtime = response.body().regtime;
                            res_fee = response.body().fee;
                            res_coupon_no = response.body().coupon_no;

                            if(res_install_period.equals("00")){
                                res_install_period = "일시불";
                            }

                            Log.d("결제완료된 쿠폰번호",res_coupon_no);
                            makeReceipt();
                            if (receipt != null) {
                                int ret = printer.setGrayValue(3);
                                if (ret == BMP_PRINT_COMMON_SUCCESS || ret == ERROR_NO_FONT_LIBRARY) {
                                    ret = printer.print(receipt);
                                }

                            }
                            Intent intent = new Intent(v.getContext(),MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);


                        }else{
                            // TODO: 카드 승인이 실패한 경우
                            Toast.makeText(getApplicationContext(), response.body().result_msg, Toast.LENGTH_SHORT).show();
                        }

                    }
                    //TODO: 홈으로 보내기
//                    Intent intent = new Intent(v.getContext(),MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<CardAuthResult> call, Throwable t) {
//                                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_SHORT).show();

                    Toast.makeText(getApplicationContext(), call.request().toString(), Toast.LENGTH_SHORT).show();

                }
            });

        }


//        Toast.makeText(getApplicationContext(), "결제완료+영수증출력", Toast.LENGTH_SHORT).show();

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

    private void makeReceipt() {
        try {
            receipt.setPreset(PRESET_W20H100);
            receipt.addTextAlign("신용승인\n", ALIGN_CENTER);
            receipt.setPreset(PRESET_W30H100);
            receipt.addText(ExFormat.format("%-15s%15s\n", "거 래 일 시 :", res_app_date));
            receipt.addText(ExFormat.format("%-13s%17s\n", "카 드 번 호 :", res_card_no));
            receipt.addText(ExFormat.format("%-15s%15s\n", "카 드 종 류 :", res_issuer_nm));
            receipt.addText(ExFormat.format("%-15s%15s\n", "유 효 기 간 :", "**/**"));
            receipt.addText(ExFormat.format("%-15s%15s\n", "거 래 유 형 :", "신용승인")); //FIXME
            receipt.addText(ExFormat.format("%-14s%16s\n", "할 부 개 월 :", res_install_period));
            receipt.addTextLine("------------------------------");
            receipt.addText(ExFormat.format("%-15s%13s원\n", "공 급 가 액 :", res_tot_amt)); //FIXME
            receipt.addText(ExFormat.format("%-15s%15s\n", "부  가  세  :", "0원")); //FIXME
            receipt.addText(ExFormat.format("%-15s%13s원\n", "합       계 :", res_tot_amt));
            receipt.addTextLine("------------------------------");
            receipt.addText(ExFormat.format("%-15s%15s\n", "승 인 번 호 :", res_auth_no));
            receipt.addTextLine("------------------------------");
            receipt.addText(ExFormat.format("%-15s%15s\n", "가 맹 점 명 :", name)); //FIXME
            receipt.addText(ExFormat.format("%-15s%15s\n", "대 표 자 명 :", ceoname)); //FIXME
            receipt.addText(ExFormat.format("%-15s%15s\n", "사 업 자 NO :", companyid)); //FIXME
            receipt.addText(ExFormat.format("%-15s%15s\n", "문 의 전 화 :", phone)); //FIXME
            receipt.addText(ExFormat.format("%s\n\n", address)); //FIXME
//            receipt.addText("경기 머머시 머머구 머머로00번 길 00(머머동) 0층\n",); //FIXME
            receipt.addText("[결제대행사]\n");
            receipt.addText("(주)온오프코리아\n");
            receipt.addText(ExFormat.format("%s %s\n\n", "사업자번호  :", "636-88-00753"));
            receipt.addText("[쿠폰발행사]\n");
            receipt.addText("(주)해피페이\n");
            receipt.addText(ExFormat.format("%s %s\n", "사업자번호  :", "140-81-49182"));
            receipt.addText(ExFormat.format("%s %s\n\n\n", "문 의 전 화 :", "1600-8952"));
            receipt.addText(ExFormat.format("%-15s%15s\n", "", "구매상품내역"));
            receipt.addTextLine("------------------------------");
            receipt.addTextAlign("HAPPY COUPON\n", ALIGN_CENTER);
            receipt.addTextLine("------------------------------");
            receipt.addText(ExFormat.format("%-15s%13s원\n", "쿠 폰 금 액 :", res_tot_amt));
            receipt.addText(ExFormat.format("%-15s%15s\n", "쿠 폰 번 호  :", res_coupon_no));
            receipt.addTextLine("------------------------------");
//            receipt.addText("http://m.happycoupon.co.kr\n");
            receipt.addText("사용하신 쿠폰번호로 \n");
//            receipt.addText("해피캐시를 적립하신 후\n");
            receipt.addText("리커버샵(www.recovershop.co.kr\n");
            receipt.addText("에서 포인트 등록 후 편리하게 사용하세요\n");
            receipt.addText("(서명/SIGNATURE)\n");

        } catch (ReceiptPrint.BitmapOutOfRangeException e) {
            e.printStackTrace();
        }
    }


}