package com.example.allnotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText mforgotpassword;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

//        getSupportActionBar().hide();

        mforgotpassword=findViewById(R.id.forgotpassword);
        Button mpasswordrecoverbutton = findViewById(R.id.passwordrecoverbutton);
        TextView mgobacktologin = findViewById(R.id.gobacktologin);

        firebaseAuth= FirebaseAuth.getInstance();

        mgobacktologin.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPassword.this,MainActivity.class);
            startActivity(intent);
        });

        mpasswordrecoverbutton.setOnClickListener(view -> {
            String mail = mforgotpassword.getText().toString().trim();
            if (mail.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Nhập vào email của bạn", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //we have to send password recover email
                firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(task -> {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Đã gửi email! Bạn có thể khôi phục mật khẩu bằng email.", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Email không đúng hoặc tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}