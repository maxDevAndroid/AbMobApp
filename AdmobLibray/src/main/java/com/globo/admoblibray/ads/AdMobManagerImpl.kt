import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.globo.admoblibray.ads.IAdMobManager
import com.globo.admoblibray.model.AdEvent
import com.globo.admoblibray.model.AdType
import com.globo.admoblibray.ui.AdBanner
import com.globo.admoblibray.ui.CustomAdInternal
import com.globo.admoblibray.ui.InterstitialAdInternal
import com.globo.admoblibray.ui.RewardedAdInternal
import com.google.android.gms.ads.MobileAds

internal class AdMobManagerImpl : IAdMobManager {

    override fun initialize(context: Context) {
        MobileAds.initialize(context)
    }

    @Composable
    override fun init(
        adUnitId: String,
        adType: AdType,
        modifier: Modifier,
        onEvent: ((AdEvent) -> Unit)?
    ) {
        when (adType) {
            is AdType.Banner -> AdBanner(adUnitId, adType ,modifier, onEvent)
            is AdType.Interstitial -> onEvent?.let {
                InterstitialAdInternal(adUnitId, it)
            }

            is AdType.Rewarded -> onEvent?.let {
                RewardedAdInternal(adUnitId, it)
            }

            is AdType.Custom -> onEvent?.let {
                CustomAdInternal(adUnitId, adType.adSize, it)
            }
        }
    }
}
