package com.google.android.exoplayer2.audio;

import android.media.AudioAttributes;
import android.os.Bundle;
import b6.l0;
import com.google.android.exoplayer2.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements com.google.android.exoplayer2.g {

    /* renamed from: g  reason: collision with root package name */
    public static final a f9313g = new e().a();

    /* renamed from: h  reason: collision with root package name */
    private static final String f9314h = l0.r0(0);

    /* renamed from: j  reason: collision with root package name */
    private static final String f9315j = l0.r0(1);

    /* renamed from: k  reason: collision with root package name */
    private static final String f9316k = l0.r0(2);

    /* renamed from: l  reason: collision with root package name */
    private static final String f9317l = l0.r0(3);

    /* renamed from: m  reason: collision with root package name */
    private static final String f9318m = l0.r0(4);

    /* renamed from: n  reason: collision with root package name */
    public static final g.a<a> f9319n = k4.d.a;

    /* renamed from: a  reason: collision with root package name */
    public final int f9320a;

    /* renamed from: b  reason: collision with root package name */
    public final int f9321b;

    /* renamed from: c  reason: collision with root package name */
    public final int f9322c;

    /* renamed from: d  reason: collision with root package name */
    public final int f9323d;

    /* renamed from: e  reason: collision with root package name */
    public final int f9324e;

    /* renamed from: f  reason: collision with root package name */
    private d f9325f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b {
        public static void a(AudioAttributes.Builder builder, int i8) {
            builder.setAllowedCapturePolicy(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class c {
        public static void a(AudioAttributes.Builder builder, int i8) {
            builder.setSpatializationBehavior(i8);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class d {

        /* renamed from: a  reason: collision with root package name */
        public final AudioAttributes f9326a;

        private d(a aVar) {
            AudioAttributes.Builder usage = new AudioAttributes.Builder().setContentType(aVar.f9320a).setFlags(aVar.f9321b).setUsage(aVar.f9322c);
            int i8 = l0.f8063a;
            if (i8 >= 29) {
                b.a(usage, aVar.f9323d);
            }
            if (i8 >= 32) {
                c.a(usage, aVar.f9324e);
            }
            this.f9326a = usage.build();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        private int f9327a = 0;

        /* renamed from: b  reason: collision with root package name */
        private int f9328b = 0;

        /* renamed from: c  reason: collision with root package name */
        private int f9329c = 1;

        /* renamed from: d  reason: collision with root package name */
        private int f9330d = 1;

        /* renamed from: e  reason: collision with root package name */
        private int f9331e = 0;

        public a a() {
            return new a(this.f9327a, this.f9328b, this.f9329c, this.f9330d, this.f9331e);
        }

        public e b(int i8) {
            this.f9330d = i8;
            return this;
        }

        public e c(int i8) {
            this.f9327a = i8;
            return this;
        }

        public e d(int i8) {
            this.f9328b = i8;
            return this;
        }

        public e e(int i8) {
            this.f9331e = i8;
            return this;
        }

        public e f(int i8) {
            this.f9329c = i8;
            return this;
        }
    }

    private a(int i8, int i9, int i10, int i11, int i12) {
        this.f9320a = i8;
        this.f9321b = i9;
        this.f9322c = i10;
        this.f9323d = i11;
        this.f9324e = i12;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ a c(Bundle bundle) {
        e eVar = new e();
        String str = f9314h;
        if (bundle.containsKey(str)) {
            eVar.c(bundle.getInt(str));
        }
        String str2 = f9315j;
        if (bundle.containsKey(str2)) {
            eVar.d(bundle.getInt(str2));
        }
        String str3 = f9316k;
        if (bundle.containsKey(str3)) {
            eVar.f(bundle.getInt(str3));
        }
        String str4 = f9317l;
        if (bundle.containsKey(str4)) {
            eVar.b(bundle.getInt(str4));
        }
        String str5 = f9318m;
        if (bundle.containsKey(str5)) {
            eVar.e(bundle.getInt(str5));
        }
        return eVar.a();
    }

    public d b() {
        if (this.f9325f == null) {
            this.f9325f = new d();
        }
        return this.f9325f;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || a.class != obj.getClass()) {
            return false;
        }
        a aVar = (a) obj;
        return this.f9320a == aVar.f9320a && this.f9321b == aVar.f9321b && this.f9322c == aVar.f9322c && this.f9323d == aVar.f9323d && this.f9324e == aVar.f9324e;
    }

    public int hashCode() {
        return ((((((((527 + this.f9320a) * 31) + this.f9321b) * 31) + this.f9322c) * 31) + this.f9323d) * 31) + this.f9324e;
    }
}
