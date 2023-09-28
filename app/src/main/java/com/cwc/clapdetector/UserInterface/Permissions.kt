package com.cwc.clapdetector.UserInterface

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.cwc.clapdetector.databinding.ActivityPermissionsBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class Permissions : AppCompatActivity() {
    private lateinit var binding:ActivityPermissionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            if (areCameraAndAudioPermissionsGranted()) {
                startActivity(Intent(this@Permissions, MainActivity::class.java))
                finish()
            }

            binding.allow.setOnClickListener {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.RECORD_AUDIO
                    )
                )
            }
        }

    }
    fun areCameraAndAudioPermissionsGranted(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        )

        val audioPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.RECORD_AUDIO
        )

        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                audioPermission == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermissions(permissions: Array<String>) {
        Dexter.withContext(this)
            .withPermissions(*permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // Check if both permissions are granted after the request
                    if (report.areAllPermissionsGranted()) {
                        // Permissions are granted, proceed to MainActivity
                        startActivity(Intent(this@Permissions, MainActivity::class.java))
                        finish()
                    }

                    // Check for permanent denial of permissions
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // Handle the case where any permission is permanently denied
                        // You might want to navigate the user to app settings here
                    }
                    showPermissionAlertDialog()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    // Notify the user about the need for permissions and continue the request
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun showPermissionAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permissions Required")
        builder.setMessage("Camera and audio permissions are required for this app. Please allow them in App Settings.")

        builder.setPositiveButton("Go to Settings") { dialog: DialogInterface, which: Int ->
            // Open the app settings screen
            openAppSettings()
        }

        builder.setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
            // Handle cancel
        }

        builder.setCancelable(false)
        builder.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}