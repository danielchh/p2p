package com.dannie.p2p.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.dannie.p2p.models.room.ContactRoom

@Dao
interface P2PDataDao {

    @Query("select * from ${ContactRoom.TABLE_NAME}")
    fun getAllContacts(): List<ContactRoom>

    @Insert(onConflict = REPLACE)
    fun insertContact(contact: ContactRoom)

    @Insert(onConflict = REPLACE)
    fun insertAllContacts(contacts: List<ContactRoom>)

    @Query("select * from ${ContactRoom.TABLE_NAME} where ${ContactRoom.COLUMN_REFERENCE} = :reference AND ${ContactRoom.COLUMN_REFERENCE_ID} = :referenceId")
    fun loadSingleContact(reference: String, referenceId:String): ContactRoom

}