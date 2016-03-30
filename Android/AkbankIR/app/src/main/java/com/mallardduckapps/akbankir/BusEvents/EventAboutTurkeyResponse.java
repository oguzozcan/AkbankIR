package com.mallardduckapps.akbankir.busevents;

import com.mallardduckapps.akbankir.objects.AboutTurkeyObject;

import retrofit2.Response;

/**
 * Created by oguzemreozcan on 21/03/16.
 */
public class EventAboutTurkeyResponse {
    private final Response<AboutTurkeyObject> aboutTurkeyObject;

    public EventAboutTurkeyResponse(final Response<AboutTurkeyObject> aboutTurkeyObject) {
        this.aboutTurkeyObject = aboutTurkeyObject;
    }

    public Response<AboutTurkeyObject> getAboutTurkeyObject() {
        return aboutTurkeyObject;
    }
}
