package com.tubili.androidmarket.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tubili.androidmarket.Model.Request;
import com.tubili.androidmarket.Model.User;

public class Common {
    public static User currentUser;
    public static Request currentRequest;
    public final static String UPDATE = "Güncelle";
    public final static String DELETE = "Sil";
    public final static String USER_PHONE = "KullaniciNo";
    public final static String USER_PASSWORD = "KullanıcıParola";
    public final static String USER_NAME = "KullanıcıAd";
    public final static String CLIENT = "client";
    public final static String SERVER = "server";


    public static String getStatus(String status) {
        switch (status) {
            case "0":
                return "Alındı";
            case "1":
                return "Yolda";
            case "2":
                return "Teslim Edildi";
            default:
                return "Status Not Available";
        }
    }


    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
