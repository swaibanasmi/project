package com.example.hppc.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.hppc.myapplication.models.userprofile;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class search_share extends AppCompatActivity implements View.OnClickListener {

    Button butn1;
    Button butn2;
    Button butn3;
    EditText message;
    userprofile user;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String json = bundle.getString("userprofile");
            user = new Gson().fromJson(json, userprofile.class);
        }
        butn1 = (Button) findViewById(R.id.butn1);
        butn1.setOnClickListener(this);
        butn2 = (Button)findViewById(R.id.butn2);
        butn2.setOnClickListener(this);
        butn3 = (Button)findViewById(R.id.butn3);
        butn3.setOnClickListener(this);


        final ViewGroup rlayout = (ViewGroup)findViewById(R.id.rel);
        Animation animate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.goup);

        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rlayout.bringToFront();
        rlayout.startAnimation(animate);
        rlayout.setVisibility(View.VISIBLE);

        Log.d("TAG","1");
    }

    public void share_contact() throws IOException{

        message = (EditText)findViewById(R.id.mssg);
        String data = message.getText().toString();

//        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("mydata", Context.MODE_PRIVATE);
        String name= user.getname();
        String mail= user.getEmail();
        String tel= user.getPhone();
        String des= user.getdesignation();
        String comp= user.getcompany();


        File vcfFile = new File(this.getExternalFilesDir(null), "generated.vcf");
        FileWriter fw = new FileWriter(vcfFile);

        fw.write("BEGIN:VCARD\r\n");
        fw.write("VERSION:3.0\r\n");
//        fw.write("N:" + p.getSurname() + ";" + p.getFirstName() + "\r\n");
        fw.write("N:" + name+ "\r\n");
        fw.write("FN:" + name  + "\r\n");
        fw.write("TEL;TYPE=WORK,VOICE:" + tel + "\r\n");
        fw.write("ORG:" +comp + "\r\n");
        fw.write("TITLE:" + des + "\r\n");
        fw.write("EMAIL;TYPE=PREF,INTERNET:" + mail + "\r\n");
        fw.write("END:VCARD\r\n");
        fw.append(data);
        fw.close();

        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.setType("text/x-vcard");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Contact");
//        i.putExtra(android.content.Intent.EXTRA_TEXT, data);
        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(vcfFile));
        startActivity(Intent.createChooser(i,"Share contact via"));

    }

    public void share_link(){


        String link= user.getProfilelink();

        message = (EditText)findViewById(R.id.mssg);
        String data = message.getText().toString();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, link);
        startActivity(Intent.createChooser(shareIntent, "Share link via"));
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.butn3:
                startActivity(new Intent(this,search.class));
                break;
            case R.id.butn2:
                try {
                    share_contact();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.butn1:
                share_link();

        }
    }

}


