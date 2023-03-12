package com.like_magic.jpgtopngconverter.presentation

import android.graphics.Bitmap

interface ConverterContract {

    interface View{
        fun openImage()
        fun showSnackBar(message:String, state:Int)
        fun setNewImage(bitmap: Bitmap)
    }

    interface Presenter{
        fun convert(bitmap: Bitmap, pathToBitmap: String)
        fun cancel()
        fun attach(view:View)
        fun detach()
    }

}