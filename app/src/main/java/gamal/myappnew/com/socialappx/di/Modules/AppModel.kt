package gamal.myappnew.com.socialappx.di.Modules

import android.content.Context
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.gun0912.tedpermission.PermissionListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gamal.myappnew.com.socialappx.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


//
@Module
@InstallIn(SingletonComponent::class)
object AppModel {

    @Singleton
    @Provides
    fun provideApplicationContext(
        @ApplicationContext context: Context
    ) = context

    @Singleton
    @Provides
    fun provideMainDispatcher():CoroutineDispatcher = Dispatchers.Main


//    @Singleton
//    @Provides
//    fun provideAuthRepository(): AuthRepository = DefaultAuthRepository()
//
    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_error)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )
}