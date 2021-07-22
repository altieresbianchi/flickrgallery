package br.com.phaneronsoft.flickergallery.model.repository

import br.com.phaneronsoft.flickergallery.BuildConfig
import br.com.phaneronsoft.flickergallery.model.api.FlickrApiClient
import br.com.phaneronsoft.flickergallery.model.vo.PhotoResponse
import br.com.phaneronsoft.flickergallery.model.vo.PhotoSizeResponse
import br.com.phaneronsoft.flickergallery.model.vo.PhotoVO
import br.com.phaneronsoft.flickergallery.utils.Constants
import kotlinx.coroutines.Deferred
import retrofit2.Response

class FlickrApiRepository(
    private val flickrApiClient: FlickrApiClient
) : FlickrRepositoryContract {

    override fun fetchPhotoList(page: Int, tag: String): Deferred<Response<PhotoResponse>> {
        try {
            val params = HashMap<String, String>()
            params["api_key"] = BuildConfig.API_KEY
            params["format"] = Constants.LIST_FORMAT_JSON
            params["method"] = Constants.FLICKR_METHOD_PHOTO_SEARCH
            params["per_page"] = Constants.LIST_PAGINATION_RESULT.toString()
            params["nojsoncallback"] = "1"
            params["page"] = page.toString()
            params["tags"] = tag

            return flickrApiClient.fetchPhotoList(params)

        } catch (e: Exception) {
            throw e
        }
    }

    override fun fetchPhotoSizeList(photo: PhotoVO): Deferred<Response<PhotoSizeResponse>> {
        try {
            val params = HashMap<String, String>()
            params["api_key"] = BuildConfig.API_KEY
            params["format"] = Constants.LIST_FORMAT_JSON
            params["method"] = Constants.FLICKR_METHOD_PHOTO_SIZES
            params["nojsoncallback"] = "1"
            params["photo_id"] = photo.id

            return flickrApiClient.fetchPhotoSizeList(params)

        } catch (e: Exception) {
            throw e
        }
    }
}
