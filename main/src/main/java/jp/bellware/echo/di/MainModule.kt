package jp.bellware.echo.di

import jp.bellware.echo.actioncreator.DelayActionCreatorHelper
import jp.bellware.echo.actioncreator.DelayActionCreatorHelperImpl
import jp.bellware.echo.actioncreator.MainActionCreator
import jp.bellware.echo.store.MainStore
import jp.bellware.echo.view.main.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val localDataStoreModuleInMainModule = localDataStoreModuleInRepositoryModule
val repositoryModuleInMainModule = repositoryModule

val mainModule = module {
    // ActionCreator
    single { DelayActionCreatorHelperImpl() as DelayActionCreatorHelper }
    single { MainActionCreator(get(), get()) }
    // Store
    viewModel { MainStore(get()) }
    // ViewHelper
    viewModel { SoundEffectViewHelper(androidContext(), get()) }
    viewModel { RecordViewHelper(get()) }
    viewModel { PlayViewHelper(get()) }
    viewModel { VisualVolumeViewHelper() }
    viewModel { TimerViewHelper() }
    single { AnimatorViewHelper() }
}

