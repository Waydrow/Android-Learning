package com.waydrow.booklisting;

import java.io.Serializable;

import static android.R.attr.author;

/**
 * Created by Waydrow on 2016/11/15.
 */

public class Book implements Serializable {

    /* Book's name */
    private String mTitle;

    /*Book's ahthor */
    private String mAuthors;

    /* Constructor of Book Class */
    public Book(String title, String authors) {
        this.mTitle = title;
        this.mAuthors = authors;
    }

    /* Getter methods */
    public String getTitle() {
        return mTitle;
    }

    public String getAuthors() {
        return mAuthors;
    }

}
