package org.hango.com.Data

import android.net.Uri

data class UserInfoData(
    var userID: String,
    var userName: String,
    var userProfile: String,
    var userMbti: String
) {
    var profileUri: Uri? = null

}