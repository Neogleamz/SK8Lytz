package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    private e f12123a;

    /* renamed from: b  reason: collision with root package name */
    private e f12124b;

    /* renamed from: c  reason: collision with root package name */
    private List<e> f12125c;

    public d() {
        this.f12123a = new e(BuildConfig.FLAVOR, 0L, null);
        this.f12124b = new e(BuildConfig.FLAVOR, 0L, null);
        this.f12125c = new ArrayList();
    }

    private d(e eVar) {
        this.f12123a = eVar;
        this.f12124b = (e) eVar.clone();
        this.f12125c = new ArrayList();
    }

    public final e a() {
        return this.f12123a;
    }

    public final void b(e eVar) {
        this.f12123a = eVar;
        this.f12124b = (e) eVar.clone();
        this.f12125c.clear();
    }

    public final void c(String str, long j8, Map<String, Object> map) {
        HashMap hashMap = new HashMap();
        for (String str2 : map.keySet()) {
            hashMap.put(str2, e.c(str2, this.f12123a.b(str2), map.get(str2)));
        }
        this.f12125c.add(new e(str, j8, hashMap));
    }

    public final /* synthetic */ Object clone() {
        d dVar = new d((e) this.f12123a.clone());
        for (e eVar : this.f12125c) {
            dVar.f12125c.add((e) eVar.clone());
        }
        return dVar;
    }

    public final e d() {
        return this.f12124b;
    }

    public final void e(e eVar) {
        this.f12124b = eVar;
    }

    public final List<e> f() {
        return this.f12125c;
    }
}
