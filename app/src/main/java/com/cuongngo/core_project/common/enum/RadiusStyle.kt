package com.okxe.core_okxe.common.view.enum

enum class RadiusStyle(val value: Int) {
    LARGE(1),
    MEDIUM(2),
    SMALL(3),
    ROUND(4);

    companion object {
        fun valueOf(typeValue: Int): RadiusStyle{
            return when(typeValue){
                1 -> LARGE
                2 -> MEDIUM
                3 -> SMALL
                4 -> ROUND
                else -> MEDIUM
            }
        }
    }
}