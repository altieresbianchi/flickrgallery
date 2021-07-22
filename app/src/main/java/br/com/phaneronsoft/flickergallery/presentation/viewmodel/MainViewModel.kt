package br.com.phaneronsoft.flickergallery.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.phaneronsoft.flickergallery.model.repository.FlickrRepositoryContract
import br.com.phaneronsoft.flickergallery.model.vo.PhotoVO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val coroutineContext: CoroutineContext,
    private val flickerRepository: FlickrRepositoryContract
) : ViewModel() {
    private val coroutineScope = CoroutineScope(coroutineContext)

    private val _photoList = MutableLiveData<List<PhotoVO>>()
    val photoList: MutableLiveData<List<PhotoVO>> get() = _photoList

    private val _error = MutableLiveData<String>()
    val error: MutableLiveData<String> get() = _error

    private val _updatedPhoto = MutableLiveData<PhotoVO>()
    val updatedPhoto: MutableLiveData<PhotoVO> get() = _updatedPhoto

    var currentPage = 1
    var hasMoreData = false

    fun fetchPhotoList(tag: String) {
        coroutineScope.launch {
            try {
                val response = flickerRepository.fetchPhotoList(currentPage, tag).await()

                if (response.isSuccessful) {
                    response.body()?.let { photoResponse ->
                        val list = response.body()!!.photos.photoList
                        _photoList.postValue(list)

                        currentPage = (photoResponse.photos.page + 1)
                        hasMoreData = (photoResponse.photos.page < photoResponse.photos.pages)
                    }
                } else {
                    error.postValue("Failed result")
                }

            } catch (e: Exception) {
                Log.e(javaClass.simpleName, e.message, e)

                error.postValue(e.message)
            }
        }
    }

    fun fetchPhotoSizeList(photo: PhotoVO) {
        coroutineScope.launch {
            try {
                val response = flickerRepository.fetchPhotoSizeList(photo).await()

                if (response.isSuccessful) {
                    photo.sizeList = response.body()?.sizes?.sizeList

                    updatedPhoto.postValue(photo)

                } else {
                    error.postValue("Failed result")
                }

            } catch (e: Exception) {
                error.postValue(e.message)
                Log.e(javaClass.simpleName, e.message, e)
            }
        }
    }
}