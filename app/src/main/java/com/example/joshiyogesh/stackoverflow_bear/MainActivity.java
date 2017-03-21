package com.example.joshiyogesh.stackoverflow_bear;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

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
}
