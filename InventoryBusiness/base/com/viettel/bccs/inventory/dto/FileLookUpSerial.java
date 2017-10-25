package com.viettel.bccs.inventory.dto;

/**
 * Created by viet on 05/10/2016.
 */
public class FileLookUpSerial {
    private String id;
    private String customerCausePhone;
    private String dateCause;
    private String timeCause;
    private String serial;

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FileLookUpSerial) {
            if (obj != null) {
                FileLookUpSerial temp = (FileLookUpSerial) obj;
                if (temp.getId().equals(this.getId())) {
                }
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerCausePhone() {
        return customerCausePhone;
    }

    public void setCustomerCausePhone(String customerCausePhone) {
        this.customerCausePhone = customerCausePhone;
    }

    public String getDateCause() {
        return dateCause;
    }

    public void setDateCause(String dateCause) {
        this.dateCause = dateCause;
    }

    public String getTimeCause() {
        return timeCause;
    }

    public void setTimeCause(String timeCause) {
        this.timeCause = timeCause;
    }
}
