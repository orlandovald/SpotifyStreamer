package com.ovlstuff.android.spotifystreamer.fragment;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ovlstuff.android.spotifystreamer.R;
import com.ovlstuff.android.spotifystreamer.adapter.SearchResultsAdapter;
import com.ovlstuff.android.spotifystreamer.util.SpotifyWrapperUtil;
import com.ovlstuff.android.spotifystreamer.vo.SearchResult;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchActivityFragment extends Fragment {

    private final String LOG_TAG = SearchActivityFragment.class.getSimpleName();

    private SearchResultsAdapter mSearchResultsAdapter;
    private List<SearchResult> mSearchResults = new ArrayList<SearchResult>();

    public SearchActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "Oncreateview");
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        mSearchResultsAdapter = new SearchResultsAdapter(getActivity(), mSearchResults);

        ListView list = (ListView) rootView.findViewById(R.id.searchResultListView);
        list.setAdapter(mSearchResultsAdapter);

        new SearchArtistTask().execute("megadeth");
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
                        SearchResult res = new SearchResult();
                        res.setLabel(artist.name);
                        String imgUrl = SpotifyWrapperUtil.getImageUrlForSearchResultList(
                                artist.images,
                                getResources().getDimensionPixelSize(R.dimen.search_result_thumbnail_max_height),
                                getResources().getDimensionPixelSize(R.dimen.search_result_thumbnail_max_width));
                        res.setThumbnailUrl(imgUrl);
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
                mSearchResults.clear();
                mSearchResults.addAll(artists);
//                for(SearchResult searchResult : artists) {
//                    mSearchResults.add(searchResult);
//                }
            }
        }
    }
}
