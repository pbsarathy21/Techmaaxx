package com.android.ninos.techmaaxx.Report;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.ninos.techmaaxx.BaseActivity;
import com.android.ninos.techmaaxx.BeanClass.Report;
import com.android.ninos.techmaaxx.Language_Adapter;
import com.android.ninos.techmaaxx.NavigationActivity;
import com.android.ninos.techmaaxx.R;
import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.Utils.CustomTextViewBold;
import com.android.ninos.techmaaxx.Utils.CustomTextviewRegular;
import com.android.ninos.techmaaxx.Utils.CustomTextviewTitle;
import com.android.ninos.techmaaxx.Utils.MLog;
import com.android.ninos.techmaaxx.Utils.Util;
import com.android.ninos.techmaaxx.Utils.Validation;
import com.android.ninos.techmaaxx.common.CmdDialog;
import com.android.ninos.techmaaxx.common.MessageType;
import com.android.ninos.techmaaxx.database.Bluetooth;
import com.android.ninos.techmaaxx.database.DatabaseHelper;
import com.android.ninos.techmaaxx.language.LanguageModel;
import com.android.ninos.techmaaxx.printer.PrinterHelper;
import com.android.ninos.techmaaxx.printer.entity.SupermakerBill;
import com.android.ninos.techmaaxx.request.CommonRequest;
import com.android.ninos.techmaaxx.retrofit.ListDetails;
import com.android.ninos.techmaaxx.retrofit.RetrofitInterface;
import com.android.ninos.techmaaxx.session.Session;
import com.android.ninos.techmaaxx.util.ExecutorFactory;

import java.io.File;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.android.ninos.techmaaxx.BaseActivity.DEVICE_MODEL;
import static com.android.ninos.techmaaxx.BaseActivity.mIzkcService;
import static com.android.ninos.techmaaxx.BaseActivity.module_flag;

public class ReportActivity extends BaseActivity {


    RecyclerView report_recyclerview;
    CustomTextViewBold text_total_Value;
    RetrofitInterface apiInterface;
    Activity activity;
    Context context;
    Session session;
    LinearLayout linear_l_w, linear_l_c;

    Report_adapter report_adapter;
    public static final String TAG = ReportActivity.class.getSimpleName();

    public ArrayList<Report> obj_report = new ArrayList<>();
    ImageView menu;
    CustomTextviewTitle text_ti;
    DatabaseHelper databaseHelper;

    List<Bluetooth> Report_list = new ArrayList<>();
    LinearLayout line_l_w;


    //Print
    Bitmap bit;
    private static final int REQUEST_EX = 1;
    private int fontType = 0;

    private String printTextString = "";
    private boolean checkedPicFlag = false;
    boolean isPrint = true;
    long startTimes = 0;
    long endTimes = 0;
    long timeSpace = 0;
    private Bitmap mBitmap = null;

    @Override
    protected void onStop() {
        super.onStop();
    }

    //线程运行标志 the running flag of thread
    private boolean runFlag = true;
    //打印机检测标志 the detect flag of printer
    private boolean detectFlag = false;
    //打印机连接超时时间 link timeout of printer
    private float PINTER_LINK_TIMEOUT_MAX = 30 * 1000L;
    //标签打印标记 the flag of tag print
    private boolean autoOutputPaper = false;
    //自动打印线程 thread of auto printer
    // AutoPrintThread mAutoPrintThread = null;
    String text;
    /**
     * 图片打印类型
     * image type of print
     */
    int imageType = 0;
    final String[] imageTypeArray = new String[]{"POINT", "GRAY", "RASTER"};
    private RadioGroup radio_cut;
    DetectPrinterThread mDetectPrinterThread;
    LinearLayout li_ok, lin_ca;

    public int total_re = 0, total_mobile = 0, total_cash = 0;

