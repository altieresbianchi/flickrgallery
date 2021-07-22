package br.com.phaneronsoft.flickergallery.model.vo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PhotoListVO(
    val page: Int,
    val pages: Int,
    @SerializedName("perpage") val perPage: Int,
    val total: Int,
    @SerializedName("photo") val photoList: List<PhotoVO>
) : Serializable