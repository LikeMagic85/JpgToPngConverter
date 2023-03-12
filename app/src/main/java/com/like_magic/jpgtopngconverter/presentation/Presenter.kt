package com.like_magic.jpgtopngconverter.presentation

import android.graphics.Bitmap
import com.like_magic.jpgtopngconverter.data.ConverterRepositoryImpl
import com.like_magic.jpgtopngconverter.domain.usecases.ConvertUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Presenter : ConverterContract.Presenter {

    private val repository = ConverterRepositoryImpl()
    private val convertUseCase = ConvertUseCase(repository)
    private var view: ConverterContract.View? = null
    private val converterDisposable = CompositeDisposable()


    override fun convert(bitmap: Bitmap, pathToBitmap: String) {
        view?.showSnackBar(IN_PROGRESS, 0)
        val disposable = convertUseCase(bitmap, pathToBitmap)
            .subscribeOn(Schedulers.io())
            .delay(3000, TimeUnit.MILLISECONDS)
            .subscribe({
                view?.setNewImage(it.second)
                view?.showSnackBar(DONE, 1)
            }, {
                view?.showSnackBar(it.message!!, 2)
            })
        converterDisposable.add(disposable)
    }


    override fun attach(view: ConverterContract.View) {
        this.view = view
    }

    override fun cancel() {
        converterDisposable.dispose()
    }

    override fun detach() {
        view = null
        converterDisposable.dispose()
    }

    companion object {
        const val IN_PROGRESS = "In progress"
        const val DONE = "Done"
    }
}