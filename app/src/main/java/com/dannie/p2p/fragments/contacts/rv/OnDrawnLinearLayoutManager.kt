package com.dannie.p2p.fragments.contacts.rv

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Used to determine when initial items are layout and drawn
 * @param callback used to tell when items are layout and drawn
 */
class OnDrawnLinearLayoutManager(val context: Context, private val callback: RecyclerViewOnLayoutCallback): LinearLayoutManager(context) {

    /**
     * Used to only call RecyclerViewOnLayoutCallback#.onInitLayout once
     */
    private var isCalled = false

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        super.onLayoutChildren(recycler, state)
        val firstPosition = findFirstVisibleItemPosition()
        val lastPosition = findLastVisibleItemPosition()
        if (firstPosition > -1 && firstPosition < lastPosition && !isCalled){
            isCalled = true
            callback.onInitLayout()
        }
    }
}