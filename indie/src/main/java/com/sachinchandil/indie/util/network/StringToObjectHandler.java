package com.sachinchandil.indie.util.network;

public interface StringToObjectHandler
{

    /**
     * <h1></h1>
     * <h1>public Object convertIntoObject(String encodedContent)</h1>
     * <p>mothod is implemented to convert encodedContent coming from remote location to desired form.</p>
     *
     * @param encodedContent - this contains data received from remote location.
     * @return an object.
     */
    public Object convertIntoObject(String encodedContent);
}
