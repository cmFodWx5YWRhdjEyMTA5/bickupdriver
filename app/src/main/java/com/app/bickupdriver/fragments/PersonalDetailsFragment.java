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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickupdriver.R;
import com.app.bickupdriver.ResetAndForgetPasswordActivity;
import com.app.bickupdriver.controller.NetworkCallBack;
import com.app.bickupdriver.controller.WebAPIManager;
import com.app.bickupdriver.model.User;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.app.bickupdriver.utility.Image;
import com.app.bickupdriver.utility.Utils;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

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


public class PersonalDetailsFragment extends Fragment implements View.OnClickListener, NetworkCallBack {

    public static String TAG = PersonalDetailsFragment.class.getSimpleName();
    private ResetAndForgetPasswordActivity mActivityReference;
    private Activity activity;
    private EditText edtAccountNumber;
    private EditText edtBankName;
    private EditText edtConfirmAccountNumber;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private CircleImageView userImage;
    private File imgFile;
    private CircularProgressView circularProgressBar;
    private String message;
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
        View view = inflater.inflate(R.layout.fragment_personal_details, container, false);
        InitializeViews(view);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivityReference = (ResetAndForgetPasswordActivity) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void InitializeViews(View view) {
        mTypefaceRegular = Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold = Typeface.createFromAsset(mActivityReference.getAssets(), ConstantValues.TYPEFACE_BOLD);
        setUIToHideKeyBoard(view);
        circularProgressBar = (CircularProgressView) view.findViewById(R.id.progress_view);
        TextView txtSteps = view.findViewById(R.id.txt_steps);
        TextView txtSkip = view.findViewById(R.id.txt_skip);
        txtSkip.setOnClickListener(this);
        txtSkip.setTypeface(mTypefaceRegular);
        txtSteps.setTypeface(mTypefaceRegular);
        edtAccountNumber = (EditText) view.findViewById(R.id.edt_account_number);
        edtConfirmAccountNumber = (EditText) view.findViewById(R.id.edt_confirm_account_number);
        edtAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edtAccountNumber.getText().toString();
                int textlength = edtAccountNumber.getText().length();
                if (text.endsWith("-"))
                    return;
                if (textlength == 5 || textlength == 10 || textlength == 15) {
                    edtAccountNumber.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                    edtAccountNumber.setSelection(edtAccountNumber.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtConfirmAccountNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = edtConfirmAccountNumber.getText().toString();
                int textlength = edtConfirmAccountNumber.getText().length();
                if (text.endsWith("-"))
                    return;
                if (textlength == 5 || textlength == 10 || textlength == 15) {
                    edtConfirmAccountNumber.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                    edtConfirmAccountNumber.setSelection(edtConfirmAccountNumber.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtBankName = (EditText) view.findViewById(R.id.edt_bank_name);


        Button btnSave = (Button) view.findViewById(R.id.btn_proceed);
        btnSave.setOnClickListener(this);

        userImage = (CircleImageView) view.findViewById(R.id.img_user);
        userImage.setOnClickListener(this);

        edtAccountNumber.setTypeface(mTypefaceRegular);
        edtConfirmAccountNumber.setTypeface(mTypefaceRegular);

        edtBankName.setTypeface(mTypefaceRegular);
        btnSave.setTypeface(mTypefaceRegular);

    }


    //hide keyboard onTouch outSide the EditText
    public void setUIToHideKeyBoard(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
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

    public void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(mActivityReference, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(mActivityReference, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            if (ContextCompat.checkSelfPermission(mActivityReference, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivityReference,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        111);
            }
            if (ContextCompat.checkSelfPermission(mActivityReference, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivityReference,
                        new String[]{Manifest.permission.CAMERA},
                        113);
            }

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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


                } catch (Exception e) {
                }
            } else if (requestCode == 0) {
                try {
                    Bitmap help1 = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), file);
                    onCaptureImageResult(help1);
                } catch (Exception e) {

                }

            }
        }
    }

    public void launch_camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            startActivityForResult(intent, 0);
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

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void onCaptureImageResult(Bitmap bitmap) {
        bitmap = checkOrientation(userImage, bitmap);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

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

    public void selectImage() {
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

    public void prepareuserLogin(final String bankName, final String accountNumber) {
        String createUserUrl = WebAPIManager.getInstance().getbankUrl();
        final JsonObject requestBody = new JsonObject();
        callAPI(requestBody, createUserUrl, this, 60 * 1000, 100, bankName, accountNumber);
    }


    private void callAPI(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode, String bankName, String accountNumber) {
        circularProgressBar.setVisibility(View.VISIBLE);
        if (imgFile != null) {
            Ion.with(this)
                    .load(createUserUrl)
                    .setHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken())
                    .setMultipartFile("profile_image", imgFile)
                    .setMultipartParameter("bank_name", bankName)
                    .setMultipartParameter("account_number", accountNumber)
                    .setMultipartParameter("is_update", "0")
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            circularProgressBar.setVisibility(View.GONE);
                            if (e != null) {
                                Utils.printLogs(TAG, "Exception 2 : " + e.getMessage());
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
                                Utils.printLogs(TAG, "Exception : " + e1.getMessage());
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
        } else {
            Ion.with(this)
                    .load(createUserUrl)
                    .setHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken())
                    .setMultipartParameter("bank_name", bankName)
                    .setMultipartParameter("account_number", accountNumber)
                    .setMultipartParameter("is_update", "0")
                    .asJsonObject()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<JsonObject>>() {
                        @Override
                        public void onCompleted(Exception e, Response<JsonObject> result) {
                            circularProgressBar.setVisibility(View.GONE);
                            if (e != null) {
                                Utils.printLogs(TAG, "Exception 3 : " + e.getMessage());
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
                                Utils.printLogs(TAG, "Exception 4 : " + e1.getMessage());
                            }
                            switch (status) {
                                case 422:
                                case 400:
                                case 500:
                                case 200:
                                    loginActivity.onSuccess(resultObject, requestCode, status);
                                    break;
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
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_proceed:
                if (validateFields()) {
                    String bankName = edtBankName.getText().toString().trim();
                    String accountNumber = edtAccountNumber.getText().toString().trim();
                    prepareuserLogin(bankName.trim(), accountNumber);
                    // mActivityReference.callFragment(ConstantValues.CIRTIFICATE);
                }
                break;
            case R.id.backImage_header:
                break;
            case R.id.id_image_container:
            case R.id.img_user:
                //checkCameraPermission();
                this.checkForPermissions();
                break;
            case R.id.txt_skip:
                mActivityReference.callFragment(ConstantValues.LICENSE_NUMBER);
                break;
        }
    }

    private boolean validateFields() {
        String bankName = edtBankName.getText().toString().trim();
        String accountNumber = edtAccountNumber.getText().toString().trim();
        String confirmAccountNumber = edtConfirmAccountNumber.getText().toString().trim();
        if (TextUtils.isEmpty(bankName)) {
            Utils.showToast("Please enter bank name", getContext());
            return false;
        } else if (TextUtils.isEmpty(accountNumber)) {
            Utils.showToast("Please enter account number", getContext());
            return false;
        } else if (TextUtils.isEmpty(confirmAccountNumber)) {
            Utils.showToast("Please enter confirm account number", getContext());
            return false;
        } else if (!accountNumber.equals(confirmAccountNumber)) {
            Utils.showToast("Account number and confirm account number does not match", getContext());
            return false;
        } else {
            return true;
        }
    }

    private void checkForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(mActivityReference,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mActivityReference,
                        Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Permission Granted");
            this.selectImage();
        } else {

            requestPermissions(new String[]{
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
            // String message=hashMap.get("message");
            //Toast.makeText(mActivityReference,message,Toast.LENGTH_SHORT).show();
            mActivityReference.callFragment(ConstantValues.LICENSE_NUMBER);
        }
    }


}
