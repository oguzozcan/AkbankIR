package com.mallardduckapps.akbankir;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.services.AboutTurkeyRestApi;
import com.mallardduckapps.akbankir.services.CalendarRestApi;
import com.mallardduckapps.akbankir.services.CalendarService;
import com.mallardduckapps.akbankir.services.DashboardRestApi;
import com.mallardduckapps.akbankir.services.DashboardService;
import com.mallardduckapps.akbankir.services.DownloadFileApi;
import com.mallardduckapps.akbankir.services.GraphRestApi;
import com.mallardduckapps.akbankir.services.GraphService;
import com.mallardduckapps.akbankir.services.IrTeamRestApi;
import com.mallardduckapps.akbankir.services.MainGraphRestApi;
import com.mallardduckapps.akbankir.services.MiscService;
import com.mallardduckapps.akbankir.services.RatingsRestApi;
import com.mallardduckapps.akbankir.services.SnapshotRestApi;
import com.squareup.okhttp.Cache;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by oguzemreozcan on 28/01/16.
 */
public class AkbankApp extends Application{

    public final static String ROOT_URL_FOREX = "https://cloud.foreks.com/";
    public final static String ROOT_URL = "http://akbank.steelkiwi.com/";
    public final static String ROOT_URL_1 = "http://akbank.steelkiwi.com";
    private static Bus mBus;

    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofitForex = new Retrofit.Builder()
                .baseUrl(ROOT_URL_FOREX)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                        .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(ROOT_URL_1)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();

        com.squareup.okhttp.OkHttpClient okHttpClient = new com.squareup.okhttp.OkHttpClient();
        File customCacheDirectory = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/AkbankIRCache");
        okHttpClient.setCache(new Cache(customCacheDirectory, 32 * 1024 * 1024)); // Cache = 32MB
        OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
        Picasso picasso = new Picasso.Builder(getApplicationContext()).downloader(okHttpDownloader).build();
        // picasso.setIndicatorsEnabled(true);
        //picasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(picasso);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/DIN.ttf")
                        .setFontAttrId(R.attr.fontPath)
//                        .addCustomViewWithSetTypeface(CustomViewWithTypefaceSupport.class)
//                        .addCustomStyle(TextField.class, R.attr.textFieldStyle)
                        .build()
        );

        getBus().register(this);

        getBus().register(new GraphService(retrofitForex.create(MainGraphRestApi.class),retrofitForex.create(GraphRestApi.class),retrofitForex.create(SnapshotRestApi.class), getBus()));
        getBus().register(new DashboardService(retrofit.create(DashboardRestApi.class),retrofit.create(RatingsRestApi.class), retrofit.create(AboutTurkeyRestApi.class), retrofit1.create(DownloadFileApi.class), getBus()));
        getBus().register(new CalendarService(retrofit.create(CalendarRestApi.class), getBus()));
        getBus().register(new MiscService(retrofit.create(IrTeamRestApi.class), getBus()));
    }

    public Bus getBus() {
        if (mBus == null) {
            mBus = new Bus(ThreadEnforcer.ANY);
        }
        return mBus;
    }

    public void setBus(Bus bus) {
        mBus = bus;
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {

        if(event.getErrorMessage() == null){
            if(isNetworkAvailable(getApplicationContext())){
                Toast.makeText(getApplicationContext(),"Something went wrong, please try again.", Toast.LENGTH_LONG);
            }else{
                Toast.makeText(getApplicationContext(),"İnternetinizi kontrol edin ve tekrar deneyin.", Toast.LENGTH_LONG);
            }
        }
        //Log.e("ReaderApp", event.getErrorMessage());
    }

    public boolean isNetworkAvailable(Context context) {
        if(context == null){
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return networkInfo != null && networkInfo.isConnected();
    }
}
