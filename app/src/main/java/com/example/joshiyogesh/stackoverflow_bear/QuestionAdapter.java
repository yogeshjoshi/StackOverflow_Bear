package com.example.joshiyogesh.stackoverflow_bear;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Joshi Yogesh on 21/03/2017.
 */

public class QuestionAdapter  extends ArrayAdapter<Question> {
    static Context context;
    static int layoutResourcesId;
    Question data[] = null;
    public QuestionAdapter(Context context, int resource, Question[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourcesId = resource;
        data = objects;
    }
    public long getItemId(int position){
        return position;
    }
    /*The LayoutInflater takes your layout XML-files and creates different View-objects from its contents. */
    public View getView(int position , View convertView , ViewGroup parent){
        /*providing view to object so that to access R file object in adapter class*/
        View listRow = convertView;
        QuestionHolder questionHolder = null;
        if(listRow == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            listRow = inflater.inflate(layoutResourcesId,parent,false);
            questionHolder = new QuestionHolder();
            questionHolder.textView = (TextView)listRow.findViewById(R.id.questionTitle);
            questionHolder.textView1 = (TextView)listRow.findViewById(R.id.questionAuthor) ;
            questionHolder.textView2 = (TextView)listRow.findViewById(R.id.questionVotes) ;
            questionHolder.textView3 = (TextView)listRow.findViewById(R.id.questionID);

            listRow.setTag(questionHolder);
        }
        else
        {
            questionHolder = (QuestionHolder)listRow.getTag();
        }

        Question questionHold = data[position];

        if(questionHold!=null){
            if (position>19&&data.length<20){
                questionHolder.textView.setVisibility(View.GONE);
                questionHolder.textView1.setVisibility(View.GONE);
                questionHolder.textView2.setVisibility(View.GONE);
                questionHolder.textView3.setVisibility(View.GONE);
            }
            else {
                /*setting questionds related field in question list view*/
                questionHolder.textView.setText(Html.fromHtml(questionHold.title));
                questionHolder.textView1.setText( questionHold.author);
                questionHolder.textView2.setText(questionHold.votes);
                questionHolder.textView3.setText(questionHold.id);
            }

        }
        return listRow;
    }

    /*View holder design pattern to allow for recycling of list items*/
    static class QuestionHolder{
        TextView textView;
        TextView textView1;
        TextView textView2;
        TextView textView3;

    }

}
