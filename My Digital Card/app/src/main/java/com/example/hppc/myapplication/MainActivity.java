package com.example.hppc.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.name;

public class MainActivity extends Activity implements View.OnClickListener {

    VideoView videoview;
    LinearLayout first;
    CircleImageView manuel, linkedin;
    String profilelink ;
    String designation;
    String firstName;
    String lastName;
    String pictureUrl;
    String emailAddress;


//     private  static  int ctr = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("counter",ctr);
//        editor.commit();
        manuel = (CircleImageView) findViewById(R.id.manuel);
        manuel.setOnClickListener(this);
        linkedin = (CircleImageView) findViewById(R.id.linledin);
        linkedin.setOnClickListener(this);

        videoview = (VideoView) findViewById(R.id.li);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash1);

        videoview.setVideoURI(video);
//        videoview.requestFocus();

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (isFinishing())
                    return;
                SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
                String check = sharedPreferences.getString("name",null);

                if (check == null) {
                    ViewGroup relativeLayout1 = (ViewGroup) findViewById(R.id.rel2);
                    Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.moverestup);
                    //use this to make it longer:  animation.setDuration(1000);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }
                    });
                    relativeLayout1.bringToFront();
                    relativeLayout1.startAnimation(animation1);
                    relativeLayout1.setVisibility(View.VISIBLE);
                    ViewGroup relativeLayout = (ViewGroup) findViewById(R.id.rel1);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.movevideoup);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }
                    });

                    relativeLayout.startAnimation(animation);
                } else {
                    Intent i=new Intent(MainActivity.this,cardformate.class);
                    startActivity(i);
                    finish();
                }

            }
        });
        videoview.start();
//        computePakageHash();

    }


//    private void computePakageHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.example.hppc.myapplication",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (Exception e) {
//            Log.e("TAG",e.getMessage());
//        }
//    }



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.manuel:
                startActivity(new Intent(MainActivity.this,registration_manual.class));
                break;
            case R.id.linledin:
                 handleLogin();
                break;


        }

    }

   private void handleLogin(){
        LISessionManager.getInstance(getApplicationContext()).init(this, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess() {
                fetchPersonalInfo();
            }

            @Override
            public void onAuthError(LIAuthError error) {
                // Handle authentication errors
                Log.e("Error",error.toString());
            }
        }, true);
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void fetchPersonalInfo(){
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,headline,public-profile-url,picture-url,email-address,picture-urls::(original))";

        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                try {
                    JSONObject jsonObject = apiResponse.getResponseDataAsJson();
                    profilelink = jsonObject.getString("publicProfileUrl");
                    designation = jsonObject.getString("headline");
                    firstName = jsonObject.getString("firstName");
                    lastName = jsonObject.getString("lastName");
                     pictureUrl = jsonObject.getString("pictureUrl");
                    emailAddress = jsonObject.getString("emailAddress");
                    Intent intent = new Intent(MainActivity.this,Registeration.class);
//                    Intent intent = new Intent();
                    intent.putExtra("Fname",firstName + lastName);
                    intent.putExtra("headline",designation);
                    intent.putExtra("email",emailAddress);
                    intent.putExtra("picture",pictureUrl);
                    intent.putExtra("profile",profilelink);
//                    startActivity(new Intent(MainActivity.this, Registeration.class));
                    startActivity(intent);


//                    Picasso.with(getApplicationContext()).load(pictureUrl).into(imgProfile);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
                Log.e("Error",liApiError.getMessage());
            }
        });
    }

}





//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//
//    CircleImageView manuel,linkedin;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        manuel=(CircleImageView)findViewById(R.id.manuel);
//        manuel.setOnClickListener(this);
//        linkedin=(CircleImageView)findViewById(R.id.linledin);
//        linkedin.setOnClickListener(this);
//
//
//
//    }
//    public void onClick(View view) {
//        switch(view.getId()){
//            case R.id.manuel:
//                startActivity(new Intent(this,Registeration.class) );
//                break;
//            case R.id.linledin:
//                startActivity(new Intent(this,Registeration.class) );
//                break;
//
//
//
//        }
//    }
//
//
//
//}
