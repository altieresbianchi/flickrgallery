package br.com.phaneronsoft.flickergallery.model.vo

import java.io.Serializable

data class PhotoSizeResponse(
    val sizes: SizeListVO,
    val stat: String
) : Serializable