package com.google.android.gms.internal.measurement;

import android.content.Intent;
import android.os.Bundle;
import android.os.IInterface;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface c2 extends IInterface {
    void beginAdUnitExposure(String str, long j8);

    void clearConditionalUserProperty(String str, String str2, Bundle bundle);

    void clearMeasurementEnabled(long j8);

    void endAdUnitExposure(String str, long j8);

    void generateEventId(h2 h2Var);

    void getAppInstanceId(h2 h2Var);

    void getCachedAppInstanceId(h2 h2Var);

    void getConditionalUserProperties(String str, String str2, h2 h2Var);

    void getCurrentScreenClass(h2 h2Var);

    void getCurrentScreenName(h2 h2Var);

    void getGmpAppId(h2 h2Var);

    void getMaxUserProperties(String str, h2 h2Var);

    void getSessionId(h2 h2Var);

    void getTestFlag(h2 h2Var, int i8);

    void getUserProperties(String str, String str2, boolean z4, h2 h2Var);

    void initForTests(Map map);

    void initialize(x6.a aVar, zzdq zzdqVar, long j8);

    void isDataCollectionEnabled(h2 h2Var);

    void logEvent(String str, String str2, Bundle bundle, boolean z4, boolean z8, long j8);

    void logEventAndBundle(String str, String str2, Bundle bundle, h2 h2Var, long j8);

    void logHealthData(int i8, String str, x6.a aVar, x6.a aVar2, x6.a aVar3);

    void onActivityCreated(x6.a aVar, Bundle bundle, long j8);

    void onActivityDestroyed(x6.a aVar, long j8);

    void onActivityPaused(x6.a aVar, long j8);

    void onActivityResumed(x6.a aVar, long j8);

    void onActivitySaveInstanceState(x6.a aVar, h2 h2Var, long j8);

    void onActivityStarted(x6.a aVar, long j8);

    void onActivityStopped(x6.a aVar, long j8);

    void performAction(Bundle bundle, h2 h2Var, long j8);

    void registerOnMeasurementEventListener(i2 i2Var);

    void resetAnalyticsData(long j8);

    void setConditionalUserProperty(Bundle bundle, long j8);

    void setConsent(Bundle bundle, long j8);

    void setConsentThirdParty(Bundle bundle, long j8);

    void setCurrentScreen(x6.a aVar, String str, String str2, long j8);

    void setDataCollectionEnabled(boolean z4);

    void setDefaultEventParameters(Bundle bundle);

    void setEventInterceptor(i2 i2Var);

    void setInstanceIdProvider(m2 m2Var);

    void setMeasurementEnabled(boolean z4, long j8);

    void setMinimumSessionDuration(long j8);

    void setSessionTimeoutDuration(long j8);

    void setSgtmDebugInfo(Intent intent);

    void setUserId(String str, long j8);

    void setUserProperty(String str, String str2, x6.a aVar, boolean z4, long j8);

    void unregisterOnMeasurementEventListener(i2 i2Var);
}
