package com.mallardduckapps.akbankir.busevents;

/**
 * Created by oguzemreozcan on 20/04/16.
 */
public class EventDeviceRegisterRequest {
    private final String registrationId;
    private final boolean isActive;
    private final String lang;

    public EventDeviceRegisterRequest(String registrationId, boolean isActive, String lang) {
        this.registrationId = registrationId;
        this.isActive = isActive;
        this.lang = lang;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getLang() {
        return lang;
    }
}
