package com.bs.ecommerce.more.barcode


import android.graphics.PixelFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bs.ecommerce.R
import com.bs.ecommerce.auth.register.RegistrationViewModel
import com.bs.ecommerce.base.BaseFragment
import com.bs.ecommerce.base.BaseViewModel
import com.bs.ecommerce.db.DbHelper
import com.bs.ecommerce.product.ProductDetailFragment
import com.bs.ecommerce.utils.Const
import com.bs.ecommerce.utils.isNumeric
import com.bs.ecommerce.utils.replaceFragmentSafely
import com.bs.ecommerce.utils.toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.android.synthetic.main.fragment_barcode_capture.*


class BarCodeCaptureFragment : BaseFragment()
{
    override fun getFragmentTitle() = DbHelper.getString(Const.MORE_SCAN_BARCODE)

    override fun getLayoutId(): Int = R.layout.fragment_barcode_capture

    override fun getRootLayout(): RelativeLayout? = barcode_root_layout

    override fun createViewModel(): BaseViewModel = RegistrationViewModel()

    private var beepManager: BeepManager? = null

    private var lastText: String = ""



    private val callback: BarcodeCallback = object : BarcodeCallback {

        override fun barcodeResult(result: BarcodeResult)
        {

            if(result.text == null || result.text == lastText)
                return


            lastText = result.text

            barcodeView?.setStatusText(result.text)
            beepManager?.playBeepSoundAndVibrate()


            if(result.text.toString().isNumeric())
                replaceFragmentSafely(ProductDetailFragment.newInstance(result.text.toLong(), null))
            else
                toast(DbHelper.getString(Const.INVALID_PRODUCT))
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }


    private fun initializeBarcodeView()
    {
        val formats: Collection<BarcodeFormat> = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.EAN_13, BarcodeFormat.EAN_8, BarcodeFormat.CODE_128)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.initializeFromIntent(activity?.intent)
        barcodeView.decodeContinuous(callback)

        //barcodeView.setStatusText(getString(R.string.place_barcode_in_camera))

        beepManager = BeepManager(activity)
        beepManager?.isVibrateEnabled = true


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

            = inflater.inflate(R.layout.fragment_barcode_capture, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        initializeBarcodeView()

        surfaceView.setZOrderOnTop(true)
        val sfhTrackHolder = surfaceView.holder
        sfhTrackHolder.setFormat(PixelFormat.TRANSPARENT)



    }


    override fun onResume()
    {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause()
    {
        super.onPause()
        barcodeView.pause()
    }

}
