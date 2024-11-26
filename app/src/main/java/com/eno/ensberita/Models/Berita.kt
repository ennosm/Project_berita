package com.eno.ensberita.Models

import android.os.Parcel
import android.os.Parcelable

data class Berita(
    val id: String? = null, // Tambahkan properti 'id'
    var title: String? = null,
    var description: String? = null,
    var poster: String? = null,
    var time: String? = null,
    var imdb: Int = 0,
    var year: Int = 0,
    var category: ArrayList<String> = ArrayList()
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(), // id
        parcel.readString(), // Title
        parcel.readString(), // Description
        parcel.readString(), // Poster
        parcel.readString(), // Time
        parcel.readInt(),    // Imdb
        parcel.readInt(),    // Year
        parcel.createStringArrayList() ?: ArrayList(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id) // Tulis id ke Parcel
        parcel.writeString(title)  // Menyimpan Title dengan kapital
        parcel.writeString(description)  // Menyimpan Description dengan kapital
        parcel.writeString(poster)  // Menyimpan Poster dengan kapital
        parcel.writeString(time)  // Menyimpan Time dengan kapital
        parcel.writeInt(imdb)
        parcel.writeInt(year)
        parcel.writeStringList(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Berita> {
        override fun createFromParcel(parcel: Parcel): Berita {
            return Berita(parcel)
        }

        override fun newArray(size: Int): Array<Berita?> {
            return arrayOfNulls(size)
        }
    }
}
