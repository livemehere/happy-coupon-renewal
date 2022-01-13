package kr.co.pointmobile.msrdemo;

import android.app.Application;

import device.sdk.print.Printer;
import device.sdk.print.ReceiptPrint;
import device.sdk.print.wrapper.IPrint;
import vpos.apipackage.Print;

public class BaseApplication extends Application {

    public Printer printer;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public ReceiptPrint getReceiptPrint(){
        return new ReceiptPrint(this.getApplicationContext());
    }

    public Printer getPrinter(){
        if(printer == null){
            final Print print = new Print();
            printer = Printer.open(new IPrint() {
                @Override
                public int init() {
                    return print.Lib_PrnInit();
                }

                @Override
                public int print() {
                    return print.Lib_PrnStart();
                }

                @Override
                public int checkStatus() {
                    return print.Lib_PrnCheckStatus();
                }

                @Override
                public int setGray(byte b) {
                    return print.Lib_PrnSetGray(b);
                }

                @Override
                public int addBmp(byte[] array) {
                    return print.Lib_PrnLogo(array);
                }
            });

        }
        return printer;
    }
}
