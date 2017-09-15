package com.example.lpg_ops.lagosjavadeveloper;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {


    private static final String GITHUB_URL =
            "https://api.github.com/search/users?q=location:Lagos+language:Java?access_token=<641c0ca2c8933874e08dc2bf35adc4051e996f53>";
    ProgressBar progress;
    ArrayList<UserClass> arrayList;
    ListView listForMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayList = new ArrayList<>();

        listForMainActivity = (ListView) findViewById(R.id.listView);

        //Listener for the list to listen to click and launch another activity  using explicit intent
        listForMainActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView,
                                    View v,
                                    int position,
                                    long id) {
                Object o = listForMainActivity.getItemAtPosition(position);

                UserClass user = (UserClass) o;

                Intent intent = new Intent(MainActivity.this, gitUserDetails.class);
                intent.putExtra("userId", user.getUserName());
                intent.putExtra("avatarId", user.getAvatarUrl());
                intent.putExtra("htmlId", user.getHtmlUrl());


                startActivity(intent);
            }


        });

        //Start the asynchronous task of fetching data from  GitHub if there is connection
        isThereConnection();

    }


    public Loader<String> onCreateLoader(int id, Bundle args) {

        return new HttpTaskLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setIndeterminate(false);
        progress.setVisibility(View.GONE);
        String git = (String) o;
        getUserInfoFromJson(git);
        setListAdapter();

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }


    //parse the Json String and Populate the ArrayList with Data(Login, Avatar, HTML Link, Numbers of Follower and those Following
    private void getUserInfoFromJson(String gitHubJson) {
        try {
            JSONObject root = new JSONObject(gitHubJson);
            JSONArray items = root.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject eachUser = items.getJSONObject(i);
                arrayList.add(new UserClass(eachUser.getString("login"), eachUser.getString("avatar_url"),
                        eachUser.getString("html_url")));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //Bind the arrayList  to the listForMainActivity using the custom UserClassAdapter class
    private void setListAdapter() {

        UserClassAdapter adapter = new UserClassAdapter(getApplicationContext(), R.layout.activity_custom_list, arrayList);
        listForMainActivity.setAdapter(adapter);


    }

    public void isThereConnection() {

        ConnectivityManager connection = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connection.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            getLoaderManager().initLoader(1, null, this).forceLoad();
            progress = (ProgressBar) findViewById(R.id.progress);
            progress.setIndeterminate(true);
            progress.setVisibility(View.VISIBLE);
        } else {

            TextView noConnectionState = (TextView) findViewById(R.id.noConnectionState);
            noConnectionState.setText("No Internet Connection");
        }


    }

}

