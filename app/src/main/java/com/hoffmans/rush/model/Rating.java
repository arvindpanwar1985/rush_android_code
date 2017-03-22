package com.hoffmans.rush.model;

public class Rating{
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
}
