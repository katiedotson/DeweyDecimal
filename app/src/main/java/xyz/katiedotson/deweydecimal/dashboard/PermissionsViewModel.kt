package xyz.katiedotson.deweydecimal.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class PermissionsViewModel @Inject constructor() : ViewModel() {

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
        val newValue = if (permissionWasGranted) PermissionEvent.Granted else PermissionEvent.Denied
        _permissionEventState.update {
            newValue
        }
    }

    fun validateCameraPermission(permissionIsGranted: Boolean, shouldShowRationale: Boolean) {
        val update = if (permissionIsGranted) {
            PermissionEvent.Granted
        } else if (shouldShowRationale.not()) {
            PermissionEvent.ShowFinalRationale
        } else if (shouldShowRationale) {
            PermissionEvent.ShowInitialRationale
        } else {
            PermissionEvent.LaunchRequest
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
