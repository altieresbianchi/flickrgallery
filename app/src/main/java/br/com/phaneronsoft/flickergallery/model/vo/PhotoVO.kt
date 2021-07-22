package br.com.phaneronsoft.flickergallery.model.vo

import br.com.phaneronsoft.flickergallery.utils.Constants
import java.io.Serializable

data class PhotoVO(
    val id: String,
    val server: String?,
    val title: String?,
    var sizeList: List<SizeVO>? = mutableListOf()
) : Serializable {

    constructor(id: String) : this(id, null, null)

    constructor(id: String, server: String, title: String) : this(id, server, title, null)

    fun getThumbImage(): SizeVO? {
        sizeList?.map { sizeVO ->
            if (sizeVO.label == Constants.PHOTO_LABEL_LARGE_SQUARE) {
                return sizeVO
            }
        }
        return null
    }

    fun getLargeImage(): SizeVO? {
        sizeList?.map { sizeVO ->
            if (sizeVO.label == Constants.PHOTO_LABEL_LARGE) {
                return sizeVO
            }
        }
        return null
    }
}