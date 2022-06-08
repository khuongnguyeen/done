package com.tools.files.myreader.ulti;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtility {
    private static final String NAME_PREF = "receive_shared_pre";
    public static final String TAG_ENABLE_FISRT_INSTALL_APP = "TAG_FISRT_INSTALL_APP";
    private static final String EQUALIZER_ENABLED = "EQUALIZER_ENABLED";
    private static final String CHECK_PERMISSION = "CHECK_PERMISSION";
    private static final String RATE_FIRST_SHOW_DIALOG = "RATE_FIRST_SHOW_DIALOG";
    private static final String SET_RATE = "SET_RATE";
    private static final String SET_TIME_RATE = "SET_TIME_RATE";
    private static final String SET_TIME_USE_APP = "SET_TIME_USE_APP";
    private static final String SET_TIME_RATE_CURRENT = "SET_TIME_RATE_CURRENT";
    private static final String SET_NEW_VERSION = "SET_NEW_VERSION";
    private static final String SET_SHOW_FULL_ADS = "SET_SHOW_FULL_ADS";
    private static final String SET_SHOW_FULL_ADS_DEFAULT = "SET_SHOW_FULL_ADS_DEFAULT";
    private static final String SET_IS_SHOW_ADS = "SET_IS_SHOW_ADS";
    private static final String SET_SHOW_ADS = "SET_SHOW_ADS";
    private static final String SET_CHANGE_DIALOG_REMOVE_ADS = "SET_CHANGE_DIALOG_REMOVE_ADS";
    private static final String SET_CHANGE_DIALOG_REMOVE_ADS_DEFAULT = "SET_CHANGE_DIALOG_REMOVE_ADS_DEFAULT";


    public static boolean getCheckPermission(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getBoolean(CHECK_PERMISSION, false);
    }

    public static void setCheckPermission(Context context, boolean getCheckPermission) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(CHECK_PERMISSION, getCheckPermission).apply();
    }

    /**
     * remote config count show dialog rating: ONLY once
     *
     * @param context: MainPCActivities
     */
    public static boolean getRatingFirst(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getBoolean(RATE_FIRST_SHOW_DIALOG, true);
    }

    public static void setRatingFirst(Context context, boolean getRatingFirst) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(RATE_FIRST_SHOW_DIALOG, getRatingFirst).apply();
    }

    // Rate
    public static boolean getRate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getBoolean(SET_RATE, false);
    }

    public static void setRate(Context context, boolean getRate) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SET_RATE, getRate).apply();
    }

    public static void setNewVersion(Context context, int versionCode) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SET_NEW_VERSION, versionCode).apply();
    }

    public static int getNewVersion(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getInt(SET_NEW_VERSION, 0);
    }



    public static void setTimeRate(Context context, long getTimeRate) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(SET_TIME_RATE, getTimeRate).apply();
    }

    public static long getTimeRateCurrent(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getLong(SET_TIME_RATE_CURRENT, 0);
    }

    public static void setTimeRateCurrent(Context context, long getTimeRate) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(SET_TIME_RATE_CURRENT, getTimeRate).apply();
    }

    public static long getTimeUseApp(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getLong(SET_TIME_USE_APP, 0);
    }

    public static void setTimeUseApp(Context context, long getTimeUseApp) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(SET_TIME_USE_APP, getTimeUseApp).apply();
    }

    public static boolean getShowRate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getBoolean(SET_SHOW_ADS, true);
    }

    public static void setShowRate(Context context, boolean getShowRate) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SET_SHOW_ADS, getShowRate).apply();
    }


public static boolean getIsShowAds(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getBoolean(SET_IS_SHOW_ADS, true);
    }

    public static void setIsShowAds(Context context, boolean getIsShowAds) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SET_IS_SHOW_ADS, getIsShowAds).apply();
    }

    public static int getShowFullAds(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getInt(SET_SHOW_FULL_ADS, 0);
    }

    public static void setShowFullAds(Context context, int getShowFullAds) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SET_SHOW_FULL_ADS, getShowFullAds).apply();
    }

    public static int getShowFullAdsDefault(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getInt(SET_SHOW_FULL_ADS_DEFAULT, 0);
    }

    public static void setShowFullAdsDefault(Context context, int getShowFullAdsDefault) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SET_SHOW_FULL_ADS_DEFAULT, getShowFullAdsDefault).apply();
    }

    public static int getRemoveAdsChangeDialog(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getInt(SET_CHANGE_DIALOG_REMOVE_ADS, 0);
    }

    public static void setRemoveAdsChangeDialog(Context context, int getRemoveAdsChangeDialog) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SET_CHANGE_DIALOG_REMOVE_ADS, getRemoveAdsChangeDialog).apply();
    }

    public static int getRemoveAdsChangeDialogDefault(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        return preferences.getInt(SET_CHANGE_DIALOG_REMOVE_ADS_DEFAULT, 0);
    }

    public static void setRemoveAdsChangeDialogDefault(Context context, int getRemoveAdsChangeDialogDefault) {
        SharedPreferences preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SET_CHANGE_DIALOG_REMOVE_ADS_DEFAULT, getRemoveAdsChangeDialogDefault).apply();
    }
}
