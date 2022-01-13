package kr.co.pointmobile.msrdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import device.sdk.print.Printer;
import device.sdk.print.ReceiptPrint;
import device.sdk.print.utils.ExFormat;
import kr.co.pointmobile.msrdemo.models.CardAuthResult;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitFactory;
import kr.co.pointmobile.msrdemo.retrofit.RetrofitService;
import kr.co.pointmobile.msrdemo.utils.PmUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vpos.apipackage.Icc;
import vpos.apipackage.Mcr;
import vpos.messenger.MessengerClient;

import static vpos.apipackage.Icc.Lib_IccClose;
import static vpos.apipackage.Mcr.Lib_McrClose;
import static vpos.apipackage.Mcr.Lib_McrRead;
import static vpos.emvkernel.EmvKernel.EmvLib_GetTLV;

import static device.sdk.print.Printer.BMP_PRINT_COMMON_SUCCESS;
import static device.sdk.print.Printer.ERROR_NO_FONT_LIBRARY;
import static device.sdk.print.ReceiptPrint.ALIGN_CENTER;
import static device.sdk.print.ReceiptPrint.ALIGN_RIGHT;
import static device.sdk.print.ReceiptPrint.BITMAP_FIX_WIDTH;
import static device.sdk.print.ReceiptPrint.BITMAP_MAX_HEIGHT;
import static device.sdk.print.ReceiptPrint.PRESET_W20H100;
import static device.sdk.print.ReceiptPrint.PRESET_W20H150;
import static device.sdk.print.ReceiptPrint.PRESET_W20H200;
import static device.sdk.print.ReceiptPrint.PRESET_W30H100;
import static device.sdk.print.ReceiptPrint.PRESET_W30H150;
import static device.sdk.print.ReceiptPrint.PRESET_W30H200;
import static device.sdk.print.ReceiptPrint.PRESET_W40H100;
import static device.sdk.print.ReceiptPrint.PRESET_W40H150;
import static device.sdk.print.ReceiptPrint.PRESET_W40H200;
import static device.sdk.print.ReceiptPrint.PRESET_W45H100;
import static device.sdk.print.ReceiptPrint.PRESET_W45H150;
import static device.sdk.print.ReceiptPrint.PRESET_W45H200;
import static device.sdk.print.ReceiptPrint.PRESET_W60H100;
import static device.sdk.print.ReceiptPrint.PRESET_W60H150;
import static device.sdk.print.ReceiptPrint.PRESET_W60H200;

public class MsrDemoActivity extends AppCompatActivity
{

    private static final String TAG = MsrDemoActivity.class.getSimpleName();
    private static final int SUCCESS = 0;

    //TODO: HTTP 요청시 QUERY
    private static final String QUERY = "posts";

    //TODO: 결제시 http 전송할 데이터들
    public String mTrack2View; //채번된 카드번호
    public String card_no;
    public String expire_date; // 유효기간
    public static String serial = android.os.Build.SERIAL;
    public String install_period; //할부
    public String tot_amt; //금액

    private Button mStartReadButton ;
    private String mResultTextView ;
    private String mStatusTextView ;
    private String mTrack1View;
    private String mTrack3View;





    //    private String mTrack3View = null;
    private boolean mAutoScanModeCheck;
    private boolean mBeepSoundCheck;

    private int mSuccessCount = 0;
    private int mPartCount = 0;
    private int mFailCount = 0;
    private int mTrack1Count = 0;
    private int mTrack2Count = 0;
    private int mTrack3Count = 0;
    private int mTotalCount = 0;

    boolean isQuit = true;
    int ret = -1;
    int checkCount = 0;
    private boolean mIsTriggered = false;

    private MediaPlayer mSuccessBeep = null;
    private MediaPlayer mFailBeep = null;

    private MessengerClient mClient = null;

    private ProgressDialog mProgress;

    private Resources res;

    //TODO: print 파트

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

//    public String res_result_cd;
//    public String res_result_msg;
//    public String res_tot_amt;
//    public String res_card_no;
//    public String res_expire_date;
//    public String res_install_period;
//    public String res_transeq;
//    public String res_auth_no;
//    public String res_coupon_no;
//    public String res_app_date;
//    public String res_iss_cd;
//    public String res_iss_nm;

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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mClient = MessengerClient.getInstance(getApplicationContext());
        mClient.init();

