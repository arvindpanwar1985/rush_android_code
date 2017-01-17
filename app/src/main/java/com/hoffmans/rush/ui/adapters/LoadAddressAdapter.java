package com.hoffmans.rush.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.OnitemClickListner;
import com.hoffmans.rush.model.PickDropAddress;

import java.util.List;


/**
 * Created by devesh on 10/1/17.
 */

public class LoadAddressAdapter  extends RecyclerView.Adapter<LoadAddressAdapter.ViewHolder> {


    private List<PickDropAddress> addressdata;
    private Context mContext;
    private OnitemClickListner.OnFrequentAddressClicked mItemClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case

        TextView txtAddress,txtviewFrequentlyAddress;

        public ViewHolder(View v) {
            super(v);
            txtAddress=(TextView)v.findViewById(R.id.txtAddress);
            txtviewFrequentlyAddress=(TextView)v.findViewById(R.id.viewFrequentlyAddress);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (mItemClickListener != null) {

                //mItemClickListener.onitemclicked(v,getPosition());
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public LoadAddressAdapter(Context context,List<PickDropAddress> addressData, OnitemClickListner.OnFrequentAddressClicked listner) {
        addressdata=addressData;
        mContext=context;
        mItemClickListener = listner;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public LoadAddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_address, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(position==0){
            holder.txtAddress.setHint(mContext.getString(R.string.str_hint_start));
        }else{
            holder.txtAddress.setHint(mContext.getString(R.string.str_hint_end));
        }
        final  PickDropAddress address=addressdata.get(position);
        if(!TextUtils.isEmpty(address.getStreetAddress())){
            //update address
            holder.txtAddress.setText(address.getStreetAddress());
        }

        holder.txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onitemclicked(view,position);
            }
        });

        holder.txtviewFrequentlyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mItemClickListener.onfrequentAddressclicked(view,position);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
    return addressdata.size();
    }

}