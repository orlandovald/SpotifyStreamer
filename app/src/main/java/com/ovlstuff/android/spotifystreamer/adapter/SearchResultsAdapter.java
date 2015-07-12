package com.ovlstuff.android.spotifystreamer.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ovlstuff.android.spotifystreamer.R;
import com.ovlstuff.android.spotifystreamer.vo.SearchResult;

import java.util.List;

/**
 * Created by ovaldez on 7/10/15.
 */
public class SearchResultsAdapter extends ArrayAdapter<SearchResult> {

    private static final String LOG_TAG = SearchResultsAdapter.class.getSimpleName();

    public SearchResultsAdapter(Activity context, List<SearchResult> searchResults) {
        super(context, 0, searchResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SearchResult result = getItem(position);

        Log.v(LOG_TAG, "Item: " + result.toString());

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_artist_list_item, parent, false);
        }

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.searchResultImg);
        thumbnail.setImageResource(R.mipmap.artist_thumbnail);

        TextView artistName = (TextView) convertView.findViewById(R.id.search_result_label);
        artistName.setText(result.getLabel());

        // No need for sub-label for search artist
        TextView subLabel = (TextView) convertView.findViewById(R.id.search_result_sublabel);
        artistName.setVisibility(View.GONE);

        return convertView;
    }
}
