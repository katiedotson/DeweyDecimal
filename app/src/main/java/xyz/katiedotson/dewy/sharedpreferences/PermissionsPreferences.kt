package xyz.katiedotson.dewy.sharedpreferences

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

interface PermissionsPreferences {
    var cameraPermissionRequested: Boolean
}

class PermissionsPreferencesImpl @Inject constructor(
    context: Context
) : PermissionsPreferences {

    private val cameraPermissionsRequestedKey = "cameraPermissionsRequested"
    private val sharedPreferences = context.getSharedPreferences(
        "xyz.katiedotson.dewy.PermissionsPreferences",
        Context.MODE_PRIVATE
    )

    override var cameraPermissionRequested: Boolean
        set(value) {
            sharedPreferences.edit().putBoolean(cameraPermissionsRequestedKey, value).apply()
        }
        get() = sharedPreferences.getBoolean(cameraPermissionsRequestedKey, false)
}

@Module
@InstallIn(SingletonComponent::class)
object PermissionsPreferencesModule {

    @Provides
    fun providePermissionsPreferences(@ApplicationContext context: Context): PermissionsPreferences {
        return PermissionsPreferencesImpl(context)
    }
}
