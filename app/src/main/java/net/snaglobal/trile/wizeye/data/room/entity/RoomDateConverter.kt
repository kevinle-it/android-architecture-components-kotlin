package net.snaglobal.trile.wizeye.data.room.entity

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * @author lmtri
 * @since Oct 22, 2018 at 1:17 PM
 */
object RoomDateConverter {

    @JvmStatic
    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @JvmStatic
    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? = millisSinceEpoch?.let { Date(it) }
}