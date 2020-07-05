package com.bs.ecommerce.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.bs.ecommerce.BuildConfig
import com.bs.ecommerce.MyApplication
import com.bs.ecommerce.R
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
                fileName = fileName.replace("filename=", "").replace("\"", "")
            }

            if(fileName == null) fileName = filenameHint
            val myFile: File? = File(
                Environment.getExternalStorageDirectory(),
                Environment.DIRECTORY_DOWNLOADS + File.separator + fileName
            )


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
                inputStream?.close()
                outputStream.close()

                val uri = FileProvider.getUriForFile(
                    MyApplication.mAppContext!!,
                    BuildConfig.APPLICATION_ID + ".provider",
                    myFile!!
                )

                val mimeType: String = response.headers()?.get("content-type") ?: "*/*"

                showDownloadCompleteNotification(uri, mimeType, fileName)

                return true

            } catch (e: IOException) {
                "file".showLog(e.toString())
                inputStream?.close()
                outputStream?.close()

                false
            }
        } catch (e: IOException) {
            false
        }
    }

    private fun showDownloadCompleteNotification(uri: Uri, mimeType: String, fileName: String)
    {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, mimeType)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val pendingIntent = PendingIntent.getActivity(
            MyApplication.mAppContext,
            123  ,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )


        val notificationBuilder = NotificationCompat.Builder(MyApplication.mAppContext!!, "123")
        notificationBuilder.setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("$fileName downloaded")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val mNotificationManager = MyApplication.mAppContext?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            var notificationChannel = mNotificationManager.getNotificationChannel("123")

            if(notificationChannel == null)
            {
                val importance = NotificationManager.IMPORTANCE_HIGH
                notificationChannel = NotificationChannel("123", "123", importance)

                notificationChannel.enableVibration(true)
                notificationBuilder.setChannelId("123")
                mNotificationManager.createNotificationChannel(notificationChannel)
            }

        }

        mNotificationManager.notify(123, notificationBuilder.build())

    }
}