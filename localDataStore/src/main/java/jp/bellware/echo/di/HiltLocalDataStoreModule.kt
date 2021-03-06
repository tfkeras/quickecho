package jp.bellware.echo.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import jp.bellware.echo.datastore.local.QuickEchoDatabase
import jp.bellware.echo.datastore.local.QuickEchoDatabaseFactory
import jp.bellware.echo.datastore.local.SettingLocalDataStore
import jp.bellware.echo.datastore.local.SettingLocalDataStoreImpl
import jp.bellware.echo.datastore.local.SoundFileLocalDataStore
import jp.bellware.echo.datastore.local.SoundFileLocalDataStoreImpl
import jp.bellware.echo.datastore.local.SoundMemoLocalDataStore
import jp.bellware.echo.datastore.local.SoundMemoLocalDataStoreImpl
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStore
import jp.bellware.echo.datastore.local.SoundMemoryLocalDataStoreImpl

@Module
@InstallIn(ApplicationComponent::class)
abstract class HiltLocalDataStoreModule {
    @Binds
    @Singleton
    abstract fun bindSoundMemoryLocalDataStore(
        soundMemoryLocalDataStore: SoundMemoryLocalDataStoreImpl
    ): SoundMemoryLocalDataStore

    @Binds
    @Singleton
    abstract fun bindSoundFileLocalDataStore(
        soundFileLocalDataStore: SoundFileLocalDataStoreImpl
    ): SoundFileLocalDataStore

    @Binds
    abstract fun bindSettingLocalDataStore(settingLocalDataStore: SettingLocalDataStoreImpl):
        SettingLocalDataStore

    @Binds
    @Singleton
    abstract fun bindSoundMemoLocalDataStore(
        soundMemoLocalDataStore: SoundMemoLocalDataStoreImpl
    ): SoundMemoLocalDataStore
}

@Module
@InstallIn(ApplicationComponent::class)
object HiltLocalDataStoreModuleProvider {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideQuickEchoDataBase(@ApplicationContext context: Context): QuickEchoDatabase {
        return QuickEchoDatabaseFactory.create(context)
    }
}
