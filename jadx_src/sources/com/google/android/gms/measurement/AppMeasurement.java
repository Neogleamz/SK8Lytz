package com.google.android.gms.measurement;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Keep;
import com.google.android.gms.internal.measurement.zzdq;
import com.google.android.gms.measurement.internal.f6;
import com.google.firebase.analytics.FirebaseAnalytics;
import f7.m;
import f7.w;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import n6.j;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppMeasurement {

    /* renamed from: b  reason: collision with root package name */
    private static volatile AppMeasurement f16280b;

    /* renamed from: a  reason: collision with root package name */
    private final a f16281a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class ConditionalUserProperty {
        @Keep
        public boolean mActive;
        @Keep
        public String mAppId;
        @Keep
        public long mCreationTimestamp;
        @Keep
        public String mExpiredEventName;
        @Keep
        public Bundle mExpiredEventParams;
        @Keep
        public String mName;
        @Keep
        public String mOrigin;
        @Keep
        public long mTimeToLive;
        @Keep
        public String mTimedOutEventName;
        @Keep
        public Bundle mTimedOutEventParams;
        @Keep
        public String mTriggerEventName;
        @Keep
        public long mTriggerTimeout;
        @Keep
        public String mTriggeredEventName;
        @Keep
        public Bundle mTriggeredEventParams;
        @Keep
        public long mTriggeredTimestamp;
        @Keep
        public Object mValue;

        public ConditionalUserProperty() {
        }

        ConditionalUserProperty(Bundle bundle) {
            j.l(bundle);
            this.mAppId = (String) m.a(bundle, "app_id", String.class, null);
            this.mOrigin = (String) m.a(bundle, "origin", String.class, null);
            this.mName = (String) m.a(bundle, "name", String.class, null);
            this.mValue = m.a(bundle, "value", Object.class, null);
            this.mTriggerEventName = (String) m.a(bundle, "trigger_event_name", String.class, null);
            this.mTriggerTimeout = ((Long) m.a(bundle, "trigger_timeout", Long.class, 0L)).longValue();
            this.mTimedOutEventName = (String) m.a(bundle, "timed_out_event_name", String.class, null);
            this.mTimedOutEventParams = (Bundle) m.a(bundle, "timed_out_event_params", Bundle.class, null);
            this.mTriggeredEventName = (String) m.a(bundle, "triggered_event_name", String.class, null);
            this.mTriggeredEventParams = (Bundle) m.a(bundle, "triggered_event_params", Bundle.class, null);
            this.mTimeToLive = ((Long) m.a(bundle, "time_to_live", Long.class, 0L)).longValue();
            this.mExpiredEventName = (String) m.a(bundle, "expired_event_name", String.class, null);
            this.mExpiredEventParams = (Bundle) m.a(bundle, "expired_event_params", Bundle.class, null);
            this.mActive = ((Boolean) m.a(bundle, "active", Boolean.class, Boolean.FALSE)).booleanValue();
            this.mCreationTimestamp = ((Long) m.a(bundle, "creation_timestamp", Long.class, 0L)).longValue();
            this.mTriggeredTimestamp = ((Long) m.a(bundle, "triggered_timestamp", Long.class, 0L)).longValue();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a implements w {
        private a() {
        }
    }

    private AppMeasurement(f6 f6Var) {
        this.f16281a = new b(f6Var);
    }

    private AppMeasurement(w wVar) {
        this.f16281a = new com.google.android.gms.measurement.a(wVar);
    }

    private static AppMeasurement a(Context context, String str, String str2) {
        if (f16280b == null) {
            synchronized (AppMeasurement.class) {
                if (f16280b == null) {
                    w b9 = b(context, null);
                    if (b9 != null) {
                        f16280b = new AppMeasurement(b9);
                    } else {
                        f16280b = new AppMeasurement(f6.a(context, new zzdq(0L, 0L, true, null, null, null, null, null), null));
                    }
                }
            }
        }
        return f16280b;
    }

    private static w b(Context context, Bundle bundle) {
        return (w) FirebaseAnalytics.class.getDeclaredMethod("getScionFrontendApiImplementation", Context.class, Bundle.class).invoke(null, context, null);
    }

    @Keep
    @Deprecated
    public static AppMeasurement getInstance(Context context) {
        return a(context, null, null);
    }

    @Keep
    public void beginAdUnitExposure(String str) {
        this.f16281a.b(str);
    }

    @Keep
    public void clearConditionalUserProperty(String str, String str2, Bundle bundle) {
        this.f16281a.a(str, str2, bundle);
    }

    @Keep
    public void endAdUnitExposure(String str) {
        this.f16281a.k(str);
    }

    @Keep
    public long generateEventId() {
        return this.f16281a.e();
    }

    @Keep
    public String getAppInstanceId() {
        return this.f16281a.g();
    }

    @Keep
    public List<ConditionalUserProperty> getConditionalUserProperties(String str, String str2) {
        List<Bundle> d8 = this.f16281a.d(str, str2);
        ArrayList arrayList = new ArrayList(d8 == null ? 0 : d8.size());
        for (Bundle bundle : d8) {
            arrayList.add(new ConditionalUserProperty(bundle));
        }
        return arrayList;
    }

    @Keep
    public String getCurrentScreenClass() {
        return this.f16281a.f();
    }

    @Keep
    public String getCurrentScreenName() {
        return this.f16281a.j();
    }

    @Keep
    public String getGmpAppId() {
        return this.f16281a.i();
    }

    @Keep
    public int getMaxUserProperties(String str) {
        return this.f16281a.h(str);
    }

    @Keep
    protected Map<String, Object> getUserProperties(String str, String str2, boolean z4) {
        return this.f16281a.l(str, str2, z4);
    }

    @Keep
    public void logEventInternal(String str, String str2, Bundle bundle) {
        this.f16281a.m(str, str2, bundle);
    }

    @Keep
    public void setConditionalUserProperty(ConditionalUserProperty conditionalUserProperty) {
        j.l(conditionalUserProperty);
        a aVar = this.f16281a;
        Bundle bundle = new Bundle();
        String str = conditionalUserProperty.mAppId;
        if (str != null) {
            bundle.putString("app_id", str);
        }
        String str2 = conditionalUserProperty.mOrigin;
        if (str2 != null) {
            bundle.putString("origin", str2);
        }
        String str3 = conditionalUserProperty.mName;
        if (str3 != null) {
            bundle.putString("name", str3);
        }
        Object obj = conditionalUserProperty.mValue;
        if (obj != null) {
            m.b(bundle, obj);
        }
        String str4 = conditionalUserProperty.mTriggerEventName;
        if (str4 != null) {
            bundle.putString("trigger_event_name", str4);
        }
        bundle.putLong("trigger_timeout", conditionalUserProperty.mTriggerTimeout);
        String str5 = conditionalUserProperty.mTimedOutEventName;
        if (str5 != null) {
            bundle.putString("timed_out_event_name", str5);
        }
        Bundle bundle2 = conditionalUserProperty.mTimedOutEventParams;
        if (bundle2 != null) {
            bundle.putBundle("timed_out_event_params", bundle2);
        }
        String str6 = conditionalUserProperty.mTriggeredEventName;
        if (str6 != null) {
            bundle.putString("triggered_event_name", str6);
        }
        Bundle bundle3 = conditionalUserProperty.mTriggeredEventParams;
        if (bundle3 != null) {
            bundle.putBundle("triggered_event_params", bundle3);
        }
        bundle.putLong("time_to_live", conditionalUserProperty.mTimeToLive);
        String str7 = conditionalUserProperty.mExpiredEventName;
        if (str7 != null) {
            bundle.putString("expired_event_name", str7);
        }
        Bundle bundle4 = conditionalUserProperty.mExpiredEventParams;
        if (bundle4 != null) {
            bundle.putBundle("expired_event_params", bundle4);
        }
        bundle.putLong("creation_timestamp", conditionalUserProperty.mCreationTimestamp);
        bundle.putBoolean("active", conditionalUserProperty.mActive);
        bundle.putLong("triggered_timestamp", conditionalUserProperty.mTriggeredTimestamp);
        aVar.c(bundle);
    }
}
