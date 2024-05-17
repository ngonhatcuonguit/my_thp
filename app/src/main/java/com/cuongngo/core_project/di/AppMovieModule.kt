package com.cuongngo.core_project.di

import androidx.lifecycle.ViewModelProvider
import com.cuongngo.core_project.base.viewmodel.bindViewModel
import com.cuongngo.core_project.data.database.data_source.RecordProcessLocalDataSource
import com.cuongngo.core_project.services.THPApi
import com.cuongngo.core_project.services.network.BaseRemoteDataSource
import com.cuongngo.core_project.services.network.invoker.NetworkConnectionInterceptor
import com.cuongngo.core_project.services.remote.UserRemoteDataSource
import com.cuongngo.core_project.services.repository.RecordProcessRepository
import com.cuongngo.core_project.services.repository.UserRepository
import com.cuongngo.core_project.ui.home.HomeViewModel
import com.cuongngo.core_project.ui.login.UserViewModel
import com.cuongngo.core_project.ui.test_room_db.RecordProcessViewModel
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

const val APP_MODULE = "app_module"

val appMovieModule = Kodein.Module(APP_MODULE, false) {
    bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }

    bind() from singleton { UserRemoteDataSource(instance()) }
    bind() from singleton { RecordProcessLocalDataSource(instance()) }

    bind() from singleton { NetworkConnectionInterceptor(instance()) }
    bind() from singleton { THPApi()}

    bind() from singleton { RecordProcessRepository(instance()) }
    bind() from singleton { UserRepository(instance()) }

    bindViewModel<HomeViewModel>() with provider {
        HomeViewModel(instance())
    }
    bindViewModel<UserViewModel>() with provider {
        UserViewModel(instance())
    }

    bindViewModel<RecordProcessViewModel>() with provider {
        RecordProcessViewModel(instance())
    }
}