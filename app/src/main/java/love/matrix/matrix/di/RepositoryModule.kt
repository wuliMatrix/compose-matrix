package com.instagram.clone.di

import com.instagram.clone.utils111.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory { UserRepository(get()) }
}