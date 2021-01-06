package com.example.moderndaypharmacy.Models;

import java.io.Serializable;

public class QueryModel implements Serializable {
    private String queryText,queryId,reply;


    public QueryModel(){

    }
    public QueryModel(String queryText,String queryId,String reply){
        this.queryText=queryText;
        this.queryId=queryId;
        this.reply=reply;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }


}
