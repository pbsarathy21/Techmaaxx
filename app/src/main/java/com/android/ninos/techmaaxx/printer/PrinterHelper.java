package com.android.ninos.techmaaxx.printer;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.ninos.techmaaxx.BeanClass.Ship;
import com.android.ninos.techmaaxx.Countable.CountableActivity;
import com.android.ninos.techmaaxx.Countable.Shipment_adapter;
import com.android.ninos.techmaaxx.R;
import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.Utils.MLog;
import com.android.ninos.techmaaxx.Utils.Util;
import com.android.ninos.techmaaxx.database.Bluetooth;
import com.android.ninos.techmaaxx.database.DatabaseHelper;
import com.android.ninos.techmaaxx.printer.entity.GoodsInfo;
import com.android.ninos.techmaaxx.printer.entity.SupermakerBill;
import com.android.ninos.techmaaxx.request.CommonRequest;
import com.android.ninos.techmaaxx.retrofit.ListDetails;
import com.android.ninos.techmaaxx.retrofit.RetrofitInterface;
import com.android.ninos.techmaaxx.session.Session;
import com.smartdevice.aidl.IZKCService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.android.ninos.techmaaxx.PrinterActivity.TAG;


public class PrinterHelper {


    /* 等待打印缓冲刷新的时间 */
    private static final int mIzkcService_BUFFER_FLUSH_WAITTIME = 150;
    /* 分割线 */
    private static final String mIzkcService_CUT_OFF_RULE = "--------------------------------\n";

    // 品名占位长度
    private static final int GOODS_NAME_LENGTH = 6;
    // 单价占位长度
    private static final int GOODS_UNIT_PRICE_LENGTH = 6;
    // 价格占位长度
    private static final int GOODS_PRICE_LENGTH = 6;
    // 数量占位长度
    private static final int GOODS_AMOUNT = 6;

    private Context mContext;
    public Session session;
    List<Bluetooth> count_list = new ArrayList<>();
    List<Bluetooth> Report_list = new ArrayList<>();
    DatabaseHelper databaseHelper;

    Bitmap bit;
    private static PrinterHelper _instance;

    private PrinterHelper(Context mContext) {
        this.mContext = mContext;
        session = new Session(mContext);
        databaseHelper = new DatabaseHelper(mContext);

        count_list = databaseHelper.getAllNotes();
        Report_list = databaseHelper.getAll_Report_list();
        StringToBitMap(session.getImg_log());
    }


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            bit = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

            return bit;


        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    synchronized public static PrinterHelper getInstance(Context mContext) {
        if (null == _instance)
            _instance = new PrinterHelper(mContext);
        return _instance;
    }

    synchronized public void printPurchaseBillModelOne(
            IZKCService mIzkcService, SupermakerBill bill, int imageType) {

        try {
            if (mIzkcService != null && mIzkcService.checkPrinterAvailable()) {
                mIzkcService.printGBKText("\n");
                if (bill.start_bitmap != null) {
//					mIzkcService.printBitmapAlgin(bill.start_bitmap, 376, 120, 1);
                    switch (imageType) {
                        case 0:
                            mIzkcService.printBitmap(bill.start_bitmap);
                            break;
                        case 1:
                            mIzkcService.printImageGray(bill.start_bitmap);
                            break;
                        case 2:
                            mIzkcService.printRasterImage(bill.start_bitmap);
                            break;
                    }
                }
                SystemClock.sleep(50);

                if (Consts.Method.equalsIgnoreCase("weighable")) {


                    if (session.getImg_log().equalsIgnoreCase("")) {

                    } else {
                        mIzkcService.printBitmapAlgin(bit, 376, 120, 0);
                    }

                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printTextAlgin(centerAlign(session.getCompany_Name()), 0, 1, 18);
                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printGBKText(centerAlign(session.getCompany_add1()) + "\n");
                    mIzkcService.printGBKText(centerAlign(session.getCompany_add2()) + "\n");


                    mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);

                    mIzkcService.printGBKText("Name/Seat No" + "      " + session.getName() + "\n");
                    mIzkcService.printGBKText("Phone       " + "      " + session.getU_Mobile() + "\n");
                    mIzkcService.printGBKText("Fleet  No   " + "      " + session.getFleet() + "\n");
                    mIzkcService.printGBKText("Orgin       " + "      " + session.getOrgin_value() + "\n");
                    mIzkcService.printGBKText("Designation " + "      " + session.getDes_value() + "\n");
                    mIzkcService.printGBKText("Weight      " + "      " + session.getW_Weight() + "Kg" + "\n");
                    mIzkcService.printGBKText("Quantity    " + "      " + session.getBox() + "\n");
                    mIzkcService.printGBKText("Consignment No" + "    " + session.getConsigment_no() + "\n");
                    mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);
                    mIzkcService.printGBKText("Payment mode" + "      " + session.getPayment_mode() + "\n");
                    mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);
                    mIzkcService.printGBKText(PrintTag.PurchaseBillTag.TOTAL + "             " + session.getCurrency_Symbol() + " " + session.getWeighable_total() + ".00" + "\n");
                    mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);
                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printTextAlgin(centerAlign("Thank you"), 0, 1, 18);

