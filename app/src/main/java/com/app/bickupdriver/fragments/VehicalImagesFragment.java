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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.bickupdriver.EditProfileActivity;
import com.app.bickupdriver.R;
import com.app.bickupdriver.ResetAndForgetPasswordActivity;
import com.app.bickupdriver.controller.NetworkCallBack;
import com.app.bickupdriver.controller.WebAPIManager;
import com.app.bickupdriver.model.User;
import com.app.bickupdriver.utility.CommonMethods;
import com.app.bickupdriver.utility.ConstantValues;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


public class VehicalImagesFragment extends Fragment implements View.OnClickListener,NetworkCallBack {

    public static String TAG=VehicalImagesFragment.class.getSimpleName();

    public EditProfileActivity mActivityResponse;
    private Typeface mTypefaceRegular;
    private Typeface mTypefaceBold;
    private ResetAndForgetPasswordActivity mActivty;
    private CircleImageView imgFront,imgBack,imgLeft,imgRight;
    private int REQUEST_IMAGE_CAPTURE=111;
    private File frontImage;
    private File backImage;
    private File rightImage;
    private File leftImage;
    private CircularProgressView circularProgressBar;
    private  String message;
    private int update=0;
    private String mCurrentPhotoPath;
    private Uri file;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            if(getArguments().getBoolean("update")){
                update=1;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view =inflater.inflate(R.layout.fragment_vehical_images, container, false);
        initializeViews(view);
        return view;
    }







    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mActivty=(ResetAndForgetPasswordActivity)activity;
        }catch (Exception e) {
            mActivityResponse = (EditProfileActivity) activity;
        }

    }



    private void initializeViews(View view) {
        Activity activity=getActivityRefrence();
        mTypefaceRegular= Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_REGULAR);
        mTypefaceBold=Typeface.createFromAsset(activity.getAssets(), ConstantValues.TYPEFACE_BOLD);
        circularProgressBar=(CircularProgressView)view.findViewById(R.id.progress_view);
        TextView txtFronSide=(view.findViewById(R.id.txt_front_side));
        TextView txtBackSide=(view.findViewById(R.id.txt_back_side));
        TextView txtLeftSide=(view.findViewById(R.id.txt_left_side));
        TextView txtRightSide=(view.findViewById(R.id.txt_right_side));
        TextView txtSteps=(view.findViewById(R.id.txt_steps_license));
        imgBack=view.findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);
        imgBack.setTag(false);

        imgFront=view.findViewById(R.id.img_front);
        imgFront.setOnClickListener(this);
        imgFront.setTag(false);

        imgLeft=view.findViewById(R.id.img_left);
        imgLeft.setOnClickListener(this);
        imgLeft.setTag(false);

        imgRight=view.findViewById(R.id.img_right);
        imgRight.setOnClickListener(this);
        imgRight.setTag(false);

        txtFronSide.setTypeface(mTypefaceRegular);
        txtBackSide.setTypeface(mTypefaceRegular);
        txtSteps.setTypeface(mTypefaceRegular);
        txtLeftSide.setTypeface(mTypefaceRegular);
        txtRightSide.setTypeface(mTypefaceRegular);

        Button btnNext=view.findViewById(R.id.btn_next_license_number);
        btnNext.setOnClickListener(this);
        btnNext.setTypeface(mTypefaceRegular);

        if(update==1){
            btnNext.setText(activity.getResources().getString(R.string.txt_save));
            txtSteps.setVisibility(View.GONE);
        }else {
            txtSteps.setVisibility(View.VISIBLE);
            btnNext.setText(activity.getResources().getString(R.string.txt_proceed));
        }
        setDataToViews();


    }
    private void setDataToViews() {
        Activity activity=getActivityRefrence();
        if(update==1){
            if(getArguments()!=null){
                String frontImage1=getArguments().getString(ConstantValues.VEHICAL_FRONT);
                String backImage1=getArguments().getString(ConstantValues.VEHICAL_BACK);
                String leftImage1=getArguments().getString(ConstantValues.VEHICAL_LEFT);
                String rightImage1=getArguments().getString(ConstantValues.VEHICAL_RIGHT);


                Picasso.with(activity)
                        .load(ConstantValues.BASE_URL+"/"+frontImage1)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .into(imgFront);


                Picasso.with(activity)
                        .load(ConstantValues.BASE_URL+"/"+backImage1)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .into(imgBack);
                Picasso.with(activity)
                        .load(ConstantValues.BASE_URL+"/"+leftImage1)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .into(imgLeft);


                Picasso.with(activity)
                        .load(ConstantValues.BASE_URL+"/"+rightImage1)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .into(imgRight);


            /*    Ion.with(imgFront)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .load(ConstantValues.BASE_URL+"/"+frontImage1);


                Ion.with(imgBack)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .load(ConstantValues.BASE_URL+"/"+backImage1);

                Ion.with(imgLeft)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .load(ConstantValues.BASE_URL+"/"+leftImage1);


                Ion.with(imgRight)
                        .placeholder(R.drawable.upload_doc)
                        .error(R.drawable.upload_doc)
                        .load(ConstantValues.BASE_URL+"/"+rightImage1);*/


                MyAsync myAsync=new MyAsync(1);
                myAsync.execute(ConstantValues.BASE_URL+"/"+frontImage1);

                MyAsync myAsync2=new MyAsync(2);
                myAsync2.execute(ConstantValues.BASE_URL+"/"+backImage1);


                MyAsync myAsync3=new MyAsync(3);
                myAsync3.execute(ConstantValues.BASE_URL+"/"+leftImage1);

                MyAsync myAsync4=new MyAsync(4);
                myAsync4.execute(ConstantValues.BASE_URL+"/"+rightImage1);

            }
        }
    }





