package com.cuongngo.core_project.utils

class Constants {
    companion object{
        val PROVIDER_APP = "app"
        val PROVIDER_FB = "facebook"
        val PROVIDER_GOOGLE = "google"
        val USAGE_REGISTER = "register"
        val PLATFORM_ANDROID = "android"
        val ANNOUNCEMENT = "announcement"
        val NEWS = "news"
        val APP = "core_project"
        val FACEBOOK_APPLICATION_ID = "com.facebook.katana"
        val BOTTOM_SHEET_HEIGHT_VALUE = "height_value"
        val BASE_API_URL ="https://salereport.thp.com.vn/"
        val YOUTUBE_API_KEY = "AIzaSyC_MiFZ-xai5-TnhHLdnpbIMWSw9HC27vA"
        val ENGLISH = "eng"
        val VIETNAM = "vi"
    }
    class Exception{
        companion object{
            val CANCELLATION_EXCEPTION = -1000
        }
    }

    class CategoryRequestDetail{
        companion object{
            const val ADD = "add"
            const val VIEW = "view"
            const val EDIT = "edit"
            const val OTHER = "other"
        }
    }

    class TimeWindow{
        companion object{
            const val DAY = "day"
            const val WEEK = "week"
        }
    }

}