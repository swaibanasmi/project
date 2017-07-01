package com.example.hppc.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hppc.myapplication.models.userprofile;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.hppc.myapplication.R.id.progressBar;
import static com.example.hppc.myapplication.R.id.spost;

public class search_card extends AppCompatActivity implements View.OnClickListener {

    CircleImageView share;
    public static userprofile user;

    ImageView search,backb;

    private ViewPager mViewPager;
    static int[] logoim = { R.drawable.l0,R.drawable.l1,R.drawable.l2,R.drawable.l3};

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_card);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String json = bundle.getString("userprofile");
            user = new Gson().fromJson(json, userprofile.class);
        }

        share=(CircleImageView)findViewById(R.id.share);
        share.setOnClickListener(this);


        search=(ImageView)findViewById(R.id.search);
        search.setOnClickListener(this);
        backb=(ImageView)findViewById(R.id.back);
        backb.setOnClickListener(this);

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
                Intent i=new Intent(this, search_share.class);
                i.putExtra("userprofile",new Gson().toJson(user));
                startActivity(i);
                break;
            case R.id.search:
                startActivity(new Intent(this, search.class));
                break;
            case R.id.back:
                startActivity(new Intent(this, search.class));
                break;



        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
            View rootView = inflater.inflate(R.layout.fragment_share, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.logo);
            imageView.setImageResource(logoim[getArguments().getInt(ARG_SECTION_NUMBER) - 1]);
            final ImageView img;
             img = (ImageView) rootView.findViewById(R.id.spic);
            ImageLoader.getInstance().displayImage(user.getImage(), img, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    //progressBar.setVisibility(View.VISIBLE);
                    img.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    //progressBar.setVisibility(View.GONE);
                    img.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    //progressBar.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    //progressBar.setVisibility(View.GONE);
                    img.setVisibility(View.INVISIBLE);
                }
            });

//            URL url = null;
//            try {
//                url = new URL(user.getImage());
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//
//            img.setImageURI(url);
//            String profile_pic = user.getButtonimage();
//            assert profile_pic != null;
//            if(!profile_pic.equals("photo"))
//            {
//                byte[] b = Base64.decode(profile_pic, Base64.DEFAULT);
//                InputStream is = new ByteArrayInputStream(b);
//                Bitmap bit = BitmapFactory.decodeStream(is);
//                img.setImageBitmap(bit);
//            }

            TextView tname = (TextView) rootView.findViewById(R.id.sinfo_text);
            TextView tpost = (TextView) rootView.findViewById(spost);
            TextView tno = (TextView) rootView.findViewById(R.id.snum);
            TextView temail = (TextView) rootView.findViewById(R.id.smail);

            String name = user.getname();
            String mail = user.getEmail();
            String tel = user.getPhone();
            String des = user.getdesignation();


            tname.setText(name);
            tpost.setText(des);
            tno.setText(tel);
            temail.setText(mail);

            return rootView;
        }
    }


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