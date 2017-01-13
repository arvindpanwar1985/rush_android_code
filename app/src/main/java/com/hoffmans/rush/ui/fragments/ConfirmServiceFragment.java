package com.hoffmans.rush.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.activities.CardListActivity;


public class ConfirmServiceFragment extends BaseFragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RelativeLayout viewCardDetails;

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



    @Override
    protected void initViews(View view) {

        viewCardDetails=(RelativeLayout)view.findViewById(R.id.viewCardDetails);
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
                cardListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(cardListIntent);
                break;
        }
    }
}
