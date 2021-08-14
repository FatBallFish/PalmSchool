package com.fatballfish.palmschool.logic.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @description Hilt 依赖注入module文件
 * @author FatBallFish
 * @Date 2020/11/25 12:11
 * */
@Module
@InstallIn(ApplicationComponent::class)
class ServiceModule {
    private val baseUrl = "http://ps.fatballfish.com/api/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun providePortraitService(retrofit: Retrofit): PortraitService =
        create(retrofit)

    @Singleton
    @Provides
    fun provideSchoolService(retrofit: Retrofit): SchoolService = create(retrofit)

    @Singleton
    @Provides
    fun provideTemplateService(retrofit: Retrofit): TemplateService =
        create(retrofit)

    @Singleton
    @Provides
    fun provideUserInfoService(retrofit: Retrofit): UserInfoService =
        create(retrofit)

    @Singleton
    @Provides
    fun provideUserLoginService(retrofit: Retrofit): UserLoginService =
        create(retrofit)

    @Singleton
    @Provides
    fun provideRealAuthService(retrofit: Retrofit): RealAuthService =
        create(retrofit)

    @Singleton
    @Provides
    fun provideSmsCaptchaService(retrofit: Retrofit): SmsCaptchaService =
        create(retrofit)

    @Singleton
    @Provides
    fun provideLessonTemplateService(retrofit: Retrofit): LessonTemplateService =
        create(retrofit)

    inline fun <reified T> create(retrofit: Retrofit): T = retrofit.create(T::class.java)
}