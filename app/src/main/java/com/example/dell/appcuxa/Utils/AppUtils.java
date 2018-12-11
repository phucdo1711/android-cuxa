package com.example.dell.appcuxa.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class AppUtils {
    public static final int REQUEST_CAMERA = 11;
    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final String TAG = "CUXA";
    public static final int PICK_IMAGE_1 = 1;
    public static final int PICK_IMAGE_2 = 2;
    public static SharedPreferences sharedPreferences;

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public static Bitmap getScaledBitmap(int maxWidth, Bitmap rotatedBitmap) {
        try {

            int nh = (int) (rotatedBitmap.getHeight() * (512.0 / rotatedBitmap.getWidth()));
            return Bitmap.createScaledBitmap(rotatedBitmap, maxWidth, nh, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Hàm có yêu cầu quyền
     *
     * @param context
     * @return
     */
    public static boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Kiểm tra dịch vụ google có trong trạng thái thỏa mãn không?
     * @param context
     * @return
     */
    public static boolean isServiceOk(Activity context){
        Log.d(TAG,"isServiceOk: checking google service version");
        int variable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        //everything is find and the user can make map requests
        if(variable == ConnectionResult.SUCCESS){
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(variable)){
            //an error occured but we can resolve it
            Log.d(TAG,"an error occured but we can resolve it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(context,variable,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(context, "We cannot make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public static int getImageItemWidth(Activity activity) {
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        int densityDpi = activity.getResources().getDisplayMetrics().densityDpi;
        int cols = screenWidth / densityDpi;
        cols = cols < 3 ? 3 : cols;
        int columnSpace = (int) (2 * activity.getResources().getDisplayMetrics().density);
        return (screenWidth - columnSpace * (cols - 1)) / cols;
    }
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public static String getToken(Activity activity){
        String token = "";
        sharedPreferences = activity.getSharedPreferences("login_data", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        return token;
    }
    public static String getToken(Context activity){
        String token = "";
        sharedPreferences = activity.getSharedPreferences("login_data", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        return token;
    }
    public static String getIdUser(Activity activity){
        String id_user = "";
        sharedPreferences = activity.getSharedPreferences("login_data", MODE_PRIVATE);
        id_user = sharedPreferences.getString("id_user", "");
        return id_user;
    }
    public static String getIdUser(Context context){
        String id_user = "";
        sharedPreferences = context.getSharedPreferences("login_data", MODE_PRIVATE);
        id_user = sharedPreferences.getString("id_user", "");
        return id_user;
    }

    public static String getName(Context context){
        String name = "";
        sharedPreferences = context.getSharedPreferences("login_data", MODE_PRIVATE);
        name = sharedPreferences.getString("name", "");
        return name;
    }

    public static String formatMoney2(String input) {
        if (input.trim().equals("")) {
            return "";
        }
        try {
            Double value = Double.parseDouble(input);
            DecimalFormat formatter = new DecimalFormat("#,###,###.####");
            return formatter.format(value);
//            String[] split = formatter.format(value).split("\\.");
//            return split[0].replaceAll(",", ".") + (split.length > 1 ? "," + split[1] : "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }
    public static void showKeybord(Activity activity, RobEditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }
    public static void hideSoftKeyboard(Activity activity, RobEditText editText) {
        if(activity==null)
        {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    public static String getCurrentUTC(Date time){
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        return outputFmt.format(time);
    }
    public static Date parseStringToDate(String date){
        Date dateP = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateP = format.parse(date);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateP;
    }
    public static String parseDateFromWS(String d){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = format.parse(d);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateTime = format.format(date);
        return dateTime;
    }

}
