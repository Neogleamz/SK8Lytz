package com.google.android.gms.internal.mlkit_vision_common;

import com.google.firebase.encoders.EncodingException;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m implements k8.b {

    /* renamed from: d  reason: collision with root package name */
    private static final j8.c f15670d = new j8.c() { // from class: com.google.android.gms.internal.mlkit_vision_common.l
        public final void a(Object obj, Object obj2) {
            j8.d dVar = (j8.d) obj2;
            int i8 = m.f15671e;
            throw new EncodingException("Couldn't find encoder for type ".concat(String.valueOf(obj.getClass().getCanonicalName())));
        }
    };

    /* renamed from: e  reason: collision with root package name */
    public static final /* synthetic */ int f15671e = 0;

    /* renamed from: a  reason: collision with root package name */
    private final Map f15672a = new HashMap();

    /* renamed from: b  reason: collision with root package name */
    private final Map f15673b = new HashMap();

    /* renamed from: c  reason: collision with root package name */
    private final j8.c f15674c = f15670d;

    public final /* bridge */ /* synthetic */ k8.b a(Class cls, j8.c cVar) {
        this.f15672a.put(cls, cVar);
        this.f15673b.remove(cls);
        return this;
    }

    public final n b() {
        return new n(new HashMap(this.f15672a), new HashMap(this.f15673b), this.f15674c);
    }
}
