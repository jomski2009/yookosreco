package com.yookos.reco.enums;

/**
 * Created by jome on 2014/02/07.
 */
public enum ProfileTypes {
    TITLE(1), DEPARTMENT(2), ADDRESS(3), PHONE_NUMBER(4), HOME_PHONE_NUMBER(5), MOBILE_PHONE_NUMBER(6), HIRE_DATE(7), COMPANY(5015), BIOGRAPHY(8), EXPERTISE(9), ALTERNATE_EMAIL(10), RELATIONSHIPSTATUS(5012), HOME_ADDRESS(12), LOCATION(12), USERHOBBY(5010), USERCOUNTRY(5009), SALUTATION(5006), BIRTHDATE(5002), GENDER(5001);
    private int value;

    private ProfileTypes(int value) {
        this.value = value;
    }

    public int getValue(){
        return  value;
    }

}
