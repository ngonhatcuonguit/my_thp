package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

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
    fun fromUserList(value: List<UserTHPEntity>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<UserTHPEntity>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toUserList(value: String?): List<UserTHPEntity>? {
        val gson = Gson()
        val type = object : TypeToken<List<UserTHPEntity>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromSheetList(bodyList: List<Body>?): String? {
        val gson = Gson()
        val type = object : TypeToken<List<Body>>() {}.type
        return gson.toJson(bodyList, type)
    }

    @TypeConverter
    fun toSheetList(sheetListString: String?): List<Body>? {
        val gson = Gson()
        val type = object : TypeToken<List<Body>>() {}.type
        return gson.fromJson(sheetListString, type)
    }

    // Chuyển đổi từ Calendar thành Long (lưu vào database)
    @TypeConverter
    fun fromCalendar(calendar: Calendar?): Long? {
        return calendar?.timeInMillis
    }

    // Chuyển đổi từ Long thành Calendar (khi đọc từ database)
    @TypeConverter
    fun toCalendar(timestamp: Long?): Calendar? {
        return timestamp?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar
        }
    }

}