package br.com.phaneronsoft.flickergallery.model.api

import br.com.phaneronsoft.flickergallery.model.vo.PhotoResponse
import br.com.phaneronsoft.flickergallery.model.vo.PhotoSizeResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FlickrApiClient {
    @GET("rest/")
    fun fetchPhotoList(
        @QueryMap params: Map<String, String>
    ): Deferred<Response<PhotoResponse>>

    @GET("rest/")
    fun fetchPhotoSizeList(
        @QueryMap params: Map<String, String>
    ): Deferred<Response<PhotoSizeResponse>>
}