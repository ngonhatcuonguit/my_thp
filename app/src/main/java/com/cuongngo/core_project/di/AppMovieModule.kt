package com.cuongngo.core_project.di

import androidx.lifecycle.ViewModelProvider
import com.cuongngo.core_project.base.viewmodel.bindViewModel
import com.cuongngo.core_project.data.database.data_source.FormLocalDataSource
import com.cuongngo.core_project.data.database.data_source.RequestLocalDataSource
import com.cuongngo.core_project.data.database.data_source.RecordProcessLocalDataSource
import com.cuongngo.core_project.services.THPApi
import com.cuongngo.core_project.services.network.invoker.NetworkConnectionInterceptor
import com.cuongngo.core_project.services.remote.UserRemoteDataSource
import com.cuongngo.core_project.services.repository.FormRepository
import com.cuongngo.core_project.services.repository.RequestRepository
import com.cuongngo.core_project.services.repository.RecordProcessRepository
import com.cuongngo.core_project.services.repository.UserRepository
import com.cuongngo.core_project.ui.form_schema.RequestViewModel
import com.cuongngo.core_project.ui.home.HomeViewModel
import com.cuongngo.core_project.ui.login.UserViewModel
import com.cuongngo.core_project.ui.search_form.ListFormViewModel
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
    /**
     * RemoteDataSource binding
     */
    bind() from singleton { UserRemoteDataSource(instance()) }
    bind() from singleton { RecordProcessLocalDataSource(instance()) }
    bind() from singleton { FormLocalDataSource(instance()) }
    bind() from singleton { RequestLocalDataSource(instance()) }

    /**
     * Network binding
     */
    bind() from singleton { NetworkConnectionInterceptor(instance()) }
    bind() from singleton { THPApi()}

    /**
     * Repository binding
     */
    bind() from singleton { RecordProcessRepository(instance()) }
    bind() from singleton { UserRepository(instance()) }
    bind() from singleton { FormRepository(instance()) }
    bind() from singleton { RequestRepository(instance()) }

    /**
     * ViewModel binding
     */
    bindViewModel<HomeViewModel>() with provider {
        HomeViewModel(instance(), instance())
    }
    bindViewModel<UserViewModel>() with provider {
        UserViewModel(instance())
    }
    bindViewModel<RecordProcessViewModel>() with provider {
        RecordProcessViewModel(instance())
    }
    bindViewModel<ListFormViewModel>() with provider {
        ListFormViewModel(instance())
    }
    bindViewModel<RequestViewModel>() with provider {
        RequestViewModel(instance())
    }

}