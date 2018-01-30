package com.sachinchandil.indie.util.lifecycle.arch

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.ace.diettracker.util.annotation.RequiresTagName
import com.ace.diettracker.util.initViewModel

/**
 * Created by sachin on 28/5/17.
 */
abstract class BaseDialogFragment<E : ViewModel> : DialogFragment() {

    val mViewModel: E  by lazy { initViewModel(getViewModelClass(), attachViewModelToActivity) }

    private var attachViewModelToActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(getLayoutId(), container, false)

        init(view)
        setDrawConfig()
        setEvents()
        return view
    }

    abstract fun getViewModelClass(): Class<E>

    abstract fun getLayoutId(): Int

    /**
     * Initialise views and load initial config
     */
    abstract fun init(view: View)

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
                activity?.supportFragmentManager?.apply {
                    if (backStackEntryCount > 0) popBackStack()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun attachViewModelToActivity(value: Boolean) {
        this.attachViewModelToActivity = value
    }

    fun isViewModelAttachedToActivity(): Boolean = attachViewModelToActivity

    companion object {
        val TAG_NAME = (BaseDialogFragment::class.annotations.find { it == RequiresTagName::class } as? RequiresTagName)?.tagName

        fun findInstance(activity: FragmentActivity?): Fragment? {
            if (activity == null) return null
            return activity.supportFragmentManager.findFragmentByTag(TAG_NAME)
        }
    }

}