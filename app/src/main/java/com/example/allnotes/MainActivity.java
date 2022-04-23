package com.example.allnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    // Google
//        ImageView google_img ;
//        GoogleSignInOptions gso;
//        GoogleSignInClient gsc;
        // Login email and password
        private EditText mloginemail,mloginpassword;
        private Button mlogin;
        private TextView mgotoforgotpassword,mgotosignup;
        private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        google_img = findViewById(R.id.fab_google);
        //Login
//        getSupportActionBar().hide();

        mloginemail=findViewById(R.id.loginemail);
        mloginpassword=findViewById(R.id.loginpassword);
        mlogin=findViewById(R.id.btnLogin);
        mgotoforgotpassword=findViewById(R.id.gotoforgotpassword);
        mgotosignup=findViewById(R.id.gotosignup);
//      gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//             .requestEmail()
//             .build();
//        gsc = GoogleSignIn.getClient(this,gso);
//        google_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SignIn();
//            }
//        });
        //firebase email+password
       firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,ContentMain.class));
        }

        mgotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });

       mgotoforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ForgotPassword.class));
            }
        });
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=mloginemail.getText().toString().trim();
                String password=mloginpassword.getText().toString().trim();

                if(mail.isEmpty()|| password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Điền vào tất cả các miền",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    // login the user
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                checkMailverfication();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Tài khoản không tồn tại!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            private void checkMailverfication() {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser.isEmailVerified()==true)
                {
                    Toast.makeText(getApplicationContext(), "Đã đăng nhập!",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(MainActivity.this,ContentMain.class));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Xác nhận email của bạn trước!",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                }
            }

        });
    }
//   private void SignIn(){
//        Intent intent = gsc.getSignInIntent();
//        startActivityForResult(intent,100);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//        if (requestCode==100){
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                task.getResult(ApiException.class);
//            finish();
//            startActivity(new Intent(MainActivity.this,ContentMain.class) );
//            }catch (ApiException e){
//                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
//            }
//        }
    }


