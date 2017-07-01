package com.example.hppc.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import app.AppController;
import de.hdodenhof.circleimageview.CircleImageView;

public class edit extends AppCompatActivity  implements View.OnClickListener{

    private static final int result_image = 1;

    Button create;
    EditText username,designation,company,location,dno,tno,chat,fb,email,link,linkedin,skype,wno,gid,notes,cell;
    CircleImageView pic;
    String base64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        username=(EditText)findViewById(R.id.username);
        designation=(EditText)findViewById(R.id.designation);
        company=(EditText)findViewById(R.id.company);
        location=(EditText)findViewById(R.id.location);

        cell=(EditText) findViewById(R.id.cell);
        dno =(EditText)findViewById(R.id.dno);
        email=(EditText)findViewById(R.id.email);
        tno=(EditText)findViewById(R.id.tno);
        chat =(EditText)findViewById(R.id.chat);
        link =(EditText)findViewById(R.id.link);
        fb=(EditText)findViewById(R.id.fb);
        linkedin=(EditText)findViewById(R.id.linkedin);

        skype=(EditText)findViewById(R.id.skype);
        gid=(EditText)findViewById(R.id.gid);
        wno=(EditText)findViewById(R.id.wno);
        notes=(EditText)findViewById(R.id.notes);
        pic = (CircleImageView)findViewById(R.id.pic);


//        String fname=getIntent().getExtras().getString("Fname");
//        username.setText(fname);

        SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
        username.setText(sharedPreferences.getString("name",null));
        designation.setText(sharedPreferences.getString("designation",null));
        company.setText(sharedPreferences.getString("company",null));
        location.setText(sharedPreferences.getString("location",null));
        cell.setText(sharedPreferences.getString("phone",null));
        dno.setText(sharedPreferences.getString("chat",null));
        email.setText(sharedPreferences.getString("email",null));
        tno.setText(sharedPreferences.getString("tno",null));
        chat.setText(sharedPreferences.getString("chat",null));
        link.setText(sharedPreferences.getString("website",null));
        fb.setText(sharedPreferences.getString("facebook",null));
        linkedin.setText(sharedPreferences.getString("linkedin",null));
        skype.setText(sharedPreferences.getString("whatsapp",null));
        gid.setText(sharedPreferences.getString("skype",null));
        wno.setText(sharedPreferences.getString("google",null));
        notes.setText(sharedPreferences.getString("notes",null));
         String temp =sharedPreferences.getString("imagePreferance","");
        assert temp != null;
        if(!temp.equals("photo"))
        {
            byte[] b = Base64.decode(temp, Base64.DEFAULT);
            InputStream is = new ByteArrayInputStream(b);
            Bitmap bit = BitmapFactory.decodeStream(is);
            pic.setImageBitmap(bit);
        }


        create=(Button)findViewById(R.id.create);
        create.setOnClickListener(this);
        pic=(CircleImageView)findViewById(R.id.pic);
        pic.setOnClickListener(this);


    }

    public void call_edit_api(){

        BitmapDrawable drawable = (BitmapDrawable) pic.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
        pic.setImageBitmap(bitmap);
        base64=encodeTobase64(bitmap);

        String tag_json_obj="json_obj_req";
        String url="http://35.167.28.22/DCard/addcontact.php";

        JSONObject obj = new JSONObject();
        try {
            obj.put("name", username.getText().toString());
            obj.put("designation",designation.getText().toString() );
            obj.put("company", company.getText().toString());
            obj.put("location", location.getText().toString());
            obj.put("base64Image",encodeTobase64(bitmap));
            obj.put("picture", "");
            obj.put("phone",cell.getText().toString() );
            obj.put("chat", chat.getText().toString());
            obj.put("email", email.getText().toString());
            obj.put("website", link.getText().toString());
            obj.put("facebook",fb .getText().toString());
            obj.put("linkedin", linkedin.getText().toString());
            obj.put("whatsapp", wno.getText().toString());
            obj.put("skype", skype.getText().toString());
            obj.put("google",gid.getText().toString());
            obj.put("notes", notes.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("TAG",obj.toString());


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, obj,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        pDialog.hide();
                        SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String profileId = null;
                        String profileLink = null;
                        try {
                            profileId = response.getString("profileId");
                            profileLink = response.getString("profileLink");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        editor.putString("profileId", profileId );
                        editor.putString("profileLink", profileLink);
                        editor.commit();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    public void store_sp(){

        BitmapDrawable drawable = (BitmapDrawable) pic.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
        pic.setImageBitmap(bitmap);
        base64=encodeTobase64(bitmap);

        SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String name = username.getText().toString();
        String des = designation.getText().toString();
        String comp = company.getText().toString();
        String loc = location.getText().toString();
//        String base64Image = encodedImage;
        String phone = cell.getText().toString();
        String cht = chat.getText().toString();
        String mail = email.getText().toString();
        String website = link.getText().toString();
        String facebook = fb.getText().toString();
        String lnkdin = linkedin.getText().toString();
        String whatsapp = wno.getText().toString();
        String skyp = skype.getText().toString();
        String google = gid.getText().toString();
        String nts = notes.getText().toString();
//        String facebk = fb.getText().toString();


        if ((!TextUtils.isEmpty(name))&&(!TextUtils.isEmpty(des))&&(!TextUtils.isEmpty(phone))&&(!TextUtils.isEmpty(mail))) {

            editor.putString("name", username.getText().toString());
            editor.putString("designation",designation.getText().toString() );
            editor.putString("company", company.getText().toString());
            editor.putString("location", location.getText().toString());
            editor.putString("base64Image","");
            editor.putString("picture", "");
            editor.putString("phone",cell.getText().toString() );
            editor.putString("chat", chat.getText().toString());
            editor.putString("email", email.getText().toString());
            editor.putString("website", link.getText().toString());
            editor.putString("facebook",fb .getText().toString());
            editor.putString("linkedin", linkedin.getText().toString());
            editor.putString("whatsapp", wno.getText().toString());
            editor.putString("skype", skype.getText().toString());
            editor.putString("google",gid.getText().toString());
            editor.putString("notes", notes.getText().toString());
            editor.putString("imagePreferance", encodeTobase64(bitmap));

            editor.commit();
            Toast.makeText(this, "data saved", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, cardformate.class);
            startActivity(i);
            finish();
        } else {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast,
                    (ViewGroup) findViewById(R.id.custom_toast_container));
            TextView tname = layout.findViewById(R.id.toast1);
            TextView tdes = layout.findViewById(R.id.toast2);
            TextView tnum = layout.findViewById(R.id.toast4);
            TextView tmail = layout.findViewById(R.id.toast3);



            if (TextUtils.isEmpty(name)){
                tname.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(des)){
                tdes.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(mail)){
                tmail.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(phone)){
                tnum.setVisibility(View.VISIBLE);
            }
            Toast toast = new Toast(getApplicationContext());
            //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();


        }

    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.create:
                call_edit_api();
                store_sp();
                break;
            case R.id.pic:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,result_image);
                break;


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == result_image && resultCode == RESULT_OK && data != null ){
            Uri selectedImage = data.getData();
            pic.setImageURI(selectedImage);
//            BitmapDrawable drawable = (BitmapDrawable) pic.getDrawable();
//            Bitmap bitmap = drawable.getBitmap();
//            bitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
//            pic.setImageBitmap(bitmap);
//            base64=encodeTobase64(bitmap);
//            SharedPreferences sharedPreferences=getSharedPreferences("mydata", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor=sharedPreferences.edit();
//            editor.putString("imagePreferance", encodeTobase64(bitmap));
//            editor.commit();
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }


}
