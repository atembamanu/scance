package com.blueman.scanwithit.qrcode.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.blueman.scanwithit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntroActivity extends AppCompatActivity {

    @BindView(R.id.mContinue)
    Button mContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ButterKnife.bind(this);

        mContinue.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountsActivity.class);
            startActivity(intent);
        });
    }
}
