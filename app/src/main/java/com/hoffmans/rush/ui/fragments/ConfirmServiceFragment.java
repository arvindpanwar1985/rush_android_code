package com.hoffmans.rush.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.R;
import com.hoffmans.rush.model.CardData;
import com.hoffmans.rush.ui.activities.CardListActivity;
import com.hoffmans.rush.ui.activities.ConfirmServiceActivity;
import com.hoffmans.rush.utils.Constants;


public class ConfirmServiceFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RelativeLayout viewCardDetails;
    private static final int REQUEST_SELECT_CARD=108;
    private CardData mSelectedCard;
    private ImageView imgCardType;
    private TextView txtCardData;

    public ConfirmServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmServiceFragment newInstance(String param1, String param2) {
        ConfirmServiceFragment fragment = new ConfirmServiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_confirm, container, false);
        initViews(view);
        initListeners();
        return view;
    }



    private void onCardSelected(CardData selectedCard){
        mSelectedCard=selectedCard;
        if(selectedCard!=null){
            try{
                txtCardData.setText("***********"+selectedCard.getLast4());
                Glide.with(mActivity).load(selectedCard.getImageUrl()).into(imgCardType);
            }catch (NullPointerException e){

            }
        }

    }

    @Override
    protected void initViews(View view) {

        viewCardDetails=(RelativeLayout)view.findViewById(R.id.viewCardDetails);
        imgCardType=(ImageView)view.findViewById(R.id.imgCardType);
        txtCardData=(TextView)view.findViewById(R.id.txtCardNumber);

    }

    @Override
    protected void initListeners() {

        viewCardDetails.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.viewCardDetails:
                Intent cardListIntent=new Intent(mActivity, CardListActivity.class);
                cardListIntent.putExtra(Constants.KEY_IS_CARD_SELECTABLE,true);
                startActivityForResult(cardListIntent,REQUEST_SELECT_CARD);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_SELECT_CARD && resultCode==mActivity.RESULT_OK){
            if(data!=null){
                CardData selectedCard=data.getParcelableExtra(ConfirmServiceActivity.KEY_CARD_DATA);
                onCardSelected(selectedCard);
            }
        }
    }
}
