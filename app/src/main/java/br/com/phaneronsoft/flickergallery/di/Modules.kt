package br.com.phaneronsoft.flickergallery.di

import br.com.phaneronsoft.flickergallery.BuildConfig
import br.com.phaneronsoft.flickergallery.model.api.FlickrApiClient
import br.com.phaneronsoft.flickergallery.model.api.RestClient
import br.com.phaneronsoft.flickergallery.model.repository.FlickrApiRepository
import br.com.phaneronsoft.flickergallery.model.repository.FlickrRepositoryContract
import br.com.phaneronsoft.flickergallery.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

val coroutineModule: Module = module {
    factory<CoroutineContext> { Dispatchers.IO }
}

val flickrRepositoryModule: Module = module {
    factory<FlickrRepositoryContract> {
        FlickrApiRepository(flickrApiClient = get())
    }

    single {
        RestClient.getApiClient(
            serviceClass = FlickrApiClient::class.java,
            baseEndpoint = BuildConfig.BASE_API
        )
    }
}

val mainViewModelModule: Module = module {
    viewModel {
        MainViewModel(
            coroutineContext = get(),
            flickerRepository = get()
        )
    }
}