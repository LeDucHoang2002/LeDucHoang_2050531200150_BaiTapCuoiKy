package com.example.leduchoang_2050531200150_baitapcuoiky;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Account> listacc;

    TextView editText,editText2,sigup,logIn;
    LinearLayout singUpLayout,logInLayout;
    Button singIn,btn_singUp;
    EditText eMails,passwordss,passwords01;
    CheckBox cbRemember;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listacc = new ArrayList<>();
        listacc.add(new Account("admin","admin"));
        listacc.add(new Account("huy","123"));
        listacc.add(new Account("test","1"));

        editText = (TextView) findViewById(R.id.eMail);
        editText2 = (TextView) findViewById(R.id.passwords);

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
        editText.setText(sharedPreferences.getString("taikhoan",""));
        editText2.setText(sharedPreferences.getString("matkhau",""));
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
                String uername =editText.getText().toString().trim();
                String pass =editText2.getText().toString().trim();
                if (checkpass()){
                    Toast.makeText(MainActivity.this,"Đăng nhập thành công", Toast.LENGTH_SHORT).show();

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
                    Intent profile = new Intent(MainActivity.this,MainActivity2.class);
                    startActivity(profile);
                }
                else {
                    Toast.makeText(MainActivity.this,"Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_singUp.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                boolean next =true;
                if (eMails.getText().toString().equals("")
                        || passwordss.getText().toString().equals("")
                        || passwords01.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"Nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                    next =false;
                }

                if (next && !passwordss.getText().toString().equals(passwords01.getText().toString()))
                {
                    Toast.makeText(MainActivity.this,"Xác nhận sai",Toast.LENGTH_SHORT).show();
                    next =false;
                }

                if (next)
                {

                    listacc.add(new Account(eMails.getText().toString(),passwordss.getText().toString()));
                    logIn.setBackground(getDrawable(R.drawable.switch_trcks));
                    logIn.setTextColor(getColor(R.color.white));
                    sigup.setBackground(getDrawable(R.drawable.switch_tumbs));
                    sigup.setTextColor(getColor(R.color.pinkColor));
                    logInLayout.setVisibility(View.VISIBLE);
                    singUpLayout.setVisibility(View.GONE);
                    //finish();
                }
            }
        });
    }
    private ActivityResultLauncher<Intent> getResult =registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        Bundle dataacc =  data.getBundleExtra("bundle");
                        Account newacc = (Account) dataacc.getSerializable("newacc");
                        listacc.add(newacc);
                        editText.setText(newacc.getUserName());
                        editText2.setText(newacc.getPassWord());
                    }
                    if (result.getResultCode() == Activity.RESULT_CANCELED){
                        Toast.makeText(MainActivity.this,"Bạn chưa tạo tài khoản",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    public boolean checkpass(){
        boolean result = false;
        for (Account acc : listacc) {
            boolean check1 = String.valueOf(editText.getText()).equals(acc.getUserName());
            boolean check2 = String.valueOf(editText2.getText()).equals(acc.getPassWord());
            if (check1 && check2){
                result = true;
            }
        }
        return result;
    }
}