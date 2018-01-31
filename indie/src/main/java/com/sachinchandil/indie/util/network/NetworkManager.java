package com.sachinchandil.indie.util.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <h1></h1>
 * <h1>public class NetworkManager</h1>
 * <p>This class handles network related operations.</p>
 */
public class NetworkManager
{
    /**
     * Not connected.
     */
    public static final int TYPE_NOT_CONNECTED = -1;
    public static final int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
    public static final int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
    public static final int TYPE_WIMAX = ConnectivityManager.TYPE_WIMAX;
    public static final int TYPE_BLUETOOTH = ConnectivityManager.TYPE_BLUETOOTH;
    /**
     * indicates download was successful.
     */
    public static final int SUCCESS = 1;
    /**
     * indicates download was successful.
     */
    public static final int UNSUCCESS = -1;
    ConnectivityManager manager;
    /**
     * Activity context or application context.
     */
    Context context;
    NetworkInfo netWorkInfo;


    /**
     * <h1></h1>
     * <h1>public NetworkManager(Context context)</h1>
     * <p>Constructor</p>
     *
     * @param context - Context of current activity or application.
     */
    public NetworkManager(Context context)
    {
        this.context = context;
        // getting manager for connectivity service to check Internet connection.
        manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // get network inforamtion.
        netWorkInfo = manager.getActiveNetworkInfo();
    }

    /**
     * <h1></h1>
     * <h1>public boolean isConnectedToInternet()</h1>
     * <p>checks whether device is connected to Internet.</p>
     *
     * @return a boolean.
     */
    public boolean isConnectedToInternet()
    {
        netWorkInfo = manager.getActiveNetworkInfo();
        if (netWorkInfo == null)
            return false;
        else
            return netWorkInfo.isConnected();
    }

    /**
     * <p>Reports the type of network to which the device is connected to Internet. </p>
     *
     * @return - one of TYPE_MOBILE,<br/> TYPE_WIFI, TYPE_WIMAX, TYPE_BLUETOOTH types defined by NetworkManager
     */
    public int getConnectedNetworkType()
    {
        netWorkInfo = manager.getActiveNetworkInfo();
        if (netWorkInfo != null)
            return netWorkInfo.getType();
        else
            return -1;
    }

    public boolean isInternetConnectedThroughMobile()
    {
        netWorkInfo = manager.getActiveNetworkInfo();
        if (netWorkInfo != null)
            return netWorkInfo.getType() == TYPE_MOBILE;
        else
            return false;
    }

    public boolean isInternetConnectedThroughWifi()
    {
        netWorkInfo = manager.getActiveNetworkInfo();
        if (netWorkInfo != null)
            return netWorkInfo.getType() == TYPE_WIFI;
        else
            return false;
    }

}
