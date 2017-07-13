package com.hoffmans.rush.ui.driver.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.OnRecordsItemClickListeners;
import com.hoffmans.rush.model.CustomerDetail;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.Rating;
import com.hoffmans.rush.model.Record;

import java.util.List;

/**
 * Created by devesh on 14/3/17.
 */

public class RecordAdapter  extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {


    private List<Record> recordDataList;
    private Context mContext;
    private SpannableStringBuilder mbuilder=new SpannableStringBuilder();
    private OnRecordsItemClickListeners itemClickListeners;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case


        ImageView imgProfile,imgRating1,imgRating2,imgRating3;
        TextView txtNamePhone,txtSource,txtDestination,txtEstimatedPrice,txtDateTime,txtState,txtRatingStaus;
        RelativeLayout layoutDriverRow;


        public ViewHolder(View v) {
            super(v);
            imgProfile       =(ImageView)v.findViewById(R.id.imgProfile);
            txtNamePhone     =(TextView)v.findViewById(R.id.txtNamePhone);
            txtSource        =(TextView)v.findViewById(R.id.txtSource);
            txtDestination   =(TextView)v.findViewById(R.id.txtdestination);
            txtEstimatedPrice=(TextView)v.findViewById(R.id.txtPriceEstimated);
            txtDateTime      =(TextView)v.findViewById(R.id.txtdateTime);
            txtState         =(TextView)v.findViewById(R.id.txtState);
            imgRating1=(ImageView)v.findViewById(R.id.imgRating1);
            imgRating2=(ImageView)v.findViewById(R.id.imgRating2);
            imgRating3=(ImageView)v.findViewById(R.id.imgRating3);
            txtRatingStaus=(TextView)v.findViewById(R.id.txtRatingStaus) ;
            layoutDriverRow   =(RelativeLayout)v.findViewById(R.id.layout_driver_records);
            layoutDriverRow.setOnClickListener(this);
             v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {



        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecordAdapter(Context context, List<Record> recordDataList,OnRecordsItemClickListeners listener) {
        this.recordDataList=recordDataList;
        itemClickListeners=listener;
        mContext=context;



    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.row_driver_records, parent, false);
        // set the view's size, margins, paddings and layout parameters

        RecordAdapter.ViewHolder vh = new RecordAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, final int position) {

        Record record =recordDataList.get(position);
        CustomerDetail customerDetail=record.getCustomer_details();
        Estimate estimate=record.getEstimate();
        PickDropAddress pickUpAddress=record.getPick_up();
        DateTime dateTime=record.getDate_time();
        Rating rating=record.getRate();
        List<PickDropAddress> dropAddresses=record.getDrop_down();
        if(customerDetail!=null){
            setCustomerDetail(customerDetail,holder);
        }
        if(estimate!=null){
           setPriceEstimate(estimate,holder);
        }
        if(pickUpAddress!=null){
           setPickAddress(pickUpAddress,holder);
        }
        if(dropAddresses!=null && dropAddresses.size()>0){
           setDropAddresses(dropAddresses,holder);
        }
        if(dateTime!=null){
            holder.txtDateTime.setText(dateTime.getDate()+" "+dateTime.getTime());
        }



        if(rating!=null && rating.getRating()!=null){


            if(rating.getRating().equals("3")){
                holder.imgRating1.setImageResource(R.drawable.star_select);
                holder.imgRating2.setImageResource(R.drawable.star_select);
                holder.imgRating3.setImageResource(R.drawable.star_select);
                holder.txtRatingStaus.setText("Great Service");
            }else if(rating.getRating().equals("2")){
                holder.imgRating1.setImageResource(R.drawable.star_select);
                holder.imgRating2.setImageResource(R.drawable.star_select);
                holder.imgRating3.setImageResource(R.drawable.start_unselect);

                holder.txtRatingStaus.setText("Average Service");
            }else if(rating.getRating().equals("1")){
                holder.imgRating1.setImageResource(R.drawable.star_select);
                holder.imgRating2.setImageResource(R.drawable.start_unselect);
                holder.imgRating3.setImageResource(R.drawable.start_unselect);
                holder.txtRatingStaus.setText("Worst Service");
            }

        }

        holder.txtState.setText(mContext.getString(R.string.str_finished));

        holder.layoutDriverRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListeners.onRecordsItemClicked(position);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return recordDataList.size();
    }


    /**
     * set customer detail
     * @param customerDetail
     */
    private void setCustomerDetail(CustomerDetail customerDetail ,RecordAdapter.ViewHolder holder ){
        String customerphone=customerDetail.getPhone();
        if(customerphone==null){
            customerphone="";
        }
        StringBuilder namePhoneBuilder=new StringBuilder("").append(customerDetail.getName())
                .append("     ").append(customerphone);
        holder.txtNamePhone.setText(namePhoneBuilder.toString());
        if(customerDetail.getPicUrl()!=null){
            Glide.with(mContext).load(customerDetail.getPicUrl()).into(holder.imgProfile);
        }
    }

    /**
     * set price estimation
     * @param estimate
     */
    private void setPriceEstimate(Estimate estimate,RecordAdapter.ViewHolder holder){
        StringBuilder stringBuilder=new StringBuilder();
        if(estimate.getSymbol()!=null){
            stringBuilder.append(estimate.getSymbol());
        }
        if(estimate.getApproxConvertedAmount()!=null){
            stringBuilder.append(" ").append(estimate.getApproxConvertedAmount());
        }
        holder.txtEstimatedPrice.setText(stringBuilder.toString());
    }

    /**
     * set source address
     * @param pickUpAddress
     */
    private void setPickAddress(PickDropAddress pickUpAddress,RecordAdapter.ViewHolder holder){
        //set spannable string
        mbuilder.clear();

        String start = mContext.getString(R.string.str_collect)+": ";
        getSpanableBuilder(start,ContextCompat.getColor(mContext,R.color.civ_border));
        String address=pickUpAddress.getStreetAddress();
        SpannableStringBuilder builder=getSpanableBuilder(address,ContextCompat.getColor(mContext,R.color.colorPrimary));

        holder.txtSource.setText(builder, TextView.BufferType.SPANNABLE);

    }

    /**
     * set multiple drop address if having multiple drops
     * @param dropAddresses
     */
    private void setDropAddresses(List<PickDropAddress>dropAddresses,RecordAdapter.ViewHolder holder){
        if(dropAddresses.size()==1){
            //single destination order
            PickDropAddress dropAddress=dropAddresses.get(0);
            mbuilder.clear();

            String start = mContext.getString(R.string.str_deliver)+": ";
            getSpanableBuilder(start,ContextCompat.getColor(mContext,R.color.civ_border));
            String address=dropAddress.getStreetAddress();
            SpannableStringBuilder builder=getSpanableBuilder(address,ContextCompat.getColor(mContext,R.color.colorPrimary));

            holder.txtDestination.setText(builder, TextView.BufferType.SPANNABLE);

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
            holder.txtDestination.setText(builder, TextView.BufferType.SPANNABLE);
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
