package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class SetupActivity extends AppCompatActivity {

    TextView et_name;
    EditText etp_password, etp_confirmPass;
    public static final String my_PREFERENCES = "UserPrefs";
    public static final String PREF_name = "NameKey";
    public static final String PREF_pass = "PassKey";
    public static final String PREF_posts = "PostsKey";

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        sp = getSharedPreferences(my_PREFERENCES, MODE_PRIVATE);
        et_name = findViewById(R.id.et_name);
        etp_password = findViewById(R.id.etp_password);
        etp_confirmPass = findViewById(R.id.etp_confirmPass);

        String savedName = sp.getString(PREF_name, "");

        if(!savedName.equals("") && savedName != null) {
            et_name.setText(savedName);
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void startMain(View view){
        String diaryName = et_name.getText().toString();
        String password = etp_password.getText().toString();
        String confirmPass = etp_confirmPass.getText().toString();

        if (WelcomeActivity.forgotPass) {
            try {
                FileOutputStream fos = openFileOutput(AddPostActivity.FILE_NAME, MODE_PRIVATE);
                fos.close();

                SharedPreferences.Editor editor = sp.edit();

                editor.putInt(SetupActivity.PREF_posts, 0);
                editor.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!diaryName.equals("")){
            if (!password.equals("") && !confirmPass.equals("")){
                if (password.equals(confirmPass)) {
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString(PREF_name, diaryName);
                    editor.putString(PREF_pass, password);

                    editor.commit();

                    Intent intent = new Intent(this, WelcomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Setup Success!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Password is not the same!", Toast.LENGTH_SHORT).show();
                    etp_password.setText("");
                    etp_confirmPass.setText("");
                    etp_password.requestFocus();
                }
            }
            else {
                Toast.makeText(this, "Please enter a password!", Toast.LENGTH_SHORT).show();
                etp_password.requestFocus();
            }
        }
        else {
            Toast.makeText(this, "Please enter a name!", Toast.LENGTH_SHORT).show();
            et_name.requestFocus();
        }
    }
}