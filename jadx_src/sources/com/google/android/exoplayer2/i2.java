package com.google.android.exoplayer2;

import android.os.Bundle;
import com.google.android.exoplayer2.g;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i2 implements g {

    /* renamed from: b  reason: collision with root package name */
    public static final i2 f9796b = new i2(ImmutableList.E());

    /* renamed from: c  reason: collision with root package name */
    private static final String f9797c = b6.l0.r0(0);

    /* renamed from: d  reason: collision with root package name */
    public static final g.a<i2> f9798d = i4.o0.a;

    /* renamed from: a  reason: collision with root package name */
    private final ImmutableList<a> f9799a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a implements g {

        /* renamed from: f  reason: collision with root package name */
        private static final String f9800f = b6.l0.r0(0);

        /* renamed from: g  reason: collision with root package name */
        private static final String f9801g = b6.l0.r0(1);

        /* renamed from: h  reason: collision with root package name */
        private static final String f9802h = b6.l0.r0(3);

        /* renamed from: j  reason: collision with root package name */
        private static final String f9803j = b6.l0.r0(4);

        /* renamed from: k  reason: collision with root package name */
        public static final g.a<a> f9804k = i4.p0.a;

        /* renamed from: a  reason: collision with root package name */
        public final int f9805a;

        /* renamed from: b  reason: collision with root package name */
        private final h5.u f9806b;

        /* renamed from: c  reason: collision with root package name */
        private final boolean f9807c;

        /* renamed from: d  reason: collision with root package name */
        private final int[] f9808d;

        /* renamed from: e  reason: collision with root package name */
        private final boolean[] f9809e;

        public a(h5.u uVar, boolean z4, int[] iArr, boolean[] zArr) {
            int i8 = uVar.f20308a;
            this.f9805a = i8;
            boolean z8 = false;
            b6.a.a(i8 == iArr.length && i8 == zArr.length);
            this.f9806b = uVar;
            if (z4 && i8 > 1) {
                z8 = true;
            }
            this.f9807c = z8;
            this.f9808d = (int[]) iArr.clone();
            this.f9809e = (boolean[]) zArr.clone();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ a g(Bundle bundle) {
            h5.u a9 = h5.u.f20307h.a((Bundle) b6.a.e(bundle.getBundle(f9800f)));
            return new a(a9, bundle.getBoolean(f9803j, false), (int[]) com.google.common.base.i.a(bundle.getIntArray(f9801g), new int[a9.f20308a]), (boolean[]) com.google.common.base.i.a(bundle.getBooleanArray(f9802h), new boolean[a9.f20308a]));
        }

        public h5.u b() {
            return this.f9806b;
        }

        public w0 c(int i8) {
            return this.f9806b.b(i8);
        }

        public int d() {
            return this.f9806b.f20310c;
        }

        public boolean e() {
            return com.google.common.primitives.a.b(this.f9809e, true);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || a.class != obj.getClass()) {
                return false;
            }
            a aVar = (a) obj;
            return this.f9807c == aVar.f9807c && this.f9806b.equals(aVar.f9806b) && Arrays.equals(this.f9808d, aVar.f9808d) && Arrays.equals(this.f9809e, aVar.f9809e);
        }

        public boolean f(int i8) {
            return this.f9809e[i8];
        }

        public int hashCode() {
            return (((((this.f9806b.hashCode() * 31) + (this.f9807c ? 1 : 0)) * 31) + Arrays.hashCode(this.f9808d)) * 31) + Arrays.hashCode(this.f9809e);
        }
    }

    public i2(List<a> list) {
        this.f9799a = ImmutableList.x(list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ i2 d(Bundle bundle) {
        ArrayList parcelableArrayList = bundle.getParcelableArrayList(f9797c);
        return new i2(parcelableArrayList == null ? ImmutableList.E() : b6.c.b(a.f9804k, parcelableArrayList));
    }

    public ImmutableList<a> b() {
        return this.f9799a;
    }

    public boolean c(int i8) {
        for (int i9 = 0; i9 < this.f9799a.size(); i9++) {
            a aVar = this.f9799a.get(i9);
            if (aVar.e() && aVar.d() == i8) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || i2.class != obj.getClass()) {
            return false;
        }
        return this.f9799a.equals(((i2) obj).f9799a);
    }

    public int hashCode() {
        return this.f9799a.hashCode();
    }
}
