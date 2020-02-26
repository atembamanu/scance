package com.blueman.scanwithit.qrcode.ui;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blueman.scanwithit.R;
import com.blueman.scanwithit.qrcode.models.Student;
import com.blueman.scanwithit.qrcode.models.UserData;
import com.blueman.scanwithit.qrcode.models.network.ApiClient;
import com.blueman.scanwithit.qrcode.models.network.ApiInterface;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity {

    @BindView(R.id.scan_qrcode)
    SurfaceView surfaceView;
    @BindView(R.id.txt_confirm)
    TextView textViewConfirm;
    @BindView(R.id.txt_false)
    TextView txtNo;
    @BindView(R.id.txt_true)
    TextView txtYES;
    @BindView(R.id.txt_final)
    TextView txtOK;
    @BindView(R.id.mView)
    View mView;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private  ApiInterface apiInterface;

    private String userName;
    private String userEmail;
    private String userClass;
    private int userId;
    private String responseMessage;
    private String responseMessage2;
    private int responseCode;
    private String errorMessage;
    private Boolean isErrorMessage = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(640, 400).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                externalStoragePermission();
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0) {
                    getUserDataFromDatabase(qrCodes.valueAt(0).displayValue);
                    StringBuilder stringBuilder = new StringBuilder();
                    if (responseCode == 401) {
                        isErrorMessage = true;
                        errorMessage = responseMessage;
                        mView.setVisibility(View.INVISIBLE);
                        txtNo.setVisibility(View.INVISIBLE);
                        txtYES.setVisibility(View.INVISIBLE);
                    } else if (responseCode == 100) {
                        isErrorMessage = true;
                        errorMessage = responseMessage2;
                        mView.setVisibility(View.INVISIBLE);
                        txtNo.setVisibility(View.INVISIBLE);
                        txtYES.setVisibility(View.INVISIBLE);
                    } else {
                        if (userName == null && userEmail == null && userClass == null) {
                            isErrorMessage = true;
                            errorMessage = "I couldn\'t get that right, Focus well for best results";
                            mView.setVisibility(View.INVISIBLE);
                            txtNo.setVisibility(View.INVISIBLE);
                            txtYES.setVisibility(View.INVISIBLE);
                        } else {
                            isErrorMessage = false;
                            stringBuilder.append("Got it..Is this you?").append("\n\n").append(userName).append("\n").append(userEmail).append("\n").append(userClass);
                            mView.setVisibility(View.VISIBLE);
                            txtNo.setVisibility(View.VISIBLE);
                            txtYES.setVisibility(View.VISIBLE);
                        }
                    }

                    textViewConfirm.post(() -> {
                        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        assert vibrator != null;
                        vibrator.vibrate(50);
                        textViewConfirm.setText(isErrorMessage ? errorMessage : stringBuilder.toString());
                        textViewConfirm.setTextColor(isErrorMessage ? getResources().getColor(R.color.md_red_A200) : getResources().getColor(R.color.black_shade));


                        txtYES.setOnClickListener(v->{
                            //update user attendance status in the database
                            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                            Call<Student> call = apiInterface.updateAttendance(userId, "present");

                            call.enqueue(new Callback<Student>() {
                                @Override
                                public void onResponse(Call<Student> call, Response<Student> response) {
                                    if (response.isSuccessful() && response.body() != null){
                                        if(response.body().getCode() == 200){
                                            textViewConfirm.setText(response.body().getMessage());
                                            mView.setVisibility(View.INVISIBLE);
                                            txtNo.setVisibility(View.INVISIBLE);
                                            txtYES.setVisibility(View.INVISIBLE);
                                            txtOK.setVisibility(View.VISIBLE);
                                        }else{
                                            textViewConfirm.setText(response.body().getMessage());
                                            mView.setVisibility(View.INVISIBLE);
                                            txtNo.setVisibility(View.INVISIBLE);
                                            txtYES.setVisibility(View.INVISIBLE);
                                            txtOK.setVisibility(View.VISIBLE);

                                        }
                                    }else{
                                        assert response.body() != null;
                                        textViewConfirm.setText(response.body().getMessage());
                                        mView.setVisibility(View.INVISIBLE);
                                        txtNo.setVisibility(View.INVISIBLE);
                                        txtYES.setVisibility(View.INVISIBLE);
                                        txtOK.setVisibility(View.VISIBLE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Student> call, Throwable t) {
                                    textViewConfirm.setText(t.getMessage());

                                }

                            });
                        });
                    });
                }
            }
        });

        txtNo.setOnClickListener(v -> {
           refreshActivity();
        });
        txtOK.setOnClickListener(v->{
            refreshActivity();
        });



    }
    public void refreshActivity(){
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

    }

    private void getUserDataFromDatabase(final String QR_code) {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserData> call = apiInterface.getUserData(QR_code);

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        userName = response.body().getData().getStudentName();
                        userEmail = response.body().getData().getStudentEmail();
                        userClass = response.body().getData().getStudentClass();
                        userId = Integer.parseInt(response.body().getData().getStudentId());
                        responseCode = response.body().getCode();
                        //end progress bar
                    } else {
                        responseMessage = response.body().getMessage();
                        responseCode = response.body().getCode();
                    }
                } else {
                    assert response.body() != null;
                    responseMessage2 = response.body().getMessage();
                    responseCode = response.body().getCode();

                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(ScanActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void externalStoragePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
}
