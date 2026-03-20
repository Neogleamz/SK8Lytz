package com.google.android.gms.internal.measurement;

import com.google.common.collect.ImmutableSet;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: d  reason: collision with root package name */
    private static final ImmutableSet<String> f12150d = ImmutableSet.L("_syn", "_err", "_el");

    /* renamed from: a  reason: collision with root package name */
    private String f12151a;

    /* renamed from: b  reason: collision with root package name */
    private long f12152b;

    /* renamed from: c  reason: collision with root package name */
    private Map<String, Object> f12153c;

    public e(String str, long j8, Map<String, Object> map) {
        this.f12151a = str;
        this.f12152b = j8;
        HashMap hashMap = new HashMap();
        this.f12153c = hashMap;
        if (map != null) {
            hashMap.putAll(map);
        }
    }

    public static Object c(String str, Object obj, Object obj2) {
        if (!f12150d.contains(str) || !(obj2 instanceof Double)) {
            if (str.startsWith("_")) {
                return ((obj instanceof String) || obj == null) ? obj2 : obj;
            } else if (obj instanceof Double) {
                return obj2;
            } else {
                if (!(obj instanceof Long)) {
                    return obj instanceof String ? obj2.toString() : obj2;
                }
            }
        }
        return Long.valueOf(Math.round(((Double) obj2).doubleValue()));
    }

    public final long a() {
        return this.f12152b;
    }

    public final Object b(String str) {
        if (this.f12153c.containsKey(str)) {
            return this.f12153c.get(str);
        }
        return null;
    }

    public final /* synthetic */ Object clone() {
        return new e(this.f12151a, this.f12152b, new HashMap(this.f12153c));
    }

    public final void d(String str, Object obj) {
        if (obj == null) {
            this.f12153c.remove(str);
            return;
        }
        this.f12153c.put(str, c(str, this.f12153c.get(str), obj));
    }

    public final String e() {
        return this.f12151a;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof e) {
            e eVar = (e) obj;
            if (this.f12152b == eVar.f12152b && this.f12151a.equals(eVar.f12151a)) {
                return this.f12153c.equals(eVar.f12153c);
            }
            return false;
        }
        return false;
    }

    public final void f(String str) {
        this.f12151a = str;
    }

    public final Map<String, Object> g() {
        return this.f12153c;
    }

    public final int hashCode() {
        long j8 = this.f12152b;
        return (((this.f12151a.hashCode() * 31) + ((int) (j8 ^ (j8 >>> 32)))) * 31) + this.f12153c.hashCode();
    }

    public final String toString() {
        String str = this.f12151a;
        long j8 = this.f12152b;
        String valueOf = String.valueOf(this.f12153c);
        return "Event{name='" + str + "', timestamp=" + j8 + ", params=" + valueOf + "}";
    }
}
