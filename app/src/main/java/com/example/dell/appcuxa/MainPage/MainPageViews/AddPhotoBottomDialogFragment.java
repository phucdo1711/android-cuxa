package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.ObjectModels.ImageObject;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AddPhotoBottomDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener{
    public static final int CAMERA = 111;
    TextView tvCamera, tvGallery;
    public int position = -1;
    List<Bitmap> lstBitmap = new ArrayList<>();
    CuXaAPI fileService;
    List<ImageObject> lstImageObject = new ArrayList<>();

    List<Uri> uriList = new ArrayList<>();
    List<byte[]> listByteImage = new ArrayList<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String token;
    OnChooseReasonListener listener;

    public static AddPhotoBottomDialogFragment newInstance() {
        return new AddPhotoBottomDialogFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();
        if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},20);
        }
    }

    public interface OnChooseReasonListener {
        void onChooseReason(List<byte[]> bytes, int pos);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_choose_image, container,
                false);

        // get the views and attach the listener
        tvCamera = view.findViewById(R.id.tvCamera);
        tvGallery = view.findViewById(R.id.tvGallery);
        tvCamera.setOnClickListener(this);
        tvGallery.setOnClickListener(this);
        sharedPreferences = getActivity().getSharedPreferences("login_data", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        token = sharedPreferences.getString("token", "");

        fileService = NetworkController.upload();
        Bundle bundle = getArguments();
        if (bundle != null) {
            position = bundle.getInt("pos", 0);
        }
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCamera:
                if(position>=10){
                    Toast.makeText(getActivity(), "Max 10 ảnh, đéo cho chọn :))", Toast.LENGTH_SHORT).show();
                }else {
                    intentCamera();
                }
                break;
            case R.id.tvGallery:
                if(position>=10){
                    Toast.makeText(getActivity(), "Max 10 ảnh, đéo cho chọn :))", Toast.LENGTH_SHORT).show();
                }else {
                    intentGetImage();
                }
                break;
        }
    }

    public void intentCamera() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){

                requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA);
            }else{
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);
            }
        }
    }

    public void intentGetImage() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},20);
            }else{
                PhotoPicker.builder()
                        .setPhotoCount(10 - position)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(getActivity(), PhotoPicker.REQUEST_CODE);
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (data != null) {
            if (requestCode == CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap photoResized = AppUtils.getResizedBitmap(photo,500);
                lstBitmap.add(photoResized);
                sendBackImage(lstBitmap);
                Uri imageUri = getImageUri(getContext(),photoResized);
                List<MultipartBody.Part> parts = new ArrayList<>();

                parts.add(prepareFilePart("images", imageUri));
                MultipartBody.Part[] parts1 = new MultipartBody.Part[parts.size()];
                parts.toArray(parts1);
                Call<ResponseBody> call = fileService.postImage("Bearer " + token, parts1);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            JSONArray jArray = null;
                            try {
                                jArray = new JSONArray(response.body().string());
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject object = jArray.optJSONObject(i);
                                    FragmentUpRoom.imageHinhId.add(object.getString("id"));
                                }
                                Log.d("hihhh", response.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("hihhh", "failure");
                    }
                });
            } else if (requestCode ==  PhotoPicker.REQUEST_CODE) {
                Log.d("dataa",data.toString());
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Log.d("photosize",photos.size()+"");
                for(int i = 0;i<photos.size();i++){
                    File f = new File(photos.get(i));
                    Bitmap d = new BitmapDrawable(getActivity().getResources(), f.getAbsolutePath()).getBitmap();
                    Bitmap scaled = AppUtils.getScaledBitmap(512, d);
                    uriList.add(getImageUri(getContext(),scaled));
                    lstBitmap.add(scaled);
                }
                List<MultipartBody.Part> parts = new ArrayList<>();
                for(int i = 0;i<uriList.size();i++){
                    parts.add(prepareFilePart("images", uriList.get(i)));
                }
                MultipartBody.Part[] parts1 = new MultipartBody.Part[parts.size()];
                parts.toArray(parts1);
                Call<ResponseBody> call = fileService.postImage("Bearer " + token, parts1);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                          /* String  a = "[{\"id\":\"5baa6e9bd17a1e4453d5673a\",\"src\":\"http://haihoa.emmasoft.com.vn:9000/cuxa/5ba2df77d07ed52ff0d8e60b/images-1537896091152.jpeg\"}" +
                                   ",{\"id\":\"5baa6e9bd17a1e4453d56739\",\"src\":\"http://haihoa.emmasoft.com.vn:9000/cuxa/5ba2df77d07ed52ff0d8e60b/images-1537896091152.jpeg\"}]";*/
                            try {
                                JSONArray jArray = new JSONArray(response.body().string());
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject object = jArray.optJSONObject(i);
                                    lstImageObject.add(new ImageObject(object.getString("id"),object.getString("src")));
                                    FragmentUpRoom.imageHinhId.add(object.getString("id"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("onFailure uploadImage", "failure");
                    }
                });
                sendBackImage(lstBitmap);
            }
        }else{
            Log.d("dattaaa","null");
        }

    }

    public void setOnChooseReasonListener(OnChooseReasonListener listener) {
        this.listener = listener;
    }

    public void sendBackImage(List<Bitmap> data) {
        for(int i = 0;i<data.size();i++){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            data.get(i).compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            listByteImage.add(byteArray);
        }

        listener.onChooseReason(listByteImage, position);
        dismiss();
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(getContext(), fileUri);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(getActivity().getContentResolver().getType(fileUri)), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==CAMERA && grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
        }else if(requestCode==20 && grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
          /*  PhotoPicker.builder()
                    .setPhotoCount(10 - position)
                    .setShowCamera(true)
                    .setShowGif(true)
                    .setPreviewEnabled(false)
                    .start(getActivity(), PhotoPicker.REQUEST_CODE);*/
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
