package com.eno.ensberita.Models

import android.os.Parcel
import android.os.Parcelable

data class TopBerita(
    val id: String? = null,
    val Title: String? = null,
    var Description: String? = null,
    var Poster: String? = null,
    var Time: String? = null,
    var Imdb: Int = 0,
    var Year: Int = 0,
    var Category: ArrayList<String> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.createStringArrayList() ?: ArrayList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(Title)
        parcel.writeString(Description)
        parcel.writeString(Poster)
        parcel.writeString(Time)
        parcel.writeInt(Imdb)
        parcel.writeInt(Year)
        parcel.writeStringList(Category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopBerita> {
        override fun createFromParcel(parcel: Parcel): TopBerita {
            return TopBerita(parcel)
        }

        override fun newArray(size: Int): Array<TopBerita?> {
            return arrayOfNulls(size)
        }
    }
}
