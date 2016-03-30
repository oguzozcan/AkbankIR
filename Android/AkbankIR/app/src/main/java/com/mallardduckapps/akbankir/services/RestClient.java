package com.mallardduckapps.akbankir.services;

/**
 * Created by oguzemreozcan on 25/01/16.
 */

import android.util.Log;

import com.mallardduckapps.akbankir.objects.BasicNameValuePair;
import com.mallardduckapps.akbankir.objects.ReturnType;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class RestClient {

    private final OkHttpClient client = new OkHttpClient();
    private String mainUrl;
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType IMAGE
            = MediaType.parse("image/jpeg");
    public final static String ACCESS_TOKEN = "YWtiYW5rLXlhdGlyaW06YTI0Y0JzIXpY";
    private final String TAG = "REST_CLIENT";
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String NAME_VALUE_SEPARATOR = "=";
    // public final String testAuthToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBcm11dFdlYkFQSSIsImF1ZCI6ImI2OWNmYzczY2U5ODQyMjE4ZTg5YTQ1YzI4YzJiMGMxIiwiZXhwIjoxNDUxMzkwMjk4LCJuYmYiOjE0MTk4NTQyOTh9.PTxgbzMlOWyNmBcMMS1IxJ3xQNGRloFluCQury154Ww";

    public RestClient(String mainUrl){
        this.mainUrl = mainUrl;
    }
    public RestClient(){

    }

    public ReturnType doGetRequest(String url, List<BasicNameValuePair> pairs, BasicNameValuePair header) throws IOException {
        Request request = null;
//        String paramString = pairs != null ? format(pairs, "utf-8"): "";
//        if(!paramString.equals("")){
//            url =  new StringBuilder(mainUrl).append(url).append("?").append(paramString).toString();
//        }else{
//            url = mainUrl + url;
//        }
        Log.d(TAG, "DO GET REQUEST: URL: " + url);
//        if(ACCESS_TOKEN != null){
            Request.Builder requestBuilder = new Request.Builder().url(url);
//            if(ACCESS_TOKEN != null) {
                //Log.d(TAG, "ACCESS TOKEN: " + ACCESS_TOKEN);
                requestBuilder.addHeader("Authorization", ACCESS_TOKEN);
//                requestBuilder.addHeader("client_info", ArmutUtils.clientInfo);
                if(header != null){
                    requestBuilder.addHeader(header.getName(), header.getValue());
                }
//            }
            request = requestBuilder.build();
//        }else{
//            Log.e("EXCEPTION", "ACCESS TOKEN NULL!!!");
//        }
        Response response = client.newCall(request).execute();
        int responseCode = response.code();
        Log.d(TAG, "RESPONSE CODE: " + responseCode);
        return new ReturnType(responseCode, response.body().string());
    }

    public ReturnType doGetRequestWithStatus(String url, List<BasicNameValuePair> pairs, BasicNameValuePair header) throws IOException {
        Request request = null;
        String paramString = pairs != null ? format(pairs, "utf-8"): "";
        if(!paramString.equals("")){
            url =  new StringBuilder(mainUrl).append(url).append("?").append(paramString).toString();
        }else{
            url = mainUrl + url;
        }
        Log.d(TAG, "DO GET REQUEST: URL: " + url);
        if(ACCESS_TOKEN != null){
            Request.Builder requestBuilder = new Request.Builder().url(url);
            if(ACCESS_TOKEN !=null) {
                requestBuilder.addHeader("Authorization", "Bearer "+ ACCESS_TOKEN);
//                requestBuilder.addHeader("client_info", ArmutUtils.clientInfo);
                if(header != null){
                    requestBuilder.addHeader(header.getName(), header.getValue());
                }
            }
            request = requestBuilder.build();
        }else{
            Log.e("EXCEPTION", "ACCESS TOKEN NULL!!!");
        }
        Response response = client.newCall(request).execute();
        int responseCode = response.code();
        Log.d(TAG, "RESPONSE CODE: " + responseCode);
        return new ReturnType(responseCode, response.body().string());
    }

    public ReturnType doDeleteRequest(String url, List<BasicNameValuePair> pairs) throws IOException {
        Request request = null;
        String paramString = pairs != null ? format(pairs, "utf-8"): "";
        if(!paramString.equals("")){
            url = new StringBuilder(mainUrl).append(url).append("?").append(paramString).toString();
        }else{
            url = mainUrl + url;
        }
        Log.d(TAG, "DO DELETE REQUEST: URL: " + url);
        if(ACCESS_TOKEN != null){
            Request.Builder requestBuilder = new Request.Builder().url(url);
            if(ACCESS_TOKEN !=null) {
                requestBuilder.addHeader("Authorization", "Bearer "+ ACCESS_TOKEN);
//                requestBuilder.addHeader("client_info", ArmutUtils.clientInfo);
            }
            request = requestBuilder.delete().build();
        }else{
            Log.e("EXCEPTION", "ACCESS TOKEN NULL!!!");
        }
        Response response = client.newCall(request).execute();
        int responseCode = response.code();
        Log.d(TAG, "RESPONSE CODE: " + responseCode);
        return new ReturnType(responseCode, response.body().string());
    }

    public String doPostRequestAsForm(String url, BasicNameValuePair... pairs) throws IOException {
        Request request = createRequestAsForm(url, pairs);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static String format(
            final List<? extends BasicNameValuePair> parameters,
            final String encoding) {
        final StringBuilder result = new StringBuilder();
        for (final BasicNameValuePair parameter : parameters) {
            final String encodedName = encode(parameter.getName(), encoding);
            final String value = parameter.getValue();
            final String encodedValue = value != null ? encode(value, encoding) : "";
            if (result.length() > 0)
                result.append(PARAMETER_SEPARATOR);
            result.append(encodedName);
            result.append(NAME_VALUE_SEPARATOR);
            result.append(encodedValue);
        }
        return result.toString();
    }
    private static String encode(final String content, final String encoding) {

        try {
            return URLEncoder.encode(content,
                    encoding != null ? encoding : "ISO-8859-1");
        } catch (UnsupportedEncodingException problem) {
            throw new IllegalArgumentException(problem);
        }
    }

    public ReturnType doPostOrPatchRequestWithJSON(boolean isPost, String url, String accessToken, String json) throws IOException {
        url = mainUrl + url;
        if(accessToken == null){
            accessToken = ACCESS_TOKEN;
            Log.d("TAG", "Url" + url + " - ACCESS TOKEN: " + accessToken);
        }
        Request request = createRequestWithBody(isPost, url,accessToken, json);
        Response response = client.newCall(request).execute();
        int responseCode = response.code();
        Log.d(TAG, "RESPONSE CODE: " + responseCode);
//        ReturnType returnType = new ReturnType(responseCode, response.body().string());
//        if(responseCode == 500){
//            return null;
//        }
//        if(responseCode == 201 || responseCode == 204 || responseCode == 200){
//            return response.body().string();
//        }
        return new ReturnType(responseCode, response.body().string());
    }

    public ReturnType doPostRequestForImage(String url, String accessToken,List<BasicNameValuePair> pairs, byte[] imageEncoded) throws IOException {
        //url = mainUrl + url;
        if(accessToken == null){
            accessToken = ACCESS_TOKEN;
        }
        String paramString = pairs != null ? format(pairs, "utf-8"): "";
        if(!paramString.equals("")){
            url =  new StringBuilder(mainUrl).append(url).append("?").append(paramString).toString();
        }else{
            url = mainUrl + url;
        }
        Log.d("TAG", "Url" + url + " - ACCESS TOKEN: " + accessToken);
        Request request = createRequestWithBodyForImage(url, accessToken, imageEncoded);
        Response response = client.newCall(request).execute();
        int responseCode = response.code();
        Log.d(TAG, "RESPONSE CODE: " + responseCode);

        return new ReturnType(responseCode, response.body().string());
    }

//    public ReturnType doPostRequest(String url, boolean requestWithHeader, String accessToken,BasicNameValuePair... pairs) throws IOException, JSONException {
//        Request request = null;
//        if(!requestWithHeader){
//            request = createRequestWithBody(url, ArmutUtils.getBasicJson(pairs));
//        }else{
//            request = createRequestWithHeaders(url, pairs);
//        }
//        Response response = client.newCall(request).execute();
//        int responseCode = response.code();
//        Log.d(TAG, "RESPONSE CODE: " + responseCode);
//        ReturnType returnType = new ReturnType(responseCode, response.body().string());
////        if(responseCode == 500){
////            return null;
////        }
////        if(responseCode == 201 || responseCode == 204 || responseCode == 200){
////            return response.body().string();
////        }
//        return returnType;
//    }

//    private Request createRequestWithHeaders(String url, BasicNameValuePair... pairs ){
//        Request.Builder requestBuilder = new Request.Builder().url(url);
//        if(ACCESS_TOKEN !=null) {
//            Log.d(TAG, "ACCESS TOKEN: " + ACCESS_TOKEN);
//            requestBuilder.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);
//            requestBuilder.addHeader("client_info", ArmutUtils.clientInfo);
//        }
//        for(BasicNameValuePair pair : pairs){
//            requestBuilder.addHeader(pair.getName(), pair.getValue());
//        }
//        return requestBuilder.build();
//    }
//
//    private Request createRequestWithBody(String url, String json ){
//        RequestBody body = null;
//        if(json != null) {
//            body = RequestBody.create(JSON, json);
//        }
//        Request.Builder requestBuilder = new Request.Builder().url(url);
//        if(ACCESS_TOKEN !=null) {
//            Log.d(TAG, "ACCESS TOKEN: " + ACCESS_TOKEN);
//            requestBuilder.addHeader("Authorization", "Bearer " + ACCESS_TOKEN);
//            requestBuilder.addHeader("client_info", ArmutUtils.clientInfo);
//        }
//        if(body == null){
//            return requestBuilder.build();
//        }
//        return requestBuilder.post(body).build();
//    }

    private Request createRequestWithBodyForImage(String url, String accessToken, byte[] encodedImage){
        RequestBody body = null;
        if(encodedImage != null) {
            body = RequestBody.create(IMAGE, encodedImage);
        }
        Request.Builder requestBuilder = new Request.Builder().url(url);
        if(accessToken !=null){
            Log.d(TAG, "ACCESS TOKEN: " + accessToken);
            requestBuilder.addHeader("Content-Type", "image/jpeg");
            requestBuilder.addHeader("Authorization", "Bearer "+ accessToken);
//            requestBuilder.addHeader("client_info", ArmutUtils.clientInfo);
        }
        if(body == null){
            return requestBuilder.build();
        }
        return requestBuilder.post(body).build();
    }

    private Request createRequestWithBody(boolean isPost, String url, String accessToken, String json){
        RequestBody body = null;
        if(json != null) {
            body = RequestBody.create(JSON, json);
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        if(accessToken !=null){
            Log.d(TAG, "ACCESS TOKEN: " + accessToken);
            requestBuilder.addHeader("Content-Type", "application/json");
//            requestBuilder.addHeader("access_token", accessToken);
            //Log.d(TAG, "ACCESS TOKEN: AH6VHwcNNBQbi61LhvHgOPXIq9nuwIdHk6CeQPvb");
            requestBuilder.addHeader("Authorization", "Bearer "+ accessToken);
//            requestBuilder.addHeader("client_info", ArmutUtils.clientInfo);
        }
        if(body == null){
            return requestBuilder.build();
        }
        if(isPost){
            return requestBuilder.post(body).build();
        }else{
            return requestBuilder.patch(body).build();
        }
    }

    private Request createRequestAsForm(String url, BasicNameValuePair ... pairs){
        Request.Builder requestBuilder = new Request.Builder().url(url);
        FormEncodingBuilder builder = new FormEncodingBuilder();
        //Log.d(TAG, "PAIR: " + pairs[0].getName() + " - value: " + pairs[0].getValue());
        for(BasicNameValuePair pair: pairs){
            builder.add(pair.getName(), pair.getValue());
        }
        RequestBody formBody = builder.build();
        return requestBuilder.post(formBody).build();
    }
}
