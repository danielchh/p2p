package com.dannie.p2p.fragments.firstopen

import android.app.FragmentTransaction
import android.os.Bundle
import android.view.View
import com.dannie.p2p.R
import com.dannie.p2p.fragments.BaseFragment
import com.dannie.p2p.fragments.contacts.ContactsFragment
import com.dannie.p2p.fragments.main.MainFragment
import kotlinx.android.synthetic.main.frag_first_open.*

class FirstOpenFragment: BaseFragment(), View.OnClickListener {

    override val resource = R.layout.frag_first_open

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI(view)
    }

    override fun initUI(view: View) {
        btnImportContacts.setOnClickListener(this)
        btnSkip.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnImportContacts -> importContactsClicked()
            R.id.btnSkip -> skipClicked()
        }
    }

    private fun skipClicked() {
        replaceFragment(MainFragment(), true)
    }

    private fun importContactsClicked() {
        replaceFragment(ContactsFragment(), true, FragmentTransaction.TRANSIT_NONE, R.anim.anim_fade_out)
    }
}