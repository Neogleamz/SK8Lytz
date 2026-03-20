package com.google.android.exoplayer2.source;

import a6.h;
import a6.n;
import android.content.Context;
import com.google.android.exoplayer2.source.k;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e implements k.a {

    /* renamed from: a  reason: collision with root package name */
    private final a f10421a;

    /* renamed from: b  reason: collision with root package name */
    private h.a f10422b;

    /* renamed from: c  reason: collision with root package name */
    private long f10423c;

    /* renamed from: d  reason: collision with root package name */
    private long f10424d;

    /* renamed from: e  reason: collision with root package name */
    private long f10425e;

    /* renamed from: f  reason: collision with root package name */
    private float f10426f;

    /* renamed from: g  reason: collision with root package name */
    private float f10427g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final n4.p f10428a;

        /* renamed from: b  reason: collision with root package name */
        private final Map<Integer, com.google.common.base.r<k.a>> f10429b = new HashMap();

        /* renamed from: c  reason: collision with root package name */
        private final Set<Integer> f10430c = new HashSet();

        /* renamed from: d  reason: collision with root package name */
        private final Map<Integer, k.a> f10431d = new HashMap();

        /* renamed from: e  reason: collision with root package name */
        private h.a f10432e;

        public a(n4.p pVar) {
            this.f10428a = pVar;
        }

        public void a(h.a aVar) {
            if (aVar != this.f10432e) {
                this.f10432e = aVar;
                this.f10429b.clear();
                this.f10431d.clear();
            }
        }
    }

    public e(h.a aVar, n4.p pVar) {
        this.f10422b = aVar;
        a aVar2 = new a(pVar);
        this.f10421a = aVar2;
        aVar2.a(aVar);
        this.f10423c = -9223372036854775807L;
        this.f10424d = -9223372036854775807L;
        this.f10425e = -9223372036854775807L;
        this.f10426f = -3.4028235E38f;
        this.f10427g = -3.4028235E38f;
    }

    public e(Context context, n4.p pVar) {
        this(new n.a(context), pVar);
    }
}
