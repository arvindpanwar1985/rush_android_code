package com.hoffmans.rush.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Rating implements Parcelable {
	private String service_id;
	private String service_review;
	private String rating;

	public void setServiceId(String serviceId){
		this.service_id = serviceId;
	}

	public String getServiceId(){
		return service_id;
	}

	public void setServiceReview(String serviceReview){
		this.service_review = serviceReview;
	}

	public String getServiceReview(){
		return service_review;
	}

	public void setRating(String rating){
		this.rating = rating;
	}

	public String getRating(){
		return rating;
	}

	@Override
 	public String toString(){
		return 
			"Rating{" + 
			"service_id = '" + service_id + '\'' +
			",service_review = '" + service_review + '\'' +
			",rating = '" + rating + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.service_id);
		dest.writeString(this.service_review);
		dest.writeString(this.rating);
	}

	public Rating() {
	}

	protected Rating(Parcel in) {
		this.service_id = in.readString();
		this.service_review = in.readString();
		this.rating = in.readString();
	}

	public static final Parcelable.Creator<Rating> CREATOR = new Parcelable.Creator<Rating>() {
		@Override
		public Rating createFromParcel(Parcel source) {
			return new Rating(source);
		}

		@Override
		public Rating[] newArray(int size) {
			return new Rating[size];
		}
	};
}
