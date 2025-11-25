package com.bacer.notesapp.data.notes

import androidx.room.TypeConverter
import org.json.JSONArray

class Converters {

    @TypeConverter
    fun fromList(list: List<String>): String = JSONArray(list).toString()

    @TypeConverter
    fun toList(data: String): List<String> {
        val jsonArray = JSONArray(data)
        return List(jsonArray.length()) { index -> jsonArray.getString(index) }
    }
}