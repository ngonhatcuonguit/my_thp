package com.cuongngo.my_thp.di

import androidx.lifecycle.ViewModelProvider
import com.cuongngo.my_thp.base.viewmodel.bindViewModel
import com.cuongngo.my_thp.data.database.data_source.FormRemoteDatSource
import com.cuongngo.my_thp.data.database.data_source.RequestRemoteDataSource
import com.cuongngo.my_thp.services.THPApi
import com.cuongngo.my_thp.services.network.invoker.NetworkConnectionInterceptor
import com.cuongngo.my_thp.services.remote.UserRemoteDataSource
import com.cuongngo.my_thp.services.repository.FormRepository
import com.cuongngo.my_thp.services.repository.RequestRepository
import com.cuongngo.my_thp.services.repository.UserRepository
import com.cuongngo.my_thp.ui.acb_app.GdRepository
import com.cuongngo.my_thp.ui.acb_app.GdViewModel
import com.cuongngo.my_thp.ui.form_schema.RequestViewModel
import com.cuongngo.my_thp.ui.home.HomeViewModel
import com.cuongngo.my_thp.ui.login.UserViewModel
import com.cuongngo.my_thp.ui.profile.ProfileViewModel
import com.cuongngo.my_thp.ui.search_form.FormViewModel
import com.cuongngo.my_thp.ui.sync_data.SyncDataViewModel
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

const val APP_MODULE = "app_module"

val appModule = Kodein.Module(APP_MODULE, false) {

    bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }
    /**
     * RemoteDataSource binding
     */
    bind() from singleton { UserRemoteDataSource(instance()) }
    bind() from singleton { RequestRemoteDataSource(instance()) }
    bind() from singleton { FormRemoteDatSource(instance()) }

    /**
     * Network binding
     */
    bind() from singleton { NetworkConnectionInterceptor(instance()) }
    bind() from singleton { THPApi() }

    /**
     * Repository binding
     */
    bind() from singleton { UserRepository(instance(), instance()) }
    bind() from singleton { FormRepository(instance(), instance()) }
    bind() from singleton { RequestRepository(instance(), instance()) }
    bind() from singleton { GdRepository(instance()) }

    /**
     * ViewModel binding
     */
    bindViewModel<HomeViewModel>() with provider {
        HomeViewModel(instance(), instance(), instance())
    }
    bindViewModel<UserViewModel>() with provider {
        UserViewModel(instance())
    }
    bindViewModel<FormViewModel>() with provider {
        FormViewModel(instance())
    }
    bindViewModel<RequestViewModel>() with provider {
        RequestViewModel(instance(), instance())
    }
    bindViewModel<ProfileViewModel>() with provider {
        ProfileViewModel(instance())
    }
    bindViewModel<SyncDataViewModel>() with provider {
        SyncDataViewModel(instance(), instance())
    }
    bindViewModel<GdViewModel>() with provider {
        GdViewModel(instance())
    }

}