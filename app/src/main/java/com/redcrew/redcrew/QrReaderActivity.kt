package com.redcrew.redcrew

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.redcrew.redcrew.utils.DeviceScreenUtil
import kotlinx.android.synthetic.main.activity_qr.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.roundToInt

/**
 * Created by Gökberk Erüst on 1.12.2018.
 *
 */
class QrReaderActivity : AppCompatActivity() {

    private var barcodeDetector: BarcodeDetector? = null
    private var toast: Toast? = null
    private var cameraSource: CameraSource? = null
    private val codeScanned = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)
    }

    override fun onStart() {
        super.onStart()
        initLayout()
        initCameraSource()
        startBarcodeDetection()
    }

    override fun onResume() {
        super.onResume()
        initLayout()
    }

    override fun onDestroy() {
        releaseBarcodeDetection()
        qrReaderPreviewLayout.holder.surface.release()
        super.onDestroy()
    }

    private fun initLayout() {
        barcodeDetector = BarcodeDetector.Builder(applicationContext)
            .setBarcodeFormats(Barcode.QR_CODE).build()

        qrReaderPreviewLayout.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                onSurfaceCreated()
            }
        })

    }

    private fun allowInternetQr() {
        internettext.setTypeface(null, Typeface.BOLD)
        internetImage.setImageResource(R.drawable.check)
    }

    private fun denyInternetQr() {
        internettext.setTypeface(null, Typeface.NORMAL)
        internetImage.setImageResource(R.drawable.cross)
    }

    private fun onSurfaceCreated() {
        if (!isCameraPermissionGranted()) {
            requestForCameraPermission()
        } else {
            onCameraPermissionGranted()
        }
    }

    private fun startBarcodeDetection() {
        codeScanned.set(false)

        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                detections?.detectedItems?.let {
                    if (it.size() > 0 && !codeScanned.getAndSet(true)) {
                        runOnUiThread {
                            onQrCodeDetected(it.valueAt(0).displayValue)
                        }
                    }
                }
            }

        })
    }


    private fun initCameraSource() {

        val width = 180
        val height = qrReaderPreviewLayout.height
        val ratio =
            DeviceScreenUtil.getScreenHeight(applicationContext).toDouble() / DeviceScreenUtil.getScreenWidth(
                applicationContext
            ).toDouble()
        cameraSource = CameraSource.Builder(applicationContext, barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(width, (width * ratio).roundToInt())
            .build()

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                cameraSource?.start(qrReaderPreviewLayout.holder)
            } catch (exception: Exception) {

            }
        }

    }

    private fun releaseBarcodeDetection() {
        runOnUiThread {
            cameraSource?.release()
            barcodeDetector?.release()
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestForCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 23)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_KEY -> {
                val result = grantResults.firstOrNull()
                when (result) {
                    PackageManager.PERMISSION_GRANTED -> {
                        onCameraPermissionGranted()
                    }
                    PackageManager.PERMISSION_DENIED -> {
                    }
                }
            }
        }

    }

    private fun onCameraPermissionGranted() {
        initCameraSource()
        startBarcodeDetection()
    }

    private fun onQrCodeDetected(code: String) {
        println("QR CODE: $code")
        val qrCode = when (code) {
            QrCodes.Donate.text -> QrCodes.Donate
            QrCodes.Internet1.text -> QrCodes.Internet1
            QrCodes.Internet2.text -> QrCodes.Internet2
            QrCodes.Internet3.text -> QrCodes.Internet3
            QrCodes.Tariff.text -> QrCodes.Tariff
            QrCodes.SMS.text -> QrCodes.SMS
            else -> QrCodes.UnRecognized
        }
        if (qrCode != QrCodes.UnRecognized) {
            barcodeDetector?.release()
            startActivity(ApprovalActivity.newIntent(applicationContext, qrCode))
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        } else {
            displayErrorToast()
            startBarcodeDetection()
        }
    }

    fun removeToastMessage() {
        toast?.cancel()
    }

    @SuppressLint("ShowToast")
    fun displayErrorToast() {
        if (toast == null) {
            toast = Toast.makeText(
                applicationContext, "Hatalı bir QR okuttunuz.",
                Toast.LENGTH_SHORT
            )
        }
        toast?.show()
    }

    enum class QrCodes(val text: String) {
        Donate("Bağış"),
        Internet1("internet1"),
        Internet2("internet2"),
        Internet3("internet3"),
        Tariff("Paket"),
        SMS("SMS"),
        UnRecognized("unRecognized")
    }

    companion object {

        private const val CAMERA_PERMISSION_KEY = 1224

        fun newIntent(context: Context): Intent {
            return Intent(context, QrReaderActivity::class.java)
        }
    }
}