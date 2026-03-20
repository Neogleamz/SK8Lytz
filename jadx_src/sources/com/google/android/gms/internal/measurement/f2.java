package com.google.android.gms.internal.measurement;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.util.HashMap;
import x6.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class f2 extends y0 implements c2 {
    public f2() {
        super("com.google.android.gms.measurement.api.internal.IAppMeasurementDynamiteService");
    }

    public static c2 asInterface(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.measurement.api.internal.IAppMeasurementDynamiteService");
        return queryLocalInterface instanceof c2 ? (c2) queryLocalInterface : new e2(iBinder);
    }

    @Override // com.google.android.gms.internal.measurement.y0
    protected final boolean d(int i8, Parcel parcel, Parcel parcel2, int i9) {
        h2 j2Var;
        h2 h2Var = null;
        h2 h2Var2 = null;
        h2 h2Var3 = null;
        h2 h2Var4 = null;
        i2 i2Var = null;
        i2 i2Var2 = null;
        i2 i2Var3 = null;
        h2 h2Var5 = null;
        h2 h2Var6 = null;
        h2 h2Var7 = null;
        h2 h2Var8 = null;
        h2 h2Var9 = null;
        h2 h2Var10 = null;
        m2 m2Var = null;
        h2 h2Var11 = null;
        h2 h2Var12 = null;
        h2 h2Var13 = null;
        h2 h2Var14 = null;
        switch (i8) {
            case 1:
                long readLong = parcel.readLong();
                x0.f(parcel);
                initialize(a.AbstractBinderC0227a.e(parcel.readStrongBinder()), (zzdq) x0.a(parcel, zzdq.CREATOR), readLong);
                break;
            case 2:
                boolean h8 = x0.h(parcel);
                boolean h9 = x0.h(parcel);
                long readLong2 = parcel.readLong();
                x0.f(parcel);
                logEvent(parcel.readString(), parcel.readString(), (Bundle) x0.a(parcel, Bundle.CREATOR), h8, h9, readLong2);
                break;
            case 3:
                String readString = parcel.readString();
                String readString2 = parcel.readString();
                Bundle bundle = (Bundle) x0.a(parcel, Bundle.CREATOR);
                IBinder readStrongBinder = parcel.readStrongBinder();
                if (readStrongBinder == null) {
                    j2Var = null;
                } else {
                    IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    j2Var = queryLocalInterface instanceof h2 ? (h2) queryLocalInterface : new j2(readStrongBinder);
                }
                long readLong3 = parcel.readLong();
                x0.f(parcel);
                logEventAndBundle(readString, readString2, bundle, j2Var, readLong3);
                break;
            case 4:
                String readString3 = parcel.readString();
                String readString4 = parcel.readString();
                x6.a e8 = a.AbstractBinderC0227a.e(parcel.readStrongBinder());
                boolean h10 = x0.h(parcel);
                long readLong4 = parcel.readLong();
                x0.f(parcel);
                setUserProperty(readString3, readString4, e8, h10, readLong4);
                break;
            case 5:
                String readString5 = parcel.readString();
                String readString6 = parcel.readString();
                boolean h11 = x0.h(parcel);
                IBinder readStrongBinder2 = parcel.readStrongBinder();
                if (readStrongBinder2 != null) {
                    IInterface queryLocalInterface2 = readStrongBinder2.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var = queryLocalInterface2 instanceof h2 ? (h2) queryLocalInterface2 : new j2(readStrongBinder2);
                }
                x0.f(parcel);
                getUserProperties(readString5, readString6, h11, h2Var);
                break;
            case 6:
                String readString7 = parcel.readString();
                IBinder readStrongBinder3 = parcel.readStrongBinder();
                if (readStrongBinder3 != null) {
                    IInterface queryLocalInterface3 = readStrongBinder3.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var14 = queryLocalInterface3 instanceof h2 ? (h2) queryLocalInterface3 : new j2(readStrongBinder3);
                }
                x0.f(parcel);
                getMaxUserProperties(readString7, h2Var14);
                break;
            case 7:
                String readString8 = parcel.readString();
                long readLong5 = parcel.readLong();
                x0.f(parcel);
                setUserId(readString8, readLong5);
                break;
            case 8:
                long readLong6 = parcel.readLong();
                x0.f(parcel);
                setConditionalUserProperty((Bundle) x0.a(parcel, Bundle.CREATOR), readLong6);
                break;
            case 9:
                x0.f(parcel);
                clearConditionalUserProperty(parcel.readString(), parcel.readString(), (Bundle) x0.a(parcel, Bundle.CREATOR));
                break;
            case 10:
                String readString9 = parcel.readString();
                String readString10 = parcel.readString();
                IBinder readStrongBinder4 = parcel.readStrongBinder();
                if (readStrongBinder4 != null) {
                    IInterface queryLocalInterface4 = readStrongBinder4.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var13 = queryLocalInterface4 instanceof h2 ? (h2) queryLocalInterface4 : new j2(readStrongBinder4);
                }
                x0.f(parcel);
                getConditionalUserProperties(readString9, readString10, h2Var13);
                break;
            case 11:
                boolean h12 = x0.h(parcel);
                long readLong7 = parcel.readLong();
                x0.f(parcel);
                setMeasurementEnabled(h12, readLong7);
                break;
            case 12:
                long readLong8 = parcel.readLong();
                x0.f(parcel);
                resetAnalyticsData(readLong8);
                break;
            case 13:
                long readLong9 = parcel.readLong();
                x0.f(parcel);
                setMinimumSessionDuration(readLong9);
                break;
            case 14:
                long readLong10 = parcel.readLong();
                x0.f(parcel);
                setSessionTimeoutDuration(readLong10);
                break;
            case 15:
                x6.a e9 = a.AbstractBinderC0227a.e(parcel.readStrongBinder());
                String readString11 = parcel.readString();
                String readString12 = parcel.readString();
                long readLong11 = parcel.readLong();
                x0.f(parcel);
                setCurrentScreen(e9, readString11, readString12, readLong11);
                break;
            case 16:
                IBinder readStrongBinder5 = parcel.readStrongBinder();
                if (readStrongBinder5 != null) {
                    IInterface queryLocalInterface5 = readStrongBinder5.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var12 = queryLocalInterface5 instanceof h2 ? (h2) queryLocalInterface5 : new j2(readStrongBinder5);
                }
                x0.f(parcel);
                getCurrentScreenName(h2Var12);
                break;
            case 17:
                IBinder readStrongBinder6 = parcel.readStrongBinder();
                if (readStrongBinder6 != null) {
                    IInterface queryLocalInterface6 = readStrongBinder6.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var11 = queryLocalInterface6 instanceof h2 ? (h2) queryLocalInterface6 : new j2(readStrongBinder6);
                }
                x0.f(parcel);
                getCurrentScreenClass(h2Var11);
                break;
            case 18:
                IBinder readStrongBinder7 = parcel.readStrongBinder();
                if (readStrongBinder7 != null) {
                    IInterface queryLocalInterface7 = readStrongBinder7.queryLocalInterface("com.google.android.gms.measurement.api.internal.IStringProvider");
                    m2Var = queryLocalInterface7 instanceof m2 ? (m2) queryLocalInterface7 : new l2(readStrongBinder7);
                }
                x0.f(parcel);
                setInstanceIdProvider(m2Var);
                break;
            case 19:
                IBinder readStrongBinder8 = parcel.readStrongBinder();
                if (readStrongBinder8 != null) {
                    IInterface queryLocalInterface8 = readStrongBinder8.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var10 = queryLocalInterface8 instanceof h2 ? (h2) queryLocalInterface8 : new j2(readStrongBinder8);
                }
                x0.f(parcel);
                getCachedAppInstanceId(h2Var10);
                break;
            case 20:
                IBinder readStrongBinder9 = parcel.readStrongBinder();
                if (readStrongBinder9 != null) {
                    IInterface queryLocalInterface9 = readStrongBinder9.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var9 = queryLocalInterface9 instanceof h2 ? (h2) queryLocalInterface9 : new j2(readStrongBinder9);
                }
                x0.f(parcel);
                getAppInstanceId(h2Var9);
                break;
            case 21:
                IBinder readStrongBinder10 = parcel.readStrongBinder();
                if (readStrongBinder10 != null) {
                    IInterface queryLocalInterface10 = readStrongBinder10.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var8 = queryLocalInterface10 instanceof h2 ? (h2) queryLocalInterface10 : new j2(readStrongBinder10);
                }
                x0.f(parcel);
                getGmpAppId(h2Var8);
                break;
            case 22:
                IBinder readStrongBinder11 = parcel.readStrongBinder();
                if (readStrongBinder11 != null) {
                    IInterface queryLocalInterface11 = readStrongBinder11.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var7 = queryLocalInterface11 instanceof h2 ? (h2) queryLocalInterface11 : new j2(readStrongBinder11);
                }
                x0.f(parcel);
                generateEventId(h2Var7);
                break;
            case 23:
                String readString13 = parcel.readString();
                long readLong12 = parcel.readLong();
                x0.f(parcel);
                beginAdUnitExposure(readString13, readLong12);
                break;
            case 24:
                String readString14 = parcel.readString();
                long readLong13 = parcel.readLong();
                x0.f(parcel);
                endAdUnitExposure(readString14, readLong13);
                break;
            case 25:
                x6.a e10 = a.AbstractBinderC0227a.e(parcel.readStrongBinder());
                long readLong14 = parcel.readLong();
                x0.f(parcel);
                onActivityStarted(e10, readLong14);
                break;
            case 26:
                x6.a e11 = a.AbstractBinderC0227a.e(parcel.readStrongBinder());
                long readLong15 = parcel.readLong();
                x0.f(parcel);
                onActivityStopped(e11, readLong15);
                break;
            case 27:
                long readLong16 = parcel.readLong();
                x0.f(parcel);
                onActivityCreated(a.AbstractBinderC0227a.e(parcel.readStrongBinder()), (Bundle) x0.a(parcel, Bundle.CREATOR), readLong16);
                break;
            case 28:
                x6.a e12 = a.AbstractBinderC0227a.e(parcel.readStrongBinder());
                long readLong17 = parcel.readLong();
                x0.f(parcel);
                onActivityDestroyed(e12, readLong17);
                break;
            case 29:
                x6.a e13 = a.AbstractBinderC0227a.e(parcel.readStrongBinder());
                long readLong18 = parcel.readLong();
                x0.f(parcel);
                onActivityPaused(e13, readLong18);
                break;
            case 30:
                x6.a e14 = a.AbstractBinderC0227a.e(parcel.readStrongBinder());
                long readLong19 = parcel.readLong();
                x0.f(parcel);
                onActivityResumed(e14, readLong19);
                break;
            case 31:
                x6.a e15 = a.AbstractBinderC0227a.e(parcel.readStrongBinder());
                IBinder readStrongBinder12 = parcel.readStrongBinder();
                if (readStrongBinder12 != null) {
                    IInterface queryLocalInterface12 = readStrongBinder12.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var6 = queryLocalInterface12 instanceof h2 ? (h2) queryLocalInterface12 : new j2(readStrongBinder12);
                }
                long readLong20 = parcel.readLong();
                x0.f(parcel);
                onActivitySaveInstanceState(e15, h2Var6, readLong20);
                break;
            case 32:
                Bundle bundle2 = (Bundle) x0.a(parcel, Bundle.CREATOR);
                IBinder readStrongBinder13 = parcel.readStrongBinder();
                if (readStrongBinder13 != null) {
                    IInterface queryLocalInterface13 = readStrongBinder13.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var5 = queryLocalInterface13 instanceof h2 ? (h2) queryLocalInterface13 : new j2(readStrongBinder13);
                }
                long readLong21 = parcel.readLong();
                x0.f(parcel);
                performAction(bundle2, h2Var5, readLong21);
                break;
            case 33:
                int readInt = parcel.readInt();
                String readString15 = parcel.readString();
                x6.a e16 = a.AbstractBinderC0227a.e(parcel.readStrongBinder());
                x6.a e17 = a.AbstractBinderC0227a.e(parcel.readStrongBinder());
                x6.a e18 = a.AbstractBinderC0227a.e(parcel.readStrongBinder());
                x0.f(parcel);
                logHealthData(readInt, readString15, e16, e17, e18);
                break;
            case 34:
                IBinder readStrongBinder14 = parcel.readStrongBinder();
                if (readStrongBinder14 != null) {
                    IInterface queryLocalInterface14 = readStrongBinder14.queryLocalInterface("com.google.android.gms.measurement.api.internal.IEventHandlerProxy");
                    i2Var3 = queryLocalInterface14 instanceof i2 ? (i2) queryLocalInterface14 : new k2(readStrongBinder14);
                }
                x0.f(parcel);
                setEventInterceptor(i2Var3);
                break;
            case 35:
                IBinder readStrongBinder15 = parcel.readStrongBinder();
                if (readStrongBinder15 != null) {
                    IInterface queryLocalInterface15 = readStrongBinder15.queryLocalInterface("com.google.android.gms.measurement.api.internal.IEventHandlerProxy");
                    i2Var2 = queryLocalInterface15 instanceof i2 ? (i2) queryLocalInterface15 : new k2(readStrongBinder15);
                }
                x0.f(parcel);
                registerOnMeasurementEventListener(i2Var2);
                break;
            case 36:
                IBinder readStrongBinder16 = parcel.readStrongBinder();
                if (readStrongBinder16 != null) {
                    IInterface queryLocalInterface16 = readStrongBinder16.queryLocalInterface("com.google.android.gms.measurement.api.internal.IEventHandlerProxy");
                    i2Var = queryLocalInterface16 instanceof i2 ? (i2) queryLocalInterface16 : new k2(readStrongBinder16);
                }
                x0.f(parcel);
                unregisterOnMeasurementEventListener(i2Var);
                break;
            case 37:
                HashMap b9 = x0.b(parcel);
                x0.f(parcel);
                initForTests(b9);
                break;
            case 38:
                IBinder readStrongBinder17 = parcel.readStrongBinder();
                if (readStrongBinder17 != null) {
                    IInterface queryLocalInterface17 = readStrongBinder17.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var4 = queryLocalInterface17 instanceof h2 ? (h2) queryLocalInterface17 : new j2(readStrongBinder17);
                }
                int readInt2 = parcel.readInt();
                x0.f(parcel);
                getTestFlag(h2Var4, readInt2);
                break;
            case 39:
                boolean h13 = x0.h(parcel);
                x0.f(parcel);
                setDataCollectionEnabled(h13);
                break;
            case 40:
                IBinder readStrongBinder18 = parcel.readStrongBinder();
                if (readStrongBinder18 != null) {
                    IInterface queryLocalInterface18 = readStrongBinder18.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var3 = queryLocalInterface18 instanceof h2 ? (h2) queryLocalInterface18 : new j2(readStrongBinder18);
                }
                x0.f(parcel);
                isDataCollectionEnabled(h2Var3);
                break;
            case 41:
            case 47:
            default:
                return false;
            case 42:
                x0.f(parcel);
                setDefaultEventParameters((Bundle) x0.a(parcel, Bundle.CREATOR));
                break;
            case 43:
                long readLong22 = parcel.readLong();
                x0.f(parcel);
                clearMeasurementEnabled(readLong22);
                break;
            case 44:
                long readLong23 = parcel.readLong();
                x0.f(parcel);
                setConsent((Bundle) x0.a(parcel, Bundle.CREATOR), readLong23);
                break;
            case 45:
                long readLong24 = parcel.readLong();
                x0.f(parcel);
                setConsentThirdParty((Bundle) x0.a(parcel, Bundle.CREATOR), readLong24);
                break;
            case 46:
                IBinder readStrongBinder19 = parcel.readStrongBinder();
                if (readStrongBinder19 != null) {
                    IInterface queryLocalInterface19 = readStrongBinder19.queryLocalInterface("com.google.android.gms.measurement.api.internal.IBundleReceiver");
                    h2Var2 = queryLocalInterface19 instanceof h2 ? (h2) queryLocalInterface19 : new j2(readStrongBinder19);
                }
                x0.f(parcel);
                getSessionId(h2Var2);
                break;
            case 48:
                x0.f(parcel);
                setSgtmDebugInfo((Intent) x0.a(parcel, Intent.CREATOR));
                break;
        }
        parcel2.writeNoException();
        return true;
    }
}
