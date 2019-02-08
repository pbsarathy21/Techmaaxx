package com.android.ninos.techmaaxx.Setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.ninos.techmaaxx.Countable.CountableActivity;
import com.android.ninos.techmaaxx.Destination_Adapter;
import com.android.ninos.techmaaxx.Login.LoginActivity;
import com.android.ninos.techmaaxx.NavigationActivity;
import com.android.ninos.techmaaxx.R;
import com.android.ninos.techmaaxx.SplashActivity;
import com.android.ninos.techmaaxx.Utils.Consts;
import com.android.ninos.techmaaxx.Utils.CustomEditTextviewRegular;
import com.android.ninos.techmaaxx.Utils.CustomTextViewBold;
import com.android.ninos.techmaaxx.Utils.CustomTextviewRegular;
import com.android.ninos.techmaaxx.Utils.CustomTextviewTitle;
import com.android.ninos.techmaaxx.Utils.MarshMallowPermission;
import com.android.ninos.techmaaxx.Utils.Util;
import com.android.ninos.techmaaxx.Utils.Validation;
import com.android.ninos.techmaaxx.session.Session;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SettingActivity extends AppCompatActivity {

    boolean currency = false;

    Validation validation;

    String Company, Add_1, Add_2;
    boolean com, add1, add2;

    CustomEditTextviewRegular edit_company, edit_address1, edit_address2, edit_power, edit_com_do;
    TextInputLayout text_company, text_add1, text_address2, text_power, text_com_do;
    CustomTextviewRegular currency_value;

    private Vibrator vib;
    Animation animShake;

    LinearLayout linear_save, linear_Logo, linear_cfa, linear_usd, linear_ghc;
    ImageView close_icon;
    Activity activity;
    Context context;
    Session session;


    //Camera Function
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    Uri fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static final int IMG_REQ = 1, REQUEST_PERMISSION_REQ_CODE = 34, GALLERY_CODE = 201;
    public static int media_Type = 1;

    // public static final String TAG = License_BackActivity.class.getSimpleName();

    int GALLERY_INTENT_CALLED = 33;
    int GALLERY_KITKAT_INTENT_CALLED = 32;
    ProgressDialog pDialog;
    Bitmap thumbnail = null;
    boolean isCamera = false;
    ImageView cl_icon;
    boolean insurance_photo, certificate_photo, Permit_photo;
    String photo = "";

    //PopUp Image
    public LayoutInflater inflater = null;
    public View layout = null;
    public RelativeLayout.LayoutParams params = null;
    //Save Uri
    Uri saveUri = null;
    private PopupWindow pwimg;
    ImageView imageView, img_logo;
    String mCurrentPhotoPath;
    //Crop the Image
    final int PIC_CROP = 3;
    Bitmap theCropPic = null;
    MarshMallowPermission marshMallowPermission = null;
    View li, ak;
    String Power, Com_do;
    boolean power, com_do;
    CustomTextviewTitle text_f;
    Bitmap bit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        activity = SettingActivity.this;
        context = SettingActivity.this;
        validation = new Validation();
        session = new Session(this);

        initViews();

        Log.e("img_path", "Img_path===>" + Consts.img_bit);


        if (Consts.Setting.equalsIgnoreCase("true")) {
            text_power.setVisibility(View.VISIBLE);
            li.setVisibility(View.VISIBLE);
            text_f.setVisibility(View.VISIBLE);
            text_com_do.setVisibility(View.GONE);
            ak.setVisibility(View.GONE);


        } else {


        }


        if (session.getImg_log().equalsIgnoreCase("")) {


        } else {


            Util.showProgress(activity);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    String inn = session.getImg_log();

                    StringToBitMap(inn);


                    Util.hideProgress();
                }


            }, SPLASH_DISPLAY_LENGTH);


        }

        edit_company.setText(session.getCompany_Name());
        edit_address1.setText(session.getCompany_add1());
        edit_address2.setText(session.getCompany_add2());
        currency_value.setText(session.getCurrency_Symbol());
        edit_com_do.setText(session.getCompany_domain());
        edit_power.setText(session.getPower_text());
        Consts.Domain_url = session.getCompany_domain();


        //cameraCaptureImage();

        marshMallowPermission = new MarshMallowPermission(SettingActivity.this);
        //PopImage or rotation for Image
        // We need to get the instance of the LayoutInflater for popImage
        inflater = (LayoutInflater) SettingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.activity_image_rotate, (ViewGroup) findViewById(R.id.popup));
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        imageView = (ImageView) layout.findViewById(R.id.image1);


        linear_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Company = edit_company.getText().toString();
                Add_1 = edit_address1.getText().toString();
                Add_2 = edit_address2.getText().toString();
                Power = edit_power.getText().toString();
                Com_do = edit_com_do.getText().toString();
                Consts.Domain_url = Com_do;
                session.setPower_text(Power);


                if (!validation.isEmpty(Company)) {
                    com = true;
                    text_company.setError(null);

                } else {
                    com = false;
                    text_company.setError("Enter Your Company Name!");
                    text_company.setAnimation(animShake);
                    text_company.startAnimation(animShake);
                    vib.vibrate(120);
                }

                if (!validation.isEmpty(Com_do)) {
                    com_do = true;
                    text_com_do.setError(null);

                } else {
                    com_do = false;
                    text_com_do.setError("Enter Your Company Domain!");
                    text_com_do.setAnimation(animShake);
                    text_com_do.startAnimation(animShake);
                    vib.vibrate(120);
                }

                if (!validation.isEmpty(Add_1)) {
                    add1 = true;
                    text_add1.setError(null);

                } else {
                    add1 = false;
                    text_add1.setError("Enter Your Company Address!");
                    text_add1.setAnimation(animShake);
                    text_add1.startAnimation(animShake);
                    vib.vibrate(120);
                }


                if (!validation.isEmpty(Add_2)) {
                    add2 = true;
                    text_address2.setError(null);

                } else {
                    add2 = false;
                    text_address2.setError("Enter Your Company Address Line2!");
                    text_address2.setAnimation(animShake);
                    text_address2.startAnimation(animShake);
                    vib.vibrate(120);
                }


                if (com && add1 && add2 && com_do) {

                    if (!session.getCurrency_Symbol().equalsIgnoreCase("")) {

                        session.setCompany_Name(Company);
                        session.setCompany_add1(Add_1);
                        session.setCompany_add2(Add_2);
                        session.setCompany_domain(Com_do);


                        if (Consts.Setting.equalsIgnoreCase("true")) {
                            Consts.Setting = "false";
                            Success_dialog();
                            // finish();

                        } else {
                            //Success_dialog();
                            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.enter, R.anim.exit);


                        }


                    } else {
                        Toast.makeText(SettingActivity.this, "Choose the Currency Symbol", Toast.LENGTH_SHORT).show();
                    }


                } else {


                }
            }
        });

        currency_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Destination_Dialog();
            }
        });


        linear_Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        cl_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_logo.invalidate();
                img_logo.setImageBitmap(null);
                session.setImg_log("");
                cl_icon.setVisibility(View.GONE);
                Toast.makeText(SettingActivity.this, "Logo Removed successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            bit = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);


            img_logo = (ImageView) findViewById(R.id.img_logo);

            img_logo.setImageBitmap(bit);
            cl_icon.setVisibility(View.VISIBLE);
            return bit;


        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void Success_dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);

        builder.setTitle("Success");
        builder.setMessage("");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                dialog.dismiss();
                finish();


            }
        });

       /* builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });*/

        AlertDialog alert = builder.create();
        alert.show();
    }


    //Select the Image
    private void selectImage() {

        final CharSequence[] items = {"Choose from Library",
                "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Choose from Library")) {

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, GALLERY_CODE);


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    //finish();
                }
            }
        });
        builder.show();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {


            super.onActivityResult(requestCode, resultCode, data);
            Log.e("Activity Result Blck", "Activity Result Blck");
            Log.e("saveUri Data", "SaveUri Data" + data);


            if (requestCode == IMG_REQ && resultCode == RESULT_OK) {
                // Show the thumbnail on ImageView
                Uri imageUri = Uri.parse(mCurrentPhotoPath);
                File file = new File(imageUri.getPath());
                try {
                    InputStream ims = new FileInputStream(file);
                    //ivPreview.setImageBitmap(BitmapFactory.decodeStream(ims));
                    Bitmap bitmap = BitmapFactory.decodeStream(ims);


                    //Crop the Data
                    saveUri = fileUri;

                    //This
                    // is the Original Code
                    thumbnail = bitmap;
                    // PopupImage();
                } catch (FileNotFoundException e) {
                    return;
                }

                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(SettingActivity.this,
                        new String[]{imageUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } else if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != data) {

                Log.e("GALLERY_CODE Blck", "GALLERY_CODE blck");
                saveUri = data.getData();
                System.out.println("Gallery Image URI : " + saveUri);

                if (saveUri != null) {
                    Log.e("saveUri Blck ", "saveUri Blck ");
                    isCamera = false;


                    PopupImage();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void PopupImage() {
        try {
            Log.e("PopupImage Blck 1", "PopupImage Blck 1");
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = (100);
            int width = (100);


            params.setMargins(0, 0, 0, 0);
            layout.setLayoutParams(params);

            pwimg = new PopupWindow(layout, width, height, true);
            pwimg.showAtLocation(layout, Gravity.CENTER, 0, 0);

            if (!isCamera) {

                //For Gallery Image Processing
                Log.e("Bfr UriToBitmap", "Bfr UriToBitmap");
                new SettingActivity.UriToBitmap().execute();
                Log.e("Afr UriToBitmap", "Afr UriToBitmap");
                isCamera = true;

                saveOutput(thumbnail);

                Util.showProgress(activity);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        File imgFile = new File(String.valueOf(thumbnail));

                        if (imgFile.exists()) {

                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


                            // img_logo = (ImageView) findViewById(R.id.img_logo);
                            //img_logo.setImageBitmap(myBitmap);

                            // Consts.img_bit = myBitmap;


                            //String IMG = String.valueOf(Consts.img_bit);

                            //ession.setImg_log(IMG);


                        }
                        BitMapToString(thumbnail);
                        img_logo.setImageBitmap(thumbnail);
                        cl_icon.setVisibility(View.VISIBLE);
                        //   Consts.img_bit = thumbnail;
                        Util.hideProgress();


                 /*       String IMG = String.valueOf(thumbnail);
                        Util.hideProgress();


                        Log.e("img", "img_Session====>" + IMG);
                        session.setImg_log(IMG);
*/
                    }


                }, SPLASH_DISPLAY_LENGTH);


            } else {
                //Camera Image Processing
                Log.e("thumbnail blck", "thumbnail blck");
                imageView.setImageBitmap(thumbnail);
                Log.e("thumbnail", "thumbnail" + thumbnail);
                saveUri = getImageUriForCrop(SettingActivity.this, thumbnail);

                Log.e("End thumbnail blck", "End thumbnail blck");
            }

/*
            layout.findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("Done Blck", "Done Blck");

                   *//* if (Consts.photo.equals("insurance")) {
                        Photo1.setVisibility(View.VISIBLE);
                        Cam1.setVisibility(View.GONE);
                        Consts.tick1 = "true";
                    }
*//*
                    //Toast.makeText(HomeActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    saveOutput(thumbnail);

                }
            });*/


            layout.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        if (pwimg.isShowing())
                            pwimg.dismiss();


                    }
                    return false;
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        session.setImg_log(temp);
        return temp;

    }

    private Bitmap rotateImage(int rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        return Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);

    }


    private void saveOutput(Bitmap croppedImage) {

        try {
            Log.e("Final SaveOutput Blck", "Final SaveOutput Blck");
            //profileMenteeImg_ImgView.setImageBitmap(croppedImage);
            imageView.setImageBitmap(thumbnail);
            BitmapToString(croppedImage);

        } catch (Exception e) {
        }


        pwimg.dismiss();
    }

    public void BitmapToString(final Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();


            // Consts.Profile_photo = Base64.encodeToString(b, Base64.DEFAULT);
            // Log.e("Profile_photo", "Profile_photo---->" + Consts.Profile_photo);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class UriToBitmap extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            Log.e("onPreExecute", "onPreExecute uritoBitmap");
            pDialog = new ProgressDialog(SettingActivity.this);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            Log.e("doInBackground", "doInBackground uritoBitmap");
            try {

                InputStream in = null;

                try {
                    Utils utils = new Utils();
                    long IMAGE_MAX_SIZE = 512000;
                    long fileSize = 200000;
                    if (!isCamera) {
                        fileSize = utils.getFileSize(SettingActivity.this, saveUri);
                        System.out.println("FileSize" + fileSize);
                        isCamera = true;
                    }
                    if (fileSize <= 102400) {
                        IMAGE_MAX_SIZE = fileSize;//below 100kb
                        thumbnail = MediaStore.Images.Media.getBitmap(SettingActivity.this.getContentResolver(), saveUri);

                        System.out.println("Thumbail" + thumbnail);
                        Log.e("thumbnail", "thumbnail" + thumbnail);
                    } else {
                        IMAGE_MAX_SIZE = 512000;//500kb
                        in = SettingActivity.this.getContentResolver().openInputStream(saveUri);
                        // Decode image size
                        BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(in, null, o);
                        in.close();
                        int scale = 1;
                        while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
                            scale++;
                        }
                        Log.e("", "scale = " + scale + ", orig-width: " + o.outWidth + ",orig-height: " + o.outHeight);

                        thumbnail = null;
                        in = SettingActivity.this.getContentResolver().openInputStream(saveUri);
                        if (scale > 1) {
                            scale--;
                            // scale to max possible inSampleSize that still yields an image
                            // larger than target
                            o = new BitmapFactory.Options();
                            o.inSampleSize = scale;
                            thumbnail = BitmapFactory.decodeStream(in, null, o);

                            Log.e("thumbnail 500kb", "thumbnail 500kb" + thumbnail);

                            // resize to desired dimensions
                            int height = thumbnail.getHeight();
                            int width = thumbnail.getWidth();
                            Log.d("", "1th scale operation dimenions - width: " + width + ",height: " + height);

                            double y = Math.sqrt(IMAGE_MAX_SIZE
                                    / (((double) width) / height));
                            double x = (y / height) * width;

                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(thumbnail, (int) x, (int) y, true);
                            //Bitmap scaledBitmap = Bitmap.createScaledBitmap(thumbnail, width,height, true);
                            if (!thumbnail.isRecycled())
                                thumbnail.recycle();
                            thumbnail = scaledBitmap;

                            System.gc();
                        } else {
                            thumbnail = BitmapFactory.decodeStream(in);
                        }


                        in.close();

                        Log.e("", "bitmap size - width: " + thumbnail.getWidth() + ", height: " + thumbnail.getHeight());
                    }


                } catch (IOException e) {
                    Log.e("", e.getMessage(), e);
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            Log.e("OnPostExe", "OnPostExe");
            imageView.setImageBitmap(thumbnail);


        }

    }


    public Uri getImageUriForCrop(Context inContext, Bitmap inImage) {

        Log.e("ImageUriCrop blck", "ImageUriCrop Blck");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.e("uriCrop", "uriCrop" + path);
        return Uri.parse(path);
    }


    public void initViews() {

        session = new Session(context);
        text_f = (CustomTextviewTitle) findViewById(R.id.text_f);

        edit_company = (CustomEditTextviewRegular) findViewById(R.id.edit_company);
        edit_address1 = (CustomEditTextviewRegular) findViewById(R.id.edit_address1);
        edit_address2 = (CustomEditTextviewRegular) findViewById(R.id.edit_address2);
        edit_power = (CustomEditTextviewRegular) findViewById(R.id.edit_power);
        li = (View) findViewById(R.id.li);
        ak = (View) findViewById(R.id.ak);

        text_company = (TextInputLayout) findViewById(R.id.text_company);
        text_add1 = (TextInputLayout) findViewById(R.id.text_add1);
        text_address2 = (TextInputLayout) findViewById(R.id.text_address2);
        text_power = (TextInputLayout) findViewById(R.id.text_power);
        text_com_do = (TextInputLayout) findViewById(R.id.text_com_do);
        edit_com_do = (CustomEditTextviewRegular) findViewById(R.id.edit_com_do);

        currency_value = (CustomTextviewRegular) findViewById(R.id.currency_value);
        cl_icon = (ImageView) findViewById(R.id.cl_icon);


        linear_save = (LinearLayout) findViewById(R.id.linear_save);
        linear_Logo = (LinearLayout) findViewById(R.id.linear_Logo);

        img_logo = (ImageView) findViewById(R.id.img_logo);
        animShake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    public void Destination_Dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        LayoutInflater inflater = LayoutInflater.from(SettingActivity.this);
        // builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        View content = inflater.inflate(R.layout.currency_dailog, null);
        builder.setView(content);


        close_icon = (ImageView) content.findViewById(R.id.close_icon);
        linear_cfa = (LinearLayout) content.findViewById(R.id.linear_cfa);
        linear_usd = (LinearLayout) content.findViewById(R.id.linear_usd);
        linear_ghc = (LinearLayout) content.findViewById(R.id.linear_ghc);


        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        // alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.card_terms));
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        // alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_language;


        //GET THE SELECTED DATA..

        //  getSelectedData();


        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        linear_ghc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                session.setCurrency_Symbol("GHC");
                currency_value.setText(session.getCurrency_Symbol());


            }
        });

        linear_usd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
                session.setCurrency_Symbol("\u0024");
                currency_value.setText(session.getCurrency_Symbol());
            }
        });

        linear_cfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                session.setCurrency_Symbol("CFA");
                currency_value.setText(session.getCurrency_Symbol());


            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            if (Consts.Setting.equalsIgnoreCase("true")) {
                Consts.Setting = "false";
                finish();

            } else {
                finish();
            }


        }
        return true;
    }
}
