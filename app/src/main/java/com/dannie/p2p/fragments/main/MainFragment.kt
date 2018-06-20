package com.dannie.p2p.fragments.main

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dannie.p2p.R
import com.dannie.p2p.fragments.BaseFragment
import android.widget.TextView
import android.widget.LinearLayout
import android.view.animation.AlphaAnimation
import com.dannie.p2p.other.extensions.getFloatDimen
import com.dannie.p2p.other.shadowview.ViewUtils
import kotlinx.android.synthetic.main.frag_main.*

class MainFragment: BaseFragment(), View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    private val ANIM_START = 0.7F
    private val ANIM_END = 0.3F

    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200L

    private var mIsTheTitleContainerVisible = true

    private var mTitleContainer: LinearLayout? = null
    private var mAppBarLayout: AppBarLayout? = null
    private var mToolbar: Toolbar? = null

    private val debtInitWidth by lazy { txtDebt.width }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)
        mAppBarLayout?.addOnOffsetChangedListener(this)

    }

    override fun initUI(view: View) {
        mToolbar = view.findViewById(R.id.main_toolbar)
        mTitleContainer = view.findViewById(R.id.main_linearlayout_title)
        mAppBarLayout = view.findViewById(R.id.main_appbar)


    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout?.totalScrollRange
        val percentage = Math.abs(verticalOffset) / (maxScroll!!.times(1.0F))

        handleHeaders(percentage)
        handleAlphaOnTitle(percentage)
    }

    private fun handleHeaders(percentage: Float) {
        val avatarScale = context?.getFloatDimen(R.dimen.avatar_scale) ?: 1.4F
        val avatarTranslate = context?.resources?.getDimension(R.dimen.avatar_translation) ?: 20F

        val scaleFactor = countCoef(ANIM_START, ANIM_END, 1F, avatarScale, percentage)
        val translateAvatarFactor = countCoef(ANIM_START, ANIM_END, 0F, avatarTranslate, percentage)

        ivAvatar.scaleX = scaleFactor ?: ivAvatar.scaleX
        ivAvatar.scaleY = scaleFactor ?: ivAvatar.scaleY
        ivAvatar.translationX = translateAvatarFactor ?: ivAvatar.translationX
        ivAvatar.translationY = translateAvatarFactor ?: ivAvatar.translationY

        txtYou.scaleX = scaleFactor ?: txtYou.scaleX
        txtYou.scaleY = scaleFactor ?: txtYou.scaleY
        val newTranslate = ivAvatar.height / 2 + txtYou.height / 4
        val translateYouFactor = countCoef(ANIM_START, ANIM_END, 0F, newTranslate.toFloat(), percentage)
        txtYou.translationX = translateYouFactor ?: txtYou.translationX
        txtYou.translationY = translateYouFactor ?: txtYou.translationY

        //Scale txtDebt the same as txtYou and translate a bit to the left accordingly to the new scaled width
        txtDebt.scaleX = scaleFactor ?: txtDebt.scaleX
        txtDebt.scaleY = scaleFactor ?: txtDebt.scaleY
        val youHeight = txtYou.height.times(txtYou.scaleY)
        val debtHeight = txtDebt.height.times(txtDebt.scaleY)
        txtDebt.y = txtYou.y + (youHeight - debtHeight) / 2
        val newWidth = ((txtDebt.width.times(txtDebt.scaleX)) - debtInitWidth) / 2
        txtDebt.translationX = - newWidth

        //TODO: change alpha animation to translation
        val alpha = countCoef(0.8F, 0.5F, 1F, 0F, percentage)
        card1.alpha = alpha ?: card1.alpha
        card2.alpha = alpha ?: card2.alpha
    }

    private fun countCoef(startPercent: Float, endPercent: Float, zero: Float, one: Float, percentage: Float) : Float?{
        Log.d("MyLog", "% = $percentage")
        return when {
            percentage in endPercent..startPercent -> {
                val end = startPercent - endPercent
                val range = one - zero
                val dxPerPercent = range / end
                if (zero < one){
                    zero + Math.abs((percentage - startPercent) * dxPerPercent)
                } else {
                    zero - Math.abs((percentage - startPercent) * dxPerPercent)
                }
            }
            percentage > startPercent -> return zero
            percentage < endPercent -> return  one
            else -> return null
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                mIsTheTitleContainerVisible = false
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                mIsTheTitleContainerVisible = true
            }
        }
    }

    private fun startAlphaAnimation(v: View?, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE)
            AlphaAnimation(0f, 1f)
        else
            AlphaAnimation(1f, 0f)

        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v?.startAnimation(alphaAnimation)
    }
}
