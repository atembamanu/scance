package com.blueman.scanwithit.qrcode.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blueman.scanwithit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterFragment extends Fragment {
    @BindView(R.id.txtEmail) EditText student_email;
    @BindView(R.id.regtxtName) EditText student_name;
    @BindView(R.id.regtxtClass) EditText student_class;
    @BindView(R.id.regtxtSec) EditText security_word;
    @BindView(R.id.regtxtPwd) EditText student_pass;

    @BindView(R.id.regbtnRegister)
    Button register;
    @BindView(R.id.lnkLogin)
    TextView goToLogin;

    private FragmentInterface fragmentInterface;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        ButterKnife.bind(this, view);

        register.setOnClickListener(v -> registerStudent());

        goToLogin.setOnClickListener(v -> {
            if (fragmentInterface!=null){
                fragmentInterface.changeFragment();
            }
        });
        return view;
    }

    private void registerStudent() {
        Toast.makeText(getContext(), "Checking....", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setFragmentInterface(FragmentInterface fragmentInterface){
        this.fragmentInterface = fragmentInterface;
    }
}
