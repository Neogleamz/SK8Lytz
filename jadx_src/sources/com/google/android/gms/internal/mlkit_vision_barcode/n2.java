package com.google.android.gms.internal.mlkit_vision_barcode;

import com.google.firebase.encoders.EncodingException;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n2 implements k8.b {

    /* renamed from: d  reason: collision with root package name */
    private static final j8.c f13792d = new j8.c() { // from class: com.google.android.gms.internal.mlkit_vision_barcode.m2
        public final void a(Object obj, Object obj2) {
            j8.d dVar = (j8.d) obj2;
            int i8 = n2.f13793e;
            throw new EncodingException("Couldn't find encoder for type ".concat(String.valueOf(obj.getClass().getCanonicalName())));
        }
    };

    /* renamed from: e  reason: collision with root package name */
    public static final /* synthetic */ int f13793e = 0;

    /* renamed from: a  reason: collision with root package name */
    private final Map f13794a = new HashMap();

    /* renamed from: b  reason: collision with root package name */
    private final Map f13795b = new HashMap();

    /* renamed from: c  reason: collision with root package name */
    private final j8.c f13796c = f13792d;

    public final /* bridge */ /* synthetic */ k8.b a(Class cls, j8.c cVar) {
        this.f13794a.put(cls, cVar);
        this.f13795b.remove(cls);
        return this;
    }

    public final o2 b() {
        return new o2(new HashMap(this.f13794a), new HashMap(this.f13795b), this.f13796c);
    }
}
