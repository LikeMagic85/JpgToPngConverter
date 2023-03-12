package com.like_magic.jpgtopngconverter.domain.usecases

import android.graphics.Bitmap
import com.like_magic.jpgtopngconverter.domain.ConverterRepository
import io.reactivex.Single

class ConvertUseCase(private val repository: ConverterRepository) {

    operator fun invoke(bitmap: Bitmap, pathToBitmap: String): Single<Pair<String, Bitmap>> {
       return repository.convert(bitmap, pathToBitmap)
    }

}