package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class WelcomeActivity extends AppCompatActivity {

    TextView tv_diaryName;
    EditText et_enterPass;
    Button btn_forgotPass;
    SharedPreferences sp;
    public static boolean loggedIn = false;
    public static boolean forgotPass = false;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sp = getSharedPreferences(SetupActivity.my_PREFERENCES, MODE_PRIVATE);

        String diaryName = sp.getString(SetupActivity.PREF_name, "");

        tv_diaryName = findViewById(R.id.tv_diaryName);
        tv_diaryName.setText("This diary belongs to " + diaryName);

        btn_forgotPass = findViewById(R.id.btn_forgotPass);
        btn_forgotPass.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void verifyPass(View view){
        sp = getSharedPreferences(SetupActivity.my_PREFERENCES, MODE_PRIVATE);

        et_enterPass = findViewById(R.id.et_enterPass);

        String enterPass = et_enterPass.getText().toString();
        String savedPass = sp.getString(SetupActivity.PREF_pass, "");

        if(!enterPass.equals("")) {
            if(enterPass.equals(savedPass)){
                Intent intent = new Intent(this, MainActivity.class);
                loggedIn = true;
                startActivity(intent);
            }
            else {
                counter++;
                Toast.makeText(this, "Wrong Password! Please Try Again. " + counter + " tries.", Toast.LENGTH_SHORT).show();
                et_enterPass.setText("");
                et_enterPass.requestFocus();

                if(counter >= 3) {
                    btn_forgotPass.setVisibility(View.VISIBLE);
                    btn_forgotPass.setFocusable(true);
                    btn_forgotPass.setFocusableInTouchMode(true);
                    btn_forgotPass.requestFocus();
                }
            }
        }
        else {
            Toast.makeText(this, "You have not entered a password!", Toast.LENGTH_SHORT).show();
            et_enterPass.requestFocus();
        }
    }

    public void forgotPass(View view) {
        Intent intent = new Intent(this, SetupActivity.class);
        forgotPass = true;
        startActivity(intent);
        Toast.makeText(this, "Changing password will remove all posts since you used Forgot Password.", Toast.LENGTH_LONG).show();
    }
}