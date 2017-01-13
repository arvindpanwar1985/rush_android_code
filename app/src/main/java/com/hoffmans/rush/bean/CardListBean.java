package com.hoffmans.rush.bean;

import com.hoffmans.rush.model.CardData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devesh on 13/1/17.
 */

public class CardListBean extends BaseBean {

 private List<CardData>  cards=new ArrayList<>();

    public List<CardData> getCards() {
        return cards;
    }
}
