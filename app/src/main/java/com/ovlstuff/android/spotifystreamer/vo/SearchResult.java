package com.ovlstuff.android.spotifystreamer.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a search result to be displayed in a list view
 * Created by ovaldez on 7/10/15.
 */
public class SearchResult implements Parcelable{

    private String sourceId; // id returned by source system, i.e. Spotify
    private String label; // main label to be displayed in the list view
    private String subLabel; // optional sublabel to be displayed
    private String thumbnailUrl; // URL of the thumbnail image

    public SearchResult(String sourceId, String label, String thumbnailUrl) {
        this.sourceId = sourceId;
        this.label = label;
        this.thumbnailUrl = thumbnailUrl;
    }

    /**
     * Private constructor to create an object from a Parcel
     * @param in
     */
    private SearchResult(Parcel in) {
        sourceId = in.readString();
        label = in.readString();
        subLabel = in.readString();
        thumbnailUrl = in.readString();
    }

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
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(sourceId);
        parcel.writeString(label);
        parcel.writeString(subLabel);
        parcel.writeString(thumbnailUrl);
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

    public final Parcelable.Creator<SearchResult> CREATOR = new Parcelable.Creator<SearchResult>() {

        @Override
        public SearchResult createFromParcel(Parcel source) {
            return new SearchResult(source);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };
}
