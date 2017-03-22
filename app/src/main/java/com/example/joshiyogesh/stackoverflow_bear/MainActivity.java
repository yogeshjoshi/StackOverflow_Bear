package com.example.joshiyogesh.stackoverflow_bear;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    /*Api url for stackExchange , its only initial url*/
    public String url = "https://api.stackexchange.com/2.2/search?order=desc&sort=activity&";

    QuestionDatabase q = new QuestionDatabase();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        questionList = (ListView)findViewById(R.id.QuestionList) ;
        imageView = (ImageView)findViewById(R.id.introImage);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedQuestion = ((TextView) (view.findViewById(R.id.questionID))).getText().toString();
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

    public class JSONTask extends AsyncTask<String,String,JSONObject> {
        //        making progressDialogue box to visulize data effectively
        /*progressDialog.setIndeterminate*/
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Searching ... ");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
            Log.e("TAG","onPreExecute has been completed successfully");
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                questionJSON = makeHttpRequest(url);
                Log.e("HttpRequest","makeHttpRequest method has been completed successfully");
                Log.e("TAG","doInBackground is running ");

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("TAG","doInBackGroud  has been completed successfully");

            return questionJSON;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject){
            if (jsonObject != null){ //questionJSON object from doInBackGround is received
                try {
                    ArrayList<String> ids, authors, titles, votes;
                    ids = new ArrayList<String>(20);
                    authors = new ArrayList<String>(20);
                    titles = new ArrayList<String>(20);
                    votes = new ArrayList<String>(20);

                    mJSONArray = jsonObject.getJSONArray("items");
                    imageView.setVisibility(View.GONE);
                    questionList.setVisibility(View.VISIBLE);

                    Question question[] = new Question[mJSONArray.length()];
//check if there is any question found or not
                    if (question.length == 0){
                        imageView.setVisibility(View.VISIBLE);
                        questionList.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "No Questions Found!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else{
                        for (int i = 0; i < mJSONArray.length(); i++) {
                            object1 = mJSONArray.getJSONObject(i);
                            if (object1 != null) {
                                object2 = object1.getJSONObject("owner");
                            /*Changes have been made here in object receiving from JSON object */
                                question[i] = new Question(object1.getString("title"), object2.getString("display_name"), object1.getString("score"), object1.getString("question_id"));
                                ids.add(object1.getString("question_id"));
                                authors.add(object2.getString("display_name"));
                                titles.add(object1.getString("title"));
                                votes.add(object1.getString("score"));
                            }
                        }
                    }

                    adapter = new QuestionAdapter(MainActivity.this,
                            R.layout.question_list_item, question);

                    holdID = new JSONObject();
                    holdID.put("uniqueIDs", new JSONArray(ids));
                    String _id = holdID.toString();

                    holdAuthor = new JSONObject();
                    holdAuthor.put("uniqueAuthors", new JSONArray(authors));
                    String _auth = holdAuthor.toString();

                    holdTitle = new JSONObject();
                    holdTitle.put("uniqueTitles", new JSONArray(titles));
                    String _title = holdTitle.toString();

                    holdVotes = new JSONObject();
                    holdVotes.put("uniqueVotes", new JSONArray(votes));
                    String _vote = holdVotes.toString();

                    q.insertQuestion(MainActivity.this, _id, _title, _auth, _vote, val);
                    questionList.setAdapter(adapter);
                    progressDialog.dismiss();
                /*again changing the initial url ,, so that dynamic search can occur*/
                    url = "https://api.stackexchange.com/2.2/search?order=desc&sort=activity&";


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
