package org.hango.com.Data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class TravelViewModel : ViewModel() {
    private var travelItems: MutableLiveData<ArrayList<Travel>>? = null
    private var userInfoItem: MutableLiveData<UserInfoData>? = null

    var list: ArrayList<Travel> = ArrayList()
        private set
    val travelLiveItems: LiveData<ArrayList<Travel>>
        get() {
            if (travelItems == null) travelItems = MutableLiveData()
            return travelItems!!
        }

    fun add(item: Travel) {
        list.add(item)
        if (travelItems == null) travelLiveItems
        travelItems!!.setValue(list)
    }

    fun deleteList() {
        list = ArrayList()
    }

    val userinfo: MutableLiveData<UserInfoData>?
        get() {
            if (userInfoItem == null) userInfoItem = MutableLiveData<UserInfoData>()
            return userInfoItem
        }

    fun setLiveItems(item: UserInfoData) {
        if (userInfoItem == null) userinfo
        userInfoItem!!.setValue(item)
    }
}