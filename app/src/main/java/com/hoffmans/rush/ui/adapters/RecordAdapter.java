package com.hoffmans.rush.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.R;
import com.hoffmans.rush.model.DateTime;
import com.hoffmans.rush.model.DriverDetail;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.Record;

import java.util.List;

/**
 * Created by devesh on 24/1/17.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {


    private static final String STATIC_MAP_URL="http://maps.google.com/maps/api/staticmap?center=";
    private List<Record> recordDataList;
    private Context mContext;
    private int height ,widht;
    private boolean isRecord;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case


        ImageView imgdriver;
        ImageView imgeMap;
        TextView txtDriverName,txtDateTime,txtAmount,txtVechileName,txtStreetAddress;
        View bottomLine;

        public ViewHolder(View v) {
            super(v);
            imgdriver  =(ImageView)v.findViewById(R.id.imgDriver);
            txtDriverName=(TextView)v.findViewById(R.id.txtdriverName);
            txtDateTime=(TextView)v.findViewById(R.id.txtdateTime);
            txtAmount=(TextView)v.findViewById(R.id.txtAmount);
            //txtVechileName=(TextView)v.findViewById(R.id.txtvechileName);
            imgeMap=(ImageView) v.findViewById(R.id.mapImage);
            bottomLine   =(View)v.findViewById(R.id.viewCardBottomLine);
            txtStreetAddress=(TextView)v.findViewById(R.id.txtStreetAddress);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecordAdapter(Context context, List<Record> favData,boolean isRecord) {
        recordDataList=favData;
        mContext=context;
        this.isRecord=isRecord;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
         height = displaymetrics.heightPixels/5;
         widht = displaymetrics.widthPixels;


    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.row_record, parent, false);
        // set the view's size, margins, paddings and layout parameters

        RecordAdapter.ViewHolder vh = new RecordAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, final int position) {

        final Record record=recordDataList.get(position);
        final DateTime dateTime=record.getDate_time();
        final Estimate estimate=record.getEstimate();
        final DriverDetail driverDetail=record.getDriver_details();
        if(getImageMap(record.getPick_up())!=null){
            Glide.with(mContext).load(getImageMap(record.getPick_up())).into(holder.imgeMap);
        }
        try{
            if(estimate.getApproxConvertedAmount()!=null){
                holder.txtAmount.setText(estimate.getSymbol()+estimate.getApproxConvertedAmount().toString());
            }
            if(dateTime.getDate()!=null &&dateTime.getTime()!=null){
                holder.txtDateTime.setText(dateTime.getDate()+" "+dateTime.getTime());
            }
            if(driverDetail.getName()!=null){
                holder.txtDriverName.setText(driverDetail.getName());
            }

            if(record.getDriver_details().getPicUrl()!=null) {
             Glide.with(mContext).load(record.getDriver_details().getPicUrl()).into(holder.imgdriver);
            }

            if(!isRecord && record.getPick_up()!=null){
                holder.txtStreetAddress.setText(record.getPick_up().getStreetAddress());
            }
        }catch (NullPointerException e){

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return recordDataList.size();
    }




    private String getImageMap(PickDropAddress pickDropAddress){
        if(pickDropAddress!=null) {
            return STATIC_MAP_URL+ pickDropAddress.getLatitude()+ "," + pickDropAddress.getLongitude() + "&zoom=18&size="+widht+"x"+height+"&sensor=false";
        }
        return null;
    }

}