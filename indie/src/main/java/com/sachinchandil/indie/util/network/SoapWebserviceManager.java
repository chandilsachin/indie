package com.sachinchandil.indie.util.network;

import com.sachinchandil.indie.util.generalisedClasses.NameValuePairObject;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <h1>public class SoapWebserviceManager</h1>
 * <p>
 * Manages SOAP webservice consumption
 * </p>
 */
public class SoapWebserviceManager
{

    private String NameSpace;
    private SoapSerializationEnvelope envelope;
    private HttpTransportSE transport;

    public SoapWebserviceManager(String NameSpace)
    {
        this.NameSpace = NameSpace;
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    }

    /**
     * <h1>public void setNameSpace(String NameSpace)</h1>
     * <p>
     * sets namespace.
     * </p>
     *
     * @param NameSpace - Namespace
     */
    public void setNameSpace(String NameSpace)
    {
        this.NameSpace = NameSpace;
    }

    /**
     * <h1>public void dotNetWebservice(boolean value)</h1>
     * <p>
     * sets whether calling to dotNet webservice.
     * </p>
     *
     * @param value - any boolean value.
     */
    public void dotNetWebservice(boolean value)
    {
        envelope.dotNet = value;
    }

    /**
     * <h1>public Object executeWebservice(String WebserviceUrl,String
     * MethodName,String Action,ArrayList<NameValuePair> values,OnSoapResponse
     * callback)</h1>
     * <p>
     * consumes SOAP webservice.
     * </p>
     *
     * @param WebserviceUrl - Url of webservice.
     * @param MethodName    - method name of calling webservice
     * @param Action        - action
     * @param values        - values to encapsulate in SOAP object.
     * @param callback      - method to be called on response.
     * @return Object
     * @throws HttpResponseException
     * @throws IOException
     * @throws XmlPullParserException
     */
    public <T> T executeWebservice(String WebserviceUrl, String MethodName, String Action, ArrayList<NameValuePairObject> values, OnSoapResponse<T> callback) throws IOException,
            XmlPullParserException, SocketTimeoutException
    {
        // -- creating Soap Object
        SoapObject object = new SoapObject(NameSpace, MethodName);
        if (values != null)
        {
            Iterator<NameValuePairObject> ite = values.iterator();
            NameValuePairObject pair;
            while (ite.hasNext())
            {
                pair = ite.next();
                object.addProperty(pair.getName(), pair.getValue());
            }
        }
        // -- setting SoapObject to be sent
        envelope.setOutputSoapObject(object);

        // -- reparing to send Soap request.
        transport = new HttpTransportSE(WebserviceUrl);
        transport.call(Action, envelope);

        return callback.OnResponseComplete(envelope);
    }

    public <E> E executeWebservice(String WebserviceUrl, String MethodName, ArrayList<PropertyInfo> values, String Action, OnSoapResponse<E> callback) throws IOException,
            XmlPullParserException
    {
        // -- creating Soap Object
        SoapObject object = new SoapObject(NameSpace, MethodName);
        if (values != null)
        {
            Iterator<PropertyInfo> ite = values.iterator();
            while (ite.hasNext())
            {
                object.addProperty(ite.next());
            }
        }
        // -- setting SoapObject to be sent
        envelope.setOutputSoapObject(object);

        // -- preparing to send Soap request.
        transport = new HttpTransportSE(WebserviceUrl);
        transport.call(Action, envelope);

        return callback.OnResponseComplete(envelope);
    }

}
