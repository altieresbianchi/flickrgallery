package br.com.phaneronsoft.flickergallery

import android.app.Application
import br.com.phaneronsoft.flickergallery.di.coroutineModule
import br.com.phaneronsoft.flickergallery.di.flickrRepositoryModule
import br.com.phaneronsoft.flickergallery.di.mainViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        this.initializeKoin();
    }

    private fun initializeKoin() {
        startKoin {
            androidContext(this@MainApplication)

            // Use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            modules(
                listOf(
                    coroutineModule,
                    flickrRepositoryModule,
                    mainViewModelModule,
                )
            )
        }
    }
}