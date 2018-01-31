package com.sachinchandil.indie.util.generalisedClasses;

import android.view.View;

/**
 * Initializes view and data in the fragment or activity.
 */
public interface InitMethodsInterface
{

    /**
     * Initializes views
     *
     * @param v
     */
    public void init(View v);

    /**
     * Initializes data that is going to be used to load view. for ex. behavioral file or home screen grid data json.
     * <b>Note:</b> do not use getActivity() or getContext() method in this method. use activity parameter instead.
     */
    public void initializeData();

    /**
     *
     * sets drawing configuration, look and feel of views(Behavioral file etc). for example topHeaderBar, and searchBarLayout of searchView etc.
     * <b>Note:</b> do not use getActivity() or getContext() method in this method. use activity parameter instead.
     */
    public void setDrawConfig();

    /**
     * set initial configuration. For example filling initial data in a listView or a guideView etc.
     * <b>Note:</b> do not use getActivity() or getContext() method in this method. use activity parameter instead.
     */
    public void setInitConfig();


    /**
     * Sets all events to views
     */
    public void setEvents();
}
