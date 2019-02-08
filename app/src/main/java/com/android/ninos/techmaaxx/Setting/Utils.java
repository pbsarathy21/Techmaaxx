package com.android.ninos.techmaaxx.Setting;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;


import com.android.ninos.techmaaxx.R;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utils {

    StringBuffer test = new StringBuffer();

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

   /* public static int getTabsHeight(Context context) {
        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
    }*/



    public static String BitMapToString(final Bitmap bitmap) {
        String temp="";
        ExecutorService service= Executors.newFixedThreadPool(1);
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    String temp = Base64.encodeToString(b, Base64.DEFAULT);
                    Log.e("IMAGE", temp);
                } catch (Exception e) {
                }
            }
        });
        service.shutdown();

        return temp;


    }

    public static final String sand_box_id = "APP-80W284485P519543T";
    public static final String paypal_liv_id = "APP-4PF21121TJ685590H";

    public static final String paypal_sdk_id = "AeaML0qOLHEh4yXN9SHYKeTMCt5ooMKqn9-Mnqp5SiyQrcdsQ4_QONzcs9RoLdqRK6IHi2XH2FlJ1zrj";


    public static Bitmap StringToBitMap(String encodedString) {

        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.NO_WRAP);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;

        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }

    public static String GetPhotoString(String photo) {

        String PhotoString = photo.replaceAll("\\\\n", "\n").replaceAll("\\\\u0026", "&").replaceAll("\\\\u003e", ">").replaceAll("\\\\u003c", "<").replaceAll("\\\\u0027", "'").replaceAll("\\\\u003d", "=");
        Log.e("Response", PhotoString);
        return PhotoString;
    }
    public long getFileSize(Context context, Uri uri)
    {
        long filesize=0;
        Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        filesize=returnCursor.getLong(sizeIndex);
        Log.e("fileName",returnCursor.getString(nameIndex));
        Log.e("fileSize",""+filesize);
        return filesize;
    }
}
