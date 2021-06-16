package com.mrtwon.framex.Content

import android.os.Parcel
import android.os.Parcelable


class ParcelableEnum() : Parcelable {
    var collectionEnum: CollectionContentEnum? = null
    var genresEnum: GenresEnum? = null
    var contentTypeEnum: ContentTypeEnum? = null
    constructor(parcel: Parcel) : this() {
        val arr = arrayOf<String>()
        parcel.readStringArray(arr)
        genresEnum = valueOfGenres(arr[0])
        collectionEnum = CollectionContentEnum.valueOf(arr[1])
        contentTypeEnum = ContentTypeEnum.valueOf(arr[2])
    }

    constructor(mGenresEnum: GenresEnum) : this() {
        genresEnum = mGenresEnum
    }
    constructor(mCollectionEnum: CollectionContentEnum): this(){
        collectionEnum = mCollectionEnum
    }
    constructor(mContentType: ContentTypeEnum): this(){
        contentTypeEnum = mContentType
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringArray(arrayOf(
            genresEnum.toString(),
            collectionEnum.toString(),
            contentTypeEnum.toString()))
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableEnum> {
        override fun createFromParcel(parcel: Parcel): ParcelableEnum {
            return ParcelableEnum(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableEnum?> {
            return arrayOfNulls(size)
        }
    }
    fun valueOfGenres(value: String?): GenresEnum?{
        if(value == null) return null
        val list = GenresEnum.values()
        for(elem in list){
            if(elem.toString() == value) return elem
        }
        return null
    }

}