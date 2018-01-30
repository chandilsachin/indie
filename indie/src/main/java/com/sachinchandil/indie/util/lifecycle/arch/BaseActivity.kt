package com.ace.diettracker.util.lifecycle.arch

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.ace.diettracker.util.initViewModel

/**
 * Created by sachin on 27/6/17.
 */
abstract open class BaseActivity<E : ViewModel> : AppCompatActivity() {

    val mViewModel: E  by lazy { initViewModel(getViewModelClass()) }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(getLayoutId())

        init()
        setDrawConfig()
        setEvents()
    }

    abstract fun getViewModelClass(): Class<E>

    abstract fun getLayoutId(): Int

    /**
     * Initialise views and load initial config
     */
    abstract fun init()

    /**
     * Sets all draw configs to view
     */
    abstract fun setDrawConfig()

    /**
     * Sets events to views
     */
    abstract fun setEvents()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                supportFragmentManager?.apply {
                    if (backStackEntryCount > 0) popBackStack()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}