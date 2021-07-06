package gamal.myappnew.com.socialappx.di.Modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import gamal.myappnew.com.socialappx.repositories.AuthRepository
import gamal.myappnew.com.socialappx.repositories.DefaultAuthRepository
import gamal.myappnew.com.socialappx.repositories.DefaultMainRepository
import gamal.myappnew.com.socialappx.repositories.MainRepository
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)
object MainModel {
//

    @Provides
    @ViewModelScoped
    fun provideMainRepository(): MainRepository = DefaultMainRepository()
}