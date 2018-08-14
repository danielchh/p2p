package com.dannie.p2p.room.typeconverter

import android.arch.persistence.room.TypeConverter
import java.util.*

object DateTypeConverter {

    @TypeConverter
    @JvmStatic
    fun longToDate(value: Long) = Date(value)

    @TypeConverter
    @JvmStatic
    fun dateToLong(date: Date) = date.time

}