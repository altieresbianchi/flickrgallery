package br.com.phaneronsoft.flickergallery.model.vo

import java.io.Serializable

data class SizeVO(
    val label: String,
    val width: Int,
    val height: Int,
    val source: String,
    val url: String,
    val media: String,
) : Serializable