package com.hoffmans.rush.model;

public class Rating{
	private String serviceId;
	private String serviceReview;
	private String rating;

	public void setServiceId(String serviceId){
		this.serviceId = serviceId;
	}

	public String getServiceId(){
		return serviceId;
	}

	public void setServiceReview(String serviceReview){
		this.serviceReview = serviceReview;
	}

	public String getServiceReview(){
		return serviceReview;
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
			"service_id = '" + serviceId + '\'' + 
			",service_review = '" + serviceReview + '\'' + 
			",rating = '" + rating + '\'' + 
			"}";
		}
}
