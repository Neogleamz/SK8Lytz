package com.google.android.gms.internal.measurement;

import android.content.ContentResolver;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r5 implements o5 {

    /* renamed from: a  reason: collision with root package name */
    private final AtomicBoolean f12475a = new AtomicBoolean();

    /* renamed from: b  reason: collision with root package name */
    private HashMap<String, String> f12476b = null;

    /* renamed from: c  reason: collision with root package name */
    private final HashMap<String, Boolean> f12477c = new HashMap<>(16, 1.0f);

    /* renamed from: d  reason: collision with root package name */
    private final HashMap<String, Integer> f12478d = new HashMap<>(16, 1.0f);

    /* renamed from: e  reason: collision with root package name */
    private final HashMap<String, Long> f12479e = new HashMap<>(16, 1.0f);

    /* renamed from: f  reason: collision with root package name */
    private final HashMap<String, Float> f12480f = new HashMap<>(16, 1.0f);

    /* renamed from: g  reason: collision with root package name */
    private Object f12481g = null;

    /* renamed from: h  reason: collision with root package name */
    private boolean f12482h = false;

    /* renamed from: i  reason: collision with root package name */
    private String[] f12483i = new String[0];

    /* renamed from: j  reason: collision with root package name */
    private final ContentResolver f12484j = null;

    /* renamed from: k  reason: collision with root package name */
    private final u5 f12485k = new s5();

    @Override // com.google.android.gms.internal.measurement.o5
    public final String a(ContentResolver contentResolver, String str, String str2) {
        if (contentResolver != null) {
            synchronized (this) {
                if (this.f12476b == null) {
                    this.f12475a.set(false);
                    this.f12476b = new HashMap<>(16, 1.0f);
                    this.f12481g = new Object();
                    contentResolver.registerContentObserver(m5.f12338a, true, new t5(this, null));
                } else if (this.f12475a.getAndSet(false)) {
                    this.f12476b.clear();
                    this.f12477c.clear();
                    this.f12478d.clear();
                    this.f12479e.clear();
                    this.f12480f.clear();
                    this.f12481g = new Object();
                    this.f12482h = false;
                }
                Object obj = this.f12481g;
                if (this.f12476b.containsKey(str)) {
                    String str3 = this.f12476b.get(str);
                    return str3 != null ? str3 : null;
                }
                for (String str4 : this.f12483i) {
                    if (str.startsWith(str4)) {
                        if (!this.f12482h) {
                            try {
                                HashMap<String, String> hashMap = (HashMap) this.f12485k.b(contentResolver, this.f12483i, new v5() { // from class: com.google.android.gms.internal.measurement.q5
                                    @Override // com.google.android.gms.internal.measurement.v5
                                    public final Map c(int i8) {
                                        return new HashMap(i8, 1.0f);
                                    }
                                });
                                if (!hashMap.isEmpty()) {
                                    Set<String> keySet = hashMap.keySet();
                                    keySet.removeAll(this.f12477c.keySet());
                                    keySet.removeAll(this.f12478d.keySet());
                                    keySet.removeAll(this.f12479e.keySet());
                                    keySet.removeAll(this.f12480f.keySet());
                                }
                                if (!hashMap.isEmpty()) {
                                    if (this.f12476b.isEmpty()) {
                                        this.f12476b = hashMap;
                                    } else {
                                        this.f12476b.putAll(hashMap);
                                    }
                                }
                                this.f12482h = true;
                            } catch (zzgq unused) {
                            }
                            if (this.f12476b.containsKey(str)) {
                                String str5 = this.f12476b.get(str);
                                return str5 != null ? str5 : null;
                            }
                        }
                        return null;
                    }
                }
                try {
                    String a9 = this.f12485k.a(contentResolver, str);
                    if (a9 != null && a9.equals(null)) {
                        a9 = null;
                    }
                    synchronized (this) {
                        if (obj == this.f12481g) {
                            this.f12476b.put(str, a9);
                        }
                    }
                    if (a9 != null) {
                        return a9;
                    }
                    return null;
                } catch (zzgq unused2) {
                    return null;
                }
            }
        }
        throw new IllegalStateException("ContentResolver needed with GservicesDelegateSupplier.init()");
    }
}
