package com.example.joshiyogesh.stackoverflow_bear;

/**
 * Created by Joshi Yogesh on 22/03/2017.
 */

public class Question {
    public String title,author,votes,id;

    public Question()
    {

    }
    public Question(String title,String author,String votes,String id)
    {
        this.title = title;
        this.author = author;
        this.votes = votes;
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getVotes() {
        return votes;
    }

    public String getId() {
        return id;
    }

}
