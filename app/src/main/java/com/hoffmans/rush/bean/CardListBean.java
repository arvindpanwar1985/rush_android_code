package com.hoffmans.rush.bean;

import com.hoffmans.rush.model.CardData;

import java.util.ArrayList;

/**
 * Created by devesh on 13/1/17.
 */

public class CardListBean extends BaseBean {

 private ArrayList<CardData>  cards=new ArrayList<>();
 public ArrayList<CardData> getCards() {
        return cards;
    }

}
