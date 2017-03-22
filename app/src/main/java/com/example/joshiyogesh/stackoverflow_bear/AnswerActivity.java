package com.example.joshiyogesh.stackoverflow_bear;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class AnswerActivity extends AppCompatActivity {

    private final String initialURL = "http://api.stackexchange.com/2.2/";
    private String site = "site=stackoverflow";
    String requestUrl = null;
    ListView answerList;
    JSONArray mJSONArray;
    AnswerAdapter answerAdapter;
    JSONObject ob3, ob2, answersJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*getting questionId from MainActivity*/
        Bundle extras = getIntent().getExtras();
        String question = extras.getString("QUESTION");
        try {
            question = URLEncoder.encode(question, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        answerList = (ListView) findViewById(R.id.answerList);

        //appending the questionID to the URL
        requestUrl = this.initialURL + "questions/" + question + "/answers?order=desc&sort=activity&filter=withbody&" + this.site;

        if (isNetworkAvailable()) {
            new JSONTask().execute();
        } else {
            Toast.makeText(AnswerActivity.this, "Internet Connection is needed to view Answers!", Toast.LENGTH_SHORT).show();
        }
    }

    //check for network connectivity while performing search
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /*makes Http request to api url of stackOverflow and gets JSON object*/

    public JSONObject makeHttpRequest(String url) throws IOException, JSONException, MalformedURLException {
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

            while ((line = bufferedReader.readLine()) != null) {
                responseString.append(line);
            }

            jsonString = responseString.toString();
            reponse = new JSONObject(jsonString);

            return reponse;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    public class JSONTask extends AsyncTask<String,String,JSONObject> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AnswerActivity.this);
            progressDialog.setMessage("Getting Answers ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                answersJson = makeHttpRequest(requestUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answersJson;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    mJSONArray = jsonObject.getJSONArray("items");
                    if (mJSONArray.length() == 0) {
                        progressDialog.dismiss();
                        Toast.makeText(AnswerActivity.this, "This question does not have any answers yet !", Toast.LENGTH_SHORT).show();
                    } else {
                        Answer answers[] = new Answer[mJSONArray.length()];
                        for (int i = 0; i < mJSONArray.length(); i++) {
                            ob2 = mJSONArray.getJSONObject(i);
                            if (ob2 != null) {
                                ob3 = ob2.getJSONObject("owner");
                                answers[i] = new Answer(ob2.getString("body"), ob3.getString("display_name"), ob2.getString("score"));
                            }
                        }
                        answerAdapter = new AnswerAdapter(AnswerActivity.this,
                                R.layout.answer_list_item, answers);
                        answerList.setAdapter(answerAdapter);
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
