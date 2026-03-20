package com.example.seedpoint;

import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import com.example.seedpoint.event.EventData;
import com.example.seedpoint.event.Location;
import com.example.seedpoint.event.NetworkInfo;
import com.example.seedpoint.event.Property;
import com.example.seedpoint.utils.CommonEventDataDataUtil;
import com.example.seedpoint.utils.EventCache;
import com.example.seedpoint.utils.HttpClient;
import com.example.seedpoint.utils.UUIDUtil;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SeedPoint {
    private static final String EVENT_DURATION = "event_duration";
    private static final String SESSION_ID = "session_id";
    private static final String TAG = "com.example.seedpoint.SeedPoint";
    private final String androidId;
    private final ConnectivityManager connectivityManager;
    private EventCache eventCache;
    private boolean isLogin;
    private final Property property;
    private String userId;
    private final WifiManager wifiManager;
    private final Map<String, Long> events = new ConcurrentHashMap();
    private String sessionId = com.daimajia.numberprogressbar.BuildConfig.FLAVOR;
    private Map<String, Object> extAttr = new HashMap();

    /* JADX WARN: Removed duplicated region for block: B:17:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SeedPoint(android.content.Context r4, java.lang.String r5, java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 253
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.seedpoint.SeedPoint.<init>(android.content.Context, java.lang.String, java.lang.String):void");
    }

    private EventData createEvent(String str, long j8, Map<String, Object> map) {
        EventData eventData = new EventData();
        eventData.setEvent_id(UUIDUtil.uuid());
        eventData.setUser_id(this.userId);
        eventData.setEvent_type(str);
        eventData.setEvent_time(j8);
        eventData.setNetworkInfo(new NetworkInfo());
        eventData.setLocation(new Location());
        eventData.setProperties(this.property);
        map.put(SESSION_ID, this.sessionId);
        map.putAll(this.extAttr);
        map.putAll(CommonEventDataDataUtil.createDefaultAttributes(this.connectivityManager, this.wifiManager));
        eventData.setAttributes(map);
        return eventData;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Boolean bool) {
        String str = TAG;
        Log.i(str, " network state " + bool);
        if (bool.booleanValue()) {
            this.eventCache.startFlush();
        } else {
            this.eventCache.stopFlush();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean sendEventData(List<EventData> list) {
        try {
            HttpClient.Result post = HttpClient.getInstance().post("collect/appEvent", list);
            String str = TAG;
            Log.i(str, "result : " + post + " " + post.hashCode());
            return post.code == 200;
        } catch (Exception e8) {
            Log.e(TAG, e8.getMessage());
            return false;
        }
    }

    public void addExtAttr(Map<String, Object> map) {
        this.extAttr.putAll(map);
    }

    public Map<String, Object> createEventDuration(long j8) {
        HashMap hashMap = new HashMap();
        hashMap.put(EVENT_DURATION, Long.valueOf(j8));
        return hashMap;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public boolean isLogin() {
        return this.isLogin;
    }

    public void pullAnchor(String str, Map<String, Object> map) {
        synchronized (this) {
            Long l8 = this.events.get(str);
            if (l8 == null) {
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            Long valueOf = Long.valueOf(currentTimeMillis - l8.longValue());
            if (map == null) {
                map = new HashMap<>();
            }
            map.put(EVENT_DURATION, valueOf);
            this.events.remove(str);
            this.eventCache.put(createEvent(str, currentTimeMillis, map));
        }
    }

    public void push(String str, long j8, Map<String, Object> map) {
        synchronized (this) {
            this.eventCache.put(createEvent(str, j8, map));
        }
    }

    public void push(String str, Map<String, Object> map) {
        synchronized (this) {
            this.eventCache.put(createEvent(str, System.currentTimeMillis(), map));
        }
    }

    public void setAnchor(String str) {
        synchronized (this) {
            this.events.put(str, Long.valueOf(System.currentTimeMillis()));
        }
    }

    public void setLogin(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.userId = str;
        this.isLogin = true;
    }

    public void setLogout() {
        this.userId = this.androidId;
        this.isLogin = false;
    }

    public void setPropertyValue(Map<String, Object> map) {
        try {
            for (String str : map.keySet()) {
                Field declaredField = this.property.getClass().getDeclaredField(str);
                declaredField.setAccessible(true);
                declaredField.set(this.property, map.get(str));
            }
        } catch (Exception e8) {
            e8.printStackTrace();
        }
    }

    public void setSessionId(String str) {
        this.sessionId = str;
    }
}
