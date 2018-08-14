package com.dannie.p2p.fragments

import android.os.Bundle
import android.support.annotation.AnimRes
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dannie.p2p.MainActivity

abstract class BaseFragment : Fragment() {

    /**
     * This value is used for fun onCreateView() as a view to be inflated.
     * Needs to be assigned in every fragment
     */
    protected abstract val resource: Int

    /**
     * Abstract function for initializing all UI elements.
     * Needs to be called inside onViewCreated method.
     * @param view is a root element, which children we are going to initialize
     */
    abstract fun initUI(view: View)

    /**
     * Replaces main container of MainActivity with new [fragment]
     * @param fragment fragment that will replace the old one
     * @param addToBackStack boolean which indicated whether to add [fragment] to the backstack
     * @return boolean which indicates whether transaction was successful or not
     */
    fun BaseFragment.replaceFragment(fragment: BaseFragment,
                                     addToBackStack: Boolean = false,
                                     @AnimRes enterAnimId: Int = FragmentTransaction.TRANSIT_NONE,
                                     @AnimRes exitAnimId: Int = FragmentTransaction.TRANSIT_NONE) : Boolean{
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.setCustomAnimations(enterAnimId, exitAnimId)
        transaction?.replace(MainActivity.mainContainerId, fragment)
        if (addToBackStack) {
            transaction?.addToBackStack(fragment::class.java.simpleName)
        }
        transaction?.commit()
        return transaction != null
    }

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container?.removeAllViews()
        return inflater.inflate(resource, container, false)
    }
}