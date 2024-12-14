package com.patpet.qiu

import android.os.Parcel
import android.os.Parcelable

data class User(
    var username: String,
    var password: String,
    var phoneNumber: String,
    var role: String,
    var gender: String,
    var age: Int,
    var address: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(phoneNumber)
        parcel.writeString(role)
        parcel.writeString(gender)
        parcel.writeInt(age)
        parcel.writeString(address)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User = User(parcel)
        override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
    }
}
