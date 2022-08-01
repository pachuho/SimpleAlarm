package com.pachuho.sleepAlarm.data.datasource.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo var use: Boolean,
    @ColumnInfo val hour: Int,
    @ColumnInfo val minute: Int,
    @ColumnInfo val repetition: DayOfWeek,
    @ColumnInfo val sound: Int,
    @ColumnInfo val vibration: Boolean
) : Parcelable {

    val timeText: String
    get() {
        val h = "%02d".format(if (hour < 12) hour else hour - 12)
        val m = "%02d".format(minute)
        return "$h:$m"
    }

    val amPmText: String
    get() {
        return if (hour < 12) "오전" else "오후"
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(DayOfWeek::class.java.classLoader)!!,
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeByte(if (use) 1 else 0)
        parcel.writeInt(hour)
        parcel.writeInt(minute)
        parcel.writeParcelable(repetition, flags)
        parcel.writeInt(sound)
        parcel.writeByte(if (vibration) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Alarm> {
        override fun createFromParcel(parcel: Parcel): Alarm {
            return Alarm(parcel)
        }

        override fun newArray(size: Int): Array<Alarm?> {
            return arrayOfNulls(size)
        }
    }

}
