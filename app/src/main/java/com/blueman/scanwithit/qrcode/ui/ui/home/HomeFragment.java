package com.blueman.scanwithit.qrcode.ui.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blueman.scanwithit.R;
import com.blueman.scanwithit.qrcode.ui.ScanActivity;
import com.google.zxing.WriterException;

import java.util.Objects;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    private String TAG = "GenerateQrCode";
    private Bitmap bitmap;
    private  ImageView mQRImage;
    private QRGEncoder qrgEncoder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Instantiate the ViewModel and Bind it
        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        SharedPreferences pref = Objects.requireNonNull(this.getActivity()).getSharedPreferences("MyPref", Context.MODE_PRIVATE); // 0 - for private
        final String QRCODE = pref.getString("qrcode_string", null);
        final String STUDENT_NAME = pref.getString("name_string", null);
        final String STUDENT_EMAIL = pref.getString("email_string", null);
        final String STUDENT_CLASS = pref.getString("class_string", null);


        final TextView textView = root.findViewById(R.id.text_home);
        final TextView textEmail = root.findViewById(R.id.text_email);
        final TextView textClass = root.findViewById(R.id.text_class);

        textEmail.setText(STUDENT_EMAIL);
        textClass.setText(STUDENT_CLASS);
        mQRImage = root.findViewById(R.id.qrcode);
        homeViewModel.getText().observe(getActivity(), s -> {
            String finalStringMessage = s + STUDENT_NAME;
            textView.setText(finalStringMessage);
        });

        textEmail.setOnClickListener(v -> {
            launchScanner();
        });
        assert QRCODE != null;
        if(QRCODE.length() > 0){
            WindowManager manager = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
            assert manager != null;
            Display display  = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerdimension = width<height? width:height;
            smallerdimension = smallerdimension * 3/4;

            qrgEncoder = new QRGEncoder(QRCODE, null, QRGContents.Type.TEXT, smallerdimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                mQRImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.d(TAG, e.getMessage());
            }


        }


        return root;
    }

    private void launchScanner() {
        Intent intent = new Intent(getActivity(), ScanActivity.class);
        startActivity(intent);
    }
}