package com.redcrew.redcrew

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_qr.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.roundToInt

/**
 * Created by Gökberk Erüst on 1.12.2018.
 *
 */
class QrActivity : AppCompatActivity() {

    private var barcodeDetector: BarcodeDetector? = null
    private var toast: Toast? = null
    private var cameraSource: CameraSource? = null
    private val codeScanned = AtomicBoolean(false)

    override fun onResume() {
        super.onResume()
        initLayout()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)
        initLayout()
        initCameraSource()
        startBarcodeDetection()
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

    private fun onSurfaceCreated(){
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
                            // presenter?.onQRCodeDetected(it.valueAt(0).displayValue)
                            barcodeDetector?.release()
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

    override fun onDestroy() {
        releaseBarcodeDetection()
        qrReaderPreviewLayout.holder.surface.release()
        super.onDestroy()
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
            requestPermissions(arrayOf(Manifest.permission.CAMERA),23)
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            onCameraPermissionDenied(isNeverShowAgainChecked = !shouldShowRequestPermissionRationale(permissions.first()))
                        }
                    }
                }
            }
        }

    }

    private fun onCameraPermissionGranted() {
        initCameraSource()
        startBarcodeDetection()
    }

    private fun onCameraPermissionDenied(isNeverShowAgainChecked: Boolean) {

        var buttonText: String? = ""
        var infoText: String? = ""
        if (isNeverShowAgainChecked) {
            infoText = getString(R.string.qr_reader_camera_denied_permanently_permission_info)
        } else {
            infoText = getString(R.string.qr_reader_camera_denied_permission_info)
            buttonText = getString(R.string.qr_reader_camera_allow_permission_button)
        }
        showCameraPermissionInfo(infoText = infoText, buttonText = buttonText)

    }

    private fun showCameraPermissionInfo(infoText: String?, buttonText: String?) {

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

    companion object {

        private const val CAMERA_PERMISSION_KEY = 1224

        fun newIntent(context: Context): Intent {
            return Intent(context, QrActivity::class.java)
        }
    }
}