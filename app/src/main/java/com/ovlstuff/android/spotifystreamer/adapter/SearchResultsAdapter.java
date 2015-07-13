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
import com.squareup.picasso.Picasso;

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

        ViewHolder holder = null;

        SearchResult result = getItem(position);
        Log.v(LOG_TAG, "Item: " + result.toString());

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_artist_list_item, parent, false);
            holder = new ViewHolder();
            holder.label = (TextView) convertView.findViewById(R.id.search_result_label);
            holder.subLabel = (TextView) convertView.findViewById(R.id.search_result_sublabel);
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.searchResultImg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(result.getThumbnailUrl() != null) {
            Picasso.with(getContext())
                    .load(result.getThumbnailUrl())
                    .placeholder(R.mipmap.artist_thumbnail)
                    .error(R.mipmap.no_image)
                    .into(holder.thumbnail);
        }
        else {
            holder.thumbnail.setImageResource(R.mipmap.no_image);
        }


        holder.label.setText(result.getLabel());
        holder.subLabel.setVisibility(View.GONE);

        return convertView;
    }

    static class ViewHolder {
        TextView label;
        TextView subLabel;
        ImageView thumbnail;
    }
}
