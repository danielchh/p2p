package com.dannie.p2p.models.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = TransactionRoom.TABLE_NAME)
class TransactionRoom(@ColumnInfo(name = TransactionRoom.COLUMN_TRANSACTION_DESCRIPTION) val transactionName: String,
                      @ColumnInfo(name = TransactionRoom.COLUMN_TRANSACTION_AMOUNT) val transactionAmount: Int,
                      @ColumnInfo(name = TransactionRoom.COLUMN_TRANSACTION_DATE) val transactionDate: Date,
                      @PrimaryKey(autoGenerate = true) var id: Long = 0) {

    companion object {
        const val TABLE_NAME = "transaction_room"

        const val COLUMN_TRANSACTION_DESCRIPTION = "transaction_description"
        const val COLUMN_TRANSACTION_AMOUNT = "transaction_amount"
        const val COLUMN_TRANSACTION_DATE = "transaction_date"
    }
}