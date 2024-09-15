package com.onecat.godottenjin;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import com.tenjin.android.Callback;
import com.tenjin.android.TenjinSDK;

import java.lang.Exception;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class GodotTenjin extends GodotPlugin
{
	private final Activity activity;
    private String apiKey = null;

    public GodotTenjin(Godot godot)
    {
        super(godot);
        activity = getActivity();
    }

    private TenjinSDK getTenjinInstance() {
        return TenjinSDK.getInstance(activity, apiKey);
    }

    @Nullable
    @Override
    public View onMainCreate(Activity activity) {
        return null;
    }

/*    @Override
    public void onMainResume() {

        if (apiKey != null && activity != null) {
            TenjinSDK instance = getTenjinInstance();
            instance.connect();
        }
    }

 */

    @NonNull
    @Override
    public String getPluginName() {
        return "GodotTenjin";
    }

    @NonNull
    @Override
    public Set<SignalInfo> getPluginSignals() {
        Set<SignalInfo> signalInfoSet = new HashSet<>();
        signalInfoSet.add(new SignalInfo("plugin_initialized"));
        signalInfoSet.add(new SignalInfo("gaid_obtain_failed", String.class));
        return  signalInfoSet;
    }

    @UsedByGodot
    public void initialize(final String _apiKey, final String _deepLinkUri, final int appstoreType) {
        apiKey = _apiKey;
        TenjinSDK instance = TenjinSDK.getInstance(activity, apiKey);
        TenjinSDK.AppStoreType _appstoretype = switch (appstoreType) {
            case 1 -> TenjinSDK.AppStoreType.googleplay;
            case 2 -> TenjinSDK.AppStoreType.amazon;
            case 3 -> TenjinSDK.AppStoreType.other;
            default -> TenjinSDK.AppStoreType.unspecified;
        };
        instance.setAppStore(_appstoretype);
        if(_deepLinkUri != null && !_deepLinkUri.isEmpty()) {
            instance.connect(_deepLinkUri);
        } else {
            instance.connect();
        }
        emitSignal("plugin_initialized");
        instance.getDeeplink(new Callback() {
            @Override
            public void onSuccess(boolean clickedTenjinLink, boolean isFirstSession, Map<String, String> data) {
                if (clickedTenjinLink) {
                    if (isFirstSession) {
                        if (data.containsKey(TenjinSDK.DEEPLINK_URL)) {
                           // deferred_deeplink_url logic here
                        }
                    }
                }
            }
        });
    }

    @UsedByGodot
    public void logEvent(final String event) {
        if(apiKey != null)
            getTenjinInstance().eventWithName(event);
    }

    @UsedByGodot
    public void logEventWithValue(final String event, final int value) {
        if(apiKey != null)
            getTenjinInstance().eventWithNameAndValue(event, value);
    }

    @UsedByGodot
    public void logPurchase(final String productId, final String currencyCode, final int quantity, final float unitPrice) {
        if(apiKey != null)
            getTenjinInstance().transaction(productId, currencyCode, quantity, unitPrice);
    }

    @UsedByGodot
    public void logPurchaseWithSignature(final String productId, final String currencyCode, final int quantity, final float unitPrice, final String purchaseData, final String dataSignature) {
        if(apiKey != null)
            getTenjinInstance().transaction(productId, currencyCode, quantity, unitPrice, purchaseData, dataSignature);
    }

    @UsedByGodot
    public String getGAID()
    {
        Info adInfo;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(activity);
            return adInfo.getId();
        } catch (Exception e) {
            emitSignal("gaid_obtain_failed", e.toString());
        }
        return "";
    }

}
