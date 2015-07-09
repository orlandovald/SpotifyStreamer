package com.ovlstuff.android.spotifystreamer.fragment;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ovlstuff.android.spotifystreamer.R;

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

    public SearchActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new SearchArtistTask().execute("metallica");
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    public class SearchArtistTask extends AsyncTask<String, Void, List<Artist>> {

        private final String LOG_TAG = SearchArtistTask.class.getSimpleName();

        @Override
        protected List<Artist> doInBackground(String... params) {

            if(params.length == 0) {
                return null;
            }

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            try {
                ArtistsPager artistsPager = spotify.searchArtists(params[0]);

                for(Artist artist : artistsPager.artists.items) {
                    Log.v(LOG_TAG, artist.name);
                }

                return artistsPager.artists.items;

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
        protected void onPostExecute(List<Artist> s) {
            super.onPostExecute(s);
        }
    }
}
