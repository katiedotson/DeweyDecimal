package xyz.katiedotson.deweydecimal

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import xyz.katiedotson.deweydecimal.ui.theme.DeweyDecimalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DeweyDecimalTheme {
                MainComponent()
//                Button({ enableCameraPerms() }) { }
            }
        }
    }

    private fun enableCameraPerms() {
        ActivityCompat.requestPermissions(
            this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            android.Manifest.permission.CAMERA
        ).toTypedArray()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted().not()) {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_LONG).show()
            }
        }
    }
}

