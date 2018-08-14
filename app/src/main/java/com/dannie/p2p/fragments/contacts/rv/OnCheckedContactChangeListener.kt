package com.dannie.p2p.fragments.contacts.rv

import com.dannie.p2p.models.room.ContactRoom

interface OnCheckedContactChangeListener {
    fun onCheckedChanged(contact: ContactRoom)
}