package com.cuongngo.my_thp.common.enum

enum class FieldType(val fileType: String) {
    HEADER("header"),
    NUMBER("number"),
    AUTOCOMPLETE("autocomplete"),
    BUTTON("button"),
    CHECK_BOX_GROUP("checkbox-group"),
    DATE("date"),
    FILE("file"),
    HIDDEN("hidden"),
    TEXT("text"),
    PARAGRAPH("paragraph"),
    RADIO_GROUP("radio-group"),
    SELECT("select"),
    TEXT_AREA("textarea"),
}