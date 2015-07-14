package com.ovlstuff.android.spotifystreamer.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ovlstuff.android.spotifystreamer.R;
import com.ovlstuff.android.spotifystreamer.adapter.SearchResultsAdapter;
import com.ovlstuff.android.spotifystreamer.util.SpotifyWrapperUtil;
import com.ovlstuff.android.spotifystreamer.vo.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopTracksActivityFragment extends Fragment {

    private final String LOG_TAG = TopTracksActivityFragment.class.getSimpleName();
    private final static String BUNDLE_TOP_TRACKS_RESULT_KEY = "topTrackResults";

    private SearchResultsAdapter mSearchResultsAdapter;
    private ArrayList<SearchResult> mSearchResults = new ArrayList<SearchResult>();
    private ListView mResultsList;
    private TextView mResultsMsgView;

    public TopTracksActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null
                && savedInstanceState.containsKey(BUNDLE_TOP_TRACKS_RESULT_KEY)) {
            mSearchResults = savedInstanceState.getParcelableArrayList(BUNDLE_TOP_TRACKS_RESULT_KEY);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);

        mSearchResultsAdapter = new SearchResultsAdapter(getActivity(), mSearchResults, true);

        mResultsList = (ListView) rootView.findViewById(R.id.top_tracks_list);
        mResultsList.setAdapter(mSearchResultsAdapter);

        mResultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResult searchResult = mSearchResultsAdapter.getItem(position);
                Toast.makeText(getActivity(),
                        String.format(getResources().getString(R.string.top_ten_now_playing), searchResult.getLabel()),
                        Toast.LENGTH_SHORT).show();
            }
        });

        mResultsMsgView = (TextView) rootView.findViewById(R.id.top_tracks_msg);

        if(savedInstanceState == null
                || !savedInstanceState.containsKey(BUNDLE_TOP_TRACKS_RESULT_KEY)) {
            String sourceId = getActivity().getIntent().getExtras().getString(Intent.EXTRA_TEXT);
            new GetArtistTopTracksTask().execute(sourceId);

        }

        if(mSearchResults.size() > 0) {
            mResultsList.setVisibility(View.VISIBLE);
            mResultsMsgView.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_TOP_TRACKS_RESULT_KEY, mSearchResults);
        super.onSaveInstanceState(outState);
    }

    public class GetArtistTopTracksTask extends AsyncTask<String, Void, List<SearchResult>> {

        private final String LOG_TAG = GetArtistTopTracksTask.class.getSimpleName();

        @Override
        protected List<SearchResult> doInBackground(String... params) {

            if(params.length == 0) {
                Log.i(LOG_TAG, "No parameter received for task");
                return null;
            }

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            try {
                Map<String, Object> options = new HashMap<String, Object>();
                options.put(SpotifyService.COUNTRY, "US");
                Tracks tracks = spotify.getArtistTopTrack(params[0], options);

                if(tracks.tracks.size() > 0) {

                    List<SearchResult> results = new ArrayList<SearchResult>();

                    for (Track track : tracks.tracks) {
                        Log.v(LOG_TAG, "Found track: " + track.name);
                        String imgUrl = SpotifyWrapperUtil.getImageUrlForSearchResultList(
                                track.album.images,
                                getResources().getDimensionPixelSize(R.dimen.search_result_thumbnail_max_height),
                                getResources().getDimensionPixelSize(R.dimen.search_result_thumbnail_max_width));
                        SearchResult res = new SearchResult(track.id, track.name, imgUrl);
                        res.setSubLabel(track.album.name);
                        results.add(res);
                    }

                    return results;
                }


            } catch (RetrofitError error) {
                Log.e(LOG_TAG, "Error while searching artist with param: [" + params[0] + "]");
                SpotifyError spotifyError = SpotifyError.fromRetrofitError(error);
                if(spotifyError.hasErrorDetails()) {
                    Log.e(LOG_TAG, spotifyError.getErrorDetails().message);
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(List<SearchResult> tracks) {
            if(tracks != null && tracks.size() > 0) {
                mResultsMsgView.setVisibility(View.GONE);
                mSearchResultsAdapter.setNotifyOnChange(false);
                mSearchResults.clear();
                mSearchResults.addAll(tracks);
                mSearchResultsAdapter.notifyDataSetChanged();
                mResultsList.setVisibility(View.VISIBLE);
            } else {
                mResultsMsgView.setText(R.string.top_ten_msg_no_results);
                mResultsMsgView.setVisibility(View.VISIBLE);
            }

        }
    }
}
