package com.hoffmans.rush.ui.driver.viewholders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.OnRecordsItemClickListeners;
import com.hoffmans.rush.model.CustomerDetail;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.Record;

import java.util.List;

/**
 * Created by devesh on 17/3/17.
 */

public class CharacterViewHolder  extends RecyclerView.ViewHolder {

    private ImageView imgProfile;
    private TextView txtNamePhone,txtSource,txtDestination,txtEstimatedPrice,txtDateTime,txtState;
    private RelativeLayout mLayout;
    private LinearLayout ratingLayout;
    private  Context mContext;
    private SpannableStringBuilder mbuilder=new SpannableStringBuilder();
    private OnRecordsItemClickListeners itemClickListeners;
    private int itemPosition;

    public CharacterViewHolder(View itemView,OnRecordsItemClickListeners itemClickListeners) {
        super(itemView);
        this.mContext = itemView.getContext();
        this.itemClickListeners=itemClickListeners;
        imgProfile     =(ImageView)itemView.findViewById(R.id.imgProfile);
        txtNamePhone   =(TextView)itemView.findViewById(R.id.txtNamePhone);
        txtSource      =(TextView)itemView.findViewById(R.id.txtSource);
        txtDestination =(TextView)itemView.findViewById(R.id.txtdestination);
        txtEstimatedPrice=(TextView)itemView.findViewById(R.id.txtPriceEstimated);
        txtDateTime   =(TextView)itemView.findViewById(R.id.txtdateTime);
        txtState      =(TextView)itemView.findViewById(R.id.txtState);
        ratingLayout=(LinearLayout)itemView.findViewById(R.id.layoutRating) ;
        mLayout=(RelativeLayout)itemView.findViewById(R.id.layout_driver_records);
    }

    public void render(Record record, final int position){
        this.itemPosition=position;


        CustomerDetail customerDetail=record.getCustomer_details();
        Estimate estimate=record.getEstimate();
        PickDropAddress pickUpAddress=record.getPick_up();
        DateTime dateTime=record.getDate_time();
        txtState.setText(record.getState());

        ratingLayout.setVisibility(View.GONE);

        List<PickDropAddress> dropAddresses=record.getDrop_down();
        if(customerDetail!=null){
           setCustomerDetail(customerDetail);
        }
        if(estimate!=null){
            setPriceEstimate(estimate);
        }
        if(pickUpAddress!=null){
            setPickAddress(pickUpAddress);
        }
        if(dropAddresses!=null && dropAddresses.size()>0) {
            setDropAddresses(dropAddresses);
        }
        if(dateTime!=null){
            txtDateTime.setText(dateTime.getDate()+" "+dateTime.getTime());
        }

        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListeners.onRecordsItemClicked(position);
            }
        });
    }



    /**
     * set customer detail
     * @param customerDetail
     */
    private void setCustomerDetail(CustomerDetail customerDetail){
        StringBuilder namePhoneBuilder=new StringBuilder(".").append(customerDetail.getName())
                .append(" . ").append(customerDetail.getPhone());
        txtNamePhone.setText(namePhoneBuilder.toString());
        if(customerDetail.getPicUrl()!=null){
            Glide.with(mContext).load(customerDetail.getPicUrl()).into(imgProfile);
        }
    }

    /**
     * set price estimation
     * @param estimate
     */
    private void setPriceEstimate(Estimate estimate){

        StringBuilder stringBuilder=new StringBuilder(estimate.getSymbol()).append(" ").append(estimate.getApproxConvertedAmount());
        txtEstimatedPrice.setText(stringBuilder.toString());
    }

    /**
     * set source address
     * @param pickUpAddress
     */
    private void setPickAddress(PickDropAddress pickUpAddress){

       //set spannable string
        mbuilder.clear();

        String start = mContext.getString(R.string.str_collect)+": ";
        getSpanableBuilder(start, ContextCompat.getColor(mContext,R.color.civ_border));
        String address=pickUpAddress.getStreetAddress();
        SpannableStringBuilder builder=getSpanableBuilder(address,ContextCompat.getColor(mContext,R.color.colorPrimary));

        txtSource.setText(builder, TextView.BufferType.SPANNABLE);
    }

    /**
     * set multiple drop address if having multiple drops
     * @param dropAddresses
     */
    private void setDropAddresses(List<PickDropAddress>dropAddresses){
        if(dropAddresses.size()==1){
            //single destination order
            PickDropAddress dropAddress=dropAddresses.get(0);
            mbuilder.clear();

            String start = mContext.getString(R.string.str_deliver)+": ";
            getSpanableBuilder(start,ContextCompat.getColor(mContext,R.color.civ_border));
            String address=dropAddress.getStreetAddress();
            SpannableStringBuilder builder=getSpanableBuilder(address,ContextCompat.getColor(mContext,R.color.colorPrimary));

            txtDestination.setText(builder, TextView.BufferType.SPANNABLE);

        }else{
            //multiple destination order
            SpannableStringBuilder builder=null;
            mbuilder.clear();
            // building multiple destination text
            for(PickDropAddress dropAddress:dropAddresses){
                String start=mContext.getString(R.string.str_deliver)+": ";
                getSpanableBuilder(start,ContextCompat.getColor(mContext,R.color.civ_border));
                String address=dropAddress.getStreetAddress()+"\n";
                builder=getSpanableBuilder(address,ContextCompat.getColor(mContext,R.color.colorPrimary));

            }
            txtDestination.setText(builder, TextView.BufferType.SPANNABLE);
        }
    }


    /**
     *
     * @param text text
     * @param color color
     * @return SpannableStringBuilder
     */
    private SpannableStringBuilder getSpanableBuilder(String text,int color){
        SpannableString darkSpannable = new SpannableString(text);
        darkSpannable.setSpan(new ForegroundColorSpan(color), 0, text.length(), 0);
        return  mbuilder.append(darkSpannable);
    }
}
