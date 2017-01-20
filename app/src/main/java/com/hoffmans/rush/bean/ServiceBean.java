package com.hoffmans.rush.bean;

import com.hoffmans.rush.model.CardData;
import com.hoffmans.rush.model.Estimate;

/**
 * Created by devesh on 17/1/17.
 */

public class ServiceBean extends BaseBean {

    private Estimate estimate;
    private CardData default_card;

    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    public CardData getDefault_card() {
        return default_card;
    }

    public void setDefault_card(CardData default_card) {
        this.default_card = default_card;
    }
}
