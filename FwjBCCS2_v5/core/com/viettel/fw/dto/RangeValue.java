package com.viettel.fw.dto;

/**
 * @author nhannt34
 */
public class RangeValue {
    private Object fromValue;
    private Object toValue;

    public RangeValue(Object fromValue, Object rangeValue) {
        this.fromValue = fromValue;
        this.toValue = rangeValue;
    }

    public RangeValue() {
    }

    public Object getFromValue() {
        return fromValue;
    }

    public void setFromValue(Object fromValue) {
        this.fromValue = fromValue;
    }

    public Object getToValue() {
        return toValue;
    }

    public void setToValue(Object toValue) {
        this.toValue = toValue;
    }
}
