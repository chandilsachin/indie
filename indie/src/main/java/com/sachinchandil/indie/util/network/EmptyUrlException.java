package com.sachinchandil.indie.util.network;

/**
 *
 * <h1>public class EmptyUrlException extends Exception</h1>
 * <p>Thrown when given Url is empty</p>
 */
public class EmptyUrlException extends Exception
{

    private String message;

    /**
     * <h1></h1>
     * <h1>public EmptyUrlException()</h1>
     * <p> Default constructor</p>
     */
    public EmptyUrlException()
    {

        message = "Given Url is empty";
    }

    /**
     * <h1></h1>
     * <h1>public EmptyUrlException(String message)</h1>
     * <p>Constructor that sets a message.</p>
     *
     * @param message - message to be sent.
     */
    public EmptyUrlException(String message)
    {

        this.message = message;
    }

    public String toString()
    {
        return "Exception : " + message;
    }
}
