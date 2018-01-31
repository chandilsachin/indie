package com.sachinchandil.indie.util.network;

/**
 *
 * <h1>public class NoInternetConnectionException extends Exception</h1>
 * <p>thrown when Internet connection is not connected.</p>
 */
public class NoInternetConnectionException extends Exception
{

    String message;

    /**
     * <h1></h1>
     * <h1>public NoInternetConnectionException()</h1>
     * <p> Default constructor</p>
     */
    public NoInternetConnectionException()
    {

        message = "Device is not connected to internet.";
    }

    /**
     * <h1></h1>
     * <h1>public NoInternetConnectionException(String message)</h1>
     * <p>Constructor that sets a message.</p>
     *
     * @param message - message to be sent.
     */
    public NoInternetConnectionException(String message)
    {

        this.message = message;
    }

    public String toString()
    {
        return "Exception : " + message;
    }

}
