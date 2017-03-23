package com.example.joshiyogesh.stackoverflow_bear;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Joshi Yogesh on 22/03/2017 at 16:10 IST.
 */
/*this class is used to convert View to dataSource*/
public class AnswerAdapter extends ArrayAdapter<Answer> {

    static Context context;
    static int layoutResourceId;
    Answer[] data = null;

    public AnswerAdapter(Context context, int resource, Answer[] objects) {
        super(context, resource, objects);
         /*assigning their refrence*/
        layoutResourceId = resource;
        this.context = context;
        data = objects;
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position , View convertView , ViewGroup parent){
        View rootView = convertView;
        AnswerHolder answerHolder = null;
        if(rootView==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            rootView = inflater.inflate(layoutResourceId,parent,false);
            answerHolder = new AnswerHolder();
            answerHolder.textView1 = (TextView)rootView.findViewById(R.id.answerText);
            answerHolder.textView1.setMovementMethod(LinkMovementMethod.getInstance());
            answerHolder.textView1.setAutoLinkMask(Linkify.WEB_URLS);
            answerHolder.textView2 = (TextView)rootView.findViewById(R.id.answerAuthor);
            answerHolder.textView3 = (TextView)rootView.findViewById(R.id.answerVotes);
            rootView.setTag(answerHolder);
        }else {
            answerHolder = (AnswerHolder)rootView.getTag();
        }

        Answer hold = data[position];
        if(hold!=null) {
            answerHolder.textView1.setText(Html.fromHtml(hold.text));
            answerHolder.textView2.setText("answered by  "+hold.author);
            answerHolder.textView3.setText(hold.votes+" votes");
        }
        return rootView;
    }

    static class AnswerHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }
}
