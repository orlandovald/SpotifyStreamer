package com.ovlstuff.android.spotifystreamer.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ovaldez on 7/10/15.
 */
public class SearchResult implements Parcelable{

    private String sourceId;
    private String label;
    private String subLabel;
    private String thumbnailUrl;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSubLabel() {
        return subLabel;
    }

    public void setSubLabel(String subLabel) {
        this.subLabel = subLabel;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("Id: ").append(getSourceId())
                .append(", Label=").append(getLabel())
                .append(", Sub-label=").append(getSubLabel())
                .append(", thumbnailUrl=").append(getThumbnailUrl());
        return sb.toString();
    }
}
