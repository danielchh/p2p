package com.dannie.p2p.fragments.contacts

import android.Manifest
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import android.util.Log
import com.dannie.p2p.models.room.ContactRoom
import com.dannie.p2p.models.room.setNewAvatar
import com.dannie.p2p.other.extensions.log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private val contactsNative = MutableLiveData<ArrayList<ContactRoom>>()
    private val contactsState = MutableLiveData<ContactsState>()

    init {
        val isEmpty = contactsNative.value?.isEmpty() ?: true
        val permissionGranted = checkPermission()
        if (permissionGranted) {
            if (isEmpty) {
                fetchNativeContacts()
            }
        } else {
            contactsState.value = ContactsState.NO_PERMISSION
        }
    }

    private fun fetchNativeContacts() {
        log("Start Fetching")
        contactsState.value = ContactsState.FETCHING
        doAsync {
            val list = ArrayList<ContactRoom>()
            val setOfIds = HashSet<String>()
            val whereName = ContactsContract.Data.MIMETYPE + " = ?"
            val whereNameParams = arrayOf(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            val nameCur = getApplication<Application>().contentResolver.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)
            while (nameCur.moveToNext()) {
                val id = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID))
                // ContactsContract returns a lot of duplicates, so I add all ids into a set to check if I already added it
                if (!setOfIds.contains(id)) {
                    setOfIds.add(id)

                    val firstName = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME))
                    val lastName = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME))
                    val photo = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHOTO_URI))

                    val fetchedContact = ContactRoom(firstName?.trim(), lastName?.trim(), photo, ContactRoom.REFERENCE_NATIVE, id)
                    if (!fetchedContact.isAvatarAvailable) fetchedContact.setNewAvatar(getApplication())
                    list.add(fetchedContact)
                }
            }
            nameCur.close()

            uiThread {
                log("End fetching")
                contactsState.value = ContactsState.FETCHED
                contactsNative.value = list
            }
        }
    }

    fun onPermissionAsked(state: ContactsState) {
        if (state != ContactsState.PERMISSION_DENIED && state != ContactsState.PERMISSION_GRANTED) throw IllegalStateException("Such enum in not allowed here")
        contactsState.value = state
        if (state == ContactsState.PERMISSION_GRANTED)
            fetchNativeContacts()
    }

    fun getNativeContacts(): LiveData<ArrayList<ContactRoom>> = contactsNative
    fun getContactsState(): LiveData<ContactsState> = contactsState

    private fun checkPermission(): Boolean = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

}