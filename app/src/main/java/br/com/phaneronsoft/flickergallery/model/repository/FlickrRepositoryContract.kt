package br.com.phaneronsoft.flickergallery.model.repository

import br.com.phaneronsoft.flickergallery.model.vo.PhotoResponse
import br.com.phaneronsoft.flickergallery.model.vo.PhotoSizeResponse
import br.com.phaneronsoft.flickergallery.model.vo.PhotoVO
import kotlinx.coroutines.Deferred
import retrofit2.Response

interface FlickrRepositoryContract {
    fun fetchPhotoList(page: Int, tag: String): Deferred<Response<PhotoResponse>>

    fun fetchPhotoSizeList(photo: PhotoVO): Deferred<Response<PhotoSizeResponse>>
}