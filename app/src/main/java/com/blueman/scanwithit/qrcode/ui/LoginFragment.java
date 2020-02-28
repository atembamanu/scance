package com.blueman.scanwithit.qrcode.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blueman.scanwithit.R;
import com.blueman.scanwithit.qrcode.models.UserData;
import com.blueman.scanwithit.qrcode.models.network.ApiClient;
import com.blueman.scanwithit.qrcode.models.network.ApiInterface;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment{
    @BindView(R.id.logintxtEmail) EditText _email;
    @BindView(R.id.logintxtPwd) EditText _pass;
    @BindView(R.id.mprogress_bar)
    ProgressBar progressBar;

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
       FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment bf = new RegisterFragment();
        fragmentTransaction.replace(R.id.mcontainer, bf);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loginUser() {
        progressBar.setVisibility(View.VISIBLE);
        String student_email = _email.getText().toString();
        String student_pass = _pass.getText().toString();
        authenticateUser(student_email, student_pass);
    }

    private void authenticateUser(final String student_email, final String student_pass) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserData> call = apiInterface.performLogin(student_email, student_pass);

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().getCode() == 200 ){
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //store qrcode in shared pref file
                        int std_Id = Integer.parseInt(response.body().getData().getStudentId());
                        SharedPreferences pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("qrcode_string", response.body().getData().getQRCode());
                        editor.putString("name_string", response.body().getData().getStudentName());
                        editor.putString("email_string", response.body().getData().getStudentEmail());
                        editor.putString("class_string", response.body().getData().getStudentClass());
                        editor.putInt("student_id", std_Id);
                        editor.apply();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(getActivity(), DashActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), response.body().getCode() +", "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Oops, "+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
