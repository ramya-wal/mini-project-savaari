package com.example.ramya.savaari.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LENONO on 4/5/2018.
 */

public class UserList {
    @SerializedName("results")
    @Expose
    private List<User> results = null;

    public List<User> getResults() {
        return results;
    }

    public void setResults(List<User> results) {
        this.results = results;
    }
}