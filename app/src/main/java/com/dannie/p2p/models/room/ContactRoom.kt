package com.dannie.p2p.models.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.Context
import com.dannie.p2p.R

@Entity(tableName = ContactRoom.TABLE_NAME)
data class ContactRoom (@ColumnInfo(name = ContactRoom.COLUMN_FIRST_NAME) var firstName: String?,
                        @ColumnInfo(name = ContactRoom.COLUMN_LAST_NAME) var lastName: String?,
                        @ColumnInfo(name = ContactRoom.COLUMN_AVATAR_URI) var imageUri: String?,
                        @ColumnInfo(name = ContactRoom.COLUMN_REFERENCE) var reference: String,
                        @ColumnInfo(name = ContactRoom.COLUMN_REFERENCE_ID) var referenceId: String,
                        @PrimaryKey(autoGenerate = true) var id: Long = 0){
    companion object {

        const val TABLE_NAME = "contact_room"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_AVATAR_URI = "image_uri"
        const val COLUMN_REFERENCE = "reference"
        const val COLUMN_REFERENCE_ID = "reference_id"
        const val COLUMN_AVATAR_AVAILABLE = "avatar_available"

        const val COLUMN_AVATAR_ID = "avatar_id"
        const val REFERENCE_NATIVE = "reference_native"

        const val REFERENCE_TELEGRAM = "reference_telegram"
    }
    @ColumnInfo(name = ContactRoom.COLUMN_AVATAR_ID) var avatarId: Int? = null
    @ColumnInfo(name = ContactRoom.COLUMN_AVATAR_AVAILABLE) var isAvatarAvailable: Boolean = imageUri != null || avatarId != null
    var isAlreadyAdded = false
    override fun toString(): String {
        return "id = $id, name = $firstName, lastName = $lastName, imageUri = $imageUri, reference = $reference, referenceId = $referenceId, isAvatarAvailable = $isAvatarAvailable, avatarId = $avatarId"
    }

    fun toStringLite(): String{
        return "id = $id, name = $firstName, lastName = $lastName, isAlreadyAdded = $isAlreadyAdded"
    }
}

fun ContactRoom.setNewAvatar(context: Context){
    //TODO: check if new avatar is not the same as previous one
    val avatars = context.resources.obtainTypedArray(R.array.avatars)
    val choice = (Math.random() * avatars.length()).toInt()
    avatarId = avatars.getResourceId(choice, R.drawable.avatar_zebra)
    avatars.recycle()
}

