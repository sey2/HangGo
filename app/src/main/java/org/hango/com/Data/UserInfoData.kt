package org.hango.com.Data

class UserInfoData(
    var userID: String,
    var userName: String,
    var userProfile: String,
    var userMbti: String
) {
    var profileUri: Uri? = null
    fun setProfileUri(uri: Uri?) {
        profileUri = uri
    }

    fun getProfileUri(): Uri? {
        return profileUri
    }
}