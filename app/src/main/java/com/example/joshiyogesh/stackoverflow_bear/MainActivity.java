package com.example.joshiyogesh.stackoverflow_bear;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.widget.ImageView;
import android.widget.ListView;

/*
* Created by @joshiYogesh on 21/03/2017 at 19:56 IST
*
* */
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    SearchView mSearchView;
    ListView questionList;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        questionList = (ListView)findViewById(R.id.QuestionList) ;
        imageView = (ImageView)findViewById(R.id.introImage);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
