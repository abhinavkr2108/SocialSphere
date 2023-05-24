package com.example.socialsphere.di

import com.example.socialsphere.data.repository.SocialSphereRepositoryImpl
import com.example.socialsphere.domain.repository.SocialSphereRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocialSphereModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideRepository(auth: FirebaseAuth): SocialSphereRepository{
        return SocialSphereRepositoryImpl(auth)
    }

}