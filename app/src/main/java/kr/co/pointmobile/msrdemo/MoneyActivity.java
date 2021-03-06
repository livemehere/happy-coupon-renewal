package kr.co.pointmobile.msrdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
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


public class MoneyActivity extends AppCompatActivity {
    private String serial;
    private String card_no;
    private String expire_date;
    private TextView totalTextView;
    private String totalStr;
    private int counter;
    private DecimalFormat myFormatter;
    private String moneyFormat;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);


        button1 = (Button)findViewById(R.id.button1);

        // ?????? ?????? ?????????
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        myFormatter = new DecimalFormat("###,###");



        totalTextView = findViewById(R.id.total);
        counter=0;
        totalStr="";
        // ?????????,????????????,???????????? ????????????
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            serial = extras.getString("serial");
            card_no = extras.getString("card_no");
            expire_date = extras.getString("expire_date");
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

    //???????????? ????????????
    public void clickPad(View v){
        TextView target = (TextView) v;
        String targetText = target.getText().toString(); // ???????????? ????????????

        // ????????? ????????????
        if(!targetText.equals("??????") && !targetText.equals("X")){
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



        }else if(targetText.equals("??????")){

            button1.setEnabled(false);
            //????????? ?????? ??????

            if(totalStr.length()>0){

                if(Integer.parseInt(totalStr) < 50000){
                    RetrofitService networkService = RetrofitFactory.create();
                    networkService.authCard(serial,card_no,expire_date,"00",totalStr).enqueue(new Callback<CardAuthResult>() {
                        @Override
                        public void onResponse(Call<CardAuthResult> call, Response<CardAuthResult> response) {
                            //  ?????? ?????? ??? ??????
                            if(response.isSuccessful()){
                                Log.d("???????????? ??????",response.body().result_cd);
                                if(response.body().result_cd .equals("0000")){
                                    // ?????? ????????? ??????????????? ???????????? ??????
                                    // TODO: ????????? ????????? ???????????? ??? (res_ ??? ????????????)
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
                                        res_install_period = "?????????";
                                    }

                                    Log.d("??????????????? ????????????",res_coupon_no);
                                    //FIXME: ????????? ????????????
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
                                    // TODO: ?????? ????????? ????????? ??????
                                    Toast.makeText(getApplicationContext(), response.body().result_msg, Toast.LENGTH_SHORT).show();
                                }

                            }}

                        @Override
                        public void onFailure(Call<CardAuthResult> call, Throwable t) {
//                                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_SHORT).show();

                            Toast.makeText(getApplicationContext(), call.request().toString(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }else{
                    Intent intent = new Intent(v.getContext(), HalbuActivity.class);
                    intent.putExtra("serial",serial);
                    intent.putExtra("card_no",card_no);
                    intent.putExtra("expire_date",expire_date);
                    intent.putExtra("tot_amt",totalStr);
                    startActivity(intent);
                }

                //?????? end


            }else{
//                Toast.makeText(getApplicationContext(), "????????? ???????????????", Toast.LENGTH_SHORT).show();
            }
        }






    }

    // ??? ?????? ????????? ??????????????? ??????
    public void navToHome(View v){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    // ?????? ?????? ??? ?????? Activity ??????
    public void finishApp(View v){
        finishAffinity();
    }


    private void makeReceipt() {
        try {
            receipt.setPreset(PRESET_W20H100);
            receipt.addTextAlign("????????????\n", ALIGN_CENTER);
            receipt.setPreset(PRESET_W30H100);
            receipt.addText(ExFormat.format("%-15s%15s\n", "??? ??? ??? ??? :", res_app_date));
            receipt.addText(ExFormat.format("%-13s%17s\n", "??? ??? ??? ??? :", res_card_no));
            receipt.addText(ExFormat.format("%-15s%15s\n", "??? ??? ??? ??? :", res_issuer_nm));
            receipt.addText(ExFormat.format("%-15s%15s\n", "??? ??? ??? ??? :", "**/**"));
            receipt.addText(ExFormat.format("%-15s%15s\n", "??? ??? ??? ??? :", "????????????")); //FIXME
            receipt.addText(ExFormat.format("%-14s%16s\n", "??? ??? ??? ??? :", res_install_period));
            receipt.addTextLine("------------------------------");
            receipt.addText(ExFormat.format("%-15s%13s???\n", "??? ??? ??? ??? :", res_tot_amt)); //FIXME
            receipt.addText(ExFormat.format("%-15s%15s\n", "???  ???  ???  :", "0???")); //FIXME
            receipt.addText(ExFormat.format("%-15s%13s???\n", "???       ??? :", res_tot_amt));
            receipt.addTextLine("------------------------------");
            receipt.addText(ExFormat.format("%-15s%15s\n", "??? ??? ??? ??? :", res_auth_no));
            receipt.addTextLine("------------------------------");
            receipt.addText(ExFormat.format("%-15s%15s\n", "??? ??? ??? ??? :", name)); //FIXME
            receipt.addText(ExFormat.format("%-15s%15s\n", "??? ??? ??? ??? :", ceoname)); //FIXME
            receipt.addText(ExFormat.format("%-15s%15s\n", "??? ??? ??? NO :", companyid)); //FIXME
            receipt.addText(ExFormat.format("%-15s%15s\n", "??? ??? ??? ??? :", phone)); //FIXME
            receipt.addText(ExFormat.format("%s\n\n", address)); //FIXME
//            receipt.addText("?????? ????????? ????????? ?????????00??? ??? 00(?????????) 0???\n",); //FIXME
            receipt.addText("[???????????????]\n");
            receipt.addText("(???)??????????????????\n");
            receipt.addText(ExFormat.format("%s %s\n\n", "???????????????  :", "636-88-00753"));
            receipt.addText("[???????????????]\n");
            receipt.addText("(???)????????????\n");
            receipt.addText(ExFormat.format("%s %s\n", "???????????????  :", "140-81-49182"));
            receipt.addText(ExFormat.format("%s %s\n\n\n", "??? ??? ??? ??? :", "1600-8952"));
            receipt.addText(ExFormat.format("%-15s%15s\n", "", "??????????????????"));
            receipt.addTextLine("------------------------------");
            receipt.addTextAlign("HAPPY COUPON\n", ALIGN_CENTER);
            receipt.addTextLine("------------------------------");
            receipt.addText(ExFormat.format("%-15s%13s???\n", "??? ??? ??? ??? :", res_tot_amt));
            receipt.addText(ExFormat.format("%-15s%15s\n", "??? ??? ??? ???  :", res_coupon_no));
            receipt.addTextLine("------------------------------");
//            receipt.addText("http://m.happycoupon.co.kr\n");
            receipt.addText("???????????? ??????????????? \n");
//            receipt.addText("??????????????? ???????????? ???\n");
            receipt.addText("????????????(www.recovershop.co.kr\n");
            receipt.addText("?????? ????????? ?????? ??? ???????????? ???????????????\n");
            receipt.addText("(??????/SIGNATURE)\n");

        } catch (ReceiptPrint.BitmapOutOfRangeException e) {
            e.printStackTrace();
        }
    }


}