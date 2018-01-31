package com.sachinchandil.indie.util.network;

/**
 * <h1></h1>
 * <h1>public class NoDataReceivedException extends Exception </h1>
 * <p>Thrown when webservice response is empty.</p>
 */
public class NoDataReceivedException extends Exception
{

    private String message;

    /**
     * <h1></h1>
     * <h1>public NoDataReceivedException()</h1>
     * <p> Default constructor</p>
     */
    public NoDataReceivedException()
    {
        message = "Remote server did not return data";
    }

    /**
     * <h1></h1>
     * <h1>public NoDataReceivedException(String message)</h1>
     * <p>Constructor that sets a message.</p>
     *
     * @param message - message to be sent.
     */
    public NoDataReceivedException(String message)
    {

        this.message = message;
    }

    public String toString()
    {
        return "Exception : " + message;
    }
}
