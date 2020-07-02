package com.bs.ecommerce.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Environment
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*
import java.util.regex.Matcher
import java.util.regex.Pattern


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
        filenameHint: String,
        response: Response<ResponseBody>
    ): Boolean {
        return try {

            var fileName: String? = null
            val regex: Pattern = Pattern.compile("filename[^;=\\n]*=((['\"]).*?\\2|[^;\\n]*)")
            val regexMatcher: Matcher = regex.matcher(response.headers()?.get("content-disposition") ?: "")
            if (regexMatcher.find()) {
                fileName = regexMatcher.group()
                fileName = fileName.replace("filename=", "")
            }

            if(fileName == null) fileName = filenameHint
            var myFile: File? = File(
                Environment.getExternalStorageDirectory(),
                Environment.DIRECTORY_DOWNLOADS + File.separator + fileName
            )

            /*val downLoadDir = ContextCompat.getExternalFilesDirs(ctx, null)

            if(!downLoadDir.isNullOrEmpty())
                myFile = File(downLoadDir[0], filename)*/

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                // val fileSize = response.body()?.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = response.body()?.byteStream()
                outputStream = FileOutputStream(myFile)

                while (true) {
                    val read = inputStream?.read(fileReader) ?: -1
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
                "file".showLog(e.toString())
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