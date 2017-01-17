package com.hoffmans.rush.bean;

import com.hoffmans.rush.model.Estimate;

/**
 * Created by devesh on 17/1/17.
 */

public class ServiceBean extends BaseBean {

    private Estimate estimate;

    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }
}
