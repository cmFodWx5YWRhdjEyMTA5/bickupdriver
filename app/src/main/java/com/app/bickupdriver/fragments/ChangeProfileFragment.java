package com.app.bickupdriver.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickupdriver.activity.EditProfileActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.activity.ResetAndForgetPasswordActivity;
import com.app.bickupdriver.controller.NetworkCallBack;
import com.app.bickupdriver.controller.WebAPIManager;
import com.app.bickupdriver.interfaces.UpdateUser;
import com.app.bickupdriver.model.User;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.Image;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.CountryPickerListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ChangeProfileFragment extends Fragment implements View.OnClickListener,NetworkCallBack,UpdateUser {
    public static String TAG=ChangeProfileFragment.class.getSimpleName();
    private ResetAndForgetPasswordActivity activity;
    private EditProfileActivity mActivityresponse;
    private View view;
    private CircularProgressView circularProgressBar;
    private String message;
    private String countryCode;
    private TextView edtCoutryCode;
    private EditText txtFullName;
    private EditText txtMobile;
    private EditText txtEmail;
    private EditText txtBankName;
    private EditText txtAccountNumber;
    private File imgFile=null;
    private ImageView userImage;
    private EditText txtConfirmAccountNumber;
    private Uri file;
    private String mCurrentPhotoPath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_change_profile, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View view) {
        Activity activity=getActivityRefrence();
        final EditText edtFullName,edtMobile,edtEmail,edtBankName,edtAccountNumber,edtConfirmAccount;
        TextView txtDrivingLicence,txtlicenceExpiry,txtRegistration,txtRegistrationExpiry,txtInsurence,txtInsurenceExpiry,txtTrade,txtTradeExpiry;
        circularProgressBar=view.findViewById(R.id.progress_view);

        Typeface mTypefaceRegular= Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        Typeface  mTypefaceBold=Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);
        userImage=view.findViewById(R.id.userImage);
        userImage.setOnClickListener(this);
        txtFullName=view.findViewById(R.id.edt_Value_full_name);
        txtMobile=view.findViewById(R.id.edt_mobile_number_signup);
        txtEmail=view.findViewById(R.id.edt_Value_email_id);
        txtBankName=view.findViewById(R.id.edt_Value_bank_name);
        txtAccountNumber=view.findViewById(R.id.edt_Value_account_number);
        txtConfirmAccountNumber=view.findViewById(R.id.edt_Value_confirm_account_number);

        edtCoutryCode=view.findViewById(R.id.edt_country_code);
        edtCoutryCode.setOnClickListener(this);

        txtDrivingLicence=view.findViewById(R.id.txt_driving_license);

        txtDrivingLicence.setTypeface(mTypefaceRegular);


        txtRegistration=view.findViewById(R.id.txt_driving_license);

        txtRegistration.setTypeface(mTypefaceRegular);


        txtInsurence=view.findViewById(R.id.txt_driving_license);

        txtInsurence.setTypeface(mTypefaceRegular);


        txtTrade=view.findViewById(R.id.txt_driving_license);

        txtTrade.setTypeface(mTypefaceRegular);



        edtFullName=view.findViewById(R.id.edt_Value_full_name);
        edtFullName.setTypeface(mTypefaceRegular);



        edtMobile=view.findViewById(R.id.edt_mobile_number_signup);
        edtMobile.setTypeface(mTypefaceRegular);



        edtEmail=view.findViewById(R.id.edt_Value_email_id);
        edtEmail.setTypeface(mTypefaceRegular);



        edtBankName=view.findViewById(R.id.edt_Value_bank_name);
        edtBankName.setTypeface(mTypefaceRegular);



        edtAccountNumber=view.findViewById(R.id.edt_Value_account_number);
        edtAccountNumber.setTypeface(mTypefaceRegular);



        edtConfirmAccount=view.findViewById(R.id.edt_Value_confirm_account_number);
        edtConfirmAccount.setTypeface(mTypefaceRegular);




    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareDriverDetails(view);
    }
    public void  prepareDriverDetails(View view) {
        String createUserUrl= WebAPIManager.getInstance().getUserDetailsUrl();
        final JsonObject requestBody=new JsonObject();
        callAPI(requestBody,createUserUrl,this,60*1000,100,view);
    }

    private void callAPI(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode, View view) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Ion.with(this)
                .load(createUserUrl)
                .setHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken())
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, Response<JsonObject> result) {
                        circularProgressBar.setVisibility(View.GONE);
                        if (e != null) {
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

    public void  prepareupdateProfile(String firstName,String lastName,String email,String bankName,String AccountNumber) {
        String createUserUrl= WebAPIManager.getInstance().getUpdateUser();
        final JsonObject requestBody=new JsonObject();
        requestBody.addProperty(ConstantValues.USER_FIRSTNAME,firstName);
        requestBody.addProperty(ConstantValues.USER_LASTNAME,lastName);
        requestBody.addProperty(ConstantValues.USER_EMAILADDRESS,email);
        requestBody.addProperty(ConstantValues.BANK_NAME,bankName);
        requestBody.addProperty(ConstantValues.ACCOUNT_NUMBER,AccountNumber);
        upDateProfile(requestBody,createUserUrl,this,60*1000,101,view,firstName,lastName,email,bankName,AccountNumber);
    }

    private void upDateProfile(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode, View view, String firstName, String lastName, String email, String bankName, String accountNumber) {
        circularProgressBar.setVisibility(View.VISIBLE);
        String accessToken=User.getInstance().getAccesstoken();
        if(imgFile!=null) {
            Ion.with(this)
                    .load("PUT",createUserUrl)
                    .setHeader(ConstantValues.USER_ACCESS_TOKEN,User.getInstance().getAccesstoken())
                    .setMultipartFile(ConstantValues.USER_IMAGE,imgFile)
                    .setMultipartParameter(ConstantValues.USER_FIRSTNAME,firstName)
                    .setMultipartParameter(ConstantValues.USER_LASTNAME,lastName)
                    .setMultipartParameter(ConstantValues.USER_EMAILADDRESS,email)
                    .setMultipartParameter(ConstantValues.BANK_NAME,bankName)
                    .setMultipartParameter(ConstantValues.ACCOUNT_NUMBER,accountNumber)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            circularProgressBar.setVisibility(View.GONE);
                            if(e!=null){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int status = result.getHeaders().code();
                            JsonObject resultObject = result.getResult();
                            String value=String.valueOf(resultObject);
                            try {
                                JSONObject jsonObject=new JSONObject(value);
                                message = jsonObject.getString("message");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            switch (status){
                                case 422:
                                case 400:
                                case 500:
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    break;
                                case 200:
                                case 202:
                                    loginActivity.onSuccess(resultObject,requestCode,status);
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }else {
            Ion.with(this)
                    .load("PUT",createUserUrl)
                    .setHeader(ConstantValues.USER_ACCESS_TOKEN,User.getInstance().getAccesstoken())
                    .setMultipartParameter(ConstantValues.USER_FIRSTNAME,firstName)
                    .setMultipartParameter(ConstantValues.USER_LASTNAME,lastName)
                    .setMultipartParameter(ConstantValues.USER_EMAILADDRESS,email)
                    .setMultipartParameter(ConstantValues.BANK_NAME,bankName)
                    .setMultipartParameter(ConstantValues.ACCOUNT_NUMBER,accountNumber)
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            circularProgressBar.setVisibility(View.GONE);
                            if(e!=null){
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.txt_Netork_error), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            int status = result.getHeaders().code();
                            JsonObject resultObject = result.getResult();
                            String value=String.valueOf(resultObject);
                            try {
                                JSONObject jsonObject=new JSONObject(value);
                                message = jsonObject.getString("message");
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            switch (status){
                                case 422:
                                case 400:
                                case 500:
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    break;
                                case 200:
                                case 202:
                                    loginActivity.onSuccess(resultObject,requestCode,status);
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.activity=(ResetAndForgetPasswordActivity)activity;

        }catch (Exception e){
            this.mActivityresponse=(EditProfileActivity)activity;
        }

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        Fragment fragment;
        Bundle bundle=new Bundle();
        switch (id){
            case R.id.edt_country_code:
                openCountryCodeDialog();
                break;
            case R.id.userImage:
                checkForPermissions();
                //checkCameraPermission();
                break;


        }
    }

    private boolean validateFields() {
        Activity mActivity=getActivityRefrence();
        if(!CommonMethods.getInstance().validateEditFeild(txtFullName.getText().toString())){
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.txt_validate_full_name), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!CommonMethods.getInstance().validateMobileNumber(txtMobile.getText().toString(),6)){
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.txt_vaidate_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!CommonMethods.getInstance().validateEmailAddress(txtEmail.getText().toString())){
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.txt_vaidate_emailID), Toast.LENGTH_SHORT).show();
            return false;
        }

          String confirm=txtConfirmAccountNumber.getText().toString().trim();
            String accountnumber=txtAccountNumber.getText().toString().trim();
        if(confirm!=null&&accountnumber!=null) {
            if (!confirm.equalsIgnoreCase(accountnumber)) {
                Toast.makeText(mActivity, mActivity.getResources().getString(R.string.txt_vaidate_account_number_confirm_account_number), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private Activity getActivityRefrence() {
        Activity mActivity;
        if(activity!=null){
            mActivity=activity;
        }else {
            mActivity=mActivityresponse;
        }
        return mActivity;
    }


    private void callFragment(Fragment licenseNumber,String header) {
        if(activity!=null){
            ResetAndForgetPasswordActivity.imgBack.setVisibility(View.VISIBLE);

            activity.callFragment(licenseNumber);
        }else {
            mActivityresponse.tv_header.setText(header);
           mActivityresponse.callFragment(licenseNumber);
        }
    }



    public  void  openCountryCodeDialog(){
        final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                picker.dismiss();
                countryCode=dialCode;
                edtCoutryCode.setText(dialCode);
            }
        });
        if(mActivityresponse!=null)
        picker.show(mActivityresponse.getSupportFragmentManager(), "COUNTRY_PICKER");
        else
            picker.show(activity.getSupportFragmentManager(), "COUNTRY_PICKER");
    }

    public void setUIToHideKeyBoard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    Activity activity=getActivityRefrence();
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

    private void checkForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(getActivityRefrence(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivityRefrence(),
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED){
            Log.e(TAG, "Permission Granted");
            this.selectImage();
        } else {

            requestPermissions(new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA},
                    67);
            Log.e(TAG, "Permission NOT Granted, Requesting for the same");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 67:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "onRequestPermissionsResult : Permission Granted");
                    selectImage();
                } else
                    Log.e(TAG, "onRequestPermissionsResult : Permission NOT Granted");
                break;

        }
    }


    public void checkCameraPermission(){
        Activity mActivityReference=getActivityRefrence();
        if (ContextCompat.checkSelfPermission(mActivityReference, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mActivityReference,
                        Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
            selectImage();
        }
        else {
            if(ContextCompat.checkSelfPermission(mActivityReference,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(mActivityReference,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        111);
            }
            if(ContextCompat.checkSelfPermission(mActivityReference,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(mActivityReference,
                        new String[]{Manifest.permission.CAMERA},
                        113);
            }
        }
    }

    public void selectImage() {
        Activity mActivityReference=getActivityRefrence();
        final Dialog selectImageDailog = new Dialog(mActivityReference);
        selectImageDailog.setContentView(R.layout.edit_image_dialog);
        selectImageDailog.setCancelable(true);
        selectImageDailog.setCanceledOnTouchOutside(false);
        LinearLayout camera = (LinearLayout) selectImageDailog.findViewById(R.id.camera);
        final LinearLayout gallery = (LinearLayout) selectImageDailog.findViewById(R.id.gallery);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectImageDailog.dismiss();
                launch_camera();

            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 2);
                selectImageDailog.dismiss();
            }
        });

        selectImageDailog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Activity mActivityReference=getActivityRefrence();
        if (resultCode == mActivityReference.RESULT_OK) {
            if (requestCode == 2) {
                Uri picUri = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                try {
                    Cursor c = mActivityReference.getContentResolver().query(picUri, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    String picturePath = c.getString(columnIndex);
                    c.close();
                    if (picturePath == null) {
                        picturePath = Image.getPath(mActivityReference, picUri);

                        imgFile = new File(picturePath);
                        final int THUMBSIZE = 200;
                        Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(picturePath),
                                THUMBSIZE, THUMBSIZE);
                        bitmap = MediaStore.Images.Media.getBitmap(mActivityReference.getContentResolver(), picUri);
                        userImage.setImageBitmap(bitmap);
                    } else {
//                        picturePath = Image.getPath(activity, picUri);
                        imgFile = new File(picturePath);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivityReference.getContentResolver(), picUri);
                        final int THUMBSIZE = 200;
                        bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(picturePath),
                                THUMBSIZE, THUMBSIZE);
                        userImage.setImageBitmap(bitmap);
                    }


                }catch (Exception e){}}
            else if (requestCode == 0) {
                try {
                    /*try {
                        Bitmap profileImageBitmap;
                        Uri uri = data.getData();
                        profileImageBitmap = MediaStore.Images.Media.getBitmap(
                                getActivityRefrence().getContentResolver(), uri);

                        userImage.setImageBitmap(profileImageBitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                    Bitmap help1 = MediaStore.Images.Media.getBitmap(
                            getActivityRefrence().getContentResolver(), file);
                    Log.e("", "" + help1);
                    onCaptureImageResult(help1);
                    userImage.setImageBitmap(help1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void launch_camera() {
        // the intent is my camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //getting uri of the file
        file = Uri.fromFile(getFile());
        //Setting the file Uri to my photo
        intent.putExtra(MediaStore.EXTRA_OUTPUT,file);
        if(activity != null) {
        if(intent.resolveActivity(activity.getPackageManager())!=null) {
                startActivityForResult(intent, 0);
            }
        }
        else {
            startActivityForResult(intent, 0);
        }
/*
        Intent chooseImageIntent = Utils.getPickImageIntent(getActivityRefrence(),
                "tixus_temp_profile.png");
        startActivityForResult(chooseImageIntent, 0);*/
    }


    //this method will create and return the path to the image file
    private File getFile() {
        File folder = Environment.getExternalStoragePublicDirectory("/From_camera/imagens");// the file path

        //if it doesn't exist the folder will be created
        if(!folder.exists())
        {
            folder.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+ timeStamp + "_";
        File image_file = null;
        try {
            image_file = File.createTempFile(imageFileName,".jpg",folder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCurrentPhotoPath = image_file.getAbsolutePath();
        return image_file;
    }


    public  Bitmap checkOrientation(ImageView view, Bitmap bitmap)  {
        ExifInterface ei = null;
        int orientation=0;
        try {
            ei = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(ei!=null) {
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
        }

        Bitmap rotatedBitmap = null;
        switch(orientation) {

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
        Bitmap bitmap1=CommonMethods.getInstance().scaleDown(
                rotatedBitmap,600,false);
        view.setImageBitmap(bitmap1);
        return bitmap1;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void onCaptureImageResult(Bitmap bitmap) {
        bitmap=checkOrientation(userImage,bitmap);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        imgFile = new File(Environment.getExternalStorageDirectory(),
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

    }


    @Override
    public void onSuccess(JsonObject data, int requestCode, int statusCode) {
        switch (requestCode){
            case 100:
                ParseDriverDetails parseuserLoginResponse=new ParseDriverDetails();
                parseuserLoginResponse.execute(String.valueOf(data));
                break;
            case 101:
                ParseUpdateuserDetails parseUpdateuserDetails=new ParseUpdateuserDetails();
                parseUpdateuserDetails.execute(String.valueOf(data));
                break;
        }
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void updateUser() {
        if(validateFields()) {
            String firstname="", lastName="";
            String fullname = txtFullName.getText().toString();
            String email = txtEmail.getText().toString();
            String bankName = txtBankName.getText().toString();
            String accountNumber = txtAccountNumber.getText().toString();
            if(fullname.contains(" ")){
                String[] splited = fullname.split("\\s+");
                //firstname = splited[0];
                firstname = fullname;
                lastName = splited[1];
            }else {
                firstname=fullname;
            }
            if(accountNumber.contains("-")){
                //accountNumber=accountNumber.replace("-","");
            }
            prepareupdateProfile(firstname, lastName, email, bankName, accountNumber);

        }

    }

    class ParseDriverDetails extends AsyncTask<String,Void,HashMap<String,String>> {

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String email,accessToken,phoneNumber,userId,message,flag="0";
            HashMap<String,String> map=new HashMap<>();
            String response=strings[0];
            try {
                JSONObject jsonObject=new JSONObject(response);
                message= jsonObject.getString("message");
                flag= jsonObject.getString("flag");
                map.put("flag",flag);
                map.put("message",message);
                JSONObject data=jsonObject.getJSONObject("response");
                try {
                    map.put(ConstantValues.LICENSE_NUMBER_VALUE,data.getString("license_number"));
                    map.put(ConstantValues.LICENSE_EXPIRY,data.getString("license_expiry_date"));
                    JSONArray jsonArray=data.getJSONArray("license");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    JSONObject backImageObject=jsonArray.getJSONObject(1);
                    String licenseFrontImage=imageObject.getString("image_url");
                    String licensebackImage=backImageObject.getString("image_url");
                    map.put(ConstantValues.LICENSE_FRONT,licenseFrontImage);
                    map.put(ConstantValues.LICENSE_BACK,licensebackImage);

                }catch (Exception e){
                }


                try {
                    map.put(ConstantValues.CIRTIFICATE_NUMBER,data.getString("certificate_registration_number"));
                    map.put(ConstantValues.CIRTIFICATE_EXPIRY,data.getString("certificate_expiry_date"));
                    JSONArray jsonArray=data.getJSONArray("certificate");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    JSONObject backImageObject=jsonArray.getJSONObject(1);
                    String cirtificateFrontImage=imageObject.getString("image_url");
                    String cirtificatebackImage=backImageObject.getString("image_url");
                    map.put(ConstantValues.CIRTIFICATE_FRONT,cirtificateFrontImage);
                    map.put(ConstantValues.CIRTIFICATE_BACK,cirtificatebackImage);

                }catch (Exception e){
                }


                try {
                    map.put(ConstantValues.INSURENCE_NUMBER,data.getString("insurance_paper_number"));
                    map.put(ConstantValues.INSURENCE_NUMBER_EXPIRY,data.getString("driver_insurance_expiry_date"));
                    JSONArray jsonArray=data.getJSONArray("insurance");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    JSONObject backImageObject=jsonArray.getJSONObject(1);
                    String insurenceFrontImage=imageObject.getString("image_url");
                    String insurencebackImage=backImageObject.getString("image_url");
                    map.put(ConstantValues.INSURENCE_FRONT,insurenceFrontImage);
                    map.put(ConstantValues.INSURENCE_BACK,insurencebackImage);
                }catch (Exception e){
                }


                try {
                    map.put(ConstantValues.TRADE_INSURENCE_NUMBER,data.getString("trade_license_paper_number"));
                    map.put(ConstantValues.TRADE_INSURENCE_NUMBER_EXPIRY,data.getString("trade_license_paper_expiry_date"));
                    JSONArray jsonArray=data.getJSONArray("trade_license_paper");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    JSONObject backImageObject=jsonArray.getJSONObject(1);
                    String tradefrontImage=imageObject.getString("image_url");
                    String tradebackImage=backImageObject.getString("image_url");
                    map.put(ConstantValues.TRADE_FRONT,tradefrontImage);
                    map.put(ConstantValues.TRADE_BACK,tradebackImage);
                }catch (Exception e){
                }

                try {
                    JSONArray jsonArray=data.getJSONArray("vehicle");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    JSONObject backImageObject=jsonArray.getJSONObject(1);
                    JSONObject rightImageObject=jsonArray.getJSONObject(2);
                    JSONObject leftImageObject=jsonArray.getJSONObject(3);
                    String tradefrontImage=imageObject.getString("image_url");
                    String tradebackImage=backImageObject.getString("image_url");
                    String rightImage=rightImageObject.getString("image_url");
                    String leftImage=leftImageObject.getString("image_url");
                    map.put(ConstantValues.VEHICAL_FRONT,tradefrontImage);
                    map.put(ConstantValues.VEHICAL_BACK,tradebackImage);
                    map.put(ConstantValues.VEHICAL_RIGHT,rightImage);
                    map.put(ConstantValues.VEHICAL_LEFT,leftImage);
                }catch (Exception e){
                }


                try {
                    JSONArray jsonArray=data.getJSONArray("profile_image");
                    JSONObject imageObject=jsonArray.getJSONObject(0);
                    String insurenceFrontImage=imageObject.getString("image_url");
                    map.put(ConstantValues.USER_IMAGE,insurenceFrontImage);

                }catch (Exception e){
                }

                map.put(ConstantValues.BANK_NAME,data.getString("bank_name"));
                map.put(ConstantValues.ACCOUNT_NUMBER,data.getString("account_number"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            String flag=hashMap.get("flag");
            String message=hashMap.get("message");

            setDataToViews(hashMap);


        }
    }

    class ParseUpdateuserDetails extends AsyncTask<String,Void,HashMap<String,String>> {

        @Override
        protected HashMap<String, String> doInBackground(String... strings) {
            String email,accessToken,phoneNumber,userId,message,flag="0";
            HashMap<String,String> map=new HashMap<>();
            String response=strings[0];
            try {
                JSONObject jsonObject = new JSONObject(response);
                message = jsonObject.getString("message");
                flag = jsonObject.getString("flag");
                map.put("flag", flag);
                map.put("message", message);
                JSONObject data = jsonObject.getJSONObject("response");
                map.put(ConstantValues.USER_EMAILADDRESS,data.getString("email"));
                map.put(ConstantValues.USER_FIRSTNAME,data.getString("first_name"));
                map.put(ConstantValues.USER_LASTNAME,data.getString("last_name"));
            }catch (Exception e){

            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            Intent intent;
            String flag=hashMap.get("flag");
            String message=hashMap.get("message");
            String lastname=hashMap.get(ConstantValues.USER_LASTNAME);
            if(lastname==null){
                lastname="";
            }
            if(mActivityresponse!=null){
                Toast.makeText(mActivityresponse,message,Toast.LENGTH_SHORT).show();
                ViewProfileDriverFragment viewProfileDriverFragment=new ViewProfileDriverFragment();
                User.getInstance().updateProfile(mActivityresponse,hashMap.get(ConstantValues.USER_EMAILADDRESS),hashMap.get(ConstantValues.USER_FIRSTNAME),lastname);
                mActivityresponse.callFragment(viewProfileDriverFragment);
            }else {
                Toast.makeText(activity,message,Toast.LENGTH_SHORT).show();
                ViewProfileDriverFragment viewProfileDriverFragment=new ViewProfileDriverFragment();
                User.getInstance().updateProfile(activity,hashMap.get(ConstantValues.USER_EMAILADDRESS),hashMap.get(ConstantValues.USER_FIRSTNAME),lastname);
                activity.callFragment(viewProfileDriverFragment);
            }
        }
    }

    private void setDataToViews(final HashMap<String, String> hashMap) {
        final Activity activity=getActivityRefrence();
        TextView licenseExpiry,cirtificateExpiry,insuranceExpiry,tradeInsuranceExpiry;
        CircleImageView licenseFront,licenseback,cirtificateFront,cirtificateBack,insuranceFront,insuranceback,tradeFront,TradeBack,userImage,frontVehical,backVehical,leftVehical,rightVehical;
        txtFullName=view.findViewById(R.id.edt_Value_full_name);
        txtMobile=view.findViewById(R.id.edt_mobile_number_signup);
        txtEmail=view.findViewById(R.id.edt_Value_email_id);
        txtBankName=view.findViewById(R.id.edt_Value_bank_name);
        txtAccountNumber=view.findViewById(R.id.edt_Value_account_number);
        txtConfirmAccountNumber=view.findViewById(R.id.edt_Value_confirm_account_number);

        txtAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = txtAccountNumber.getText().toString();
                int textlength = txtAccountNumber.getText().length();
                if (text.endsWith("-"))
                    return;
                if (textlength == 5||textlength==10||textlength==15) {
                    txtAccountNumber.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                    txtAccountNumber.setSelection(txtAccountNumber.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtConfirmAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = txtConfirmAccountNumber.getText().toString();
                int textlength = txtConfirmAccountNumber.getText().length();
                if (text.endsWith("-"))
                    return;
                if (textlength == 5||textlength==10||textlength==15) {
                    txtConfirmAccountNumber.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                    txtConfirmAccountNumber.setSelection(txtConfirmAccountNumber.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        licenseExpiry=view.findViewById(R.id.txt_driving_license_expiry);
        cirtificateExpiry=view.findViewById(R.id.txt_registration_cirtificate_expiry);
        insuranceExpiry=view.findViewById(R.id.txt_insurence_paper_expiry);
        tradeInsuranceExpiry=view.findViewById(R.id.txt_trade_license_paper_expiry);

        licenseExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+CommonMethods.getInstance().getDatafromTimeStamp(hashMap.get(ConstantValues.LICENSE_EXPIRY)));
        cirtificateExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+CommonMethods.getInstance().getDatafromTimeStamp(hashMap.get(ConstantValues.CIRTIFICATE_EXPIRY)));
        insuranceExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+CommonMethods.getInstance().getDatafromTimeStamp(hashMap.get(ConstantValues.INSURENCE_NUMBER_EXPIRY)));
        tradeInsuranceExpiry.setText(activity.getResources().getString(R.string.txt_expiry_date)+" "+CommonMethods.getInstance().getDatafromTimeStamp(hashMap.get(ConstantValues.TRADE_INSURENCE_NUMBER_EXPIRY)));






        txtFullName.setText(User.getInstance().getFirstName()+" "+User.getInstance().getLastName());
        txtMobile.setText(User.getInstance().getCountryCode()+" "+User.getInstance().getMobileNumber());
        txtEmail.setText(User.getInstance().getEmail());
        txtBankName.setText(hashMap.get(ConstantValues.BANK_NAME));
        txtAccountNumber.setText(hashMap.get(ConstantValues.ACCOUNT_NUMBER));
        txtConfirmAccountNumber.setText(hashMap.get(ConstantValues.ACCOUNT_NUMBER));




        licenseFront=(CircleImageView)view.findViewById(R.id.frontdrivingLicense);
        licenseback=(CircleImageView)view.findViewById(R.id.backdrivinglicense);
        cirtificateFront=(CircleImageView)view.findViewById(R.id.front_registration_Cirtificate);
        cirtificateBack=(CircleImageView)view.findViewById(R.id.back_registration_cirtificate);

        insuranceFront=(CircleImageView)view.findViewById(R.id.frontInsurence);
        insuranceback=(CircleImageView)view.findViewById(R.id.backInsurence);
        tradeFront=(CircleImageView)view.findViewById(R.id.front_trade);
        TradeBack=(CircleImageView)view.findViewById(R.id.back_trade);

        frontVehical=(CircleImageView)view.findViewById(R.id.frontVehical);
        backVehical=(CircleImageView)view.findViewById(R.id.backVehical);
        rightVehical=(CircleImageView)view.findViewById(R.id.rightVehical);
        leftVehical=(CircleImageView)view.findViewById(R.id.leftVehical);

        userImage=(CircleImageView)view.findViewById(R.id.userImage);

        view.findViewById(R.id.rl_driving_license).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Fragment fragment=new Licensefragment();
                Bundle bundle=new Bundle();
                bundle.putBoolean("update",true);
                bundle.putString(ConstantValues.LICENSE_FRONT,hashMap.get(ConstantValues.LICENSE_FRONT));
                bundle.putString(ConstantValues.LICENSE_BACK,hashMap.get(ConstantValues.LICENSE_BACK));
                bundle.putString(ConstantValues.LICENSE_NUMBER_VALUE,hashMap.get(ConstantValues.LICENSE_NUMBER_VALUE));
                bundle.putString(ConstantValues.LICENSE_EXPIRY,hashMap.get(ConstantValues.LICENSE_EXPIRY));
                fragment.setArguments(bundle);
                callFragment(fragment,activity.getResources().getString(R.string.txt_driving_license));

            }
        });
        view.findViewById(R.id.rl_registration_cirtificate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new RegistrationCirtificateFragment();
                Bundle bundle=new Bundle();
                bundle.putBoolean("update",true);
                bundle.putString(ConstantValues.CIRTIFICATE_FRONT,hashMap.get(ConstantValues.CIRTIFICATE_FRONT));
                bundle.putString(ConstantValues.CIRTIFICATE_BACK,hashMap.get(ConstantValues.CIRTIFICATE_BACK));
                bundle.putString(ConstantValues.CIRTIFICATE_NUMBER,hashMap.get(ConstantValues.CIRTIFICATE_NUMBER));
                bundle.putString(ConstantValues.CIRTIFICATE_EXPIRY,hashMap.get(ConstantValues.CIRTIFICATE_EXPIRY));
                fragment.setArguments(bundle);
                callFragment(fragment,activity.getResources().getString(R.string.txt_registration_cirtificate));
            }
        });
        view.findViewById(R.id.rl_insurence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new InsurenceFragment();
                Bundle bundle=new Bundle();
                bundle.putBoolean("update",true);
                bundle.putString(ConstantValues.INSURENCE_FRONT,hashMap.get(ConstantValues.INSURENCE_FRONT));
                bundle.putString(ConstantValues.INSURENCE_BACK,hashMap.get(ConstantValues.INSURENCE_BACK));
                bundle.putString(ConstantValues.INSURENCE_NUMBER,hashMap.get(ConstantValues.INSURENCE_NUMBER));
                bundle.putString(ConstantValues.INSURENCE_NUMBER_EXPIRY,hashMap.get(ConstantValues.INSURENCE_NUMBER_EXPIRY));
                fragment.setArguments(bundle);
                callFragment(fragment,activity.getResources().getString(R.string.txt_insurence_paper));

            }
        });
        view.findViewById(R.id.rl_trade_insurence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new TradeLicenseFragment();
                Bundle bundle=new Bundle();
                bundle.putBoolean("update",true);
                bundle.putString(ConstantValues.TRADE_FRONT,hashMap.get(ConstantValues.TRADE_FRONT));
                bundle.putString(ConstantValues.TRADE_BACK,hashMap.get(ConstantValues.TRADE_BACK));
                bundle.putString(ConstantValues.TRADE_INSURENCE_NUMBER,hashMap.get(ConstantValues.TRADE_INSURENCE_NUMBER));
                bundle.putString(ConstantValues.TRADE_INSURENCE_NUMBER_EXPIRY,hashMap.get(ConstantValues.TRADE_INSURENCE_NUMBER_EXPIRY));
                fragment.setArguments(bundle);
                callFragment(fragment,activity.getResources().getString(R.string.txt_trade_license));
            }
        });
        view.findViewById(R.id.rl_vehicle_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new VehicalImagesFragment();
                Bundle bundle=new Bundle();
                bundle.putBoolean("update",true);
                bundle.putString(ConstantValues.VEHICAL_FRONT,hashMap.get(ConstantValues.VEHICAL_FRONT));
                bundle.putString(ConstantValues.VEHICAL_BACK,hashMap.get(ConstantValues.VEHICAL_BACK));
                bundle.putString(ConstantValues.VEHICAL_LEFT,hashMap.get(ConstantValues.VEHICAL_LEFT));
                bundle.putString(ConstantValues.VEHICAL_RIGHT,hashMap.get(ConstantValues.VEHICAL_RIGHT));
                fragment.setArguments(bundle);
                callFragment(fragment,activity.getResources().getString(R.string.txt_vehical_image));
            }
        });

        if(hashMap.get(ConstantValues.LICENSE_FRONT)!=null) {
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.LICENSE_FRONT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(licenseFront);
        }
        if(hashMap.get(ConstantValues.LICENSE_BACK)!=null) {
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.LICENSE_BACK))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(licenseback);
        }

        if(hashMap.get(ConstantValues.CIRTIFICATE_FRONT)!=null) {
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.CIRTIFICATE_FRONT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(cirtificateFront);
        }
        if(hashMap.get(ConstantValues.CIRTIFICATE_BACK)!=null) {
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.CIRTIFICATE_BACK))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(cirtificateBack);

        }
        if(hashMap.get(ConstantValues.INSURENCE_FRONT)!=null) {

            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.INSURENCE_FRONT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(insuranceFront);
        }
        if(hashMap.get(ConstantValues.INSURENCE_BACK)!=null) {
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.INSURENCE_BACK))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(insuranceback);
        }
        if(hashMap.get(ConstantValues.TRADE_FRONT)!=null) {
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.TRADE_FRONT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(tradeFront);

        }
        if(hashMap.get(ConstantValues.TRADE_BACK)!=null) {
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.TRADE_BACK))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(TradeBack);
        }

        if(hashMap.get(ConstantValues.USER_IMAGE)!=null) {
            User.getInstance().setUserImage(hashMap.get(ConstantValues.USER_IMAGE),activity);

            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.USER_IMAGE))
                    .placeholder(R.drawable.profile_placeholder)
                    .error(R.drawable.profile_placeholder)
                    .into(userImage);
        }

        if(hashMap.get(ConstantValues.VEHICAL_FRONT)!=null) {
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_FRONT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(frontVehical);

        }
        if(hashMap.get(ConstantValues.VEHICAL_BACK)!=null) {
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_BACK))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(backVehical);

        }
        if(hashMap.get(ConstantValues.VEHICAL_RIGHT)!=null) {
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_RIGHT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(rightVehical);

        }
        if(hashMap.get(ConstantValues.VEHICAL_LEFT)!=null) {
            Picasso.with(activity)
                    .load(ConstantValues.BASE_URL + "/" + hashMap.get(ConstantValues.VEHICAL_LEFT))
                    .placeholder(R.drawable.good_img)
                    .error(R.drawable.good_img)
                    .into(leftVehical);

        }


    }


}


