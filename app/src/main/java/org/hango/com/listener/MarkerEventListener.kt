package org.hango.com.listener

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPOIItem.CalloutBalloonButtonType
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapPoint.GeoCoordinate
import net.daum.mf.map.api.MapView


class MarkerEventListener(private val activity: Activity) : MapView.POIItemEventListener {
    var currentLocation: GeoCoordinate? = null
    var currentLatitude = 0.0
    var currentLongitude = 0.0

    override fun onPOIItemSelected(mapView: MapView?, mapPOIItem: MapPOIItem?) {}

    // is Deprecated.
    override fun onCalloutBalloonOfPOIItemTouched(mapView: MapView?, mapPOIItem: MapPOIItem?) {}

    override fun onCalloutBalloonOfPOIItemTouched(
        mapView: MapView?,
        mapPOIItem: MapPOIItem,
        calloutBalloonButtonType: CalloutBalloonButtonType?
    ) {
        getCurrentLocation()
        val endPoint = mapPOIItem.mapPoint.mapPointGeoCoord
        val uri =
            "kakaomap://route?sp=" + currentLatitude + "," + currentLongitude + "&ep=" + endPoint.latitude + "," + endPoint.longitude + "&by=CAR"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        activity.startActivity(intent)
    }

    override fun onDraggablePOIItemMoved(mapView: MapView?, mapPOIItem: MapPOIItem?, mapPoint: MapPoint?) {}

    fun getCurrentLocation() {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var location: Location? = null

        try {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (e: SecurityException) { }

        currentLatitude = location!!.getLatitude()
        currentLongitude = location!!.getLongitude()
    }
}
