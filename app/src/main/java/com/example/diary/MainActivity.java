package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout_container, ll;
    CardView cv;
    ConstraintLayout no_posts_container;
    Snackbar sb_exit;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(SetupActivity.my_PREFERENCES, MODE_PRIVATE);
        String checkName = sp.getString(SetupActivity.PREF_name, "");
        String checkPassword = sp.getString(SetupActivity.PREF_pass, "");

        if (!WelcomeActivity.loggedIn) {
            if (!checkName.equals("") && !checkPassword.equals("")) {
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(this, SetupActivity.class);
                startActivity(intent);
            }
        }
        else {
            WelcomeActivity.loggedIn = false;
            layout_container = findViewById(R.id.layout_container);
            no_posts_container = findViewById(R.id.no_posts_container);
            sb_exit = Snackbar.make(layout_container, "Press back again to exit", Snackbar.LENGTH_SHORT);

            int checkPosts = sp.getInt(SetupActivity.PREF_posts, 0);

            if (checkPosts != 0) {
                no_posts_container.setVisibility(View.GONE);

                try {
                    FileInputStream fis = openFileInput(AddPostActivity.FILE_NAME);
                    int ctr;
                    StringBuffer buffer = new StringBuffer();

                    while ((ctr = fis.read()) != -1) {
                        buffer = buffer.append((char)ctr);
                    }

                    String[] lines = buffer.toString().split("/\n");

                    for (String line : lines) {
                        String[] info = line.split(",");

                        //Card View
                        cv = new CardView(this);

                        //Linear Layout
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(30, 30, 30, 30);

                        ll = new LinearLayout(this);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        ll.setPadding(30, 30, 30,30);
                        cv.setLayoutParams(lp);

                        //Date
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                        String currentDateandTime = sdf.format(new Date());

                        //Text View
                        TextView tv_title = new TextView(this);
                        TextView tv_date = new TextView(this);
                        TextView tv_body = new TextView(this);
                        tv_title.setTextAppearance(this, androidx.appcompat.R.style.TextAppearance_AppCompat_Title);
                        tv_date.setTextAppearance(this, androidx.appcompat.R.style.TextAppearance_AppCompat_Caption);
                        tv_title.setText(info[0]);
                        tv_body.setText(info[1]);
                        tv_date.setText("Date Posted: " + currentDateandTime + "\n");

                        //Add views
                        ll.addView(tv_title);
                        ll.addView(tv_date);
                        ll.addView(tv_body);
                        cv.addView(ll);
                        layout_container.addView(cv);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_posts:
                try {
                    FileOutputStream fos = openFileOutput(AddPostActivity.FILE_NAME, MODE_PRIVATE);
                    fos.close();

                    SharedPreferences.Editor editor = sp.edit();

                    editor.putInt(SetupActivity.PREF_posts, 0);
                    editor.apply();

                    layout_container.removeAllViews();
                    no_posts_container.setVisibility(View.VISIBLE);

                    Toast.makeText(this, "Posts Reset Sucessful!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.reset_app:
                try {
                    FileOutputStream fos = openFileOutput(AddPostActivity.FILE_NAME, MODE_PRIVATE);
                    fos.close();

                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString(SetupActivity.PREF_name, "");
                    editor.putString(SetupActivity.PREF_pass, "");
                    editor.putInt(SetupActivity.PREF_posts, 0);
                    editor.apply();

                    Intent refresh = new Intent(this, SetupActivity.class);
                    startActivity(refresh);

                    Toast.makeText(this, "App Reset Successful!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (sb_exit.isShown()) {
            finishAffinity();
        } else {
            sb_exit.show();
        }
    }

    public void addPost(View view){
        Intent intent = new Intent(this, AddPostActivity.class);
        WelcomeActivity.loggedIn = true;
        startActivity(intent);
    }
}