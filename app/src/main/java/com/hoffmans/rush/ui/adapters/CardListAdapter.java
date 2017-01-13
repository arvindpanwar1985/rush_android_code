package com.hoffmans.rush.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.listners.OnitemClickListner;

import java.util.List;

/**
 * Created by devesh on 13/1/17.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {


    private List<String> addressdata;
    private Context mContext;
    private OnitemClickListner.OnFrequentAddressClicked mItemClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case


        ImageView imgCardType;
        TextView  txtCardnumber;
        View bottomLine;

        public ViewHolder(View v) {
            super(v);
            imgCardType  =(ImageView)v.findViewById(R.id.imgCardType);
            txtCardnumber=(TextView)v.findViewById(R.id.txtCardNumber);
            bottomLine   =(View)v.findViewById(R.id.viewCardBottomLine);
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
    public CardListAdapter(Context context,List<String > addressData, OnitemClickListner.OnFrequentAddressClicked listner) {
        addressdata=addressData;
        mContext=context;
        mItemClickListener = listner;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        CardListAdapter.ViewHolder vh = new CardListAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CardListAdapter.ViewHolder holder, final int position) {



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return addressdata.size();
    }

}