package com.redcrew.redcrew

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_qr.*
import java.util.concurrent.atomic.AtomicBoolean

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
        setContentView(R.layout.activity_qr)
        initLayout()
    }

    private fun initLayout() {
        barcodeDetector = BarcodeDetector.Builder(applicationContext)
            .setBarcodeFormats(Barcode.QR_CODE).build()

        qrReaderSquareView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                // squareHeight - 2 * border - itemHeight
                /* val translationY = (qrReaderSquareView.height - qrReaderLaserView.height
                        - (resources.getDimensionPixelSize(R.dimen.qr_square_border_size) * 2)).toFloat()

                animateLaserView(translationY)
                qrReaderSquareView.viewTreeObserver.removeOnGlobalLayoutListener(this) */
            }
        })

        qrReaderPreviewLayout.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                // presenter?.onSurfaceCreated()
            }
        })


    }

    fun startBarcodeDetection() {
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


    fun initCameraSource() {

        val width = qrReaderPreviewLayout.width
        val height = qrReaderPreviewLayout.height
        val ratio =
            VLDeviceScreenUtil.getScreenHeight(applicationContext).toDouble() / VLDeviceScreenUtil.getScreenWidth(it).toDouble()
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
        qrReaderPreviewLayout.holder.surface.release()
        super.onDestroy()
    }


    fun releaseBarcodeDetection() {
        runOnUiThread {
            cameraSource?.release()
            barcodeDetector?.release()
        }
    }

    fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun showCameraPermissionExplanationDialog(onPositiveButtonClicked: () -> Unit) {

    }

    fun requestForCameraPermission() {
        // requestPermissions(arrayOf(Manifest.permission.CAMERA),23)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            // ToslaKeyStore.TOSLA_PERMISSIONS_REQUEST_CAMERA -> {
            /*val result = grantResults.firstOrNull()
        when (result) {
            PackageManager.PERMISSION_GRANTED -> {
                //presenter?.onCameraPermissionGranted()
            }
            PackageManager.PERMISSION_DENIED -> {
                // presenter?.onCameraPermissionDenied(
                //   isNeverShowAgainChecked = !shouldShowRequestPermissionRationale(permissions.first())
                )
            }
        } */
        }

    }

    fun showCameraPermissionInfo(infoText: String?, buttonText: String?) {

    }

    private fun animateLaserView(translationY: Float) {

        val duration = (translationY * 2.5).toLong()
        qrReaderLaserView?.animate()?.translationY(translationY)?.setDuration(duration)
            ?.setInterpolator(LinearInterpolator())
            ?.setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    qrReaderLaserView?.animate()?.translationY(0f)?.setDuration(duration)
                        ?.setInterpolator(LinearInterpolator())
                        ?.setListener(object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animation: Animator?) {

                            }

                            override fun onAnimationEnd(animation: Animator?) {
                                animateLaserView(translationY)
                            }

                            override fun onAnimationCancel(animation: Animator?) {

                            }

                            override fun onAnimationStart(animation: Animator?) {

                            }
                        })
                        ?.start()
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })
            ?.start()
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


    }
}