package com.cuongngo.core_project.di

import com.cuongngo.core_project.data.database.AppDatabase
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import org.kodein.di.generic.bind

const val LOCAL_MODULE = "local_module"

val localModule = Kodein.Module(LOCAL_MODULE, false) {
    bind() from singleton { AppDatabase(instance())}
}