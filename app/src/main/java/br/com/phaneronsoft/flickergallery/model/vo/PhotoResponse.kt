package br.com.phaneronsoft.flickergallery.model.vo

import java.io.Serializable

data class PhotoResponse(
    val photos: PhotoListVO,
    val stat: String,
) : Serializable