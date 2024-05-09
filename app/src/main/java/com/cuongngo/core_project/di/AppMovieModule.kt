package com.cuongngo.core_project.di

import androidx.lifecycle.ViewModelProvider
import com.cuongngo.core_project.base.viewmodel.bindViewModel
import com.cuongngo.core_project.services.MediaApi
import com.cuongngo.core_project.services.network.invoker.NetworkConnectionInterceptor
import com.cuongngo.core_project.services.remote.MediaRemoteDataSource
import com.cuongngo.core_project.services.repository.MediaRepository
import com.cuongngo.core_project.ui.home.HomeViewModel
import com.cuongngo.core_project.ui.media.MediaViewModel
import com.cuongngo.core_project.ui.search.SearchViewModel
import com.cuongngo.core_project.ui.youtube.VideoViewModel
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

const val APP_MODULE = "app_module"

val appMovieModule = Kodein.Module(APP_MODULE, false) {
    bind() from singleton { NetworkConnectionInterceptor(instance()) }
    bind() from singleton { MediaApi()}

    bind() from singleton { MediaRemoteDataSource(instance()) }
    bind() from singleton { MediaRepository(instance()) }
    bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }
    bindViewModel<MediaViewModel>() with provider {
        MediaViewModel(instance())
    }
    bindViewModel<HomeViewModel>() with provider {
        HomeViewModel(instance())
    }
    bindViewModel<SearchViewModel>() with provider {
        SearchViewModel(instance())
    }
    bindViewModel<VideoViewModel>() with provider {
        VideoViewModel(instance())
    }
}