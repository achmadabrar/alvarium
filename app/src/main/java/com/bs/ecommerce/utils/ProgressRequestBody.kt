package com.bs.ecommerce.utils

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class ProgressRequestBody(
    file: File,
    contentType: String?,
    listener: ProgressCallback
) :
    RequestBody() {
    private val file: File
    private val contentType: String

    interface ProgressCallback {
        fun onProgress(progress: Long, total: Long)
    }

    private val mListener: ProgressCallback
    override fun contentType(): MediaType? {
        return contentType.toMediaTypeOrNull()
        // NOTE: We are posting the upload as binary data so we don't need the true mimeType
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = file.length()
        val buffer =
            ByteArray(UPLOAD_PROGRESS_BUFFER_SIZE)
        val `in`: InputStream = FileInputStream(file)
        var uploaded: Long = 0
        try {
            var read: Int
            while (`in`.read(buffer).also { read = it } != -1) {
                mListener.onProgress(uploaded, fileLength)
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }
        } finally {
            `in`.close()
        }
    }

    /**
     * WARNING: You must override this function and return the file size or you will get errors
     */
    @Throws(IOException::class)
    override fun contentLength(): Long {
        return file.length()
    }

    companion object {
        private val LOG_TAG = ProgressRequestBody::class.java.simpleName
        private const val UPLOAD_PROGRESS_BUFFER_SIZE = 8192
    }

    init {
        var contentType = contentType
        if (contentType == null) contentType = "application/unknown"
        this.file = file
        mListener = listener
        this.contentType = contentType
    }
}