                 /*   if (session.getPower_text().equalsIgnoreCase("")) {
                        mIzkcService.printTextAlgin("           " + "Thank You", 0, 1, 18);

                    } else {
                        mIzkcService.printTextAlgin("           " + "Thank You", 0, 1, 18);
                        mIzkcService.printGBKText("           " + "" + "\n");
                     //   mIzkcService.printTextAlgin("           " + "Powered by", 0, 1, 18);
                      //  mIzkcService.printGBKText("           " + "" + "\n");

                        mIzkcService.printTextAlgin("    " + session.getPower_text(), 0, 1, 18);
                        mIzkcService.printGBKText("           " + "" + "\n");
                        mIzkcService.printGBKText("           " + "" + "\n");
                        mIzkcService.printGBKText("           " + "" + "\n");
                        mIzkcService.printGBKText("           " + "" + "\n");
                        mIzkcService.printGBKText("           " + "" + "\n");

                    }*/

                    session.setName("");
                    session.setU_Mobile("");
                    session.setFleet("");
                    session.setOrgin("");
                    session.setOrgin_value("Orgin");
                    session.setDestination("");
                    session.setDes_value("Destination");
                    session.setProduct("");
                    session.setProduct_value("Product");
                    session.setCategory("");
                    session.setCategory_value("Category");
                    Consts.Method = "";
                    session.setClose_cat_id("");
                    session.setWeighable_total("");
                    session.setW_Weight("");
                    session.setW_Prize("");
                    count_list.clear();
                    session.setCount_Prize("");
                    session.setConsigment_no("");
                    count_list.size();
                    databaseHelper.deleteAllRecord();

                } else if (Consts.Method.equalsIgnoreCase("countable")) {

                    if (session.getImg_log().equalsIgnoreCase("")) {

                    } else {
                        mIzkcService.printBitmapAlgin(bit, 376, 120, 0);
                    }

                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printTextAlgin(centerAlign(session.getCompany_Name()), 0, 1, 18);
                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printGBKText(centerAlign(session.getCompany_add1())+ "\n");
                    mIzkcService.printGBKText(centerAlign(session.getCompany_add2()) + "\n");

                    mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);

                    mIzkcService.printGBKText("Name/Seat No" + "     " + session.getName() + "\n");
                    mIzkcService.printGBKText("Phone       " + "     " + session.getU_Mobile() + "\n");
                    mIzkcService.printGBKText("Fleet  No   " + "     " + session.getFleet() + "\n");
                    mIzkcService.printGBKText("Orgin       " + "     " + session.getOrgin_value() + "\n");
                    mIzkcService.printGBKText("Destination " + "     " + session.getDes_value() + "\n");
                    mIzkcService.printGBKText("Consignment No" + "   " + session.getConsigment_no() + "\n");
                    mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);
                    count_list = databaseHelper.getAllNotes();
                    if (count_list.size() > 0) {

                        //     mIzkcService.printGBKText("Product        " + "    " + "Qty   " + " " + "Price" + "\n");
                        //   mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);
                        for (int i = 0; i < count_list.size(); i++) {

                            //  mIzkcService.printGBKText(count_list.get(i).pro + "           " + count_list.get(i).box + "     " + count_list.get(i).prize + "\n");
                            //  mIzkcService.printGBKText("Category    " + "      " + count_list.get(i).cat + "\n");
                            mIzkcService.printGBKText("Product     " + "     " + count_list.get(i).pro + "\n");
                            mIzkcService.printGBKText("Qty         " + "     " + count_list.get(i).box + "\n");
                            mIzkcService.printGBKText("Price       " + "     " + count_list.get(i).prize + "\n");
                            mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);
                        }

                    }
                    //    mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);

                    mIzkcService.printGBKText("Payment mode" + "      " + session.getPayment_mode() + "\n");
                    mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);
                    mIzkcService.printGBKText(PrintTag.PurchaseBillTag.TOTAL + "             " + session.getCurrency_Symbol() + " " + session.getCount_Prize() + ".00" + "\n");
                    mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);
                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printTextAlgin(centerAlign("Thank you"+"\n"),0, 1, 18);
                    mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);


                    /*if (session.getPower_text().equalsIgnoreCase("")) {
                        mIzkcService.printTextAlgin("           " + "Thank You", 0, 1, 18);

                    } else {
                        mIzkcService.printTextAlgin("           " + "Thank You", 0, 1, 18);
                        mIzkcService.printGBKText("           " + "" + "\n");
                     //   mIzkcService.printTextAlgin("           " + "Powered by", 0, 1, 18);
                      //  mIzkcService.printGBKText("           " + "" + "\n");

                        mIzkcService.printTextAlgin("    " + session.getPower_text(), 0, 1, 18);
                        mIzkcService.printGBKText("           " + "" + "\n");
                        mIzkcService.printGBKText("           " + "" + "\n");
                        mIzkcService.printGBKText("           " + "" + "\n");
                        mIzkcService.printGBKText("           " + "" + "\n");
                        mIzkcService.printGBKText("           " + "" + "\n");
                    }
*/
                    session.setName("");
                    session.setU_Mobile("");
                    session.setFleet("");
                    session.setOrgin("");
                    session.setOrgin_value("Orgin");
                    session.setDestination("");
                    session.setDes_value("Destination");
                    session.setProduct("");
                    session.setProduct_value("Product");
                    session.setCategory("");
                    session.setCategory_value("Category");
                    Consts.Method = "";
                    session.setClose_cat_id("");
                    session.setWeighable_total("");
                    session.setW_Weight("");
                    session.setW_Prize("");
                    session.setConsigment_no("");
                    count_list.clear();
                    session.setCount_Prize("");
                    count_list.size();
                    databaseHelper.deleteAllRecord();

                }

               /* else if (session.getTestPrint())
                {

                    String company = "Arun Prasad";


                    mIzkcService.printGBKText(centerAlign(company));
                    session.setTestPrint(false);
                }*/

                else {
                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printTextAlgin(centerAlign("REPORT"), 0, 1, 18);
                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printTextAlgin("Date            ", 0, 1, 18);
                    mIzkcService.printTextAlgin(" Consginment No", 0, 1, 18);
                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printTextAlgin("Orgin            ", 0, 1, 18);
                    mIzkcService.printTextAlgin("Destination", 0, 1, 18);
                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printTextAlgin("Postby           ", 0, 1, 18);
                    mIzkcService.printTextAlgin("Freight", 0, 1, 18);
                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printTextAlgin("               ", 0, 1, 18);
                    mIzkcService.printTextAlgin("  Payment mode", 0, 1, 18);
                    mIzkcService.printGBKText("" + "\n");
                    mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);


                    Report_list = databaseHelper.getAll_Report_list();

                    if (Report_list.size() > 0) {

                        for (int i = 0; i < Report_list.size(); i++) {

                            mIzkcService.printGBKText(Report_list.get(i).r_date + "        " + Report_list.get(i).r_con + "\n");
                            mIzkcService.printGBKText(Report_list.get(i).r_orgin + "           " + Report_list.get(i).r_des + "\n");
                            mIzkcService.printGBKText(Report_list.get(i).r_name + "             " + Report_list.get(i).r_freight + "\n");
                            mIzkcService.printGBKText("   " + "               " + Report_list.get(i).p_mode + "\n");

                            mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);
                        }

                        mIzkcService.printGBKText("Mobile Money Total : "+session.getMobile_money_price()+ ".00"+"\n");
                        mIzkcService.printGBKText("Cash Total         : "+session.getCash_price()+ ".00"+"\n");
                        mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);

                        mIzkcService.printTextAlgin("Total          ", 0, 1, 18);
                        mIzkcService.printTextAlgin(session.getReport_total() + ".00", 0, 1, 18);
                        mIzkcService.printGBKText("" + "\n");
                        mIzkcService.printGBKText(mIzkcService_CUT_OFF_RULE);
                        mIzkcService.printGBKText("" + "\n");
                        mIzkcService.printTextAlgin(centerAlign("Thank you"), 0, 1, 18);

                    }

                }


            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String centerAlign(String company) {

        int comLen = 15-(company.length()/2);
        StringBuilder space = new StringBuilder();
        for (int i = 0; i<comLen; i++)
        {
            space.append(" ");
        }

        return space+company;
    }

    public SupermakerBill getSupermakerBill(IZKCService mIzkcService,
                                            boolean display_start_pic, boolean display_end_pic) {
        SupermakerBill bill = new SupermakerBill();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String dateStr = sdf.format(date);
        bill.supermaker_name = "TechMaaxx";
        bill.supermaker_name = "TechMaaxx";
       /* bill.serial_number = System.currentTimeMillis() + "";
        bill.purchase_time = dateStr;
        bill.total_amount = "36";
        bill.total_cash = "1681.86";
        bill.favorable_cash = "81.86";
        bill.receipt_cash = "1600";
        bill.recived_cash = "1600";
        bill.odd_change = "0.0";
        bill.vip_number = "111111111111";
        bill.add_integral = "1600";
        bill.current_integral = "36000";
        bill.supermaker_address = "深圳市宝安区鹤州xxxxxxxx";
        bill.supermaker_call = "0755-99991668";*/

        generalBitmap(mIzkcService, bill, display_start_pic, display_end_pic);
        addGoodsInfo(bill.goosInfos);

        return bill;

    }

    private void generalBitmap(IZKCService mIzkcService, SupermakerBill bill,
                               boolean display_start_pic, boolean display_end_pic) {

        if (display_start_pic) {
            Bitmap mBitmap = null;
//			try {
//				mBitmap = mIzkcService.createBarCode("4333333367", 1, 384, 120, false);
            //    mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.zkc);
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
            bill.start_bitmap = mBitmap;
        }
        if (display_end_pic) {
            Bitmap btMap;
            try {
                btMap = mIzkcService.createQRCode("扫描关注本店，有惊喜喔", 240, 240);
                bill.end_bitmap = btMap;
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private void addGoodsInfo(ArrayList<GoodsInfo> goosInfos) {

        GoodsInfo goodsInfo0 = new GoodsInfo();
        goodsInfo0.goods_name = "黑人牙膏";
        goodsInfo0.goods_unit_price = "14.5";
        goodsInfo0.goods_amount = "2";
        goodsInfo0.goods_price = "29";

        GoodsInfo goodsInfo1 = new GoodsInfo();
        goodsInfo1.goods_name = "啤酒";
        goodsInfo1.goods_unit_price = "2.5";
        goodsInfo1.goods_amount = "12";
        goodsInfo1.goods_price = "30";

        GoodsInfo goodsInfo2 = new GoodsInfo();
        goodsInfo2.goods_name = "美的电饭煲";
        goodsInfo2.goods_unit_price = "288";
        goodsInfo2.goods_amount = "1";
        goodsInfo2.goods_price = "288";

        GoodsInfo goodsInfo3 = new GoodsInfo();
        goodsInfo3.goods_name = "剃须刀";
        goodsInfo3.goods_unit_price = "78";
        goodsInfo3.goods_amount = "1";
        goodsInfo3.goods_price = "78";

        GoodsInfo goodsInfo4 = new GoodsInfo();
        goodsInfo4.goods_name = "泰国进口红提";
        goodsInfo4.goods_unit_price = "22";
        goodsInfo4.goods_amount = "2";
        goodsInfo4.goods_price = "44";

        GoodsInfo goodsInfo5 = new GoodsInfo();
        goodsInfo5.goods_name = "太空椒";
        goodsInfo5.goods_unit_price = "4.5";
        goodsInfo5.goods_amount = "2";
        goodsInfo5.goods_price = "9";

        GoodsInfo goodsInfo6 = new GoodsInfo();
        goodsInfo6.goods_name = "进口香蕉";
        goodsInfo6.goods_unit_price = "3.98";
        goodsInfo6.goods_amount = "3";
        goodsInfo6.goods_price = "11.86";

        GoodsInfo goodsInfo7 = new GoodsInfo();
        goodsInfo7.goods_name = "烟熏腊肉";
        goodsInfo7.goods_unit_price = "33";
        goodsInfo7.goods_amount = "2";
        goodsInfo7.goods_price = "66";

        GoodsInfo goodsInfo8 = new GoodsInfo();
        goodsInfo8.goods_name = "长城红葡萄干酒";
        goodsInfo8.goods_unit_price = "39";
        goodsInfo8.goods_amount = "2";
        goodsInfo8.goods_price = "78";

        GoodsInfo goodsInfo9 = new GoodsInfo();
        goodsInfo9.goods_name = "白人牙刷";
        goodsInfo9.goods_unit_price = "14";
        goodsInfo9.goods_amount = "2";
        goodsInfo9.goods_price = "28";

        GoodsInfo goodsInfo10 = new GoodsInfo();
        goodsInfo10.goods_name = "苹果醋";
        goodsInfo10.goods_unit_price = "4";
        goodsInfo10.goods_amount = "5";
        goodsInfo10.goods_price = "20";

        GoodsInfo goodsInfo11 = new GoodsInfo();
        goodsInfo11.goods_name = "这个商品名有点长有点长有点长不是一般的长";
        goodsInfo11.goods_unit_price = "500";
        goodsInfo11.goods_amount = "2";
        goodsInfo11.goods_price = "1000";

        goosInfos.add(goodsInfo0);
        goosInfos.add(goodsInfo1);
        goosInfos.add(goodsInfo2);
        goosInfos.add(goodsInfo3);
        goosInfos.add(goodsInfo4);
        goosInfos.add(goodsInfo5);
        goosInfos.add(goodsInfo6);
        goosInfos.add(goodsInfo7);
        goosInfos.add(goodsInfo8);
        goosInfos.add(goodsInfo9);
        goosInfos.add(goodsInfo10);
        goosInfos.add(goodsInfo11);

    }


}
