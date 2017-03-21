package com.example.joshiyogesh.stackoverflow_bear;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

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

/*
* Created by @joshiYogesh on 21/03/2017 at 19:56 IST
*
* */
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    SearchView mSearchView;
    ListView questionList;
    ImageView imageView;
    /*JSON used for collecting data from api in json format ..*/
    JSONArray mJSONArray;
    JSONObject questionJSON ,holdID,holdAuthor,holdVotes,holdTitle;
    JSONObject object1 , object2;
    QuestionAdapter adapter;
    ArrayList<ArrayList<String>> listHolder = new ArrayList<ArrayList<String>>();
    ArrayList<String> sqlIds, sqlAuthors, sqlTitles, sqlVotes; //array lists to hold question details retrieved from the database
    QuestionDatabase questionDatabase = new QuestionDatabase();

    String val;
    /*Api url for stackExchange*/
    public String url = "https://api.stackexchange.com/2.2/search?order=desc&sort=activity&";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        questionList = (ListView)findViewById(R.id.QuestionList) ;
        imageView = (ImageView)findViewById(R.id.introImage);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedQuestion = null ; //here we have to remove null
                Intent intent = new Intent(MainActivity.this,AnswerActivity.class);
                intent.putExtra("QUESTION",selectedQuestion);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search for Questions");
        mSearchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    //check for network connectivity while performing search
    public boolean isNetworkAvailble(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    /*makes Http request to api url of stackOverflow and gets JSON object*/

    public JSONObject makeHttpRequest(String url)throws IOException, JSONException,MalformedURLException {
        JSONObject reponse;
        String jsonString;
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        try {
            URL urlObject = new URL(url);
            httpURLConnection = (HttpURLConnection) urlObject.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder responseString = new StringBuilder();

            String line;

            while((line = bufferedReader.readLine())!= null){
                responseString.append(line);
            }

            jsonString = responseString.toString();
            reponse = new JSONObject(jsonString);

            return reponse;
        }
        finally {
            if (httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null){
                bufferedReader.close();
            }
        }

    } /*end of connection methods */

}
