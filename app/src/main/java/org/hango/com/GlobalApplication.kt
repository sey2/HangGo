package org.hango.com
import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "fc840073cb80861950802cae8d4b0237")
    }
}