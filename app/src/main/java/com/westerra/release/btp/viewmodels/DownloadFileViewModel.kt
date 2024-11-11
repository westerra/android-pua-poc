package com.westerra.release.btp.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.westerra.release.btp.model.ApiResponseHolder
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadFileViewModel : ViewModel() {
    companion object {
        private val TAG = DownloadFileViewModel::class.java.simpleName
    }

    val responseFile = MutableLiveData<ApiResponseHolder<File>>()

    fun downloadFile(url: String, file: File) {
        CoroutineScope(Dispatchers.IO).launch {
            @Suppress("BlockingMethodInNonBlockingContext")
            try {
                URL(url).openStream().use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        responseFile.value = ApiResponseHolder(data = file, errorMessage = null)
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Unexpected exception downloading file from $url: ${e.message}")
                CoroutineScope(Dispatchers.Main).launch {
                    responseFile.value = ApiResponseHolder(data = null, errorMessage = null)
                }
            }
        }
    }
}
