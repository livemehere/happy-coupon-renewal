package kr.co.pointmobile.msrdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

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
        Log.d("ttt","yeseyseys");

        initActivity();
        clearResult();

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
                                        String res_result_cd = response.body().result_cd;
                                        String res_result_msg = response.body().result_msg;
                                        String res_tot_amt = response.body().tot_amt;
                                        String res_card_no = response.body().card_no;
                                        String res_expire_date = response.body().expire_date;
                                        String res_install_period = response.body().install_period;
                                        String res_transeq = response.body().transeq;
                                        String res_auth_no = response.body().auth_no;
                                        String res_coupon_no = response.body().coupon_no;
                                        String res_app_data = response.body().app_date;
                                        String res_iss_cd = response.body().iss_cd;
                                        String res_iss_nm = response.body().iss_nm;
                                        Log.d("결제완료된 쿠폰번호",res_coupon_no);


                                        Toast.makeText(getApplicationContext(), response.body().result_msg, Toast.LENGTH_SHORT).show();
                                        //----- 결과값 출력 팝
                                        String valuesMessage = String.format("결과코드: %s\n결과메세지: %s\n결제금액: %s\n카드번호: %s\n유효기간: %s\n할부: %s\n결제일련번호: %s\n승인번호: %s\n쿠폰번호: %s\n결제일시: %s\n신용카드 코드: %s\n신용카드명: %s",
                                                res_result_cd,res_result_msg,res_tot_amt,res_card_no,res_expire_date,res_install_period,res_transeq,res_auth_no,res_coupon_no,res_app_data,res_iss_cd,res_iss_nm);
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MsrDemoActivity.this);
                                        builder.setMessage(valuesMessage).setTitle("영수증을 출력하시겠습니까?");
                                        builder.setPositiveButton("출력", new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                //TODO: 영수증출력하기
                                                Toast.makeText(getApplicationContext(), "영수증을 출력합니다", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });

                                        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                Toast.makeText(getApplicationContext(), "영수증을 출력하지 않습니다", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getApplicationContext(), call.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("failKong",t.toString());
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_info:
                openInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openInfo()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        String version = getString(R.string.msg_version_suffix);
        try
        {
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            if (pi != null)
            {
                version = pi.versionName;
            }
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        alert.setMessage(getString(R.string.app_name) + " v" + version);
        alert.show();
    }

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
}
