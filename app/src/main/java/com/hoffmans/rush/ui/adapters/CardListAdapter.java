package com.hoffmans.rush.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.listners.OnCardClicked;
import com.hoffmans.rush.model.CardData;

import java.util.ArrayList;

/**
 * Created by devesh on 13/1/17.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {


    private ArrayList<CardData> cardDataList;
    private Context mContext;
    private boolean showDelete;
    private OnCardClicked mItemClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case


        ImageView imgCardType,imgDleteCard;
        TextView  txtCardnumber,txtCardtype;
        View bottomLine,viewDeafultCard;

        public ViewHolder(View v) {
            super(v);
            imgCardType  =(ImageView)v.findViewById(com.hoffmans.rush.R.id.imgCardType);
            txtCardnumber=(TextView)v.findViewById(com.hoffmans.rush.R.id.txtCardNumber);
            txtCardtype=(TextView)v.findViewById(com.hoffmans.rush.R.id.txtCardType);
            bottomLine   =(View)v.findViewById(com.hoffmans.rush.R.id.viewCardBottomLine);
            viewDeafultCard=(View)v.findViewById(com.hoffmans.rush.R.id.viewDefaultCArd);
            imgDleteCard =(ImageView)v.findViewById(com.hoffmans.rush.R.id.imgDelteCard);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (mItemClickListener != null) {
               if(!showDelete) {
                    mItemClickListener.oncardSelected(v, getAdapterPosition());
                }//only allow user to delete card if card list is greater than 1
                else if(getItemCount()>1 && !cardDataList.get(getAdapterPosition()).getDefault()) {
                    mItemClickListener.onitemclicked(v,getAdapterPosition());
                }
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardListAdapter(Context context, ArrayList<CardData> cardData, OnCardClicked listner, boolean showDelete) {
        cardDataList=cardData;
        mContext=context;
        mItemClickListener = listner;
        this.showDelete=showDelete;


    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(mContext)
                .inflate(com.hoffmans.rush.R.layout.row_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        CardListAdapter.ViewHolder vh = new CardListAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CardListAdapter.ViewHolder holder, final int position) {

        if(position==cardDataList.size()-1){
            holder.bottomLine.setVisibility(View.VISIBLE);
        }else{
            holder.bottomLine.setVisibility(View.GONE);
        }
        final CardData cardData=cardDataList.get(position);

        try {
            holder.txtCardnumber.setText("********" + cardData.getNumber());
            Glide.with(mContext).load(cardData.getImageUrl()).into(holder.imgCardType);
            holder.txtCardtype.setText(cardData.getCardType());
        }catch (NullPointerException e){

        }

        if(cardData.getDefault()){
            //not able to delete default card
            holder.viewDeafultCard.setVisibility(View.VISIBLE);
            holder.imgDleteCard.setVisibility(View.INVISIBLE);

            if(!showDelete){
                holder.imgDleteCard.setVisibility(View.INVISIBLE);
            }else{
                if(getItemCount()==1){
                    //not able to delete single card.
                    holder.imgDleteCard.setVisibility(View.INVISIBLE);
                }
            }
        }else{
            holder.viewDeafultCard.setVisibility(View.INVISIBLE);
            holder.imgDleteCard.setVisibility(View.VISIBLE);
            if(!showDelete){
                holder.imgDleteCard.setVisibility(View.INVISIBLE);
            }else{
                if(getItemCount()==1){
                    //not able to delete single card.
                    holder.imgDleteCard.setVisibility(View.INVISIBLE);
                }
            }
        }


       /* if(!showDelete){//hide delete icon

            holder.imgDleteCard.setVisibility(View.INVISIBLE);
            holder.viewDeafultCard.setVisibility(View.VISIBLE);

        }else{
            if(cardData.getDefault()){
                //not able to delete default card
                holder.viewDeafultCard.setVisibility(View.VISIBLE);
                holder.imgDleteCard.setVisibility(View.INVISIBLE);
            }else{
                holder.viewDeafultCard.setVisibility(View.INVISIBLE);
                holder.imgDleteCard.setVisibility(View.VISIBLE);
            }
            if(getItemCount()==1){
                //not able to delete single card.
                holder.imgDleteCard.setVisibility(View.INVISIBLE);
            }

        }*/

        holder.imgDleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemClickListener!=null){
                    //frequent address clicked i.e delete the current card
                    mItemClickListener.onCardDelet(view,position);
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return cardDataList.size();
    }



}