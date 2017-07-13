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
import com.hoffmans.rush.model.PickDropAddress;

import java.util.ArrayList;

/**
 * Created by devesh on 18/1/17.
 */

public class FavouriteAdapter  extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {


    private ArrayList<PickDropAddress> favDataList;
    private Context mContext;
    private OnitemClickListner.OnFrequentAddressClicked mItemClickListener;
    private boolean showIcons;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case


        ImageView imgFav;
        TextView txtFAvAddress,txtCardtype;
        View bottomLine;

        public ViewHolder(View v) {
            super(v);
            imgFav  =(ImageView)v.findViewById(R.id.imgFav);
            txtFAvAddress=(TextView)v.findViewById(R.id.txtfavaddress);

            bottomLine   =(View)v.findViewById(R.id.viewCardBottomLine);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (mItemClickListener != null && !showIcons) {
                mItemClickListener.onitemclicked(v,getPosition());
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FavouriteAdapter(Context context, ArrayList<PickDropAddress> favData, OnitemClickListner.OnFrequentAddressClicked listner,boolean toshowIcons) {
        favDataList=favData;
        mContext=context;
        mItemClickListener = listner;
        this.showIcons=toshowIcons;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.row_favorite, parent, false);
        // set the view's size, margins, paddings and layout parameters

        FavouriteAdapter.ViewHolder vh = new FavouriteAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FavouriteAdapter.ViewHolder holder, final int position) {

       /* if(showIcons){
            holder.imgFav.setVisibility(View.VISIBLE);
        }else{
            holder.imgFav.setVisibility(View.GONE);
        }*/

        final PickDropAddress pickDropAddress=favDataList.get(position);
        holder.txtFAvAddress.setText(pickDropAddress.getStreetAddress());
        if(position==favDataList.size()-1){
            holder.bottomLine.setVisibility(View.VISIBLE);
        }else{
            holder.bottomLine.setVisibility(View.GONE);
        }
        /*if(pickDropAddress.isFavorite()){
            holder.imgFav.setBackground(ContextCompat.getDrawable(mContext,R.drawable.ic_favorite_24dp_borderline));
        }else{
            holder.imgFav.setBackground(ContextCompat.getDrawable(mContext,R.drawable.ic_favorite_border_24dp));
        }*/
        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onFavoriteAddressclicked(view,position);
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return favDataList.size();
    }



}