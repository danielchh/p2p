package com.dannie.p2p.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.dannie.p2p.models.room.ContactRoom

@Database(entities = [(ContactRoom::class)], version = 1)
abstract class P2PDataBase: RoomDatabase() {

    abstract fun p2pDataDao(): P2PDataDao

    companion object {
        const val DB_NAME = "p2p_database"

        private var INSTANCE: P2PDataBase? = null

        fun getInstance(context: Context): P2PDataBase? {
            if (INSTANCE == null){
                synchronized(P2PDataBase::class){
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            P2PDataBase::class.java,
                            DB_NAME)
                            .build()
                }
            }
            return INSTANCE
        }
    }
}