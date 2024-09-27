package xyz.katiedotson.dewy.search

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import xyz.katiedotson.dewy.sharedpreferences.PermissionsPreferences
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val permissionsPreferences: PermissionsPreferences
) : ViewModel() {

    private val _permissionEventState = MutableStateFlow<PermissionEvent>(PermissionEvent.None)
    val permissionEventState = _permissionEventState.asStateFlow()

    sealed class PermissionEvent {
        data object None : PermissionEvent()
        data object ShowInitialRationale : PermissionEvent()
        data object ShowFinalRationale : PermissionEvent()
        data object Denied : PermissionEvent()
        data object Granted : PermissionEvent()
        data object LaunchRequest : PermissionEvent()
    }

    fun onPermissionResult(permissionWasGranted: Boolean) {
        permissionsPreferences.cameraPermissionRequested = true
        val newValue = if (permissionWasGranted) PermissionEvent.Granted else PermissionEvent.Denied
        _permissionEventState.update {
            newValue
        }
    }

    fun validateCameraPermission(permissionIsGranted: Boolean, shouldShowRationale: Boolean) {
        // note: this currently isn't handling "ask everytime" properly :/
        val update = when {
            permissionIsGranted -> PermissionEvent.Granted
            permissionsPreferences.cameraPermissionRequested.not() -> PermissionEvent.LaunchRequest
            shouldShowRationale.not() && permissionsPreferences.cameraPermissionRequested -> {
                PermissionEvent.ShowFinalRationale
            }
            shouldShowRationale -> PermissionEvent.ShowInitialRationale
            else -> PermissionEvent.LaunchRequest
        }
        _permissionEventState.update {
            update
        }
    }

    fun permissionEventHandled() {
        _permissionEventState.update {
            PermissionEvent.None
        }
    }
}
