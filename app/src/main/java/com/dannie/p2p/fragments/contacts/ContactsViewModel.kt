package com.dannie.p2p.fragments.contacts

import android.Manifest
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.support.v4.content.ContextCompat
import com.dannie.p2p.models.room.ContactRoom
import com.dannie.p2p.models.room.setNewAvatar
import com.dannie.p2p.other.extensions.log
import com.dannie.p2p.room.P2PDataBase
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = P2PDataBase.getInstance(getApplication())?.p2pDataDao()

    private val contactsNative = MutableLiveData<ArrayList<ContactRoom>>()
    private val contactsState = MutableLiveData<ContactsState>()

    private var allContacts = ArrayList<ContactRoom>()
    private var chosenContacts = ArrayList<ContactRoom>()

    init {
        val isEmpty = contactsNative.value?.isEmpty() ?: true
        val permissionGranted = checkPermission()
        if (permissionGranted) {
            contactsState.value = ContactsState.PERMISSION_GRANTED
            if (isEmpty) {
                fetchNativeContacts()
            }
        } else {
            contactsState.value = ContactsState.NO_PERMISSION
        }
    }

    private fun fetchNativeContacts() {
        doAsync {
            val setOfIds = HashSet<String>()
            val whereName = ContactsContract.Data.MIMETYPE + " = ?"
            val whereNameParams = arrayOf(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            //TODO: cannot figure out a way to sort properly ( I is shown after english and before russian )
            val nameCur = getApplication<Application>().contentResolver.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)
            while (nameCur.moveToNext()) {
                val id = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID))
                // ContactsContract returns a lot of duplicates, so I add all ids into a set to check if I already added it
                if (!setOfIds.contains(id)) {
                    setOfIds.add(id)

                    val firstName = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME))
                    val lastName = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME))
                    val photo = nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHOTO_URI))
                    // if contact was already added, I show the one from the db
                    val fetchedContact = ContactRoom(firstName?.trim(), lastName?.trim(), photo, ContactRoom.REFERENCE_NATIVE, id)
                    val contactFromDb = db?.loadSingleContact(fetchedContact.reference, fetchedContact.referenceId)
                    if (contactFromDb != null){
                        contactFromDb.isAlreadyAdded = true
                        allContacts.add(contactFromDb)
                    } else {
                        if (!fetchedContact.isAvatarAvailable) fetchedContact.setNewAvatar(getApplication())
                        allContacts.add(fetchedContact)
                    }
                }
            }
            nameCur.close()

            uiThread {
                contactsNative.value = allContacts
            }
        }
    }

    fun onPermissionAsked(state: ContactsState) {
        if (state != ContactsState.PERMISSION_DENIED && state != ContactsState.PERMISSION_GRANTED) throw IllegalStateException("Such enum in not allowed here")
        contactsState.value = state
        if (state == ContactsState.PERMISSION_GRANTED)
            fetchNativeContacts()
    }

    /**
     *  Adds chosen contact to the chosen contacts list
     *  If state of the list changes (empty or not empty), we change the visibility state of FAB
     *  @param contact which was chosen
     *  @param fabIsVisible tells the state of FAB
     */
    fun onContactChosen(contact: ContactRoom, fabIsVisible: Boolean){
        if (chosenContacts.contains(contact)){
            chosenContacts.remove(contact)
        } else {
            chosenContacts.add(contact)
        }
        if (fabIsVisible != chosenContacts.isNotEmpty()){
            when(chosenContacts.isNotEmpty()){
                true -> contactsState.value = ContactsState.SHOW_FAB
                false -> contactsState.value = ContactsState.HIDE_FAB
            }
        }
    }

    fun onAddClicked(){
        doAsync {
            db?.insertAllContacts(chosenContacts)
        }
        contactsState.value = ContactsState.IMPORT_FINISHED
    }

    fun getNativeContacts(): LiveData<ArrayList<ContactRoom>> = contactsNative
    fun getContactsState(): LiveData<ContactsState> = contactsState

    private fun checkPermission(): Boolean = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

}