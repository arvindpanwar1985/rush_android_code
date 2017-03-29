package com.hoffmans.rush.ui.driver.viewholders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.OnHeaderButtonClickListners;
import com.hoffmans.rush.model.CustomerDetail;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.ServiceData;
import com.hoffmans.rush.utils.Status;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * Created by devesh on 17/3/17.
 */
public class HeaderViewHolder extends RecyclerView.ViewHolder {
    private SpannableStringBuilder mbuilder=new SpannableStringBuilder();
    private TextView mTxtname,mtxtPhone,mtxtSource,mtxtdestination,mtxtPriceEstimate,txtDatetime,txtNoRecords,txtNoUpcoming;
    private Button btnStart;
    private Context mContext;
    private CircleImageView imgAcceptreject;
    private RelativeLayout relativeLayoutHeader;
    private OnHeaderButtonClickListners onHeaderButtonClickListner;

    public HeaderViewHolder(View itemView, OnHeaderButtonClickListners onHeaderButtonClickListner) {
        super(itemView);
        mContext=itemView.getContext();
        this.onHeaderButtonClickListner=onHeaderButtonClickListner;
        mTxtname          = (TextView)itemView.findViewById(R.id.txtARName);
        mtxtPhone         = (TextView)itemView.findViewById(R.id.txtARPhone);
        mtxtSource        = (TextView)itemView.findViewById(R.id.txtARSource);
        mtxtdestination   = (TextView)itemView.findViewById(R.id.txtARDestination);
        mtxtPriceEstimate = (TextView)itemView.findViewById(R.id.txtPriceEstimated);
        btnStart          = (Button)itemView.findViewById(R.id.btnStart);
        txtDatetime       =(TextView)itemView.findViewById(R.id.txtdateTime);
        imgAcceptreject   =(CircleImageView)itemView.findViewById(R.id.imgAcceptreject);
        txtNoRecords      =(TextView)itemView.findViewById(R.id.txtNoRecords);
        relativeLayoutHeader=(RelativeLayout)itemView.findViewById(R.id.relativeHeader);
        txtNoUpcoming   =(TextView)itemView.findViewById(R.id.txtNoUpcoming);
    }
    public void render(final ServiceData header) {
        if(header!=null ) {
             if(!header.isTypenoHeader()) {
                 relativeLayoutHeader.setVisibility(View.VISIBLE);
                 txtNoUpcoming.setVisibility(View.GONE);
                 final String state = header.getState();
                 if (state.equals(Status.ACCEPTED)) {
                     btnStart.setText("Start");
                 } else if (state.equals(Status.RUNNING)) {
                     btnStart.setText("Complete");
                 }

                 CustomerDetail customerDetail = header.getCustomerDetail();
                 Estimate estimate = header.getEstimate();
                 PickDropAddress pickUpAddress = header.getPicAddress();
                 DateTime dateTime = header.getDateTime();

                 List<PickDropAddress> dropAddresses = header.getDropAddressList();
                 if (customerDetail != null) {
                     setCustomerDetail(customerDetail);
                 }
                 if (estimate != null) {
                     setPriceEstimate(estimate);
                 }
                 if (pickUpAddress != null) {
                     setPickAddress(pickUpAddress);
                 }
                 if (dropAddresses != null && dropAddresses.size() > 0) {
                     setDropAddresses(dropAddresses);
                 }
                 if (dateTime != null) {
                     txtDatetime.setText(dateTime.getDate() + " " + dateTime.getTime());
                 }

                 btnStart.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         onHeaderButtonClickListner.onStartStopButtonClicked(state, header.getId());
                     }
                 });

             }else{
                 //hide the topview and show no header
                 relativeLayoutHeader.setVisibility(View.GONE);
                 txtNoUpcoming.setVisibility(View.VISIBLE);
             }
            }

    }


    /**
     * set customer detail
     * @param customerDetail
     */
    private void setCustomerDetail(CustomerDetail customerDetail){
        mTxtname.setText(customerDetail.getName());
        mtxtPhone.setText(customerDetail.getPhone());
        if(customerDetail.getPicUrl()!=null){
            Glide.with(mContext).load(customerDetail.getPicUrl()).into(imgAcceptreject);
        }
    }

    /**
     * set price estimation
     * @param estimate
     */
    private void setPriceEstimate(Estimate estimate){
        StringBuilder stringBuilder=new StringBuilder();
        if(estimate.getSymbol()!=null){
            stringBuilder.append(estimate.getSymbol());
        }
        if(estimate.getApproxConvertedAmount()!=null){
            stringBuilder.append(" ").append(estimate.getApproxConvertedAmount());
        }
        mtxtPriceEstimate.setText(stringBuilder.toString());
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
        SpannableStringBuilder builder=getSpanableBuilder(address, ContextCompat.getColor(mContext,R.color.colorPrimary));
        mtxtSource.setText(builder, TextView.BufferType.SPANNABLE);

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
            mtxtdestination.setText(builder, TextView.BufferType.SPANNABLE);
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
            mtxtdestination.setText(builder, TextView.BufferType.SPANNABLE);
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