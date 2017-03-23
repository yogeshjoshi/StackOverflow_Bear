package com.example.joshiyogesh.stackoverflow_bear;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joshi Yogesh on 22/03/2017.
 */
/**/

public class OfflineDatabase extends BaseAdapter {

    static Context context;
    static int layoutResourceId;
    ArrayList<String> qid = new ArrayList<String>();
    ArrayList<String> author = new ArrayList<String>();
    ArrayList<String> title = new ArrayList<String>();
    ArrayList<String> vote = new ArrayList<String>();

    public OfflineDatabase(Context context , int resource , ArrayList<String>qid,ArrayList<String>author,ArrayList<String>title,ArrayList<String>vote){
        this.context = context;
        this.layoutResourceId = resource;
        this.qid = qid;
        this.author = author;
        this.title = title;
        this.vote = vote;
    }

    @Override
    public int getCount() {
        return qid.size();
    }

    @Override
    public Object getItem(int position) {
        return qid.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        QuestionHolder questionHolder = null;
        if (rootView == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            rootView = inflater.inflate(layoutResourceId , parent , false);
            questionHolder = new QuestionHolder();
            questionHolder.textView1 = (TextView)rootView.findViewById(R.id.questionTitle);
            questionHolder.textView2 = (TextView)rootView.findViewById(R.id.questionAuthor);
            questionHolder.textView3 = (TextView)rootView.findViewById(R.id.questionVotes);
            questionHolder.textView4 = (TextView)rootView.findViewById(R.id.questionID);

            rootView.setTag(questionHolder);
        }
        else {
            questionHolder = (QuestionHolder)rootView.getTag();
        }
        if(position>19&&qid.size()<20)
        {
            questionHolder.textView1.setVisibility(View.GONE);
            questionHolder.textView2.setVisibility(View.GONE);
            questionHolder.textView3.setVisibility(View.GONE);
            questionHolder.textView4.setVisibility(View.GONE);
        }
        else {
            questionHolder.textView1.setText(Html.fromHtml(title.get(position))); //to parse the HTML question into text
            questionHolder.textView2.setText(author.get(position));
            questionHolder.textView3.setText(vote.get(position) + " votes");
            questionHolder.textView4.setText(qid.get(position));
        }

        return rootView;

    }
    /*View holder design pattern to allow for recycling of list items*/
    static class QuestionHolder
    {
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
    }
}