    CustomTextviewRegular mobileTotal, cashTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        activity = ReportActivity.this;
        context = ReportActivity.this;
        session = new Session(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews();

        session.setSelected_method("countable");
        if (session.getReport_type().equalsIgnoreCase("today")) {
            Get_Report_Today();
            text_ti.setText("Today Report");

        } else if (session.getReport_type().equalsIgnoreCase("yesterday")) {

            text_ti.setText("Yesterday Report");

            Get_Report_Yesterday();

        }


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
            }
        });


        MLog.e(TAG, "report_type==>" + session.getReport_type());


        linear_l_w.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                session.setSelected_method("weighable");
                linear_l_w.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                linear_l_c.setBackground(getResources().getDrawable(R.drawable.box_lite));

                MLog.e(TAG, "selected_type==>" + session.getSelected_method());

                if (session.getReport_type().equalsIgnoreCase("today")) {
                    Get_Report_Today();
                } else if (session.getReport_type().equalsIgnoreCase("yesterday")) {

                    Get_Report_Yesterday();

                }

            }
        });

        linear_l_c.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                linear_l_c.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                linear_l_w.setBackground(getResources().getDrawable(R.drawable.box_lite));

                session.setSelected_method("countable");
                MLog.e(TAG, "selected_type==>" + session.getSelected_method());

                if (session.getReport_type().equalsIgnoreCase("today")) {
                    Get_Report_Today();
                } else if (session.getReport_type().equalsIgnoreCase("yesterday")) {

                    Get_Report_Yesterday();

                }


            }
        });


        line_l_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PrintPopUp();
            }
        });


    }

    public void initViews() {
        session = new Session(context);
        databaseHelper = new DatabaseHelper(ReportActivity.this);
        report_recyclerview = (RecyclerView) findViewById(R.id.report_recyclerview);

        report_recyclerview.setNestedScrollingEnabled(false);
        text_total_Value = (CustomTextViewBold) findViewById(R.id.text_total_Value);

        linear_l_w = (LinearLayout) findViewById(R.id.linear_l_w);
        linear_l_c = (LinearLayout) findViewById(R.id.linear_l_c);

        text_ti = (CustomTextviewTitle) findViewById(R.id.text_ti);
        menu = (ImageView) findViewById(R.id.menu);

        line_l_w = (LinearLayout) findViewById(R.id.line_l_w);

        mobileTotal = findViewById(R.id.text_mobile_total_Value);
        cashTotal = findViewById(R.id.text_cash_total_Value);

        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitInterface.url)
                // .baseUrl("https://192.168.1.2/hostel/public/api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(RetrofitInterface.class);
        // Get_Report_Today();

    }


    public void Get_Report_Today() {

        Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.user_id = session.getUser_id();
        // commonRequest.method = session.getSelected_method();
        MLog.e(TAG, "posr_values==>" + "user_id==>" + commonRequest.user_id + "\nmethod==>" + commonRequest.method);

        Call<ListDetails> res = apiInterface.today_report(commonRequest);
        res.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {
                Util.hideProgress();

                if (response.isSuccessful()) {
                    MLog.e("Report=>", "Report==>" + response.body().msg);

                    Report_list.clear();
                    databaseHelper.deleteAllR();

                    if (response.body().status.equalsIgnoreCase("success")) {

                        MLog.e("Report=>", "Report==>" + response.body().status);

                        obj_report = response.body().today_report;


                        total_re = Integer.parseInt(response.body().total_price);
                        total_mobile = Integer.parseInt(response.body().mobile_money_price);
                        total_cash = Integer.parseInt(response.body().cash_price);

                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(total_re);
                        String total_mobile_format = formatter.format(total_mobile);
                        String total_cash_format = formatter.format(total_cash);

                        text_total_Value.setText(yourFormattedString+".00");
                        mobileTotal.setText(total_mobile_format+".00");
                        cashTotal.setText(total_cash_format+".00");

                        session.setReport_total(yourFormattedString);
                        session.setMobile_money_price(total_mobile_format);
                        session.setCash_price(total_cash_format);


                        if (!obj_report.isEmpty()) {

                            for (int i = 0; i < obj_report.size(); i++) {
                                databaseHelper.insertreport(obj_report.get(i).name, obj_report.get(i).consignmentno, obj_report.get(i).origin, obj_report.get(i).destination, obj_report.get(i).freight, obj_report.get(i).date, obj_report.get(i).payment_mode);
                            }
                            Report_list = databaseHelper.getAll_Report_list();
                            MLog.e("Report=>", "Report_size==>" + Report_list.size());


                            report_adapter = new Report_adapter(ReportActivity.this, obj_report);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ReportActivity.this);
                            report_recyclerview.setLayoutManager(layoutManager);
                            report_recyclerview.setAdapter(report_adapter);
                            report_adapter.notifyDataSetChanged();
                            Util.hideProgress();

                        } else {
                            Toast.makeText(ReportActivity.this, "No Record!", Toast.LENGTH_SHORT).show();

                        }
                    } else if (response.body().status.equalsIgnoreCase("error")) {
                        Toast.makeText(ReportActivity.this, "No Record!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Util.hideProgress();
                    Toast.makeText(ReportActivity.this, "response null!", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {
                Util.hideProgress();

                Toast.makeText(ReportActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
            }


        });
    }

    public void Get_Report_Yesterday() {

        MLog.e(TAG, "Get_Report_Yesterday==>");

        Util.showProgress(activity);
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.user_id = session.getUser_id();
        // commonRequest.method = session.getSelected_method();
        MLog.e(TAG, "posr_values==>" + "user_id==>" + commonRequest.user_id + "\nmethod==>" + commonRequest.method);

        Call<ListDetails> res = apiInterface.yesterday_report(commonRequest);
        res.enqueue(new Callback<ListDetails>() {
            @Override
            public void onResponse(Call<ListDetails> call, Response<ListDetails> response) {
                Util.hideProgress();

                if (response.isSuccessful()) {
                    MLog.e("Report=>", "Report==>" + response.body().msg);


                    Report_list.clear();
                    databaseHelper.deleteAllR();

                    if (response.body().status.equalsIgnoreCase("success")) {

                        MLog.e("Report=>", "Report==>" + response.body().status);

                        obj_report = response.body().yesterday_report;

                        total_re = Integer.parseInt(response.body().total_price);

                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        String yourFormattedString = formatter.format(total_re);

                        text_total_Value.setText(yourFormattedString +".00");

                        session.setReport_total(yourFormattedString);

                        if (!obj_report.isEmpty()) {

                            for (int i = 0; i < obj_report.size(); i++) {
                                databaseHelper.insertreport(obj_report.get(i).name, obj_report.get(i).consignmentno, obj_report.get(i).origin, obj_report.get(i).destination, obj_report.get(i).freight, obj_report.get(i).date, obj_report.get(i).payment_mode);
                            }
                            Report_list = databaseHelper.getAll_Report_list();
                            MLog.e("Report=>", "Report_size==>" + Report_list.size());

                            report_adapter = new Report_adapter(ReportActivity.this, obj_report);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ReportActivity.this);
                            report_recyclerview.setLayoutManager(layoutManager);
                            report_recyclerview.setAdapter(report_adapter);
                            report_adapter.notifyDataSetChanged();
                            Util.hideProgress();

                        } else {
                            Toast.makeText(ReportActivity.this, "No Record!", Toast.LENGTH_SHORT).show();

                        }
                    } else if (response.body().status.equalsIgnoreCase("error")) {
                        Toast.makeText(ReportActivity.this, "No Record!", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Util.hideProgress();
                    Toast.makeText(ReportActivity.this, "response null!", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ListDetails> call, Throwable t) {
                Util.hideProgress();

                Toast.makeText(ReportActivity.this, "Oops! something Went wrong. Please try again..!!!", Toast.LENGTH_LONG).show();
            }


        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            finish();


        }
        return true;
    }


    //Print Functionality


    class DetectPrinterThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (runFlag) {
                float start_time = SystemClock.currentThreadTimeMillis();
                float end_time = 0;
                float time_lapse = 0;
                if (detectFlag) {
                    //检测打印是否正常 detect if printer is normal
                    try {
                        if (mIzkcService != null) {
                            String printerSoftVersion = mIzkcService.getFirmwareVersion1();
                            if (TextUtils.isEmpty(printerSoftVersion)) {
                                mIzkcService.setModuleFlag(module_flag);
                                end_time = SystemClock.currentThreadTimeMillis();
                                time_lapse = end_time - start_time;
//								enableOrDisEnableKey(false);
                                if (time_lapse > PINTER_LINK_TIMEOUT_MAX) {
                                    detectFlag = false;
                                    //打印机连接超时 printer link timeout
                                    sendEmptyMessage(MessageType.BaiscMessage.PRINTER_LINK_TIMEOUT);
                                }
                            } else {
                                //打印机连接成功 printer link success
                                sendMessage(MessageType.BaiscMessage.DETECT_PRINTER_SUCCESS, printerSoftVersion);
                                detectFlag = false;
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                }
                SystemClock.sleep(1);
            }
        }
    }

    @Override
    protected void onResume() {
        //开始检测打印机 begin to detect printer
        detectFlag = true;
        enableOrDisEnableKey(false);
        super.onResume();
    }

    @Override
    protected void handleStateMessage(Message message) {
        super.handleStateMessage(message);
        switch (message.what) {
            //服务绑定成功 service bind success
            case MessageType.BaiscMessage.SEVICE_BIND_SUCCESS:
//				Toast.makeText(this, getString(R.string.service_bind_success), Toast.LENGTH_SHORT).show();
                break;
            //服务绑定失败 service bind fail
            case MessageType.BaiscMessage.SEVICE_BIND_FAIL:
//				Toast.makeText(this, getString(R.string.service_bind_fail), Toast.LENGTH_SHORT).show();
                break;
            //打印机连接成功 printer link success
            case MessageType.BaiscMessage.DETECT_PRINTER_SUCCESS:
                String msg = (String) message.obj;
                checkPrintStateAndDisplayPrinterInfo(msg);
                break;
            //打印机连接超时 printer link timeout
            case MessageType.BaiscMessage.PRINTER_LINK_TIMEOUT:
                Toast.makeText(this, getString(R.string.printer_link_timeout), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void checkPrintStateAndDisplayPrinterInfo(String msg) {
        enableOrDisEnableKey(true);
        if (DEVICE_MODEL == 800) radio_cut.setVisibility(View.VISIBLE);
        if (!checkedPicFlag) generateBarCode();
        String status;
        String aidlServiceVersion;
        try {
            mIzkcService.getPrinterStatus();
            status = mIzkcService.getPrinterStatus();
            // tv_printStatus.setText(status);
            aidlServiceVersion = mIzkcService.getServiceVersion();
            //tv_printer_soft_version.setText(msg + "\nAIDL Service Version:" + aidlServiceVersion);
            //打印自检信息
//			mIzkcService.printerSelfChecking();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 构造SimpleAdapter的第二个参数，类型为List<Map<?,?>>
     *
     * @param
     * @return
     */
    private List<LanguageModel> getData() {
        Resources res = getResources();
        String[] cmdStr = res.getStringArray(R.array.language);
        List<LanguageModel> languageModelList = new ArrayList<>();
        for (int i = 0; i < cmdStr.length; i++) {
            String[] cmdArray = cmdStr[i].split(",");
            if (cmdArray.length == 3) {
                LanguageModel languageModel = new LanguageModel();
                languageModel.code = Integer.parseInt(cmdArray[0]);
                languageModel.language = cmdArray[1];
                languageModel.description = cmdArray[1] + " " + cmdArray[2];
                languageModelList.add(languageModel);
            }
        }
        return languageModelList;
    }




   /* printPurcase(false, false);

                if (DEVICE_MODEL == 800) {
        try {
            //半切
            mIzkcService.sendRAWData("printer", new byte[]{0x0a, 0x0a, 0x1b, 0x69});
            mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x08});
            mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x07});
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }*/


    boolean run;

    private void MutiThreadPrint() {
        showStopMutiThreadPrintDialog();
        run = true;
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    printPic();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }


            }
        });
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    printGBKText();
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    printPurcase(true, false);
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });
        ExecutorFactory.executeThread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    printPic();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        break;
                    }
                }

            }
        });

    }

    private void showStopMutiThreadPrintDialog() {
        AlertDialog.Builder builderDialog = new AlertDialog.Builder(this);
        builderDialog.setMessage("是否停止多线程打印......");
        builderDialog.setCancelable(false);
        builderDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                run = false;
            }
        });
        builderDialog.show();

    }

    private void enableOrDisEnableKey(boolean enable) {
        //  btnPrint.setEnabled(enable);
        //  btnUnicode.setEnabled(enable);
        // btnPrintPic.setEnabled(enable);
        //  btnPrintModelOne.setEnabled(enable);
        //  btnPrintModelTwo.setEnabled(enable);
        //  btnPrintModelThree.setEnabled(enable);
        // spinner_pic_style.setEnabled(enable);
        // spinnerLanguage.setEnabled(enable);
    }

    private void printBitmapUnicode1F30() {
        //  text = et_printText.getText().toString() + "\n";
        try {
            // mIzkcService.printUnicode_1F30(text);
            if (autoOutputPaper) {
                mIzkcService.generateSpace();
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printBitmapRaster() {
        try {
            if (mBitmap != null) {
                mIzkcService.printRasterImage(mBitmap);
                if (autoOutputPaper) {
                    mIzkcService.generateSpace();
                }
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printBitmapGray() {
        try {
            if (mBitmap != null) {
                mIzkcService.printImageGray(mBitmap);
                if (autoOutputPaper) {
                    mIzkcService.generateSpace();
                }
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }


    private void printPurcase(boolean hasStartPic, boolean hasEndPic) {
        SupermakerBill bill = PrinterHelper.getInstance(this).getSupermakerBill(mIzkcService, hasStartPic, hasEndPic);
        PrinterHelper.getInstance(this).printPurchaseBillModelOne(mIzkcService, bill, imageType);
    }


/*    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            bit = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            return bit;


        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }*/


    private void printBitmapAlgin(int alginStyle) {
        //  Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zkc);
//		Bitmap bitmap1 = resizeImage(bitmap, 376, 120);
        try {
            mIzkcService.printBitmapAlgin(bit, 376, 120, alginStyle);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void printTextAlgin(int alginStyle) {
        String pString = "智能打印\n";
        try {
            mIzkcService.printTextAlgin(pString, 0, 1, alginStyle);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private void printVamplify(int type) {
        try {
            mIzkcService.printTextWithFont(text, fontType, type);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }


    private void printFont(int type) {
        try {
//			mIzkcService.setTypeface(type);
//			mIzkcService.printGBKText(text);
            mIzkcService.printTextWithFont(text, type, 0);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkedPicFlag = true;
        if (requestCode == REQUEST_EX && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            //   iv_printPic.setImageURI(selectedImage);
            mBitmap = BitmapFactory.decodeFile(picturePath);
            // iv_printPic.setImageBitmap(mBitmap);
            if (mBitmap.getHeight() > 384) {
                // iv_printPic.setImageBitmap(resizeImage(mBitmap, 384, 384));
            }
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        if (width >= newWidth) {
            float scaleWidth = ((float) newWidth) / width;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;
        } else {
            Bitmap bitmap2 = Bitmap.createBitmap(newWidth, newHeight,
                    bitmap.getConfig());
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawColor(Color.WHITE);

            canvas.drawBitmap(BitmapOrg, (newWidth - width) / 2, 0, null);

            return bitmap2;
        }
    }

    private void selectPic() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_EX);

    }

    private void generateQrCode() {
        try {
            mBitmap = mIzkcService.createQRCode("http://www.sznewbest.com", 384, 384);
            //   iv_printPic.setImageBitmap(mBitmap);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void generateBarCode() {
        try {
            mBitmap = mIzkcService.createBarCode("4333333367", 1, 384, 120, true);
            //iv_printPic.setImageBitmap(mBitmap);
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printUnicode() {
        //  text = et_printText.getText().toString() + "\n";
        try {
            mIzkcService.printUnicodeText(text);
            if (autoOutputPaper) {
                mIzkcService.generateSpace();
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printGBKText() {
        // text = et_printText.getText().toString();

        try {
//			mIzkcService.printerInit();
//			mIzkcService.sendRAWData("printer", new byte[] { 0x1E, 0x04, 0x00, (byte) 0xBF, (byte) 0xD8, (byte) 0xD6, (byte) 0xC6});
            mIzkcService.printGBKText(text);
//			mIzkcService.printGBKText("\nАа Бб Вв Гг Дд  Ее Ёё  Жж Зз Ии Йй Кк Лл Мм Нн Оо Пп Рр Сс Тт Уу Фф Хх Цц Чч Шш Щщ ъ ы ь Ээ Юю Яя\n");
            if (autoOutputPaper) {
                mIzkcService.generateSpace();
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }

    private void printPic() {
        try {
            if (mBitmap != null) {
                switch (imageType) {
                    case 0:
                        mIzkcService.printBitmap(mBitmap);
                        break;
                    case 1:
                        mIzkcService.printImageGray(mBitmap);
                        break;
                    case 2:
                        mIzkcService.printRasterImage(mBitmap);
                        break;
                }
                if (autoOutputPaper) {
                    mIzkcService.generateSpace();
                }
            }
        } catch (RemoteException e) {
            Log.e("", "远程服务未连接...");
            e.printStackTrace();
        }
    }


    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showCmd() {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "cmd.txt";
        //读取模板数据，按行保存
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(ReportActivity.this,
                    "请按规定格式将指令保存在名为cmd.txt文件中，并复制在终端根目录下",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        CmdDialog cmdDialog = new CmdDialog(this, new CmdDialog.DialogCallBack() {
            @Override
            public void submit(String cmd) {
                String cmds = cmd;
                if (mIzkcService != null) {
                    byte[] buffer = hexStringToBytes(cmds);
                    try {
                        mIzkcService.sendRAWData("printer", buffer);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        cmdDialog.show();
    }

    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toLowerCase();
        String[] hexStrings = hexString.split(" ");
        byte[] bytes = new byte[hexStrings.length];
        for (int i = 0; i < hexStrings.length; i++) {
            char[] hexChars = hexStrings[i].toCharArray();
            bytes[i] = (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
        }
        return bytes;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }


    @Override
    protected void onDestroy() {
        //禁止打印
        if (mIzkcService != null) try {
            mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x03, 0x00,
                    (byte) 0xBF, (byte) 0xD8, (byte) 0xD6, (byte) 0xC6});
        } catch (RemoteException e) {
            e.printStackTrace();
        }
       /* if (mAutoPrintThread != null) {
            //  mAutoPrintThread.interrupt();
            mAutoPrintThread = null;
        }*/
        if (mDetectPrinterThread != null) {
            runFlag = false;
            mDetectPrinterThread.interrupt();
            mDetectPrinterThread = null;
        }
        super.onDestroy();
    }


    private void PrintPopUp() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        //builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View content = inflater.inflate(R.layout.print_popup, null);
        builder.setView(content);

        //  AlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        li_ok = (LinearLayout) content.findViewById(R.id.li_ok);
        lin_ca = (LinearLayout) content.findViewById(R.id.lin_ca);


        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_view));


        li_ok.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();

                printPurcase(false, false);

                if (DEVICE_MODEL == 800) {
                    try {
                        //半切
                        mIzkcService.sendRAWData("printer", new byte[]{0x0a, 0x0a, 0x1b, 0x69});
                        mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x08, 0x08});
                        mIzkcService.sendRAWData("printer", new byte[]{0x1E, 0x07, 0x07});
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }


                }


            }
        });

        lin_ca.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                alertDialog.dismiss();

            }
        });

    }
}
