package com.cuongngo.my_thp.common.enum

enum class FontType(val typeValue: Int) {
    THIN(1),
    EXTRA_LIGHT(2),
    LIGHT(3),
    NORMAL(4),
    MEDIUM(5),
    SEMI_BOLD(6),
    BOLD(7),
    EXTRA_BOLD(8),
    BLACK(9),
    ITALIC(10),
    SEMI_BOLD_ITALIC(11),
    BOLD_ITALIC(12);

    companion object {
        fun valueOf(typeValue: Int): FontType {
            return when(typeValue){
                1 -> THIN
                2 -> EXTRA_LIGHT
                3 -> LIGHT
                4 -> NORMAL
                5 -> MEDIUM
                6 -> SEMI_BOLD
                7 -> BOLD
                8 -> EXTRA_BOLD
                9 -> BLACK
                10 -> ITALIC
                11 -> SEMI_BOLD_ITALIC
                12 -> BOLD_ITALIC
                else -> NORMAL
            }
        }
    }
}