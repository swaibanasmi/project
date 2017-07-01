package com.example.hppc.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class cardformate extends AppCompatActivity implements View.OnClickListener {

    CircleImageView share,template;

    ImageView search,edit;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static String name,post,number,area,email,profile_pic;
    static int[] logoim={R.drawable.l0,R.drawable.l1,R.drawable.l2,R.drawable.l3};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardformate);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("mydata", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("name","");
        post = sharedPreferences.getString("designation","");
        number = sharedPreferences.getString("phone","");
        email = sharedPreferences.getString("email","");
        area = sharedPreferences.getString("location","");
        profile_pic =sharedPreferences.getString("imagePreferance","");

        share=(CircleImageView)findViewById(R.id.share);
        share.setOnClickListener(this);
        template=(CircleImageView)findViewById(R.id.template);
        template.setOnClickListener(this);


        search=(ImageView)findViewById(R.id.search);
        search.setOnClickListener(this);
        edit=(ImageView)findViewById(R.id.edit);
        edit.setOnClickListener(this);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mViewPager,true);





    }


    public void onClick(View view) {
        switch(view.getId()){
            case R.id.share:
                startActivity(new Intent(this, share.class));
                break;
            case R.id.template:

                break;

            case R.id.search:
                startActivity(new Intent(this, search.class));
                break;
            case R.id.edit:
                startActivity(new Intent(this, edit.class));
                break;




        }
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container,false);

            ImageView imageView=(ImageView)rootView.findViewById(R.id.logo);
            imageView.setImageResource(logoim[getArguments().getInt(ARG_SECTION_NUMBER)-1]);

//            textView.setText("Template "+getArguments().get(ARG_SECTION_NUMBER));
            ImageView img1=(ImageView)rootView.findViewById(R.id.pic);
            assert profile_pic != null;
            if(!profile_pic.equals("photo"))
            {
                byte[] b = Base64.decode(profile_pic, Base64.DEFAULT);
                InputStream is = new ByteArrayInputStream(b);
                Bitmap bit = BitmapFactory.decodeStream(is);
                img1.setImageBitmap(bit);
            }
            TextView tname=(TextView)rootView.findViewById(R.id.info_text);
            TextView tpost=(TextView)rootView.findViewById(R.id.post);
            TextView tno=(TextView)rootView.findViewById(R.id.num);
            TextView temail=(TextView)rootView.findViewById(R.id.mail);
            TextView tloc=(TextView)rootView.findViewById(R.id.area);


            Log.d("tag",name);
            tname.setText(name);
            tpost.setText(post);
            tno.setText(number);
            temail.setText(email);
            tloc.setText(area);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

    }
}

