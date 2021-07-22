package br.com.phaneronsoft.flickergallery.model.repository

import br.com.phaneronsoft.flickergallery.model.api.FlickrApiClient
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import br.com.phaneronsoft.flickergallery.model.repository.FlickrApiRepository
import br.com.phaneronsoft.flickergallery.model.vo.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals

class FlickrApiRepositoryTest {
    private lateinit var flickrApiClientMock: FlickrApiClient
    private lateinit var flickrRepositoryMock: FlickrApiRepository

    @Before
    fun setUp() {
        flickrApiClientMock = mock()
        flickrRepositoryMock = FlickrApiRepository(flickrApiClientMock)
    }

    @Test
    fun `Get list of photos filtered by tag dogs, when it is requested, then return deferred operation status`() {

        // ARRANGE
        val page = 1
        val tag = "dogs"

        val photoList = listOf(
            PhotoVO("51270197517", "65535", "Visit with Runyon to Swift Run Dog Park"),
            PhotoVO("51270231624", "65535", "April 2021 Blakes Wood Bluebells"),
        )
        val photoListVO = PhotoListVO(1, 7479, 15, 74787, photoList)
        val expectedPhotoResponse = PhotoResponse(
            photoListVO,
            "ok"
        )

        val expectedResponse = Response.success(expectedPhotoResponse)

        val expectedDeferredResponse = CompletableDeferred(expectedResponse)

        whenever(flickrRepositoryMock.fetchPhotoList(page, tag)).thenReturn(
            expectedDeferredResponse
        )

        // ACT

        val dataResponse = runBlocking { flickrRepositoryMock.fetchPhotoList(page, tag).await() }

        // ASSERT

        assertEquals(expectedResponse, dataResponse)
    }

    @Test
    fun `Get photo size list by photo id, when it is requested, then return deferred operation status`() {

        // ARRANGE

        val photoVO = PhotoVO("51270197517")

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
        val sizeListVO = SizeListVO(0, 0, 1, sizeList)
        val expectedPhotoSizeResponse = PhotoSizeResponse(
            sizeListVO,
            "ok"
        )

        val expectedResponse = Response.success(expectedPhotoSizeResponse)

        val expectedDeferredResponse = CompletableDeferred(expectedResponse)

        whenever(flickrRepositoryMock.fetchPhotoSizeList(photoVO)).thenReturn(
            expectedDeferredResponse
        )

        // ACT

        val dataResponse = runBlocking { flickrRepositoryMock.fetchPhotoSizeList(photoVO).await() }

        // ASSERT

        assertEquals(expectedResponse, dataResponse)
    }
}