        mProgress = new ProgressDialog(MsrDemoActivity.this);

        ActionBar aBar = getSupportActionBar();
        aBar.setIcon(R.drawable.ic_launcher);
        aBar.setDisplayUseLogoEnabled(true);
        aBar.setDisplayShowHomeEnabled(true);

        res = getResources();

        initActivity();
        clearResult();

        //TODO: print
        initUIComponent();

        receipt = ((BaseApplication)getApplication()).getReceiptPrint();
        printer = ((BaseApplication)getApplication()).getPrinter();

//         상단 앱바 가리기
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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

    @Override
    protected void onPause()
    {
        super.onPause();

        if(mIsTriggered)
        {
            onClickStartReading();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

    }
// FIXME: 이함수 키면 에러
//    @Override
//    protected void onDestroy()남
//    {
//        Lib_McrClose();
//        isQuit = true;
//        Log.d(TAG, "Lib_McrClosed");
//        super.onDestroy();
//    }

    @Override
    public void onBackPressed()
    {
        //Lib_McrClose();
        Log.d(TAG, "onBackPressed");
        super.onBackPressed();
    }

    private void initActivity()
    {
//        String mResultTextView;
//        String mStatusTextView;
//        String mTrack1View;
//         String mTrack2View;
//        String mTrack3View;

        mAutoScanModeCheck = false;
        mBeepSoundCheck = false;
        mBeepSoundCheck = true;

        mStartReadButton = findViewById(R.id.button_card_reader);
        mStartReadButton.setOnClickListener(mOnClickListener);
        findViewById(R.id.button_count_clear).setOnClickListener(mOnClickListener);

        clearTextView();

        showProgress(MsrDemoActivity.this, true);
        AsyncMasterConnect asyncMaster = new AsyncMasterConnect();
        asyncMaster.execute();

        showProgress(MsrDemoActivity.this, true);
        AsyncInit async = new AsyncInit();
        async.execute();

    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.button_card_reader)
            {
                onClickStartReading();
            }
            else if (v.getId() == R.id.button_count_clear)
            {
                onClickClearData();
            }
        }
    };

    private void initIccrDemo()
    {
        IccrDemo.initIccrDemo(getApplicationContext(), getFilesDir().getPath());
    }

    private void setBtnActive(final boolean isActive)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (mStartReadButton == null)
                {
                    return;
                }

                if (isActive)
                {
                    mStartReadButton.setEnabled(true);
                }
                else
                {
                    mStartReadButton.setEnabled(false);
                }
            }
        });
    }

    public void clearResult()
    {
        mSuccessCount = 0;
        mPartCount = 0;
        mFailCount = 0;
        mTrack1Count = 0;
        mTrack2Count = 0;
        mTrack3Count = 0;
        mTotalCount = 0;
    }

    public void onClickClearData()
    {
        showClearDataDialog();
    }

    public void onClickStartReading()
    {
        mIsTriggered = !mIsTriggered;

        if (mIsTriggered)
        {
            clearTextView();

            // change the button face...
            mStartReadButton.setText(getString(R.string.btn_stop_reading));
            mStatusTextView=getString(R.string.read_status);
            mTrack1View="";
            mTrack2View="";
            mTrack3View="";

            if (!isQuit)
            {
                return;
            }

            msrThread = new MSRThread();
            msrThread.start();
        }
        else
        {
            // change the button face...
            mStartReadButton.setText(getString(R.string.btn_start_reading));
            mStatusTextView="stop";
            isQuit = true;
        }
    }

    private void initMediaPlayer()
    {
        if (mSuccessBeep == null)
        {
            mSuccessBeep = MediaPlayer.create(this.getApplicationContext(), R.raw.success_beep);
        }

        if (mFailBeep == null)
        {
            mFailBeep = MediaPlayer.create(this.getApplicationContext(), R.raw.fail_beep);
        }
    }

    public void playBeep(boolean result)
    {
        initMediaPlayer();

        try
        {
            MediaPlayer player = result ? mSuccessBeep : mFailBeep;
            player.seekTo(0);
            player.start();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    MSRThread msrThread = null;

    public class MSRThread extends Thread
    {

        private byte[] track1 = new byte[250];
        private byte[] track2 = new byte[250];
        private byte[] track3 = new byte[250];

        public void run()
        {

            synchronized (this)
            {
                Message msg_h = new Message();
                Bundle b_h = new Bundle();
                String toastMsg = "";

                // always mode 1
                ret = Mcr.Lib_McrOpen(1);


                if (0 != ret)
                {
                    toastMsg = getString(R.string.msr_open_error);
                    isQuit = true;
                }
                else
                {
                    toastMsg = getString(R.string.swipe_ms_card);
                }

                showToast(toastMsg);

                isQuit = false;

                while (!isQuit)
                {

                    if (Mcr.Lib_McrCheck() == SUCCESS)
                    {
                        readMSC();
                        continue;
                    }

                    while (Mcr.Lib_McrCheck() != SUCCESS && !isQuit)
                    {
                        try
                        {
                            Thread.sleep(200);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        if (Icc.Lib_IccCheck((byte) 0) == SUCCESS)
                        {
                            setBtnActive(false);
                            readICC();

                            if (mAutoScanModeCheck)
                            {
                                while (Icc.Lib_IccCheck((byte) 0) == SUCCESS)
                                {
                                    try
                                    {
                                        Thread.sleep(200);
                                    } catch (InterruptedException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            setBtnActive(true);
                        }
                    }
                }
                isQuit = true;
            }
        }

        private void clearTrackBuffer()
        {
            Arrays.fill(track1, (byte) 0x00);
            Arrays.fill(track2, (byte) 0x00);
            Arrays.fill(track3, (byte) 0x00);
        }

        private void readMSC()
        {
            checkCount++;
            clearTrackBuffer();
            ret = Lib_McrRead(track1, track2, track3);
            resultDisplay(ret, track1, track2, track3);
            Lib_McrClose();
        }

        private void readICC()
        {
            checkCount++;
            clearTrackBuffer();
            byte[] val = new byte[256];
            int[] len = new int[1];
            int ret = 0;

            IccrDemo.startEMV();
            if ((EmvLib_GetTLV("57", track2, len)) == 0)
            {
                ret = 0x02;
                val = new byte[len[0]];
                System.arraycopy(track2, 0, val, 0, len[0]);

                track2 = PmUtils.hexToNibbleBytes(val);
            }
            else
            {
                ret = 0;
            }

            resultDisplay(ret, track1, track2, track3);
            Lib_IccClose((byte) 0);
        }
    }

    private void showToast(final String msg)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resultDisplay(final int result, final byte[] track1, final byte[] track2, final byte[] track3)
    {
        runOnUiThread(new Runnable()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run()
            {
                mTotalCount++;

                String str;

                if (result == SUCCESS)
                {
                    mFailCount++;
                    if (mBeepSoundCheck)
                    {
                        playBeep(false);
                    }
                    clearTextView();

                    str = getString(R.string.read_fail_status);
                    if (mAutoScanModeCheck)
                    {
                        str += getString(R.string.repeating_status);
                    }
                    mStatusTextView=str;
                }
                else
                {
                    if ((result & 0x01) == 0x01)
                    {
                        mTrack1Count++;
                    }
                    if ((result & 0x02) == 0x02)
                    {
                        mTrack2Count++;
                    }
                    if ((result & 0x04) == 0x04)
                    {
                        mTrack3Count++;
                    }

                    if ((result & 0x07) == 0x07)
                    {
                        mSuccessCount++;
                    }
                    else
                    {
                        mPartCount++;
                    }

                    str = String.format(res.getString(R.string.result_read), (errorMsg(result)))
                            + getString(R.string.result_track1) + (((result & 0x01) == 0) ? getString(R.string.result_fail) : getString(R.string.result_success))
                            + getString(R.string.result_track2) + (((result & 0x02) == 0) ? getString(R.string.result_fail) : getString(R.string.result_success))
                            + getString(R.string.result_track3) + (((result & 0x04) == 0) ? getString(R.string.result_fail) : getString(R.string.result_success));

                    if (mAutoScanModeCheck)
                    {
                        str += getString(R.string.repeating_status);
                    }

                    mStatusTextView=str;
                    mTrack1View=new String(track1).trim();
                    mTrack2View=new String(track2).trim();
                    mTrack3View=new String(track3).trim();



                    if (mBeepSoundCheck)
                    {
                        if (((result & 0x01) == 0x01) && ((result & 0x02) == 0x02) && ((result & 0x04) == 0x04))
                        {
                            playBeep(true);
                        }
                        else
                        {
                            playBeep(false);
                        }
                    }
                }

                if (!mAutoScanModeCheck)
                {
                    isQuit = true;
                    mStartReadButton.setEnabled(true);
                    mStartReadButton.setText(getString(R.string.btn_start_reading));
                    mIsTriggered = !mIsTriggered;
                }

                String result = String.format(res.getString(R.string.result_count),
                        mSuccessCount, mPartCount, mFailCount, mTotalCount, mTrack1Count, mTrack2Count, mTrack3Count);

                // TODO: mTrack2View = 카드채번한정보
                // TODO: myResult = 카드긁은 후 결과 값 (성공여부)
                mResultTextView=result;
//                card_no = (TextView) findViewById(R.id.textViewMsrTrack2);
//                card_no.setText(mTrack2View);

                // 채번한 카드번호에서 앞 16개는 카드번호,한칸무시하고,바로뒤 4개번호는 유효기간(YYMM)
                card_no="카드 인식 실패";
                expire_date="카드 인식 실패";
                if(mTrack2View.length() > 20){
                    card_no = mTrack2View.substring(0,16); // 카드번호
                    expire_date = mTrack2View.substring(17,21); // 유효기간
                }


                // 팝업
                // 1. 단말기번호, 카드번호, 유효기간, 할부, 금액 값 가져와서 세팅하기
                //serial = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

//                EditText get_expire_date = (EditText)findViewById(R.id.expire_date);
                Spinner get_install_period = (Spinner)findViewById(R.id.install_period_spinner);
                EditText get_tot_amt = (EditText)findViewById(R.id.tot_amt);

//                String expire_date = get_expire_date.getText().toString(); //유효기간
                install_period = get_install_period.getSelectedItem().toString(); //할부
                if(install_period.equals("일시불")){
                    install_period = "00";
                }
                tot_amt = get_tot_amt.getText().toString();//금액

                String valuesMessage = String.format("단말기번호: %s\n카드번호: %s\n유효기간: %s(YYMM)\n할부: %s (개월)\n금액: %s (원)",serial,card_no,expire_date,install_period,tot_amt);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MsrDemoActivity.this);
                builder.setMessage(valuesMessage).setTitle("아래의 정보로 결제합니다");
                builder.setPositiveButton("결제", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {

                        // 0. 카드 긁기 대기 창띄우기 + 카드 긁을때까지 대기(취소버튼있음)
                        // 0.5 . 카드를 긁으면! 아래 실행
                        // 1. 단말기번호, 카드번호, 유효기간, 할부, 금액 값 가져오기
                        // 2. http 요청하기
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
//                                        res_result_cd = response.body().result_cd;
//                                        res_result_msg = response.body().result_msg;
//                                        res_tot_amt = response.body().tot_amt;
//                                        res_card_no = response.body().card_no;
//                                        res_expire_date = response.body().expire_date;
//                                        res_install_period = response.body().install_period;
//                                        res_transeq = response.body().transeq;
//                                        res_auth_no = response.body().auth_no;
//                                        res_coupon_no = response.body().coupon_no;
//                                        res_app_date = response.body().app_date;
//                                        res_iss_cd = response.body().iss_cd;
//                                        res_iss_nm = response.body().iss_nm;

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

                                        Log.d("결제완료된 쿠폰번호",res_coupon_no);

//                                        ShowAsyncTask receiptTask = new ShowAsyncTask(SELECT_RECEIPT);
//                                        receiptTask.execute();

                                        makeReceipt();


                                        Toast.makeText(getApplicationContext(), response.body().result_msg, Toast.LENGTH_SHORT).show();
                                        //----- 결과값 출력 팝
                                        String valuesMessage = String.format("결과코드: %s\n결과메세지: %s\n승인금액: %s\n카드번호: %s\n유효기간: %s\n할부: %s\n결제일련번호: %s\n승인번호: %s\n쿠폰번호: %s\n결제일시: %s\n신용카드 코드: %s\n신용카드명: %s",
                                                res_result_cd,res_result_msg,res_tot_amt,res_card_no,res_expire_date,res_install_period,res_transeq,res_auth_no,res_coupon_no,res_app_date,res_card_no,res_issuer_nm);
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MsrDemoActivity.this);
                                        builder.setMessage(valuesMessage).setTitle("영수증을 출력하시겠습니까?");
                                        builder.setPositiveButton("출력", new DialogInterface.OnClickListener(){
                                            // 레시피 생성
//


                                            @Override
                                            public void onClick(DialogInterface dialog, int id)
                                            {

                                                //TODO: 영수증출력하기
//                                                byte grayVal = Byte.valueOf(grayValText);
                                                //프린트 시작
//                                                PrintAsyncTask printTask = new PrintAsyncTask(receipt, grayVal);
//                                                printTask.execute();

                                                if (receipt != null) {
                                                    int ret = printer.setGrayValue(3);
                                                    if (ret == BMP_PRINT_COMMON_SUCCESS || ret == ERROR_NO_FONT_LIBRARY) {
                                                        ret = printer.print(receipt);
                                                    }

                                                }



                                                Toast.makeText(getApplicationContext(), "영수증을 출력합니다", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });

                                        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                //레시피 초기화
//                                                ShowAsyncTask resetTask = new ShowAsyncTask(SELECT_RESET);
//                                                resetTask.execute();
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

                            }}

                            @Override
                            public void onFailure(Call<CardAuthResult> call, Throwable t) {
//                                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_SHORT).show();

                                Toast.makeText(getApplicationContext(), call.request().toString(), Toast.LENGTH_SHORT).show();

                            }
                        });


                        // 3. 결과값받아와서 성공이면 성공 tost 띄우고, 프린트하기
//                        Toast.makeText(getApplicationContext(), "결제되었습니다", Toast.LENGTH_SHORT).show();
                        // + 결제가 완료되면 홈으로
                        // 4. 실패하거나, 카드긁기 취소하면 실패 tost 띄우기
//                        finish();
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        card_no="";
                        expire_date="";
                        install_period="";
                        tot_amt="";

                        Toast.makeText(getApplicationContext(), "취소되었습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                android.app.AlertDialog dialog = builder.create();
                dialog.show();

//                TextView myResult = (TextView) findViewById(R.id.textViewMsrTrack1);
//                myResult.setText(mResultTextView);

            }
        });
    }

    private void clearTextView()
    {
        mStatusTextView="\n\n\n";
        mResultTextView="\n\n\n\n";
        mTrack1View="\n\n";
        mTrack2View="\n";
        mTrack3View="\n\n\n";
    }

    public String errorMsg(int status)
    {
        String msg = new String();

        status &= 0x07;

        if (status > 0x00)
        {
            msg = getString(R.string.track_success);

            if (status == 0x01)
            {
                msg += getString(R.string.track2_track3_fail);
            }
            else if (status == 0x02)
            {
                msg += getString(R.string.track1_track3_fail);
            }
            else if (status == 0x03)
            {
                msg += getString(R.string.track3_fail);
            }
            else if (status == 0x04)
            {
                msg += getString(R.string.track1_track2_fail);
            }
            else if (status == 0x05)
            {
                msg += getString(R.string.track1_track2_fail);
            }
            else if (status == 0x06)
            {
                msg += getString(R.string.track1_fail);
            }
            else if (status == 0x07)
            {
                msg += getString(R.string.all_track_success);
            }
        }
        else
        {
            msg = getString(R.string.track_read_fail);
        }

        return msg;
    }



//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case R.id.action_info:
//                openInfo();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

//    private void openInfo()
//    {
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int which)
//            {
//                dialog.dismiss();
//            }
//        });
//
//        String version = getString(R.string.msg_version_suffix);
//        try
//        {
//            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
//            if (pi != null)
//            {
//                version = pi.versionName;
//            }
//        } catch (PackageManager.NameNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//
//        alert.setMessage(getString(R.string.app_name) + " v" + version);
//        alert.show();
//    }

    public void showClearDataDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.clear_data_dialog_title));
        builder.setMessage(getString(R.string.clear_data_dialog_msg));

        builder.setPositiveButton(getString(R.string.clear_data_dialog_positive), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                clearResult();
                clearTextView();
                Toast.makeText(getApplicationContext(), getString(R.string.data_clear_success_msg), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(getString(R.string.clear_data_dialog_negative), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(getApplicationContext(), getString(R.string.data_clear_cancel_msg), Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }

    public void showProgress(final Activity act, final boolean bShow)
    {
        act.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgress.setMessage(getString(R.string.initializing));
                mProgress.setCancelable(false);

                try
                {
                    if (bShow)
                    {
                        mProgress.show();
                    }
                    else
                    {
                        mProgress.dismiss();
                    }
                } catch (Exception e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }

    class AsyncMasterConnect extends AsyncTask<Void, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... voids)
        {
            do
            {
                if (mClient.isConnect())
                    break;
                try
                {
                    Thread.sleep(1000);
                } catch (Exception e)
                {

                }
            }
            while (true);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            showProgress(MsrDemoActivity.this, false);
        }
    }

    class AsyncInit extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            initIccrDemo();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            showProgress(MsrDemoActivity.this, false);
            super.onPostExecute(aVoid);
        }
    }
    // 홈 버튼 클릭시 홈화면으로 이동
    public void navToHome(View v){
        finish();
    }
    // 종료 버튼 시 모든 Activity 종료
    public void finishApp(View v){
        finishAffinity();
    }

    //TODO: print
    // <2. Init>
    private void initUIComponent() {


//        grayValText = "";
//        printBtn = findViewById(R.id.)
//        printBtn.setOnClickListener(buttonOnClickListener);
//        checkBoxBoldStyle = false;

//        grayPlusBtn = findViewById(R.id.gray_val_plus);
//        grayPlusBtn.setOnClickListener(buttonOnClickListener);
//        grayMinusBtn = findViewById(R.id.gray_val_minus);
//        grayMinusBtn.setOnClickListener(buttonOnClickListener);

//        textMethodBtn = findViewById(R.id.textMethodBtn);
//        textMethodBtn.setOnClickListener(buttonOnClickListener);
//        presetBtn = findViewById(R.id.presetBtn);
//        presetBtn.setOnClickListener(buttonOnClickListener);
//        receiptBtn = findViewById(R.id.receiptBtn);
//        receiptBtn.setOnClickListener(buttonOnClickListener);
//        resetBtn = findViewById(R.id.resetBtn);
//        resetBtn.setOnClickListener(buttonOnClickListener);

//        imageView = findViewById(R.id.imageView);
//        scrollView = findViewById(R.id.scrollView);
//        scrollView.setHorizontalScrollBarEnabled(true);
//        scrollView.setVerticalScrollBarEnabled(true);

//        inprogressDialog = new ProgressDialog(this, R.style.ThemeCustomProgressDialog);
//        inprogressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        inprogressDialog.setCancelable(false);
//        inprogressDialog.setCanceledOnTouchOutside(false);
    }

    // </2. Init>

    // <3. Print>
    private class PrintAsyncTask extends AsyncTask<Void, Void, Integer> {
        public byte grayValue = 1;
        public ReceiptPrint receipt = null;

        public PrintAsyncTask(ReceiptPrint receipt, byte grayValue) {
            super();
            this.receipt = receipt;
            this.grayValue = grayValue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inprogressDialog.setMessage(getString(R.string.dialog_print_bmp));
            inprogressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (receipt == null) {
                return -1;
            }

            int ret = printer.setGrayValue(grayValue);
            if (ret == BMP_PRINT_COMMON_SUCCESS || ret == ERROR_NO_FONT_LIBRARY) {
                ret = printer.print(receipt);
            }
            return ret;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);
            inprogressDialog.dismiss();

            String result = printer.getReturnMessage(res);
            Toast.makeText(MsrDemoActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }
    // </3. Print>

    private class ShowAsyncTask extends AsyncTask<Void, Void, Void> {
        int select = 0;
        public ShowAsyncTask(int select){

            this.select = select;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inprogressDialog.setMessage(getString(R.string.dialog_make_bmp));
            inprogressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            receipt.init();
            if(checkBoxBoldStyle){
                receipt.setBoldTypeface();
            }

            if(select == SELECT_TEXT_METHOD){
                makeAddText();
            }else if(select == SELECT_PRESET){
                makePreset();
            }else if(select == SELECT_RECEIPT){
                makeReceipt();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            inprogressDialog.dismiss();

            final Bitmap temp = receipt.getBitmapDrawEndPoint();
            imageView.setImageBitmap(temp);
            imageView.setAdjustViewBounds(true);
        }
    }


    private void makePreset() {

    }

    private void makeReceipt() {
        try {
            receipt.setPreset(PRESET_W30H100);
            receipt.addTextAlign("신용승인\n", ALIGN_CENTER);
            receipt.setPreset(PRESET_W40H100);
            receipt.addText(ExFormat.format("%-20s%20s\n", "거 래 일 시 :", res_app_date));
            receipt.addText(ExFormat.format("%-20s%20s\n", "카 드 번 호 :", res_card_no));
            receipt.addText(ExFormat.format("%-20s%20s\n", "카 드 종 류 :", res_issuer_nm));
            receipt.addText(ExFormat.format("%-20s%20s\n", "유 효 기 간 :", "**/**"));
            receipt.addText(ExFormat.format("%-20s%20s\n", "거 래 유 형 :", "신용승인")); //FIXME
            receipt.addText(ExFormat.format("%-20s%20s\n", "할 부 개 월 :", res_install_period));
            receipt.addTextLine("----------------------------------------");
            receipt.addText(ExFormat.format("%-20s%18s원\n", "공 급 가 액 :", res_tot_amt)); //FIXME
            receipt.addText(ExFormat.format("%-20s%20s\n", "부  가  세  :", "0원")); //FIXME
            receipt.addText(ExFormat.format("%-20s%18s원\n", "합       계 :", res_tot_amt));
            receipt.addTextLine("----------------------------------------");
            receipt.addText(ExFormat.format("%-20s%20s\n", "승 인 번 호 :", res_auth_no));
            receipt.addTextLine("----------------------------------------");
            receipt.addText(ExFormat.format("%-20s%20s\n", "가 맹 점 명 :", "해피쿠폰점")); //FIXME
            receipt.addText(ExFormat.format("%-20s%20s\n", "대 표 자 명 :", "홍길동")); //FIXME
            receipt.addText(ExFormat.format("%-20s%20s\n", "사 업 자 NO :", "000-00-0000")); //FIXME
            receipt.addText(ExFormat.format("%-20s%20s\n", "문 의 전 화 :", "010-0000-0000")); //FIXME
            receipt.addText("경기 머머시 머머구 머머로00번 길 00(머머동) 0층\n\n"); //FIXME
            receipt.addText("[결제대행사]\n");
            receipt.addText("(주)온오프코리아\n");
            receipt.addText(ExFormat.format("%s %s\n\n", "사업자번호  :", "636-88-00753"));
            receipt.addText("[쿠폰발행사]\n");
            receipt.addText("(주)해피페이\n");
            receipt.addText(ExFormat.format("%s %s\n", "사업자번호  :", "140-81-49182"));
            receipt.addText(ExFormat.format("%s %s\n\n\n", "문 의 전 화 :", "1600-8952"));
            receipt.addText(ExFormat.format("%-20s%20s\n", "", "구매상품내역"));
            receipt.addTextLine("----------------------------------------");
            receipt.addTextAlign("HAPPY COUPON\n", ALIGN_CENTER);
            receipt.addTextLine("----------------------------------------");
            receipt.addText(ExFormat.format("%-20s%18s원\n", "쿠 폰 금 액 :", res_tot_amt));
            receipt.addText(ExFormat.format("%-20s%20s\n", "쿠 폰 번 호  :", res_coupon_no));
            receipt.addTextLine("----------------------------------------");
            receipt.addText("http://m.happycoupon.co.kr\n");
            receipt.addText("에서 사용하신 쿠폰번호로 \n");
            receipt.addText("해피캐시를 적립하신 후\n");
            receipt.addText("리커버샵(www.recovershop.co.kr\n");
            receipt.addText("에서 편리하게 사용하세요\n");
            receipt.addText("(서명/SIGNATURE)\n");

//            receipt.addText(ExFormat.format("%-30s%10s\n", "123 - 45 - 67890", "가      산"));
//            receipt.addText(ExFormat.format("%-20s%20s\n", "가산동 디지털로", "T.02)123-4567"));
//            receipt.addText("홈페이지 http://www.pointmobile.com\n\n");
//            receipt.setPreset(PRESET_W45H150);
//            receipt.addText(ExFormat.format("%-25s%20s\n", "구매 2020/12/24/13.36", "거래번호 : 1234-5678"));
//
//            // 영수 내역
//            receipt.addLine();
//            receipt.setPreset(PRESET_W40H100);
//            receipt.addText(ExFormat.format("%-23s%-5s%12s\n", "상 품 명", "수량", "금 액"));
//            receipt.addLine();
//            receipt.addText(ExFormat.format("%-23s%5d%12d\n", ExFormat.abb("* 브랜드 기타스포츠화", 23), 1, 39000));
//            receipt.addText(ExFormat.format("  %s\n", "123456 7890"));
//            receipt.addText(ExFormat.format("%-23s%5d%12d\n", ExFormat.abb("겨울철 여성의류 다운점퍼", 23), 1, 79000));
//            receipt.addText(ExFormat.format("  %s\n", "123456 7890"));
//            receipt.addText(ExFormat.format("%-23s%5d%12d\n", ExFormat.abb("Book Sample : 책 샘플 제목 소설책", 23), 1, 27300));
//            receipt.addText(ExFormat.format("  %s\n\n", "123456 7890"));
//
//            receipt.setPreset(PRESET_W40H100);
//            receipt.addText(ExFormat.format("%30s%10d\n", "과세 물품가액", 96637));
//            receipt.addText(ExFormat.format("%30s%10d\n\n", "부    가   세", 9663));
//
//            receipt.setPreset(PRESET_W40H200);
//            receipt.addText(ExFormat.format("%-20s%20d\n", "합             계", 106300));
//            receipt.addText(ExFormat.format("%-20s%20d\n", "카  드   결 제 액", 45300));
//            receipt.setPreset(PRESET_W40H100);
//            receipt.addText(ExFormat.format("%-20s%20s\n", "샘플카드", "1234567XXXX1234"));
//            receipt.addText(ExFormat.format("%-14s%-18s%8s\n", "승인번호", "12345678", "[승인]"));
//
//            receipt.getParam().setFakeBoldText(true);
//            receipt.addText(ExFormat.format("  %-18s%-14s%6s\n", "할부기간[일시불]", "카드청구액", "45300"));
//            receipt.getParam().setFakeBoldText(false);
//
//            receipt.addText(ExFormat.format("%-12s%-20s%8d\n", "캐시비결제", "123456789XXXX1234", 61000));
//            receipt.addText(ExFormat.format("%-12s%28d\n", "캐시비잔액", 0));
//            receipt.addText(ExFormat.format("%-12s%28s\n\n", "인증번호", "ABCDEFGH"));
//
//            // 포인트 내역
//            receipt.addTextLine("----------------------------------------");
//            receipt.addText(ExFormat.format("%-20s%20s\n", "샘플회원카드번호", "1234567XXXX1234"));
//            receipt.addText(ExFormat.format("%-20s%20s\n", "금 회 적 립 포인트", "286P"));
//            receipt.addText(ExFormat.format("%-20s%20s\n", "잔  여  포  인  트", "1800P"));
//            receipt.addText(ExFormat.format("%-20s%20s\n", "사 용 가 능 포인트", "1000P"));
//            receipt.addTextLine("----------------------------------------");
//
//            // 하단
//            receipt.addText("교환 환불은 구입 후 7일 이내에 영수증을 반드시 제시하셔야 가능하며 환불 시 사은품을 반납하셔야 합니다.\n\n");
//
//            receipt.addText(ExFormat.format("%s%s\n", "계산담당자 : ", "홍길동"));
//            receipt.addText(ExFormat.format("%s%s", "판매책임자 : ", "홍길동"));
//            receipt.addTextAlign("☎ 010-1234-5678\n", ALIGN_RIGHT);
//            receipt.addFeed();
//
//            // 바코드
//            String code = "202012251234567812345678";
//            receipt.addBarcodeFitWidth(code);
//            receipt.setPreset(PRESET_W40H150);
//            receipt.addTextAlign(code + '\n', ALIGN_CENTER);
        } catch (ReceiptPrint.BitmapOutOfRangeException e) {
            e.printStackTrace();
        }
    }

    private void makeAddText() {

    }
    // </Test Code>


//    // <Toolbar>
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_about) {
//            openInfo();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void openInfo() {
//        String version = "Unknown";
//        try {
//            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
//            if (pi != null) {
//                version = pi.versionName;
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(getString(R.string.app_name) + " v" + version);
//        builder.setPositiveButton(getString(android.R.string.ok), null);
//        builder.create().show();
//    }
    // </Toolbar>
}
