package com.bs.ecommerce.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
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
            .setVibrate(longArrayOf(0))
            .setContentIntent(pendingIntent)

        val mNotificationManager = MyApplication.mAppContext?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            var notificationChannel = mNotificationManager.getNotificationChannel("123")

            if(notificationChannel == null)
            {
                val importance = NotificationManager.IMPORTANCE_HIGH
                notificationChannel = NotificationChannel("123", "123", importance)

                notificationChannel.enableVibration(false)
                notificationBuilder.setChannelId("123")
                mNotificationManager.createNotificationChannel(notificationChannel)
            }

        }

        mNotificationManager.notify(123, notificationBuilder.build())

    }

    fun processFileForUploading(uri: Uri, activity: FragmentActivity): FileWithMimeType?{
        try {
            val contentResolver: ContentResolver = activity.contentResolver

            val mimeType: String? = contentResolver.getType(uri)
            val returnCursor: Cursor? = contentResolver.query(uri, null, null, null, null)

            val nameIndex: Int? = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex: Int? = returnCursor?.getColumnIndex(OpenableColumns.SIZE)

            returnCursor?.moveToFirst()

            var filename = nameIndex?.let { i -> returnCursor.getString(i) }
            val fileSize = sizeIndex?.let { i -> returnCursor.getString(i) }

            returnCursor?.close()

            val fileSizeInt = if(fileSize?.isNumeric() == true)
                fileSize.toInt() else 0

            /*if(fileSizeInt > 2000000) {
                blockingLoader.hideDialog()
                toast(DbHelper.getStringWithNumber(Const.COMMON_MAX_FILE_SIZE, "2000000"))
                return
            }*/


            // File Input Stream gets me file data
            val inputStream: InputStream? = contentResolver.openInputStream(uri)

            val buffer = ByteArray(inputStream?.available() ?: 0)
            inputStream?.read(buffer)

            val extension = filename?.substring(filename.lastIndexOf(".")) ?: "tmp"
            filename = filename?.replace(extension, "")

            val file: File = File.createTempFile(filename ?: "temp", extension)
            val outStream: OutputStream = FileOutputStream(file)
            outStream.write(buffer)

            inputStream?.close()
            outStream.close()

            return FileWithMimeType(file, mimeType)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}