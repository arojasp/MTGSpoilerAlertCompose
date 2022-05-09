package es.alejandro.mtgspoileralert.db

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import es.alejandro.mtgspoileralert.detail.model.CardFace


class Converters {
    @TypeConverter
    fun listToJson(value: List<CardFace>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<CardFace>::class.java).toList()

    @TypeConverter
    fun fromString(value: String?): List<String?>? {
        val listType = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToIntegerList(value: String?): List<Int?>? {
        val listType = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromIntListToString(list: List<Int?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}