package br.com.phaneronsoft.flickergallery.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.phaneronsoft.flickergallery.model.vo.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import br.com.phaneronsoft.flickergallery.getOrAwaitValueTest
import br.com.phaneronsoft.flickergallery.model.repository.FlickrRepositoryContract
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import br.com.phaneronsoft.flickergallery.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals

class MainViewModelTest {
    @JvmField
    @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var flickrRepositoryMock: FlickrRepositoryContract
    private lateinit var viewModel: MainViewModel

    private lateinit var photoListLiveDataObserver: Observer<List<PhotoVO>>
    private lateinit var errorLiveDataObserver: Observer<String>
    private lateinit var updatedPhotoLiveDataObserver: Observer<PhotoVO>

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        flickrRepositoryMock = mock()

        photoListLiveDataObserver = mock()
        errorLiveDataObserver = mock()
        updatedPhotoLiveDataObserver = mock()

        viewModel = MainViewModel(
            Dispatchers.IO,
            flickrRepositoryMock
        )
    }

    @Test
    fun `Fetch photo list by tag dogs, when fetching photos from repository happens successfully, then update photo list`() {

        // ARRANGE
        val page = 1
        val tag = "dogs"

        val sizeList = listOf(
            SizeVO(
                "Large Square", 150, 150,
                "https://live.staticflickr.com/5800/31456463045_5a0af4ddc8_q.jpg",
                "https://www.flickr.com/photos/alex53/31456463045/sizes/q/", "photo"
            ),
            SizeVO(
                "Large", 1024, 576,
                "https://live.staticflickr.com/5800/31456463045_5a0af4ddc8_b.jpg",
                "https://www.flickr.com/photos/alex53/31456463045/sizes/l/", "photo"
            ),
        )
        val photoList = listOf(
            PhotoVO("51270197517", "65535", "Visit with Runyon to Swift Run Dog Park", sizeList),
            PhotoVO("51270231624", "65535", "April 2021 Blakes Wood Bluebells", sizeList),
        )
        val photoListVO = PhotoListVO(1, 7479, 15, 74787, photoList)
        val expectedPhotoResponse = PhotoResponse(
            photoListVO,
            "ok"
        )

        val expectedResponse = Response.success(expectedPhotoResponse)

        whenever(flickrRepositoryMock.fetchPhotoList(page, tag))
            .thenReturn(CompletableDeferred(expectedResponse))

        // ACT
        viewModel.fetchPhotoList(tag)
        viewModel.photoList.observeForever(photoListLiveDataObserver)

        val resultFromLiveData = viewModel.photoList.getOrAwaitValueTest()

        // ASSERT
        assertEquals(expectedResponse.body(), expectedPhotoResponse)

        verify(photoListLiveDataObserver).onChanged(resultFromLiveData)
        assertEquals(resultFromLiveData, photoList)
    }

    @Test
    fun `Fetch photo list by tag dogs, when fetching photos from repository fails, then update error message`() {

        // ARRANGE
        val page = 1
        val tag = "dogs"
        val errorCode = 500

        val expectedJsonError = JsonObject().apply {
            addProperty("status", "")
            addProperty("message", "")
        }

        val expectedResponse = Response.error<PhotoResponse>(
            errorCode,
            Gson().toJson(expectedJsonError).toResponseBody(),
        )
        whenever(flickrRepositoryMock.fetchPhotoList(page, tag))
            .thenReturn(CompletableDeferred(expectedResponse))

        // ACT
        viewModel.fetchPhotoList(tag)
        viewModel.error.observeForever(errorLiveDataObserver)

        val resultFromLiveData = viewModel.error.getOrAwaitValueTest()

        // ASSERT
        verify(errorLiveDataObserver).onChanged(resultFromLiveData)
        assertEquals(resultFromLiveData, "Failed result")
    }

    @Test
    fun `Fetch photo size list by photo, when fetching sizes from repository happens successfully, then update photo object`() {

        // ARRANGE
        val photo = PhotoVO("51270197517", "65535", "Visit with Runyon to Swift Run Dog Park")

        val sizeList = listOf(
            SizeVO(
                "Large Square", 150, 150,
                "https://live.staticflickr.com/5800/31456463045_5a0af4ddc8_q.jpg",
                "https://www.flickr.com/photos/alex53/31456463045/sizes/q/", "photo"
            ),
            SizeVO(
                "Large", 1024, 576,
                "https://live.staticflickr.com/5800/31456463045_5a0af4ddc8_b.jpg",
                "https://www.flickr.com/photos/alex53/31456463045/sizes/l/", "photo"
            ),
        )
        val sizes = SizeListVO(0, 0, 1, sizeList)
        val expectedPhotoSizeResponse = PhotoSizeResponse(
            sizes,
            "ok"
        )

        val expectedResponse = Response.success(expectedPhotoSizeResponse)

        whenever(flickrRepositoryMock.fetchPhotoSizeList(photo))
            .thenReturn(CompletableDeferred(expectedResponse))

        // ACT
        viewModel.fetchPhotoSizeList(photo)
        viewModel.updatedPhoto.observeForever(updatedPhotoLiveDataObserver)

        val resultFromLiveData = viewModel.updatedPhoto.getOrAwaitValueTest()

        // ASSERT
        assertEquals(expectedResponse.body(), expectedPhotoSizeResponse)

        verify(updatedPhotoLiveDataObserver).onChanged(resultFromLiveData)
        assertEquals(resultFromLiveData, photo)
    }

    @Test
    fun `Fetch photo size list by photo, when fetching sizes from repository fails, then update error message`() {

        // ARRANGE
        val photo = PhotoVO("51270197517", "65535", "Visit with Runyon to Swift Run Dog Park")
        val errorCode = 500

        val expectedJsonError = JsonObject().apply {
            addProperty("status", "")
            addProperty("message", "")
        }

        val expectedResponse = Response.error<PhotoSizeResponse>(
            errorCode,
            Gson().toJson(expectedJsonError).toResponseBody(),
        )
        whenever(flickrRepositoryMock.fetchPhotoSizeList(photo))
            .thenReturn(CompletableDeferred(expectedResponse))

        // ACT
        viewModel.fetchPhotoSizeList(photo)
        viewModel.error.observeForever(errorLiveDataObserver)

        val resultFromLiveData = viewModel.error.getOrAwaitValueTest()

        // ASSERT
        verify(errorLiveDataObserver).onChanged(resultFromLiveData)
        assertEquals(resultFromLiveData, "Failed result")
    }
}
