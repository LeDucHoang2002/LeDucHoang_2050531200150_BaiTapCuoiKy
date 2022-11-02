package com.example.leduchoang_2050531200150_baitapcuoiky;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SingInSignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;

    TextView email, pass,sigup,logIn;
    LinearLayout singUpLayout,logInLayout;
    Button singIn,btn_singUp;
    EditText eMails,passwordss,passwords01;
    CheckBox cbRemember;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singin_signup);
        mAuth=FirebaseAuth.getInstance();

        email = (TextView) findViewById(R.id.eMail);
        pass = (TextView) findViewById(R.id.passwords);

        sigup =(TextView) findViewById(R.id.singUp);
        logIn =(TextView) findViewById(R.id.logIn);

        singUpLayout=(LinearLayout)findViewById(R.id.singUpLayout) ;
        logInLayout=(LinearLayout)findViewById(R.id.logInLayout);

        singIn=(Button)findViewById(R.id.singIn);
        btn_singUp=(Button)findViewById(R.id.btn_singUp);

        cbRemember =(CheckBox)findViewById(R.id.cb_Save);

        eMails = (EditText) findViewById(R.id.eMails);
        passwordss = (EditText) findViewById(R.id.passwordss);
        passwords01 = (EditText) findViewById(R.id.passwords01);

        sharedPreferences =getSharedPreferences("data", MODE_PRIVATE);
        email.setText(sharedPreferences.getString("taikhoan",""));
        pass.setText(sharedPreferences.getString("matkhau",""));
        cbRemember.setChecked(sharedPreferences.getBoolean("checked",false));
        sigup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                sigup.setBackground(getDrawable(R.drawable.switch_trcks));
                sigup.setTextColor(getColor(R.color.white));
                logIn.setBackground(getDrawable(R.drawable.switch_tumbs));
                logIn.setTextColor(getColor(R.color.pinkColor));
                singUpLayout.setVisibility(View.VISIBLE);
                logInLayout.setVisibility(View.GONE);
            }
        });
        logIn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                logIn.setBackground(getDrawable(R.drawable.switch_trcks));
                logIn.setTextColor(getColor(R.color.white));
                sigup.setBackground(getDrawable(R.drawable.switch_tumbs));
                sigup.setTextColor(getColor(R.color.pinkColor));
                logInLayout.setVisibility(View.VISIBLE);
                singUpLayout.setVisibility(View.GONE);
            }
        });

        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                String uername =email.getText().toString().trim();
                String pass = SingInSignUp.this.pass.getText().toString().trim();
                if(cbRemember.isChecked()){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("taikhoan",uername);
                    editor.putString("matkhau",pass);
                    editor.putBoolean("checked",true);
                    editor.commit();
                }else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("taikhoan");
                    editor.remove("matkhau");
                    editor.remove("checked");
                    editor.commit();
                }
            }
        });
        btn_singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }
    private void login() {
        String email1,pass1;
        email1=email.getText().toString();
        pass1=pass.getText().toString();
        if (TextUtils.isEmpty(email1)){
            Toast.makeText(this, "Vui Long Nhap Email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(pass1)){
            Toast.makeText(this, "Vui Long Nhap Password", Toast.LENGTH_SHORT).show();
        }
        if (email1.equals("")==false ||pass1.equals("")==false){
            mAuth.signInWithEmailAndPassword(email1,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(SingInSignUp.this, "Dang Nhap Thanh Cong", Toast.LENGTH_SHORT).show();

                        Intent i=new Intent(SingInSignUp.this, HomeActivity.class);
                        startActivity(i);
                    }else {
                        Toast.makeText(SingInSignUp.this, "Dang Nhap Khong Thanh Cong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }private void register() {
        String email1,pass1;
        email1=eMails.getText().toString();
        pass1=passwordss.getText().toString();
        if (TextUtils.isEmpty(email1)){
            Toast.makeText(this, "Vui Long Nhap Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass1)){
            Toast.makeText(this, "Vui Long Nhap Password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email1,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onComplete(@NonNull Task<AuthResult> task) {
                String uername =eMails.getText().toString().trim();
                String password = passwordss.getText().toString().trim();
                if (task.isSuccessful()){
                    Toast.makeText(SingInSignUp.this, "Tao Tai Khoan Thanh Cong", Toast.LENGTH_SHORT).show();
                    logIn.setBackground(getDrawable(R.drawable.switch_trcks));
                    logIn.setTextColor(getColor(R.color.white));
                    sigup.setBackground(getDrawable(R.drawable.switch_tumbs));
                    sigup.setTextColor(getColor(R.color.pinkColor));
                    logInLayout.setVisibility(View.VISIBLE);
                    singUpLayout.setVisibility(View.GONE);
                    email.setText(uername);
                    pass.setText(password);
                }else {
                    Toast.makeText(SingInSignUp.this, "Tao Tai Khoan Khong Thanh Cong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}