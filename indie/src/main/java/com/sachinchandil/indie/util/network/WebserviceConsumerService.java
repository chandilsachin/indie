package com.sachinchandil.indie.util.network;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * Performs any background task without blocking any thread unlike AsyncTask.
 */
public class WebserviceConsumerService extends IntentService
{
    private static final String TAG = "Webservice";

    public static final String RESULT_JSON_RESPONSE = "com.utils.network.result.json";
    public static final int RESPONSE_OK = 1;
    public static final int RESPONSE_FAILED = 0;
    public static final String RESPONSE_FAILED_REASON = "com.utils.network.extra.failed.reason";
    public static final String RESPONSE_FAILED_REASON_MSG = "com.utils.network.extra.failed.reason.msg";
    public static final int REASON_NO_INTERNET_CONNECTION = 1;
    public static final int REASON_CONNECTION_TIME_OUT = 2;
    public static final int REASON_NO_INTERNET_ACCESS = 3;
    // TODO: Rename parameters
    private static final String EXTRA_WEBSERVICE_NAMESPACE = "com.utils.network.extra.callback";
    private static final String EXTRA_WEBSERVICE_URL = "com.utils.network.extra.url";
    private static final String EXTRA_WEBSERVICE_METHOD = "com.utils.network.extra.method";
    private static final String EXTRA_WEBSERVICE_ACTION = "com.utils.network.extra.action";
    private static final String EXTRA_WEBSERVICE_PARAMETER = "com.utils.network.extra.perameter";
    private static final String EXTRA_RESULT_RECEIVER = "com.utils.network.extra.receiver";
    private static final String EXTRA_WEBSERVICE_SERVICE = "com.utils.network.extra.service";
    private static final int SERVICE_SOAP = 1;
    private static final int SERVICE_REST = 2;


    public WebserviceConsumerService()
    {
        super("WebserviceConsumerService");
    }

