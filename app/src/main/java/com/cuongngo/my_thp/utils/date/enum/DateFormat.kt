package com.cuongngo.my_thp.utils.date.enum

enum class DateFormat(val format: String) {
    /**
     * yyyy/MM/dd
     */
    SLASH_yyyy_MM_dd("yyyy/MM/dd"),

    /**
     * yyyy/MM/dd - HH:mm
     */
    SLASH_yyyy_MM_dd_HH_mm("dd/MM/yyyy - HH:mm"),

    /**
     * dd/MM/yyyy
     */
    SLASH_dd_MM_yyyy("dd/MM/yyyy"),

    /**
     * yyyy-MM-dd
     */
    DASH_yyyy_MM_dd("yyyy-MM-dd"),

    DASH_yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
    /**
     * dd-MM-yyyy
     */
    DASH_dd_MM_yyyy("dd-MM-yyyy"),

    /**
     * yyyy.MM.dd
     */
    DOT_yyyy_MM_dd("yyyy.MM.dd"),

    /**
     * dd.MM.yyyy
     */
    DOT_dd_MM_yyyy("dd.MM.yyyy"),

    /**
     * HH:mm
     */
    COLON_HH_mm("HH:mm")
}