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
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.androidhive.barcode.BarcodeReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

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
    private BarcodeReader barcodeReader;
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
        //new
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);

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

    @Override
    public void onScanned(Barcode barcode) {
    barcodeReader.playBeep();
    if (barcode != null){
        getUserDataFromDatabase(barcode.displayValue);
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
                Calendar c = Calendar.getInstance();
                int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
                int minOfDay = c.get(Calendar.MINUTE);
                String status;
                if (timeOfDay<8 && minOfDay<35){
                    status = "present";
                }else{
                    status = "late";
                }
                Call<Student> call = apiInterface.updateAttendance(userId, status);

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

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Toast.makeText(this, "Error Occured whileloading the scanner", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraPermissionDenied() {
        externalStoragePermission();
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
