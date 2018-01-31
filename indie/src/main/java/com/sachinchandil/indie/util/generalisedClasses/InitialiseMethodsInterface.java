package com.sachinchandil.indie.util.generalisedClasses;

import android.view.View;


public interface InitialiseMethodsInterface
{

    /**
     * Initializes views
     *
     * @param v
     */
    public void init(View v);

    /**
     * Initializes data that is going to be used to load view. for ex. behavioral file or home screen grid data json.
     * At the end of this method setDrawConfig() and setInitConfig() method must be called.
     */
    public void initializeData();

    /**
     * set drawing configuration (Behavioral file)
     */
    public void setDrawConfig();

    /**
     * set initial configuration
     */
    public void setInitConfig();


    /**
     * Sets all events to views
     */
    public void setEvents();
}
