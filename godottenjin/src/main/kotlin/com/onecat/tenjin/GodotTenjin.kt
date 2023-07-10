package com.onecat.tenjin

import android.app.Activity
import android.util.Log
import android.view.View
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.tenjin.android.TenjinSDK
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.UsedByGodot
import java.io.IOException

class GodotTenjin(activity:Godot) : GodotPlugin(activity) {
    private var apiKey : String? = null

    override fun getPluginName() : String {
        return "GodotTenjin"
    }

    @UsedByGodot
    fun getAdID() : String? {
        val adInfo : Info?
        try {
            adInfo = activity?.let { AdvertisingIdClient.getAdvertisingIdInfo(it) }
            return adInfo?.id
        } catch (e: IOException) {
            Log.e("godot", e.toString())
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.e("godot", e.toString())
        } catch (e: Exception) {
            Log.e("godot", e.toString())
        }
        return ""
    }

    @UsedByGodot
    fun initializePlugin(_apiKey : String, _deepLinkUri : String) {
        apiKey = _apiKey
        val sdkInstance : TenjinSDK = TenjinSDK.getInstance(activity, apiKey)
        sdkInstance.setAppStore(TenjinSDK.AppStoreType.googleplay)
        if (_deepLinkUri != "") {
            sdkInstance.connect(_deepLinkUri)
        }
        else {
            sdkInstance.connect()
        }

        Log.i("godot", "Godot Tenjin plugin initialized")
        Log.i("godot", "GAID:"+getAdID())
        sdkInstance.getDeeplink { clickedTenjinLink, isFirstSession, data ->
            if (clickedTenjinLink) {
                if (isFirstSession) {
                    if (data.containsKey(TenjinSDK.DEEPLINK_URL)) {
                        // direct to app part here
                    }
                }
            }
        }
    }

    @UsedByGodot
    fun logEvent(event:String) {
        if (apiKey == "" || apiKey == null){
            Log.e("godot", "Tenjin: attempt to logEvent with no API key, did nothing")
            return
        }
        tenjinInstance().eventWithName(event)
    }

    @UsedByGodot
    fun logEventWithValue(event:String, value:Int) {
        if (apiKey == "" || apiKey == null){
            Log.e("godot", "Tenjin: attempt to logEventWithValue with no API key, did nothing")
            return
        }
        tenjinInstance().eventWithNameAndValue(event, value)
    }

    @UsedByGodot
    fun logPurchase(productId:String, currencyCode:String, quantity:Int, unitPrice:Double) {
        if (apiKey == "" || apiKey == null){
            Log.e("godot", "Tenjin: attempt to logPurchase an event with no API key, did nothing")
            return
        }
        tenjinInstance().transaction(productId, currencyCode, quantity, unitPrice)
    }

    @UsedByGodot
    fun logPurchaseWithSignature(
        productId:String,
        currencyCode:String,
        quantity:Int, unitPrice:Double,
        purchaseData:String,
        dataSignature:String) {
        tenjinInstance().transaction(productId, currencyCode, quantity, unitPrice, purchaseData, dataSignature)
    }

    private fun tenjinInstance() : TenjinSDK {
        return TenjinSDK.getInstance(activity, apiKey)
    }
}
