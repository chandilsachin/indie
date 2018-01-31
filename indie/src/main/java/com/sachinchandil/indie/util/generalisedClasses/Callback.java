package com.sachinchandil.indie.util.generalisedClasses;

/**
 *
 * <h1>public interface Callback</h1>
 * <p>Handles callbacks.
 * If using for dialog return true to close dialog otherwise false.</p>
 */
@Deprecated
public class Callback<R, P>
{

    /**
     * <p>Callback method.</p>
     * Remove super.callback() statement.
     * @param param - parameters to pass.
     * @return Object type.
     */
    public R callback(P param)
    {
        throw new RuntimeException("No method implementation.");
    }

    /**
     * <p>Callback method.</p>
     ** Remove super.callback() statement.
     * @param param1 - parameters to pass.
     * @param param2 - parameters to pass.
     * @return Object type.
     */
    public R callback(P param1, P param2)
    {
        throw new RuntimeException("No method implementation.");
    }


}
