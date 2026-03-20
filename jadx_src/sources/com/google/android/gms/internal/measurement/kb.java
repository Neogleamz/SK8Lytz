package com.google.android.gms.internal.measurement;

import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class kb implements Comparable, Map.Entry {

    /* renamed from: a  reason: collision with root package name */
    private final Comparable f12286a;

    /* renamed from: b  reason: collision with root package name */
    private Object f12287b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ ya f12288c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public kb(ya yaVar, Comparable comparable, Object obj) {
        this.f12288c = yaVar;
        this.f12286a = comparable;
        this.f12287b = obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public kb(ya yaVar, Map.Entry entry) {
        this(yaVar, (Comparable) entry.getKey(), entry.getValue());
    }

    private static boolean c(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    @Override // java.lang.Comparable
    public final /* synthetic */ int compareTo(Object obj) {
        return ((Comparable) getKey()).compareTo((Comparable) ((kb) obj).getKey());
    }

    @Override // java.util.Map.Entry
    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry) obj;
            return c(this.f12286a, entry.getKey()) && c(this.f12287b, entry.getValue());
        }
        return false;
    }

    @Override // java.util.Map.Entry
    public final /* synthetic */ Object getKey() {
        return this.f12286a;
    }

    @Override // java.util.Map.Entry
    public final Object getValue() {
        return this.f12287b;
    }

    @Override // java.util.Map.Entry
    public final int hashCode() {
        Comparable comparable = this.f12286a;
        int hashCode = comparable == null ? 0 : comparable.hashCode();
        Object obj = this.f12287b;
        return hashCode ^ (obj != null ? obj.hashCode() : 0);
    }

    @Override // java.util.Map.Entry
    public final Object setValue(Object obj) {
        this.f12288c.s();
        Object obj2 = this.f12287b;
        this.f12287b = obj;
        return obj2;
    }

    public final String toString() {
        String valueOf = String.valueOf(this.f12286a);
        String valueOf2 = String.valueOf(this.f12287b);
        return valueOf + "=" + valueOf2;
    }
}
