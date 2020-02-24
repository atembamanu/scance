package com.blueman.scanwithit.qrcode.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blueman.scanwithit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment {

    @BindView(R.id.logintxtEmail) EditText student_email;
    @BindView(R.id.logintxtPwd) EditText student_pass;

    @BindView(R.id.mbtnLogin) Button login;
    @BindView(R.id.lnkRegister) TextView goTORegisterLnk;

    private FragmentInterface fragmentInterface;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        ButterKnife.bind(this, view);

        login.setOnClickListener(v -> loginUser());

        goTORegisterLnk.setOnClickListener(v -> takeUserToRegisterFragment());
        return view;
    }

    private void takeUserToRegisterFragment() {
        if (fragmentInterface !=null){
            fragmentInterface.changeFragment();
        }

    }

    private void loginUser() {

    }

    public void setFragmentInterface(FragmentInterface fragmentInterface){
        this.fragmentInterface = fragmentInterface;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        //SharedPreferences sharedPreferences = context.getSharedPreferences("usersFile", Context.MODE_PRIVATE);
       // SharedPreferences.Editor editor = sharedPreferences.edit();

        super.onAttach(context);
    }
}
