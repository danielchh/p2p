package com.dannie.p2p.fragments.contacts

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.Toast
import com.dannie.p2p.R
import com.dannie.p2p.fragments.BaseFragment
import com.dannie.p2p.fragments.contacts.rv.ContactsImportRVAdapter
import com.dannie.p2p.models.room.ContactRoom
import com.dannie.p2p.other.extensions.log
import kotlinx.android.synthetic.main.frag_contacts.*


class ContactsFragment : BaseFragment() {

    companion object {
        const val PERMISSION_REQUEST_CONTACT = 100
    }

    private val viewModel: ContactsViewModel by lazy {
        ViewModelProviders.of(this).get(ContactsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_contacts, container, false)
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
                log("Got contacts")
                rvContacts.adapter = ContactsImportRVAdapter(it)
            }
        })

        //Observing state of data
        viewModel.getContactsState().observe(this, Observer {
            when (it){
                ContactsState.FETCHED -> {}
                ContactsState.FETCHING -> {}
                ContactsState.PERMISSION_DENIED -> { showNoPermissionNoContactMessage() }
                ContactsState.PERMISSION_GRANTED -> {}
                ContactsState.NO_PERMISSION -> { askForContactPermission() }
            }
        })
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
        showEnterAnim()

        with(rvContacts){
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    //TODO("not implemented") this shit lags, brah
    private fun showEnterAnim() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        animation.interpolator = DecelerateInterpolator()
        rvContacts.startAnimation(animation)
    }

}