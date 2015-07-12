package com.ovlstuff.android.spotifystreamer.util;

import android.util.Log;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by ovaldez on 7/12/15.
 */
public class SpotifyWrapperUtil {

    private static final String LOG_TAG = SpotifyWrapperUtil.class.getSimpleName();

    public static String getImageUrlForSearchResultList(List<Image> images, int maxHeight, int maxWidth) {

        int imageCount = images.size();

        Log.v(LOG_TAG, "Trying to find suitable image: maxHeight[" + maxHeight + "] maxWidth[" + maxWidth + "]");

        if(imageCount > 0) {
            for(int i = 0; i < imageCount; i++) {
                Image img = images.get(i);
                Log.v(LOG_TAG, "Height: " + img.height + " Width: "
                        + img.width + " url: " + img.url);
                if((img.width >= maxWidth && img.height >= maxHeight)
                    || i == (imageCount - 1)) {
                    return img.url;
                }
            }
        }

        return null;

    }
}
