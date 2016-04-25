package com.mallardduckapps.akbankir;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mallardduckapps.akbankir.objects.ApiErrorEvent;
import com.mallardduckapps.akbankir.services.AboutTurkeyRestApi;
import com.mallardduckapps.akbankir.services.AnalystCovarageRestApi;
import com.mallardduckapps.akbankir.services.AnnualReportRestApi;
import com.mallardduckapps.akbankir.services.CalendarRestApi;
import com.mallardduckapps.akbankir.services.CalendarService;
import com.mallardduckapps.akbankir.services.DashboardRestApi;
import com.mallardduckapps.akbankir.services.DashboardService;
import com.mallardduckapps.akbankir.services.DeviceRestApi;
import com.mallardduckapps.akbankir.services.DownloadFileApi;
import com.mallardduckapps.akbankir.services.EarningPresentationsRestApi;
import com.mallardduckapps.akbankir.services.GraphRestApi;
import com.mallardduckapps.akbankir.services.GraphService;
import com.mallardduckapps.akbankir.services.InvestorDaysRestApi;
import com.mallardduckapps.akbankir.services.InvestorPresentationRestApi;
import com.mallardduckapps.akbankir.services.IrTeamRestApi;
import com.mallardduckapps.akbankir.services.MainGraphRestApi;
import com.mallardduckapps.akbankir.services.MiscService;
import com.mallardduckapps.akbankir.services.NewsRestApi;
import com.mallardduckapps.akbankir.services.PagesRestApi;
import com.mallardduckapps.akbankir.services.RatingsRestApi;
import com.mallardduckapps.akbankir.services.SnapshotRestApi;
import com.mallardduckapps.akbankir.services.SustainabilityReportRestApi;
import com.mallardduckapps.akbankir.services.WebcastsRestApi;
import com.mallardduckapps.akbankir.services.WebcastsService;
import com.mallardduckapps.akbankir.utils.DataSaver;
import com.mallardduckapps.akbankir.utils.TimeUtil;
import com.squareup.okhttp.Cache;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by oguzemreozcan on 28/01/16.
 */
public class AkbankApp extends Application {

    public final static String ROOT_URL_FOREX = "https://cloud.foreks.com/";
    public final static String ROOT_URL = "http://akbank.steelkiwi.com/";
    public final static String ROOT_URL_1 = "http://akbank.steelkiwi.com";
    private static Bus mBus;
    public static Locale localeTr = new Locale("tr");
    private DataSaver dataSaver;

    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofitForex = createRetrofitObject(ROOT_URL_FOREX);
        Retrofit retrofit = createRetrofitObject(ROOT_URL);
        Retrofit retrofit1 = createRetrofitObject(ROOT_URL_1);
//                new Retrofit.Builder()
//                .baseUrl(ROOT_URL_FOREX)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(new OkHttpClient())
//                .build();


//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Interceptor.Chain chain) throws IOException {
//                Request original = chain.request();
//
//                Request request = original.newBuilder()
//                        //.header("User-Agent", "Your-App-Name")
//                        .header("Accept-Language", "tr")
//                        .method(original.method(), original.body())
//                        .build();
//                return chain.proceed(request);
//            }
//        });
//        OkHttpClient client = httpClient.build();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(ROOT_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(new OkHttpClient())
//                .build();
//
//        Retrofit retrofit1 = new Retrofit.Builder()
//                .baseUrl(ROOT_URL_1)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(new OkHttpClient())
//                .build();
        getDataSaver();
        com.squareup.okhttp.OkHttpClient okHttpClient = new com.squareup.okhttp.OkHttpClient();
        File customCacheDirectory = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/AkbankIRCache");
        okHttpClient.setCache(new Cache(customCacheDirectory, 32 * 1024 * 1024)); // Cache = 32MB
        OkHttpDownloader okHttpDownloader = new OkHttpDownloader(okHttpClient);
        Picasso picasso = new Picasso.Builder(getApplicationContext()).downloader(okHttpDownloader).build();
        // picasso.setIndicatorsEnabled(true);
        //picasso.setLoggingEnabled(true);
        Picasso.setSingletonInstance(picasso);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/DINLight.ttf")// //  //dinbek-regular.ttf
                        .setFontAttrId(R.attr.fontPath)
//                        .addCustomViewWithSetTypeface(CustomViewWithTypefaceSupport.class)
//                        .addCustomStyle(TextField.class, R.attr.textFieldStyle)
                        .build()
        );
        localeTr = getResources().getConfiguration().locale;
        TimeUtil.changeLocale(localeTr);

        registerBusEvents(retrofitForex, retrofit, retrofit1);
    }

    private Retrofit createRetrofitObject(String url){
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
    }

    public void registerBusEvents(Retrofit retrofitForex, Retrofit retrofit, Retrofit retrofit1){
        if(retrofitForex == null){
            retrofitForex = createRetrofitObject(ROOT_URL_FOREX);
        }
        if(retrofit == null){
            retrofit = createRetrofitObject(ROOT_URL);
        }
        if(retrofit1 == null){
            retrofit1 = createRetrofitObject(ROOT_URL_1);
        }
        getBus().register(this);
        getBus().register(new GraphService(retrofitForex.create(MainGraphRestApi.class), retrofitForex.create(GraphRestApi.class), retrofitForex.create(SnapshotRestApi.class), getBus()));
        getBus().register(new DashboardService(retrofit.create(DashboardRestApi.class), retrofit.create(RatingsRestApi.class), retrofit.create(AboutTurkeyRestApi.class), retrofit1.create(DownloadFileApi.class), getBus()));
        getBus().register(new CalendarService(retrofit.create(CalendarRestApi.class), getBus()));
        getBus().register(new WebcastsService(retrofit.create(WebcastsRestApi.class), getBus()));
        getBus().register(new MiscService(retrofit.create(IrTeamRestApi.class), retrofit.create(NewsRestApi.class), retrofit.create(AnalystCovarageRestApi.class),
                retrofit.create(SustainabilityReportRestApi.class), retrofit.create(AnnualReportRestApi.class),
                retrofit.create(InvestorPresentationRestApi.class), retrofit.create(EarningPresentationsRestApi.class),
                retrofit.create(InvestorDaysRestApi.class), retrofit.create(PagesRestApi.class), retrofit.create(DeviceRestApi.class), getBus()));
    }

    public Bus getBus() {
        if (mBus == null) {
            mBus = new Bus(ThreadEnforcer.ANY);
        }
        return mBus;
    }

    public DataSaver getDataSaver() {
        if (dataSaver == null) {
            dataSaver = new DataSaver(getApplicationContext(), "AkbankIR", false);
        }
        return dataSaver;
    }

    public void setBus(Bus bus) {
        mBus = bus;
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {

        if (event.getErrorMessage() == null) {
            if (isNetworkAvailable(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Something went wrong, please try again.", Toast.LENGTH_LONG);
            } else {
                Toast.makeText(getApplicationContext(), "İnternetinizi kontrol edin ve tekrar deneyin.", Toast.LENGTH_LONG);
            }
        }
        //Log.e("ReaderApp", event.getErrorMessage());
    }

    public boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        return networkInfo != null && networkInfo.isConnected();
    }

    public int[] getScreenSize(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        int[] sizes = {width, height};
        Log.d("SCREEN_SIZE", "WIDTH: " + width);
        Log.d("SCREEN_SIZE", "HEIGHT: " + height);
        Log.d("SCREEN_SIZE", "Density: " + activity.getResources().getDisplayMetrics().density);
        return sizes;
    }
}
