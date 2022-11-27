package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;

public class AddPostActivity extends AppCompatActivity {

    EditText et_postTitle, etml_postBody;
    public static final String FILE_NAME = "Records.txt";
    public static int postsCount;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        et_postTitle = findViewById(R.id.et_postTitle);
        etml_postBody = findViewById(R.id.etml_postBody);
    }

    public void postDiary(View view) {
        sp = getSharedPreferences(SetupActivity.my_PREFERENCES, MODE_PRIVATE);
        String postTitle = et_postTitle.getText().toString();
        String postBody = etml_postBody.getText().toString();

        postsCount = sp.getInt(SetupActivity.PREF_posts, 0);

        if (!postTitle.equals("")) {
            if (!postBody.equals("")) {
                if (WelcomeActivity.loggedIn){
                    String data;
                    data = et_postTitle.getText() + "," + etml_postBody.getText() + "/\n";
                    try {
                        FileOutputStream fos = openFileOutput(FILE_NAME, MODE_APPEND);
                        fos.write(data.getBytes());
                        fos.close();
                        postsCount++;

                        SharedPreferences.Editor editor = sp.edit();

                        editor.putInt(SetupActivity.PREF_posts, postsCount);

                        editor.commit();

                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);

                        Toast.makeText(this, "Post Successful! Saved to Path --" + getFilesDir() + "\t" + FILE_NAME, Toast.LENGTH_LONG).show();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Intent intent = new Intent(this, WelcomeActivity.class);
                    startActivity(intent);

                    Toast.makeText(this, "Please log in first!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                etml_postBody.requestFocus();
                Toast.makeText(this, "Please enter content!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            et_postTitle.requestFocus();
            Toast.makeText(this, "Please enter a title!", Toast.LENGTH_SHORT).show();
        }

    }

    public void viewPosts(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Post Discarded.", Toast.LENGTH_SHORT).show();
    }
}