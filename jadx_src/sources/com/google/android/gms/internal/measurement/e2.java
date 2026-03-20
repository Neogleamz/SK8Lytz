package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e2 extends w0 implements c2 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public e2(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.measurement.api.internal.IAppMeasurementDynamiteService");
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void beginAdUnitExposure(String str, long j8) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeLong(j8);
        f(23, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeString(str2);
        x0.d(d8, bundle);
        f(9, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void endAdUnitExposure(String str, long j8) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeLong(j8);
        f(24, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void generateEventId(h2 h2Var) {
        Parcel d8 = d();
        x0.c(d8, h2Var);
        f(22, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void getCachedAppInstanceId(h2 h2Var) {
        Parcel d8 = d();
        x0.c(d8, h2Var);
        f(19, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void getConditionalUserProperties(String str, String str2, h2 h2Var) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeString(str2);
        x0.c(d8, h2Var);
        f(10, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void getCurrentScreenClass(h2 h2Var) {
        Parcel d8 = d();
        x0.c(d8, h2Var);
        f(17, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void getCurrentScreenName(h2 h2Var) {
        Parcel d8 = d();
        x0.c(d8, h2Var);
        f(16, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void getGmpAppId(h2 h2Var) {
        Parcel d8 = d();
        x0.c(d8, h2Var);
        f(21, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void getMaxUserProperties(String str, h2 h2Var) {
        Parcel d8 = d();
        d8.writeString(str);
        x0.c(d8, h2Var);
        f(6, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void getUserProperties(String str, String str2, boolean z4, h2 h2Var) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeString(str2);
        x0.e(d8, z4);
        x0.c(d8, h2Var);
        f(5, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void initialize(x6.a aVar, zzdq zzdqVar, long j8) {
        Parcel d8 = d();
        x0.c(d8, aVar);
        x0.d(d8, zzdqVar);
        d8.writeLong(j8);
        f(1, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void logEvent(String str, String str2, Bundle bundle, boolean z4, boolean z8, long j8) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeString(str2);
        x0.d(d8, bundle);
        x0.e(d8, z4);
        x0.e(d8, z8);
        d8.writeLong(j8);
        f(2, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void logHealthData(int i8, String str, x6.a aVar, x6.a aVar2, x6.a aVar3) {
        Parcel d8 = d();
        d8.writeInt(i8);
        d8.writeString(str);
        x0.c(d8, aVar);
        x0.c(d8, aVar2);
        x0.c(d8, aVar3);
        f(33, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void onActivityCreated(x6.a aVar, Bundle bundle, long j8) {
        Parcel d8 = d();
        x0.c(d8, aVar);
        x0.d(d8, bundle);
        d8.writeLong(j8);
        f(27, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void onActivityDestroyed(x6.a aVar, long j8) {
        Parcel d8 = d();
        x0.c(d8, aVar);
        d8.writeLong(j8);
        f(28, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void onActivityPaused(x6.a aVar, long j8) {
        Parcel d8 = d();
        x0.c(d8, aVar);
        d8.writeLong(j8);
        f(29, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void onActivityResumed(x6.a aVar, long j8) {
        Parcel d8 = d();
        x0.c(d8, aVar);
        d8.writeLong(j8);
        f(30, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void onActivitySaveInstanceState(x6.a aVar, h2 h2Var, long j8) {
        Parcel d8 = d();
        x0.c(d8, aVar);
        x0.c(d8, h2Var);
        d8.writeLong(j8);
        f(31, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void onActivityStarted(x6.a aVar, long j8) {
        Parcel d8 = d();
        x0.c(d8, aVar);
        d8.writeLong(j8);
        f(25, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void onActivityStopped(x6.a aVar, long j8) {
        Parcel d8 = d();
        x0.c(d8, aVar);
        d8.writeLong(j8);
        f(26, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void setConditionalUserProperty(Bundle bundle, long j8) {
        Parcel d8 = d();
        x0.d(d8, bundle);
        d8.writeLong(j8);
        f(8, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void setCurrentScreen(x6.a aVar, String str, String str2, long j8) {
        Parcel d8 = d();
        x0.c(d8, aVar);
        d8.writeString(str);
        d8.writeString(str2);
        d8.writeLong(j8);
        f(15, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void setDataCollectionEnabled(boolean z4) {
        Parcel d8 = d();
        x0.e(d8, z4);
        f(39, d8);
    }

    @Override // com.google.android.gms.internal.measurement.c2
    public final void setUserProperty(String str, String str2, x6.a aVar, boolean z4, long j8) {
        Parcel d8 = d();
        d8.writeString(str);
        d8.writeString(str2);
        x0.c(d8, aVar);
        x0.e(d8, z4);
        d8.writeLong(j8);
        f(4, d8);
    }
}
