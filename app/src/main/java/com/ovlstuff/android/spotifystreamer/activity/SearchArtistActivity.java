package com.ovlstuff.android.spotifystreamer.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.ovlstuff.android.spotifystreamer.R;


public class SearchArtistActivity extends ActionBarActivity {

    private String LOG_TAG = SearchArtistActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

}
