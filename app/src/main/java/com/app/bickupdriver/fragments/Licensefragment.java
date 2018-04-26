package com.app.bickupdriver.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.app.bickupdriver.EditProfileActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.ResetAndForgetPasswordActivity;
import com.app.bickupdriver.controller.NetworkCallBack;
import com.app.bickupdriver.controller.WebAPIManager;
import com.app.bickupdriver.model.User;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.Utils;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.app.bickupdriver.R.id.calender_image;
import static com.facebook.FacebookSdk.getApplicationContext;


public class Licensefragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, NetworkCallBack {

    public static String TAG = Licensefragment.class.getSimpleName();
    private EditText edtLicenseNumber, edtExpiryDate;
    private EditProfileActivity mActivityResponse;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private Calendar calendar;
    private ResetAndForgetPasswordActivity mActivty;
    private int REQUEST_IMAGE_CAPTURE = 111;
    private File imgFile = null;
    private File imgFileFront = null;
    private CircularProgressView circularProgressBar;
    private String message;
    private int update = 0;
    private Uri file;
    private String mCurrentPhotoPath;
    private RelativeLayout data_click;
    private CircleImageView imgBack, imgFront;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            boolean isUpdate = getArguments().getBoolean("update");
            if (isUpdate) {
                update = 1;
            }
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivty = (ResetAndForgetPasswordActivity) activity;
        } catch (Exception e) {
            mActivityResponse = (EditProfileActivity) activity;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driving_license, container, false);

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {
        Activity activity = getActivityRefrence();

        calendar = Calendar.getInstance();
        circularProgressBar = (CircularProgressView) view.findViewById(R.id.progress_view);
        mTypefaceRegular = Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);
        setUIToHideKeyBoard(view);
        TextView txtFronSide = (view.findViewById(R.id.txt_front_side));
        TextView txtBackSide = (view.findViewById(R.id.txt_back_side));
        TextView txtSteps = (view.findViewById(R.id.txt_steps_license));

        view.findViewById(calender_image).setOnClickListener(this);

        imgBack = view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgBack.setTag(false);

        imgFront = view.findViewById(R.id.img_front);
        imgFront.setOnClickListener(this);
        imgFront.setTag(false);

        txtFronSide.setTypeface(mTypefaceRegular);
        txtBackSide.setTypeface(mTypefaceRegular);
        txtSteps.setTypeface(mTypefaceRegular);

        edtLicenseNumber = (view.findViewById(R.id.edt_license_number));
        edtExpiryDate = (view.findViewById(R.id.edt_expiry_date));


        edtExpiryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              /*  String text = edtExpiryDate.getText().toString();
                int textlength = edtExpiryDate.getText().length();
                if (text.endsWith("/"))
                    return;
                if (textlength == 3||textlength==6) {
                    edtExpiryDate.setText(new StringBuilder(text).insert(text.length() - 1, "/").toString());
                    edtExpiryDate.setSelection(edtExpiryDate.getText().length());
                }*/
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button btnNext = view.findViewById(R.id.btn_next_license_number);
        btnNext.setOnClickListener(this);
        btnNext.setTypeface(mTypefaceRegular);
        if (update == 1) {
            btnNext.setText(activity.getResources().getString(R.string.txt_save));
            TextView step = view.findViewById(R.id.txt_steps_license);
            step.setVisibility(View.GONE);
        } else {
            btnNext.setText(activity.getResources().getString(R.string.txt_next));
        }

        edtLicenseNumber.setTypeface(mTypefaceRegular);
        edtExpiryDate.setTypeface(mTypefaceRegular);

        setDataToViews();
    }

    private void setDataToViews() {
        Activity activity = getActivityRefrence();
        if (update == 1) {
            if (getArguments() != null) {
                String expiryDate = getArguments().getString(ConstantValues.LICENSE_EXPIRY);
                String licenseNumber = getArguments().getString(ConstantValues.LICENSE_NUMBER_VALUE);
                String frontImage = getArguments().getString(ConstantValues.LICENSE_FRONT);
                String backImage = getArguments().getString(ConstantValues.LICENSE_BACK);


                edtLicenseNumber.setText(licenseNumber);
                edtExpiryDate.setText(CommonMethods.getInstance().getDatafromTimeStamp(expiryDate));
               /* Ion.with(imgFront)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .load(ConstantValues.BASE_URL+"/"+frontImage);
                */
                Picasso.with(activity)
                        .load(ConstantValues.BASE_URL + "/" + frontImage)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .into(imgFront);


                Picasso.with(activity)
                        .load(ConstantValues.BASE_URL + "/" + backImage)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .into(imgBack);


               /* Ion.with(imgBack)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .load(ConstantValues.BASE_URL+"/"+backImage);*/


                MyAsync myAsync = new MyAsync(1);
                myAsync.execute(ConstantValues.BASE_URL + "/" + frontImage);

                MyAsync myAsync2 = new MyAsync(2);
                myAsync2.execute(ConstantValues.BASE_URL + "/" + backImage);

            }
        }
    }

    public class MyAsync extends AsyncTask<String, Void, Bitmap> {
        int i;

        public MyAsync(int i) {
            this.i = i;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                setImagesFile(bitmap, i);
            }
        }
    }

    private void setImagesFile(Bitmap bitmap, int i) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File imgFile1 = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            imgFile1.createNewFile();
            fo = new FileOutputStream(imgFile1);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (i) {
            case 1:
                imgFileFront = imgFile1;
                break;
            case 2:
                imgFile = imgFile1;
                break;
        }

    }

    private Activity getActivityRefrence() {
        Activity activity;
        if (mActivty != null) {
            activity = mActivty;
        } else {
            activity = mActivityResponse;
        }
        return activity;
    }

    //hide keyboard onTouch outSide the EditText
    public void setUIToHideKeyBoard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Activity activity = getActivityRefrence();
                    CommonMethods.getInstance().hideSoftKeyBoard(activity);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setUIToHideKeyBoard(innerView);
            }
        }

    }


    public boolean validatefeilds() {
        Activity activity = getActivityRefrence();
        if (!CommonMethods.getInstance().validateEditFeild(edtLicenseNumber.getText().toString())) {
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_validate_licence_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!CommonMethods.getInstance().validateEditFeild(edtExpiryDate.getText().toString())) {
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_validate_expiry_date), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            String date = edtExpiryDate.getText().toString();
            boolean isdateValid = CommonMethods.getInstance().validateExpiredDate(date);
            if (!isdateValid) {
                Toast.makeText(activity, activity.getResources().getString(R.string.txt_invalid_date), Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        if (imgFileFront == null) {
            if (update != 1) {
                Toast.makeText(activity, activity.getResources().getString(R.string.txt_validate_fron_image), Toast.LENGTH_SHORT).show();
                return false;
            } else {
            }
        }
        if (imgFile == null) {
            if (update != 1) {
                Toast.makeText(activity, activity.getResources().getString(R.string.txt_validate_back_image), Toast.LENGTH_SHORT).show();
                return false;
            } else {
            }
        }
        return true;
    }

    private boolean validateMonthsAndDay(String[] splitdate) {
        try {
            int day = Integer.parseInt(splitdate[0]);
            int month = Integer.parseInt(splitdate[1]);
            if (month > 12 || day > 31) {
                return false;
            }

        } catch (ArithmeticException e) {
            return false;
        }
        return true;
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_next_license_number:
                if (validatefeilds()) {
                    String timeStamp = CommonMethods.getInstance().getTimeStamp(edtExpiryDate.getText().toString().trim());
                    prepareuserLogin(timeStamp, edtLicenseNumber.getText().toString().trim());
                    // mActivty.callFragment(ConstantValues.CIRTIFICATE);
                }
                break;
            case R.id.img_back:
                imgBack.setTag(true);
                checkCameraPermission();
                break;
            case R.id.img_front:
                imgFront.setTag(true);
                checkCameraPermission();
                break;
            case R.id.calender_image:
                Activity activity = getActivityRefrence();
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(activity.getFragmentManager(), "datePicker");
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 111: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    launch_camera();
                } else {
                }
                return;
            }
        }
    }

    public void checkCameraPermission() {
        Activity activity = getActivityRefrence();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            launch_camera();


        } else {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA},
                        111);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        111);
            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        111);
            }
        }
    }


    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "fname_" +
                String.valueOf(System.currentTimeMillis()) + ".jpg"));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
       /* Activity activity=getActivityRefrence();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
    }

    public void launch_camera() {
        Activity activity = getActivityRefrence();
        // the intent is my camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //getting uri of the file
        file = Uri.fromFile(getFile());
        //Setting the file Uri to my photo
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }


    //this method will create and return the path to the image file
    private File getFile() {
        File folder = Environment.getExternalStoragePublicDirectory("/From_camera/imagens");// the file path

        //if it doesn't exist the folder will be created
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image_file = null;
        try {
            image_file = File.createTempFile(imageFileName, ".jpg", folder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCurrentPhotoPath = image_file.getAbsolutePath();
        return image_file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Activity activity = getActivityRefrence();
            String manufacturer = android.os.Build.MANUFACTURER;
            try {
                Bitmap help1 = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), file);
                onCaptureImageResult(help1);

            } catch (Exception e) {

            }
        }
    }


    private void onCaptureImageResult(Bitmap bitmap) {
        if ((boolean) imgBack.getTag()) {
            bitmap = checkOrientation(imgBack, bitmap);
        } else {
            bitmap = checkOrientation(imgFront, bitmap);
        }
        //  Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File imgFile = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            imgFile.createNewFile();
            fo = new FileOutputStream(imgFile);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if ((boolean) imgBack.getTag()) {

            imgBack.setTag(false);
            this.imgFile = imgFile;
        } else {
            imgFront.setTag(false);
            this.imgFileFront = imgFile;
        }
    }


    public Bitmap checkOrientation(ImageView view, Bitmap bitmap) {
        ExifInterface ei = null;
        int orientation = 0;
        try {
            ei = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ei != null) {
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
        }

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        Bitmap bitmap1 = CommonMethods.getInstance().scaleDown(rotatedBitmap, 600, false);
        view.setImageBitmap(bitmap1);
        return bitmap1;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
        return bitmap;
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        String dateString = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear + 1) + "/" + String.valueOf(year);
        edtExpiryDate.setText(dateString);
    }


    public void prepareuserLogin(final String date, final String licenseNumber) {
        String createUserUrl = WebAPIManager.getInstance().getLicenseFragmentUrl();
        final JsonObject requestBody = new JsonObject();
        callAPI(requestBody, createUserUrl, this, 60 * 1000, 100, date, licenseNumber);
    }


    private void callAPI(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode, String date, String licensenumber) {
        circularProgressBar.setVisibility(View.VISIBLE);
        if (imgFile != null) {
            Ion.with(this)
                    .load(createUserUrl)
                    .setHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken())
                    .setMultipartFile("frontSide", imgFileFront)
                    .setMultipartFile("backSide", imgFile)
                    .setMultipartParameter("license_expiry_date", date)
                    .setMultipartParameter("license_number", licensenumber)
                    .setMultipartParameter("is_update", String.valueOf(update))
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            circularProgressBar.setVisibility(View.GONE);
                            if (e != null) {
                                Utils.printLogs(TAG, "Exception Driving Licence Fragment : " + e.getMessage());
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int status = result.getHeaders().code();
                            JsonObject resultObject = result.getResult();
                            String value = String.valueOf(resultObject);
                            try {
                                JSONObject jsonObject = new JSONObject(value);
                                message = jsonObject.getString("message");
                            } catch (JSONException e1) {
                                Utils.printLogs(TAG, "Exception Driving Licence Fragment : " + e1.getMessage());
                                e1.printStackTrace();
                            }
                            switch (status) {
                                case 422:
                                case 400:
                                case 500:
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    break;
                                case 200:
                                case 201:
                                    loginActivity.onSuccess(resultObject, requestCode, status);
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }


    @Override
    public void onSuccess(JsonObject data, int requestCode, int statusCode) {
        switch (requestCode) {
            case 100:
                ParseuserLoginResponse parseuserLoginResponse = new ParseuserLoginResponse();
                parseuserLoginResponse.execute(String.valueOf(data));
                break;
        }
    }

    @Override
    public void onError(String msg) {
        Utils.printLogs(TAG, "Some Error , onError : " + message);
    }

    class ParseuserLoginResponse extends AsyncTask<String, Void, HashMap<String, String>> {

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String email, accessToken, phoneNumber, userId, message, flag = "0";
            HashMap<String, String> map = new HashMap<>();
            String response = strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                message = jsonObject.getString("message");
                flag = jsonObject.getString("flag");
                map.put("flag", flag);
                map.put("message", message);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            String flag = hashMap.get("flag");
            String message = hashMap.get("message");
            if (mActivityResponse != null) {
                Toast.makeText(mActivityResponse, message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivty, message, Toast.LENGTH_SHORT).show();
            }

            if (update != 0) {
                ViewProfileDriverFragment viewProfileDriverFragment = new ViewProfileDriverFragment();
                callFragment(viewProfileDriverFragment);
            } else {
                RegistrationCirtificateFragment registrationCirtificateFragment = new RegistrationCirtificateFragment();
                callFragment(registrationCirtificateFragment);
            }
        }
    }

    private void callFragment(Fragment viewProfileDriverFragment) {
        if (mActivty != null) {
            mActivty.callFragment(viewProfileDriverFragment);
        } else {
            mActivityResponse.callFragment(viewProfileDriverFragment);
        }
    }


}
