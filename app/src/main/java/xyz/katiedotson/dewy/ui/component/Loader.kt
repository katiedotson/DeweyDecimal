package xyz.katiedotson.dewy.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import xyz.katiedotson.dewy.R

@Composable
fun Loader(
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible
    ) {
        Dialog(
            onDismissRequest = {}
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))
            LottieAnimation(
                iterations = LottieConstants.IterateForever,
                composition = composition,
            )
        }
    }
}

@Composable
@PreviewLightDark
fun LoaderPreview() {
    Surface {
        Loader(
            isVisible = true
        )
    }
}
