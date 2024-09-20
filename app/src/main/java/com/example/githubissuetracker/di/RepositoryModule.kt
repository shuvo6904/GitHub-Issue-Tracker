package com.example.githubissuetracker.di

import com.example.githubissuetracker.network.AppRepository
import com.example.githubissuetracker.network.GitHubApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAppRepository(
        apiService: GitHubApiService
    ): AppRepository {
        return AppRepository(apiService)
    }
}