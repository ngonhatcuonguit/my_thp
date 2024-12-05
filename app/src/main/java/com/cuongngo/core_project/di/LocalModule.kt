package com.cuongngo.core_project.di

import com.cuongngo.core_project.data.database.AppDatabase
import com.cuongngo.core_project.data.database.data_source.FormLocalDataSource
import com.cuongngo.core_project.data.database.data_source.FormRemoteDatSource
import com.cuongngo.core_project.data.database.data_source.RequestLocalDataSource
import com.cuongngo.core_project.data.database.data_source.UserLocalDataSource
import com.cuongngo.core_project.ui.acb_app.GdRemoteDataSource
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

const val LOCAL_MODULE = "local_module"

val localModule = Kodein.Module(LOCAL_MODULE, false) {
    bind() from singleton { AppDatabase(instance())}
    bind() from singleton { FormLocalDataSource(instance()) }
    bind() from singleton { RequestLocalDataSource(instance()) }
    bind() from singleton { UserLocalDataSource(instance()) }
    bind() from singleton { GdRemoteDataSource(instance()) }
}