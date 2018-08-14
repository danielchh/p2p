package com.dannie.p2p.models.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = CardRoom.TABLE_NAME)
data class CardRoom (@ColumnInfo(name = COLUMN_CARD_NUMBER) var cardNumber: String,
                @ColumnInfo(name = COLUMN_REFERENCE_ID) var referenceId: Int,
                @PrimaryKey(autoGenerate = true) var id: Long = 0){
    companion object {
        const val TABLE_NAME = "card_room"

        const val COLUMN_CARD_NUMBER = "card_number"
        const val COLUMN_CARD_TYPE = "card_type"
        const val COLUMN_LAST_USED = "last_used"
        const val COLUMN_TIMES_USED = "times_used"
        const val COLUMN_REFERENCE_ID = "reference_id"

        const val TYPE_VISA = "visa"
        const val TYPE_MASTERCARD = "mastercard"
        const val TYPE_BANK_OF_AMERICA = "bank_of_america"
    }

    @ColumnInfo(name = COLUMN_CARD_TYPE) var cardType: String? = null
    @ColumnInfo(name = COLUMN_LAST_USED) var cardLastUsed: Date = Date()
    @ColumnInfo(name = COLUMN_TIMES_USED) var cardTimesUsed: Int = 0
}
