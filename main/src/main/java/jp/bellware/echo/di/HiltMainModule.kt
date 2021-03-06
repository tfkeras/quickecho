package jp.bellware.echo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton
import jp.bellware.echo.actioncreator.DelayActionCreatorHelper
import jp.bellware.echo.actioncreator.DelayActionCreatorHelperImpl
import jp.bellware.echo.view.main.AnimatorViewHelper

@Module
@InstallIn(ApplicationComponent::class)
abstract class HiltMainModule {
    @Binds
    @Singleton
    abstract fun bindDelayActionCreatorHelper(
        delayActionCreatorHelper: DelayActionCreatorHelperImpl
    ): DelayActionCreatorHelper

    @Binds
    abstract fun bindAnimatorViewHelper(animatorViewHelper: AnimatorViewHelper): AnimatorViewHelper
}
