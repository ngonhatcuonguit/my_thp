package com.cuongngo.core_project
import android.app.Application
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import com.cuongngo.core_project.di.appMovieModule
import com.cuongngo.core_project.response.MovieResponse
import com.cuongngo.core_project.response.movie_response.GenresMovie
import com.cuongngo.core_project.response.movie_response.GenresMovieResponse
import com.cuongngo.core_project.data.database.AppDatabase
import com.cuongngo.core_project.data.database.roomdb.DaoInterFace.GenreDao
import com.cuongngo.core_project.data.database.roomdb.DaoInterFace.RecordProcessDao
import com.cuongngo.core_project.di.localModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule

class App : Application(), KodeinAware, LifecycleObserver {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@App))
        import(appMovieModule)
        import(localModule)
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        var genresMovieResponse = GenresMovieResponse(
            null,
            null,
            genres = arrayListOf()
        )

        var genreSelected: GenresMovie? = null

        var movieTrending = MovieResponse(
            null,
            null,
            null,
            null,
            null,
            results = arrayListOf()
        )

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
        fun getRecordDatabase() : RecordProcessDao {
            return AppDatabase.getDatabase(getInstance()).recordProcessDao()
        }

        fun getGenres(): GenresMovieResponse {
            return genresMovieResponse
        }

        fun setListTrending(movieResponse: MovieResponse){
            this.movieTrending = movieResponse
        }

    }
}