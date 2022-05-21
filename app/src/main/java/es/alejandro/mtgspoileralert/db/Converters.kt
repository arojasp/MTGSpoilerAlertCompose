package es.alejandro.mtgspoileralert.db

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import es.alejandro.mtgspoileralert.cards.model.AllPart
import es.alejandro.mtgspoileralert.detail.model.CardFace
import es.alejandro.mtgspoileralert.sets.model.Set


class Converters {

    @TypeConverter
    fun allPartListToString(value: List<AllPart>?) = Gson().toJson(value)

    @TypeConverter
    fun cardFaceListToString(value: List<CardFace>?) = Gson().toJson(value)

    @TypeConverter
    fun stringToCardFaceList(value: String) = Gson().fromJson(value, Array<CardFace>::class.java).toList()

    @TypeConverter
    fun stringListToString(list: List<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToStringList(value: String?): List<String?>? {
        val listType = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun intListToString(list: List<Int?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToIntList(value: String?): List<Int?>? {
        val listType = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun stringToAllPartList(value: String?): List<AllPart>? {
        val listType = object: TypeToken<List<AllPart?>?>() {}.type
        return Gson().fromJson(value, listType)
    }
}