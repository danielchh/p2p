package com.dannie.p2p.fragments

import android.support.v4.app.Fragment
import android.view.View
import com.dannie.p2p.MainActivity

abstract class BaseFragment : Fragment() {

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
    fun BaseFragment.replaceFragment(fragment: BaseFragment, addToBackStack: Boolean) : Boolean{
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(MainActivity.mainContainerId, fragment)
        if (addToBackStack) {
            transaction?.addToBackStack(fragment::class.java.simpleName)
        }
        transaction?.commit()
        return transaction != null
    }
}