package com.cuongngo.my_thp
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import com.cuongngo.my_thp.di.appModule
import com.cuongngo.my_thp.data.database.AppDatabase
import com.cuongngo.my_thp.data.database.roomdb.DaoInterFace.FormDao
import com.cuongngo.my_thp.data.database.roomdb.DaoInterFace.GdDao
import com.cuongngo.my_thp.data.database.roomdb.DaoInterFace.RequestDao
import com.cuongngo.my_thp.data.database.roomdb.DaoInterFace.GenreDao
import com.cuongngo.my_thp.data.database.roomdb.DaoInterFace.UserDao
import com.cuongngo.my_thp.di.localModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

class App : Application(), KodeinAware, LifecycleObserver {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@App))
        import(appModule)
        import(localModule)
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        @Volatile
        private var instance: App? = null

        @JvmStatic
        fun getInstance(): App = instance ?: synchronized(this) {
            instance ?: App().also {
                instance = it
            }
        }
        fun getString(@StringRes strId: Int): String {
            return getResources().getString(strId)
        }

        fun getDrawableResource(@DrawableRes drawableRes: Int): Drawable? {
            return ContextCompat.getDrawable(getInstance(), drawableRes)
        }

        fun getResources(): Resources {
            return getInstance().resources
        }

        fun getGenreDatabase() : GenreDao {
            return AppDatabase.getDatabase(getInstance()).genreDao()
        }

        fun getFormDatabase() : FormDao {
            return AppDatabase.getDatabase(getInstance()).thpFormDao()
        }
        fun getFormSchemaDatabase() : RequestDao {
            return AppDatabase.getDatabase(getInstance()).requestDao()
        }
        fun getUserDB() : UserDao {
            return AppDatabase.getDatabase(getInstance()).userDao()
        }
        fun getGdDB() : GdDao {
            return AppDatabase.getDatabase(getInstance()).gdDao()
        }
        /**
         *  Check internet available
         * */
        //check internet connect
        @Suppress("DEPRECATION")
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val capabilities = connectivityManager.activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) }
                capabilities != null && (
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                        )
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
        }
    }
}