package br.com.phaneronsoft.flickergallery.model.vo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SizeListVO(
    @SerializedName("canblog") val canBlog: Int,
    @SerializedName("canprint") val canPrint: Int,
    @SerializedName("candownload") val canDownload: Int,
    @SerializedName("size") val sizeList: List<SizeVO>
) : Serializable