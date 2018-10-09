package com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.MyGridView;
import com.example.dell.appcuxa.CustomeView.RobAutoCompleteTextView;
import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobButton;
import com.example.dell.appcuxa.CustomeView.RobCheckBox;
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.CheckBoxAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.ImageAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.PlaceAutoCompleteAdapter;
import com.example.dell.appcuxa.MainPage.MainPageViews.AddPhotoBottomDialogFragment;
import com.example.dell.appcuxa.MainPage.MainPageViews.FragmentUpRoom;
import com.example.dell.appcuxa.MainPage.MainPageViews.Interface.ILogicDeleteImage;
import com.example.dell.appcuxa.MainPage.MainPageViews.SearchTab.GenderBottomDialog;
import com.example.dell.appcuxa.MainPage.MainPageViews.SelectedImageAdapter;
import com.example.dell.appcuxa.ObjectModels.LocationRoom;
import com.example.dell.appcuxa.ObjectModels.RoomObject;
import com.example.dell.appcuxa.ObjectModels.UtilityObject;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.qrcode.encoder.QRCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class FragmentQrCode extends DialogFragment implements
        View.OnClickListener {

    private View mMainView;
    SurfaceView surfaceView;
    RobBoldText tvResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    public static final int requestCameraPermissionID = 1996;

    public FragmentQrCode() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_qrcode, container, false);
        init();

        return mMainView;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.edtGender:

                break;

        }
    }


    /**
     * Ánh xạ view
     */
    public void init() {
        surfaceView = mMainView.findViewById(R.id.cameraQrcode);
        tvResult = mMainView.findViewById(R.id.tvResult);
        barcodeDetector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector).setRequestedPreviewSize(640, 480).build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Request permission
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, requestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size()!=0){
                    tvResult.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            tvResult.setText(qrcodes.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case requestCameraPermissionID:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    try {
                        cameraSource.start(surfaceView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}

