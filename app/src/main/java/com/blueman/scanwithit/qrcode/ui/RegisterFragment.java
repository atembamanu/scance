package com.blueman.scanwithit.qrcode.ui;

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
import com.blueman.scanwithit.qrcode.models.network.ApiClient;
import com.blueman.scanwithit.qrcode.models.network.ApiInterface;
import com.blueman.scanwithit.qrcode.models.Student;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    @BindView(R.id.txtEmail) EditText _email;
    @BindView(R.id.regtxtName) EditText _name;
    @BindView(R.id.regtxtClass) EditText _class;
    @BindView(R.id.regtxtSec) EditText _sword;
    @BindView(R.id.regtxtPwd) EditText _pass;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.regbtnRegister)
    Button register;
    @BindView(R.id.lnkLogin)
    TextView goToLogin;


    //
    private String student_email;
    private String student_name;
    private String student_class;
    private String student_pass;
    private String security_word;
    private String QR_code;

    private FragmentInterface fragmentInterface;
    private ApiInterface apiInterface;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        ButterKnife.bind(this, view);

        register.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            student_email = _email.getText().toString();
            student_name = _name.getText().toString();
            student_class = _class.getText().toString();
            student_pass = _pass.getText().toString();
            security_word = _sword.getText().toString();
            QR_code = student_email.concat(security_word);
            registerStudent(student_name, student_email,security_word, student_class, QR_code, student_pass);
        });

        goToLogin.setOnClickListener(v -> {
            if (fragmentInterface!=null){
                fragmentInterface.changeFragment();
            }
        });
        return view;
    }

    private void registerStudent(final String student_name, final  String student_email, final String security_word, final String student_class, final String QR_code, final  String student_pass) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Student> call = apiInterface.registerStudent(student_name, student_email,security_word, student_class, QR_code, student_pass);

        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if(response.isSuccessful() && response.body() !=null){
                    if (response.body().getCode() == 200 ){
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        assert getFragmentManager() != null;
                        getFragmentManager().popBackStack();
                    }else{
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), response.body().getCode() +", "+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void nowYouCanLogin() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setFragmentInterface(FragmentInterface fragmentInterface){
        this.fragmentInterface = fragmentInterface;
    }
}
