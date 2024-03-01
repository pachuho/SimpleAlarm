package com.pachuho.sleepAlarm.data.datasource.model

import android.os.Parcel
import android.os.Parcelable


data class DayOfWeek(
    var monday: Boolean = true,
    var tuesday: Boolean = true,
    var wednesday: Boolean = true,
    var thursday: Boolean = true,
    var friday: Boolean = true,
    var saturday: Boolean = false,
    var sunday: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (monday) 1 else 0)
        parcel.writeByte(if (tuesday) 1 else 0)
        parcel.writeByte(if (wednesday) 1 else 0)
        parcel.writeByte(if (thursday) 1 else 0)
        parcel.writeByte(if (friday) 1 else 0)
        parcel.writeByte(if (saturday) 1 else 0)
        parcel.writeByte(if (sunday) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DayOfWeek> {
        override fun createFromParcel(parcel: Parcel): DayOfWeek {
            return DayOfWeek(parcel)
        }

        override fun newArray(size: Int): Array<DayOfWeek?> {
            return arrayOfNulls(size)
        }
    }
}