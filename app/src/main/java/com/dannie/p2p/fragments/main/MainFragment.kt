package com.dannie.p2p.fragments.main

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dannie.p2p.R
import com.dannie.p2p.fragments.BaseFragment
import com.dannie.p2p.other.extensions.getFloatDimen
import com.dannie.p2p.other.extensions.log
import com.dannie.p2p.room.P2PDataBase
import kotlinx.android.synthetic.main.frag_main.*
import kotlinx.android.synthetic.main.item_card_small_main.view.*
import org.jetbrains.anko.doAsync

class MainFragment: BaseFragment(), View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    companion object {
        const val ANIM_HEADER_START = 0.7F
        const val ANIM_HEADER_END = 0.3F

        const val ANIM_DESCRIPTION_START = 0.4F
        const val ANIM_DESCRIPTION_END = 0.1F

        const val cardAmount = 2
    }

    override val resource = R.layout.frag_main

    private var mAppBarLayout: AppBarLayout? = null
    private var mToolbar: Toolbar? = null

    private val debtInitWidth by lazy { txtDebt.width }
    private val cardInfoInitWidth by lazy { card1.clCardInfo.width }
    private val descriptionHeight by lazy{ resources.getDimension(R.dimen.description_height) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)
        mAppBarLayout?.addOnOffsetChangedListener(this)
    }

    override fun initUI(view: View) {
        mToolbar = view.findViewById(R.id.main_toolbar)
        mAppBarLayout = view.findViewById(R.id.main_appbar)

        when (cardAmount){
            0 -> {
                llContainerCards.visibility = View.GONE
                txtNoCards.visibility = View.VISIBLE
            }
            1 -> {
                val params = contentContainer.layoutParams as CoordinatorLayout.LayoutParams
                val behaviour = params.behavior as AppBarLayout.ScrollingViewBehavior
                behaviour.overlayTop = resources.getDimension(R.dimen.description_height).toInt()
            }
            2 -> {
                card2.visibility = View.VISIBLE
                val newHeight = resources.getDimension(R.dimen.item_card_small_height_double).toInt()
                spaceToolbar.layoutParams.height = newHeight
                spaceToolbar.requestLayout()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){

        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val maxScroll = appBarLayout?.totalScrollRange
        val percentage = Math.abs(verticalOffset) / (maxScroll!!.times(1.0F))

        handleHeaders(percentage)
    }

    private fun handleHeaders(percentage: Float) {

        val avatarScale = context?.getFloatDimen(R.dimen.avatar_scale) ?: 1.4F
        val avatarTranslate = context?.resources?.getDimension(R.dimen.avatar_translation) ?: 20F

        val scaleHeader = countCoef(ANIM_HEADER_START, ANIM_HEADER_END, 1F, avatarScale, percentage)
        val translationAvatar = countCoef(ANIM_HEADER_START, ANIM_HEADER_END, 0F, avatarTranslate, percentage)

        ivAvatar.scaleX = scaleHeader ?: ivAvatar.scaleX
        ivAvatar.scaleY = scaleHeader ?: ivAvatar.scaleY
        ivAvatar.translationX = translationAvatar ?: ivAvatar.translationX
        ivAvatar.translationY = translationAvatar ?: ivAvatar.translationY

        txtYou.scaleX = scaleHeader ?: txtYou.scaleX
        txtYou.scaleY = scaleHeader ?: txtYou.scaleY
        val newTranslate = ivAvatar.height / 2 + txtYou.height / 4
        val translationYou = countCoef(ANIM_HEADER_START, ANIM_HEADER_END, 0F, newTranslate.toFloat(), percentage)
        txtYou.translationX = translationYou ?: txtYou.translationX
        txtYou.translationY = translationYou ?: txtYou.translationY

        //Scale txtDebt the same as txtYou and translate a bit to the left accordingly to the new scaled width
        txtDebt.scaleX = scaleHeader ?: txtDebt.scaleX
        txtDebt.scaleY = scaleHeader ?: txtDebt.scaleY
        val youHeight = txtYou.height.times(txtYou.scaleY)
        val debtHeight = txtDebt.height.times(txtDebt.scaleY)
        txtDebt.y = txtYou.y + (youHeight - debtHeight) / 2
        val newWidth = ((txtDebt.width.times(txtDebt.scaleX)) - debtInitWidth) / 2
        txtDebt.translationX = - newWidth

        val translationCardInfo = countCoef(ANIM_HEADER_START, ANIM_HEADER_END, 0F, descriptionHeight, percentage)
        val scaleCardInfo = countCoef(ANIM_HEADER_START, ANIM_HEADER_END, 1F, 1.4F, percentage)
        val alphaScaleDescription = countCoef(ANIM_DESCRIPTION_START, ANIM_DESCRIPTION_END, 0F, 1F, percentage)
        card1.clCardInfo.translationY = translationCardInfo ?: card1.clCardInfo.translationY
        card1.clCardInfo.scaleX = scaleCardInfo ?: card1.clCardInfo.scaleX
        card1.clCardInfo.scaleY = scaleCardInfo ?: card1.clCardInfo.scaleY
        val newCardNumberWidth = (card1.clCardInfo.width.times(card1.clCardInfo.scaleX) - cardInfoInitWidth) / 2
        card1.txtDescription.alpha = alphaScaleDescription ?: card1.txtDescription.alpha
        card1.txtDescription.scaleX = alphaScaleDescription ?: card1.txtDescription.scaleX
        card1.txtDescription.scaleY = alphaScaleDescription ?: card1.txtDescription.scaleY
        card1.txtDescription.translationX = -newCardNumberWidth

        when (cardAmount){
            1 -> {
//                val translationAdditional = countCoef(ANIM_HEADER_START, ANIM_CONTENT_END, 0F, descriptionHeight / 2, percentage)
//                card1.translationY = translationAdditional ?: card1.translationY
            }
            2 -> {
                val translationAdditional = countCoef(ANIM_HEADER_START, ANIM_HEADER_END, 0F, descriptionHeight, percentage)
                card2.translationY = translationAdditional ?: card2.translationY

                card2.clCardInfo.translationY = translationCardInfo ?: card2.clCardInfo.translationY
                card2.clCardInfo.scaleX = scaleCardInfo ?: card2.clCardInfo.scaleX
                card2.clCardInfo.scaleY = scaleCardInfo ?: card2.clCardInfo.scaleY
                card2.txtDescription.alpha = alphaScaleDescription ?: card2.txtDescription.alpha
                card2.txtDescription.scaleX = alphaScaleDescription ?: card2.txtDescription.scaleX
                card2.txtDescription.scaleY = alphaScaleDescription ?: card2.txtDescription.scaleY
                card2.txtDescription.translationX = -newCardNumberWidth
            }
        }
    }

    private fun countCoef(startPercent: Float, endPercent: Float, zero: Float, one: Float, percentage: Float) : Float?{
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
}
