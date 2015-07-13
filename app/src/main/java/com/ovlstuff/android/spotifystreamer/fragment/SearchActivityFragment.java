package com.ovlstuff.android.spotifystreamer.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ovlstuff.android.spotifystreamer.R;
import com.ovlstuff.android.spotifystreamer.adapter.SearchResultsAdapter;
import com.ovlstuff.android.spotifystreamer.util.SpotifyWrapperUtil;
import com.ovlstuff.android.spotifystreamer.util.Util;
import com.ovlstuff.android.spotifystreamer.vo.SearchResult;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchActivityFragment extends Fragment {

    private final String LOG_TAG = SearchActivityFragment.class.getSimpleName();
    private final static String BUNDLE_SEARCH_RESULTS_KEY = "searchResults";
    private final static String BUNDLE_SEARCH_BOX_KEY = "searchBox";

    private SearchResultsAdapter mSearchResultsAdapter;
    private ArrayList<SearchResult> mSearchResults = new ArrayList<SearchResult>();
    private String mSearchBoxText = "";
    private ListView mResultsList;
    private TextView mSearchTextView;

    public SearchActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(BUNDLE_SEARCH_RESULTS_KEY)) {
                mSearchResults = savedInstanceState.getParcelableArrayList(BUNDLE_SEARCH_RESULTS_KEY);
            }

            if(savedInstanceState.containsKey(BUNDLE_SEARCH_BOX_KEY)) {
                mSearchBoxText = savedInstanceState.getString(BUNDLE_SEARCH_BOX_KEY);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_SEARCH_RESULTS_KEY, mSearchResults);
        outState.putString(BUNDLE_SEARCH_BOX_KEY, mSearchTextView.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        mSearchResultsAdapter = new SearchResultsAdapter(getActivity(), mSearchResults);

        mResultsList = (ListView) rootView.findViewById(R.id.searchResultListView);
        mResultsList.setAdapter(mSearchResultsAdapter);

        mSearchTextView = (EditText) rootView.findViewById(R.id.artist_search_box);
        mSearchTextView.setText(mSearchBoxText);

        Log.v(LOG_TAG, "About to set listener");

        mSearchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                Log.v(LOG_TAG, "Inside edit action listener with action: " + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    String searchInput = v.getText().toString();
                    Log.v(LOG_TAG, "Searching for " + searchInput);
                    if (searchInput != null && searchInput.length() > 0) {
                        new SearchArtistTask().execute(v.getText().toString());
                        mResultsList.setSelectionAfterHeaderView();
                        v.setText("");
                        Util.hideSfotKeyboard(getActivity(), v);
                    }
                    handled = true;
                }
                return handled;
            }
        });

        return rootView;
    }

    public class SearchArtistTask extends AsyncTask<String,Void,List<SearchResult>> {

        private final String LOG_TAG = SearchArtistTask.class.getSimpleName();

        @Override
        protected List<SearchResult> doInBackground(String... params) {

            if(params.length == 0) {
                Log.v(LOG_TAG, "No parameter received for task");
                return null;
            }

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            try {
                ArtistsPager artistsPager = spotify.searchArtists(params[0]);

                List<Artist> artists = artistsPager.artists.items;

                if(artists.size() > 0) {

                    List<SearchResult> results = new ArrayList<SearchResult>();

                    for (Artist artist : artistsPager.artists.items) {
                        Log.v(LOG_TAG, "Found artist: " + artist.name);
                        String imgUrl = SpotifyWrapperUtil.getImageUrlForSearchResultList(
                                artist.images,
                                getResources().getDimensionPixelSize(R.dimen.search_result_thumbnail_max_height),
                                getResources().getDimensionPixelSize(R.dimen.search_result_thumbnail_max_width));
                        SearchResult res = new SearchResult(
                                artist.id,
                                artist.name,
                                imgUrl
                        );
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
        protected void onPostExecute(List<SearchResult> artists) {
            if(artists != null) {
                mSearchResultsAdapter.setNotifyOnChange(false);
                mSearchResults.clear();
                mSearchResults.addAll(artists);
                mSearchResultsAdapter.notifyDataSetChanged();
//                for(SearchResult searchResult : artists) {
//                    mSearchResults.add(searchResult);
//                }
            }
        }
    }
}
