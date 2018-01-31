package com.sachinchandil.indie.util.network;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapSerializationEnvelope;

public interface OnSoapResponse<T>
{

    /**
     * <h1>Object OnResponseComplete(SoapSerializationEnvelope envolope)</h1>
     * <p>Called when response has come.</p>
     *
     * @param envelope - received envelope from server
     * @return T
     */
    T OnResponseComplete(SoapSerializationEnvelope envelope) throws SoapFault;
}
