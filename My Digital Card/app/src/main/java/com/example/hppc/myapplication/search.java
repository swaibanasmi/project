package com.example.hppc.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hppc.myapplication.models.userprofile;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.hppc.myapplication.models.userprofile;

public class search extends AppCompatActivity {
    public ListView listview;
    public ImageView img;
    private ProgressDialog dialog;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//

        //ListView listview =(ListView)findViewById(R.id.list_item);
        //ArrayList<String> names = new ArrayList<>();
        //names.addAll(Arrays.asList(getResources().getStringArray(R.array.names)));
        //adapter = new ArrayAdapter<String>(searchableactivity.this,android.R.layout.simple_list_item_1,names);
        //listview.setAdapter(adapter);
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
//
//            }
//        });
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading. Please wait..."); // showing a dialog for loading the data
        // Create default options which will be used for every
        //  displayImage(...) call if no options will be passed to this method
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        listview = (ListView) findViewById(R.id.list_item);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        //setSupportActionBar(toolbar);
        searchView = (SearchView) findViewById(R.id.searc);
        searchView.setBackgroundColor(ContextCompat.getColor(this,R.color.white));

        if (searchView != null) {
            int searchPlateId = getResources().getIdentifier("android:id/search_plate", null, null);
            ViewGroup viewGroup = (ViewGroup) searchView.findViewById(searchPlateId);

            if (viewGroup != null) {
                viewGroup.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            }
            int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = (TextView) searchView.findViewById(id);

            if (textView != null) {
                textView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
                textView.setHintTextColor(ContextCompat.getColor(this, R.color.black));

                //textView.setGravity(Gravity.CENTER);
            }
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String urlString = "http://35.167.28.22/DCard/search.php?search=" + query;
                listview = (ListView) findViewById(R.id.list_item);
                new JSONTask().execute(urlString);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                return false;
            }


        });
//        getSupportActionBar().setTitle("@string/title");
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
        //img=(ImageView)findViewById(R.id.place);

    }

    public class JSONTask extends AsyncTask<String, String, List<userprofile>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<userprofile> doInBackground(String... params) {
            //List<userprofile> updatableList = new ArrayList<>();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            //List<userprofile> updatableList=new ArrayList<>();

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("Search");


                List<userprofile> updatableList = new ArrayList<>();

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    userprofile user = new userprofile();
                    user.setname(finalObject.getString("name"));
                    user.setDesignation(finalObject.getString("desigination"));
                    user.setImage(finalObject.getString("profilePicture"));
                    user.setphone(finalObject.getString("phone"));
                    user.setEmail(finalObject.getString("email"));
                    user.setCompany(finalObject.getString("company"));
                    user.setProfilelink(finalObject.getString("profileLink"));
                    updatableList.add(user);

                }
                Log.d("tag", updatableList.toString());
                return updatableList;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final List<userprofile> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null) {
                userAdapter adapter = new userAdapter(getApplicationContext(), R.layout.row, result);
                listview.setAdapter(adapter);
                listview.setEmptyView(findViewById(R.id.place));
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        userprofile user = result.get(position); // getting the model
                        Intent intent = new Intent(search.this, search_card.class);
                        intent.putExtra("userprofile", new Gson().toJson(user)); // converting model json into string type and sending it via intent
                        startActivity(intent);
                    }
                });
//
            } else {
                Toast.makeText(getApplicationContext(), "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }


        }
    }


    public class userAdapter extends ArrayAdapter {

        private List<userprofile> userList;
        private int resource;
        private LayoutInflater inflater;

        public userAdapter(Context context, int resource, List<userprofile> objects) {
            super(context, resource, objects);
            userList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(resource, null);
                holder.propic = (ImageView) convertView.findViewById(R.id.ivIcon);
                holder.names = (TextView) convertView.findViewById(R.id.name);
                holder.designations = (TextView) convertView.findViewById(R.id.designation);
                holder.editbutton = (ImageView) convertView.findViewById(R.id.editb);
                holder.editbutton.setImageResource(R.drawable.share_link);
//                holder.tvYear = (TextView)convertView.findViewById(R.id.tvYear);
//                holder.tvDuration = (TextView)convertView.findViewById(R.id.tvDuration);
//                holder.tvDirector = (TextView)convertView.findViewById(R.id.tvDirector);
//                holder.rbMovieRating = (RatingBar)convertView.findViewById(R.id.rbMovie);
//                holder.tvCast = (TextView)convertView.findViewById(R.id.tvCast);
//                holder.tvStory = (TextView)convertView.findViewById(R.id.tvStory);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);

            // Then later, when you want to display image
            final ViewHolder finalHolder = holder;
            ImageLoader.getInstance().displayImage(userList.get(position).getImage(), holder.propic, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    finalHolder.propic.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.propic.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.propic.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    progressBar.setVisibility(View.GONE);
                    finalHolder.propic.setVisibility(View.INVISIBLE);
                }
            });

            holder.names.setText(userList.get(position).getname());
            holder.designations.setText(userList.get(position).getdesignation());
            holder.editbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(search.this, search_share.class);
                    i.putExtra("userprofile",new Gson().toJson(userList.get(position)));
                    startActivity(i);
                }
            });
            return convertView;
        }


        class ViewHolder {
            private ImageView propic;
            private TextView names;
            private TextView designations;
            private ImageView editbutton;

        }

    }


    public void onClick(View v) {
        searchView.onActionViewExpanded();}

    public void back(View v){
        Intent i = new Intent(search.this, cardformate.class);
        startActivity(i);
//
    }
}




