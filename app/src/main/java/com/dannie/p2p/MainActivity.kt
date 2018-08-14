package com.dannie.p2p

import android.app.FragmentTransaction
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import com.dannie.p2p.fragments.firstopen.FirstOpenFragment
import com.dannie.p2p.other.extensions.*


class MainActivity : AppCompatActivity() {

    companion object {
        val mainContainerId: Int = View.generateViewId()
        const val FIRST_OPEN = "sp_first_open"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        configureScreen()

        val sharedPrefs = defaultPrefs(this)
        val firstOpen = sharedPrefs.getValue(FIRST_OPEN, true)

        if (firstOpen) {
//            sharedPrefs.setValue(Const.SharedPrefs.FIRST_OPEN, false)
            supportFragmentManager.beginTransaction()
                    .replace(mainContainerId, FirstOpenFragment())
                    .commit()
        } else {
            Toast.makeText(this, "second open", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Making status bar and navbar transparent.
     * then added white view to be a navbar background
     * added padding to match status bar height
     * added them to the root with appropriate constraints
     */
    private fun configureScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val root = findViewById<ConstraintLayout>(R.id.root)
        val statusBarHeight = getStatusBarHeight()
        root.setPadding(0, statusBarHeight, 0, 0)
        val navBarHeightPx = getNavBarHeight()
        val matchConstraint = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT

        val navBarView = View(this)
        navBarView.setBackgroundColor(Color.WHITE)
        navBarView.id = View.generateViewId()
        root.addView(navBarView, ConstraintLayout.LayoutParams(matchConstraint, navBarHeightPx))

        val mainContainer = FrameLayout(this)
        mainContainer.id = mainContainerId
        root.addView(mainContainer, ConstraintLayout.LayoutParams(matchConstraint, matchConstraint))

        val constraintSet = ConstraintSet()
        with(constraintSet) {
            clone(root)

            connect(navBarView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            connect(navBarView.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
            connect(navBarView.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)

            connect(mainContainer.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
            connect(mainContainer.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
            connect(mainContainer.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(mainContainer.id, ConstraintSet.BOTTOM, navBarView.id, ConstraintSet.TOP)

            applyTo(root)
        }
    }
}
