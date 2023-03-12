package com.like_magic.jpgtopngconverter.domain

import android.graphics.Bitmap
import io.reactivex.Single

interface ConverterRepository {
    fun convert(bitmap: Bitmap, pathToBitmap: String): Single<Pair<String, Bitmap>>
}