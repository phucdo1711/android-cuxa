package com.example.dell.appcuxa.ObjectModels;

import java.io.Serializable;

public class Price implements Serializable {
    public String min;
    public String max;

    public Price(String min, String max) {
        this.min = min;
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
