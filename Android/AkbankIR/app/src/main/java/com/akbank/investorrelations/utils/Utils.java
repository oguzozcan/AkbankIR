package com.akbank.investorrelations.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Toast;

import com.akbank.investorrelations.R;

import java.util.UUID;

/**
 * Created by oguzemreozcan on 01/04/16.
 */
public class Utils {

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static boolean isMarshmallow() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public static int getActionBarHeight(Activity activity) {
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
             return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }

        return 0;
    }

//    private int getActionBarHeight(Activity activity) {
//        int actionBarHeight = activity.getSupportActionBar().getHeight();
//        if (actionBarHeight != 0)
//            return actionBarHeight;
//        final TypedValue tv = new TypedValue();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
//                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
//        } else if (activity.getTheme().resolveAttribute(com.actionbarsherlock.R.attr.actionBarSize, tv, true))
//            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
//        return actionBarHeight;
//    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static void sendMail(String email, String recipient, String subject, String imagePath, Activity activity) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:")); // .concat(recipient)
        emailIntent.setType("message/rfc822");
        //emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        emailIntent.putExtra(Intent.EXTRA_TEXT, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (imagePath != null) {
            Uri pngUri = Uri.parse("file://" + imagePath);//Uri.parse(imagePath);
            emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, pngUri);
            //emailIntent.setType("application/image");
        }
        //i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pic));

        // i.setType("image/png");
        try {
            activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.send_mail)));
            // finish();
            Log.i("Email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, activity.getString(R.string.no_mail_app),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //TODO May not work on tablets beaware
    public static String getDeviceId(Activity activity) {
        final TelephonyManager tm = (TelephonyManager) activity.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(activity.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }
}