    /**
     * Starts this service to perform Callback actionwith the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startSOAPAction(Context context, ResultReceiver receiver, String nameSpace, String url, String method, String action, ArrayList<PropertyInfo> propertyList) throws NoInternetConnectionException
    {
        if (!new NetworkManager(context).isConnectedToInternet())
        {
            throw new NoInternetConnectionException("Device is not connected to internet.");
        }
        Intent intent = new Intent(context, WebserviceConsumerService.class);
        intent.putExtra(EXTRA_RESULT_RECEIVER, receiver);
        intent.putExtra(EXTRA_WEBSERVICE_NAMESPACE, nameSpace);
        intent.putExtra(EXTRA_WEBSERVICE_URL, url);
        intent.putExtra(EXTRA_WEBSERVICE_METHOD, method);
        intent.putExtra(EXTRA_WEBSERVICE_ACTION, action);
        intent.putExtra(EXTRA_WEBSERVICE_PARAMETER, propertyList);
        intent.putExtra(EXTRA_WEBSERVICE_SERVICE, SERVICE_SOAP);
        context.startService(intent);
    }

    public static void startRESTAction(Context context, ResultReceiver receiver, String url, String parameter)
    {
        Intent intent = new Intent(context, WebserviceConsumerService.class);
        intent.putExtra(EXTRA_RESULT_RECEIVER, receiver);
        intent.putExtra(EXTRA_WEBSERVICE_URL, url);
        intent.putExtra(EXTRA_WEBSERVICE_PARAMETER, parameter);
        intent.putExtra(EXTRA_WEBSERVICE_SERVICE, SERVICE_REST);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            if (intent.getIntExtra(EXTRA_WEBSERVICE_SERVICE, 0) == SERVICE_SOAP)
            {
                handleSoapRequest(intent);
            } else
                handleRestRequest(intent);
        }
    }

    private void handleSoapRequest(Intent intent)
    {
        String nameSpace = intent.getStringExtra(EXTRA_WEBSERVICE_NAMESPACE);
        String url = intent.getStringExtra(EXTRA_WEBSERVICE_URL);
        String method = intent.getStringExtra(EXTRA_WEBSERVICE_METHOD);
        String action = intent.getStringExtra(EXTRA_WEBSERVICE_ACTION);
        ArrayList<PropertyInfo> parameter = (ArrayList<PropertyInfo>) intent.getSerializableExtra(EXTRA_WEBSERVICE_PARAMETER);
        final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);

        try
        {
            handleSoapAction(nameSpace, url, method, action, parameter, receiver);
        } catch (XmlPullParserException e)
        {
            e.printStackTrace();
        } catch (EOFException e)
        {
            sendNoInternetAccessSingle(receiver, e.getMessage());
            e.printStackTrace();
        } catch (IOException e)
        {
            sendNoInternetAccessSingle(receiver, e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendNoInternetAccessSingle(ResultReceiver receiver, String message)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(RESPONSE_FAILED_REASON, REASON_NO_INTERNET_ACCESS);
        receiver.send(RESPONSE_FAILED, bundle);
    }

    private void handleRestRequest(Intent intent)
    {
        String url = intent.getStringExtra(EXTRA_WEBSERVICE_URL);
        String params = intent.getStringExtra(EXTRA_WEBSERVICE_PARAMETER);
        final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);
        handleRestAction(url, params, receiver);
    }

    /**
     * Handle action in the provided background thread with the provided
     * parameters.
     */
    private void handleSoapAction(String nameSpace, String url, String method, String action, ArrayList<PropertyInfo> parameter, final ResultReceiver receiver) throws IOException, XmlPullParserException
    {

        SoapWebserviceManager soap = new SoapWebserviceManager(nameSpace);

        soap.executeWebservice(url, method,
                parameter, action, new OnSoapResponse<Boolean>()
                {
                    @Override
                    public Boolean OnResponseComplete(SoapSerializationEnvelope envelope)
                    {
                        Bundle bundle = new Bundle();
                        if (envelope.bodyIn instanceof SoapFault)
                        {
                            bundle.putString(RESPONSE_FAILED_REASON_MSG, ((SoapFault) envelope.bodyIn).getMessage());
                            Log.i(TAG, "WebserviceConsumerService.java/(SoapFault Exception occurred) :" + ((SoapFault) envelope.bodyIn).getMessage());
                            receiver.send(RESPONSE_FAILED, bundle);
                        } else
                        {
                            bundle.putString(RESULT_JSON_RESPONSE, ((SoapObject) envelope.bodyIn).getPropertyAsString(0));
                            receiver.send(RESPONSE_OK, bundle);
                        }
                        return null;
                    }
                });

    }

    private void handleRestAction(String url, String params, final ResultReceiver receiver)
    {
        RestWebServicesManager rest = new RestWebServicesManager(getApplicationContext());
        try
        {
            if (params != null)
                url = url + "?" + params;

            rest.excuteWebserviceRequest(url, new StringToObjectHandler()
            {
                @Override
                public Object convertIntoObject(String encodedContent)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString(RESULT_JSON_RESPONSE, encodedContent);
                    receiver.send(RESPONSE_OK, bundle);
                    return null;
                }
            });
        } catch (IOException e)
        {
            e.printStackTrace();
            Bundle bundle = new Bundle();
            bundle.putInt(RESPONSE_FAILED_REASON, REASON_CONNECTION_TIME_OUT);
            receiver.send(RESPONSE_FAILED, bundle);
        } catch (NoDataReceivedException e)
        {
            e.printStackTrace();
        } catch (NoInternetConnectionException e)
        {
            e.printStackTrace();
            Bundle bundle = new Bundle();
            bundle.putInt(RESPONSE_FAILED_REASON, REASON_NO_INTERNET_CONNECTION);
            receiver.send(RESPONSE_FAILED, bundle);
        }
    }

    public static PropertyInfo prepareParam(String property, String propertyValue)
    {
        PropertyInfo info = new PropertyInfo();
        info.setName("emailAddress");
        info.setValue(propertyValue);
        return info;
    }

    public static PropertyInfo prepareParam(String property, int propertyValue)
    {
        PropertyInfo info = new PropertyInfo();
        info.setName("emailAddress");
        info.setValue(propertyValue);
        return info;
    }

    public static PropertyInfo prepareParam(String property, boolean propertyValue)
    {
        PropertyInfo info = new PropertyInfo();
        info.setName("emailAddress");
        info.setValue(propertyValue);
        return info;
    }
}