public class MyAsync extends AsyncTask<String, Void, Bitmap>{
    int i;

    public MyAsync(int i){
        this.i=i;
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
        if(bitmap!=null) {
            setImagesFile(bitmap, i);
        }
    }
}

    private void setImagesFile(Bitmap bitmap, int i) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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

        switch (i){
            case 1:
                frontImage=imgFile1;
                break;
            case 2:
                backImage=imgFile1;
                break;
            case 3:
                leftImage=imgFile1;
                break;
            case 4:
                rightImage=imgFile1;
                break;
        }

    }


    /*public boolean validatefeilds(){

        return true;
    }
*/



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
        int id=view.getId();
        switch (id){
            case R.id.btn_next_license_number:
                if(validatefeilds()){
                    prepareuserLogin();
                  //  mActivty.callFragment(ConstantValues.PROFILE_DETAILS);
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
            case R.id.img_left:
                imgLeft.setTag(true);
                checkCameraPermission();
                break;
            case R.id.img_right:
                imgRight.setTag(true);
                checkCameraPermission();
                break;
        }

    }

    public boolean validatefeilds(){
        Activity activity=getActivityRefrence();

        if(frontImage==null){
                Toast.makeText(activity, activity.getResources().getString(R.string.txt_validate_fron_image), Toast.LENGTH_SHORT).show();
                return false;
            }
            if(backImage==null){
                Toast.makeText(activity, activity.getResources().getString(R.string.txt_validate_back_image), Toast.LENGTH_SHORT).show();
                return false;
            }
        if(rightImage==null){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_validate_right_image), Toast.LENGTH_SHORT).show();
            return false;
        }

        if(leftImage==null){
            Toast.makeText(activity, activity.getResources().getString(R.string.txt_validate_left_image), Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
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
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
        Activity activity=getActivityRefrence();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Activity activity=getActivityRefrence();
            try {
                Bitmap help1 = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),file);
                onCaptureImageResult(help1);

            }catch (Exception e){

            }
        }
    }


    public void launch_camera() {
        Activity activity=getActivityRefrence();
        // the intent is my camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //getting uri of the file
        file = Uri.fromFile(getFile());
        //Setting the file Uri to my photo
        intent.putExtra(MediaStore.EXTRA_OUTPUT,file);

        if(intent.resolveActivity(activity.getPackageManager())!=null)
        {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
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


    private void onCaptureImageResult(Bitmap bitmap) {
        if((boolean)imgBack.getTag()){
            bitmap=checkOrientation(imgBack,bitmap);

        }else{
            if((boolean)imgFront.getTag()) {
                bitmap=checkOrientation(imgFront,bitmap);
            }
        }
        if((boolean)imgLeft.getTag()){
            bitmap=checkOrientation(imgLeft,bitmap);
        }else {
            if((boolean)imgRight.getTag()){
               bitmap= checkOrientation(imgRight,bitmap);
            }
        }
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

        if((boolean)imgBack.getTag()){
            imgBack.setTag(false);
            backImage=imgFile;
        }else{
            if((boolean)imgFront.getTag()) {
                imgFront.setTag(false);
                frontImage=imgFile;
            }
        }

        if((boolean)imgLeft.getTag()){
            imgLeft.setTag(false);
            leftImage=imgFile;
        }else {
            if((boolean)imgRight.getTag()){
                imgRight.setTag(false);
                rightImage=imgFile;
            }
        }
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
        Bitmap bitmap1=CommonMethods.getInstance().scaleDown(rotatedBitmap,500,false);
        view.setImageBitmap(bitmap1);
        return bitmap1;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private Activity getActivityRefrence() {
        Activity activity;
        if(mActivty!=null){
            activity=mActivty;
        }else {
            activity=mActivityResponse;
        }
        return activity;
    }
    public void  prepareuserLogin() {
        String createUserUrl= WebAPIManager.getInstance().getVehicaleUrl();
        final JsonObject requestBody=new JsonObject();
        ArrayList<File> arrayList=new ArrayList<>();
        arrayList.add(frontImage);
        arrayList.add(backImage);
        arrayList.add(leftImage);
        arrayList.add(rightImage);
        callAPI(requestBody,createUserUrl,this,60*1000,100,arrayList,"");
    }
    private void callAPI(JsonObject requestBody, String createUserUrl, final NetworkCallBack loginActivity, int timeOut, final int requestCode, ArrayList<File> date, String licensenumber) {
        circularProgressBar.setVisibility(View.VISIBLE);
        Builders.Any.B builder=Ion.with(this).load(createUserUrl);
        builder.setHeader(ConstantValues.USER_ACCESS_TOKEN, User.getInstance().getAccesstoken());
        for (int i=0;i<date.size();i++){
            builder.setMultipartFile("vehicle[]",date.get(i));
        }
        builder .setMultipartParameter("is_update", String.valueOf(update))
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




    @Override
    public void onSuccess(JsonObject data, int requestCode, int statusCode) {
        switch (requestCode){
            case 100:
                ParseuserLoginResponse parseuserLoginResponse=new ParseuserLoginResponse();
                parseuserLoginResponse.execute(String.valueOf(data));
                break;

        }
    }

    @Override
    public void onError(String msg) {

    }

    class ParseuserLoginResponse extends AsyncTask<String,Void,HashMap<String,String>> {

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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> hashMap) {
            String flag=hashMap.get("flag");
            String message=hashMap.get("message");
            if(mActivityResponse!=null) {
                Toast.makeText(mActivityResponse,message,Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mActivty,message,Toast.LENGTH_SHORT).show();
            }

            ViewProfileDriverFragment viewProfileDriverFragment=new ViewProfileDriverFragment();
            callFragment(viewProfileDriverFragment);
        }
    }

    private void callFragment(Fragment viewProfileDriverFragment) {
        if(mActivty!=null){
            mActivty.callFragment(viewProfileDriverFragment);
        }else {
            mActivityResponse.callFragment(viewProfileDriverFragment);
        }
    }

}
