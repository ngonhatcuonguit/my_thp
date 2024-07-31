package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromFiledList(value: List<Field>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<Field>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toFiledList(value: String?): List<Field>? {
        val gson = Gson()
        val type = object : TypeToken<List<Field>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter
    fun fromProcessList(value: List<ProcessStep>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<ProcessStep>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toProcessList(value: String?): List<ProcessStep>? {
        val gson = Gson()
        val type = object : TypeToken<List<ProcessStep>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromUserEntity(user: UserTHPEntity?): String? {
        val gson = Gson()
        return gson.toJson(user)
    }

    @TypeConverter
    fun toUserEntity(userString: String?): UserTHPEntity? {
        val gson = Gson()
        return gson.fromJson(userString, UserTHPEntity::class.java)
    }

    @TypeConverter
    fun fromSheetList(sheetList: List<Sheet>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<Sheet>>() {}.type
        return gson.toJson(sheetList, type)
    }

    @TypeConverter
    fun toSheetList(sheetListString: String?): List<Sheet>? {
        val gson = Gson()
        val type = object : TypeToken<List<Sheet>>() {}.type
        return gson.fromJson(sheetListString, type)
    }

}