package com.example.pangchat.fragment.data

import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.core.DataPart
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result as fuelResult

data class UploadResult(
    val url: String,
    val success: Boolean,
    val time: String
)


class FileDataSource {
    data class fileUpload(val file: DataPart)

    fun uploadFile(file: DataPart): Result<UploadResult> {
        val param = fileUpload(file)
        val (_, _, result) = CookiedFuel.upload("/test/upload").add(file).responseObject<UploadResult>()
        if (result is fuelResult.Failure<*>) {
            return Result.Error(result.getException())
        } else {
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }

    // data class fileDownload(val filename: String)

//    fun downloadFile(filename: String): Result<DownloadResult> {
//        val param = fileDownload(filename)
//        val (_, _, result) = CookiedFuel.get("/test/url").jsonBody(param).responseObject<DownloadResult>()
//        if (result is fuelResult.Failure<*>) {
//            return Result.Error(result.getException())
//        } else {
//            return if (result.get().success)
//                Result.Success(result.get())
//            else Result.Error(Exception());
//        }
//
//    }

}