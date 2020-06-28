package com.bs.ecommerce.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Environment
import okhttp3.ResponseBody
import java.io.*


class Utils {

    /**
     * Checks internet connectivity through wifi or mobile network
     *
     * @return `true` if wifi or mobile internet is available
     */
    fun isWifiOrMobileNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo

        return !(networkInfo == null || !networkInfo.isConnected ||
                (networkInfo.type != ConnectivityManager.TYPE_WIFI
                        && networkInfo.type != ConnectivityManager.TYPE_MOBILE))
    }

    fun writeResponseBodyToDisk(
        ctx: Context?,
        fileName: String,
        body: ResponseBody
    ): Boolean {
        return try {

            val fullFileName = fileName.plus(".").plus(body.contentType()?.subtype ?: "txt")
            var myFile: File? = File(
                Environment.getExternalStorageDirectory(),
                Environment.DIRECTORY_DOWNLOADS + File.separator + fullFileName
            )

            /*val downLoadDir = ContextCompat.getExternalFilesDirs(ctx, null)

            if(!downLoadDir.isNullOrEmpty())
                myFile = File(downLoadDir[0], filename)*/

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = FileOutputStream(myFile)

                while (true) {
                    val read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    //Log.d(TAG, "file download: $fileSizeDownloaded of $fileSize")
                }

                outputStream.flush()
                true

            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }
}