package com.dannie.p2p.fragments.contacts

import android.Manifest
import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import com.dannie.p2p.R
import com.dannie.p2p.fragments.BaseFragment
import com.dannie.p2p.fragments.contacts.rv.ContactsImportRVAdapter
import com.dannie.p2p.fragments.contacts.rv.OnCheckedContactChangeListener
import com.dannie.p2p.fragments.contacts.rv.OnDrawnLinearLayoutManager
import com.dannie.p2p.fragments.contacts.rv.RecyclerViewOnLayoutCallback
import com.dannie.p2p.fragments.main.MainFragment
import com.dannie.p2p.models.room.ContactRoom
import com.dannie.p2p.other.extensions.log
import kotlinx.android.synthetic.main.frag_contacts.*


class ContactsFragment : BaseFragment(), OnCheckedContactChangeListener, RecyclerViewOnLayoutCallback {

    companion object {
        const val PERMISSION_REQUEST_CONTACT = 100
    }

    override val resource = R.layout.frag_contacts

    private var fabIsVisible = false

    private val viewModel: ContactsViewModel by lazy {
        ViewModelProviders.of(this).get(ContactsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)
        observeContactsViewModel()
    }

    private fun observeContactsViewModel() {
        //Observing list of contacts
        viewModel.getNativeContacts().observe(this, Observer {
            if (it == null){
                showNoContactsMessage()
            } else {
                rvContacts.adapter = ContactsImportRVAdapter(it, this).apply { setHasStableIds(true) }
            }
        })

        //Observing state of data
        viewModel.getContactsState().observe(this, Observer {
            when (it){
                ContactsState.PERMISSION_DENIED -> { showNoPermissionNoContactMessage() }
                ContactsState.NO_PERMISSION -> { askForContactPermission() }
                ContactsState.HIDE_FAB -> animateFab(false)
                ContactsState.SHOW_FAB -> animateFab(true)
                ContactsState.IMPORT_FINISHED -> gotoMainScreen()
            }
        })
    }

    private fun gotoMainScreen() {
        replaceFragment(MainFragment())
    }

    private fun animateFab(show: Boolean){
        if (fabIsVisible != show){
            val animation = if (show){
                AnimationUtils.loadAnimation(context, R.anim.anim_fade_scale_in)
            } else {
                AnimationUtils.loadAnimation(context, R.anim.anim_fade_scale_out)
            }
            fabAdd.startAnimation(animation)
            fabAdd.isClickable = show
            fabIsVisible = show
        }
    }

    override fun onCheckedChanged(contact: ContactRoom) {
        viewModel.onContactChosen(contact, fabIsVisible)
    }

    private fun showNoContactsMessage() {
        //TODO: show it, man
        Toast.makeText(context, "Yo, you don't have any contacts, brah", Toast.LENGTH_SHORT).show()

    }

    private fun showNoPermissionNoContactMessage() {
        //TODO: show it, man
        Toast.makeText(context, "Yo, you need that permission, man", Toast.LENGTH_SHORT).show()
    }


    private fun askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                    PERMISSION_REQUEST_CONTACT)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode){
            PERMISSION_REQUEST_CONTACT -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission granted
                    viewModel.onPermissionAsked(ContactsState.PERMISSION_GRANTED)
                } else {
                    //Permission denied
                    viewModel.onPermissionAsked(ContactsState.PERMISSION_DENIED)
                }
            }
        }
    }

    override fun initUI(view: View) {
        with(rvContacts){
            layoutManager = OnDrawnLinearLayoutManager(context, this@ContactsFragment)
            setHasFixedSize(true)
        }

        fabAdd.setOnClickListener { viewModel.onAddClicked() }
    }

    override fun onInitLayout() {
        showEnterAnim()
    }

    //rvContacts.height will return 0, if called immediately, so need to do it with View.post method
    private fun showEnterAnim() {
        log("show")
        rvContacts.translationY = rvContacts.height.toFloat()
        rvContacts.alpha = 1F
        rvContacts.animate()
                .translationY(0F)
                .setInterpolator(DecelerateInterpolator())
                .setDuration(resources.getInteger(R.integer.anim_duration_long).toLong())
                .start()
    }
}