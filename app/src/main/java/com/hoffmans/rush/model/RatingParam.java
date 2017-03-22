package com.hoffmans.rush.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by devesh on 22/3/17.
 */

public class RatingParam {
    @SerializedName("rater")
    private Rating rating;

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
