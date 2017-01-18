package com.hoffmans.rush.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.CardListBean;
import com.hoffmans.rush.http.request.PaymentRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.OnitemClickListner;
import com.hoffmans.rush.model.CardData;
import com.hoffmans.rush.ui.activities.AddCardActivity;
import com.hoffmans.rush.ui.activities.ConfirmServiceActivity;
import com.hoffmans.rush.ui.adapters.CardListAdapter;
import com.hoffmans.rush.utils.Progress;

import java.util.ArrayList;

import static com.hoffmans.rush.ui.activities.AddCardActivity.REQUEST_ADD_CARD;


public class CardListFragment extends BaseFragment implements View.OnClickListener,OnitemClickListner.OnFrequentAddressClicked {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean isCardSelectable;
    private String mParam2;
    private View view;
    private ArrayList<CardData> cardDataList;
    private RecyclerView recyclerCardList;
    private ImageButton btnAddCard;
    private CardListAdapter adapter;



    public CardListFragment() {
        // Required empty public constructor
    }


    public static CardListFragment newInstance(boolean param1, String param2) {
        CardListFragment fragment = new CardListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isCardSelectable = getArguments().getBoolean(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_card_list, container, false);
            initViews(view);
            initListeners();
            getCarList();
        }
        return view;
    }





    @Override
    protected void initViews(View view) {

        recyclerCardList=(RecyclerView)view.findViewById(R.id.cardListRecycler);
        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerCardList.setLayoutManager(llm);
        btnAddCard=(ImageButton)view.findViewById(R.id.imgAddCard);
    }

    @Override
    protected void initListeners() {

        btnAddCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imgAddCard:
                Intent addCardIntent=new Intent(mActivity, AddCardActivity.class);
                startActivityForResult(addCardIntent,AddCardActivity.REQUEST_ADD_CARD);
                break;
        }
    }


    private void getCarList(){
        Progress.showprogress(mActivity,"Loading cards..",false);
        String authToken=appPreference.getUserDetails().getToken();
        PaymentRequest request=new PaymentRequest();
        request.getPaymentCardList(authToken, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                CardListBean cardListBean=(CardListBean)body;
                if(cardListBean!=null && cardListBean.getCards().size()!=0) {
                    cardDataList =cardListBean.getCards();
                    setCardAdapter(cardDataList);

                }
            }

            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                mActivity.showSnackbar(message,0);
            }
        });
    }

    private void setCardAdapter(ArrayList<CardData> cardDataList ){

        adapter=new CardListAdapter(mActivity,cardDataList,this);
        recyclerCardList.setAdapter(adapter);
    }

    @Override
    public void onitemclicked(View view, int position) {

        if(cardDataList!=null && isCardSelectable){
            CardData selectCard=cardDataList.get(position);
            Intent intent=new Intent();
            intent.putExtra(ConfirmServiceActivity.KEY_CARD_DATA,selectCard);
            getActivity().setResult(Activity.RESULT_OK,intent);
            mActivity.finish();
        }
    }

    @Override
    public void onfrequentAddressclicked(View view, int position) {

    }

    @Override
    public void onFavoriteAddressclicked(View view, int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ADD_CARD && resultCode==mActivity.RESULT_OK){
           if(data!=null){
               CardData newlyAddedCard=data.getParcelableExtra(AddCardActivity.KEY_CARD_DATA);
               cardDataList.add(newlyAddedCard);
               adapter.notifyDataSetChanged();
               mActivity.showSnackbar("Card added Successfully.",0);
           }
        }
    }
}
