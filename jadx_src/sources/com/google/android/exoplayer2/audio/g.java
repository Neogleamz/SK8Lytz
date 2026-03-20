package com.google.android.exoplayer2.audio;

import b6.l0;
import com.google.android.exoplayer2.audio.DefaultAudioSink;
import java.math.RoundingMode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g implements DefaultAudioSink.e {

    /* renamed from: b  reason: collision with root package name */
    protected final int f9379b;

    /* renamed from: c  reason: collision with root package name */
    protected final int f9380c;

    /* renamed from: d  reason: collision with root package name */
    protected final int f9381d;

    /* renamed from: e  reason: collision with root package name */
    protected final int f9382e;

    /* renamed from: f  reason: collision with root package name */
    protected final int f9383f;

    /* renamed from: g  reason: collision with root package name */
    public final int f9384g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private int f9385a = 250000;

        /* renamed from: b  reason: collision with root package name */
        private int f9386b = 750000;

        /* renamed from: c  reason: collision with root package name */
        private int f9387c = 4;

        /* renamed from: d  reason: collision with root package name */
        private int f9388d = 250000;

        /* renamed from: e  reason: collision with root package name */
        private int f9389e = 50000000;

        /* renamed from: f  reason: collision with root package name */
        private int f9390f = 2;

        public g g() {
            return new g(this);
        }
    }

    protected g(a aVar) {
        this.f9379b = aVar.f9385a;
        this.f9380c = aVar.f9386b;
        this.f9381d = aVar.f9387c;
        this.f9382e = aVar.f9388d;
        this.f9383f = aVar.f9389e;
        this.f9384g = aVar.f9390f;
    }

    protected static int b(int i8, int i9, int i10) {
        return com.google.common.primitives.g.d(((i8 * i9) * i10) / 1000000);
    }

    protected static int d(int i8) {
        switch (i8) {
            case 5:
                return 80000;
            case 6:
            case 18:
                return 768000;
            case 7:
                return 192000;
            case 8:
                return 2250000;
            case 9:
                return 40000;
            case 10:
                return 100000;
            case 11:
                return 16000;
            case 12:
                return 7000;
            case 13:
            case 19:
            default:
                throw new IllegalArgumentException();
            case 14:
                return 3062500;
            case 15:
                return 8000;
            case 16:
                return 256000;
            case 17:
                return 336000;
            case 20:
                return 63750;
        }
    }

    @Override // com.google.android.exoplayer2.audio.DefaultAudioSink.e
    public int a(int i8, int i9, int i10, int i11, int i12, int i13, double d8) {
        return (((Math.max(i8, (int) (c(i8, i9, i10, i11, i12, i13) * d8)) + i11) - 1) / i11) * i11;
    }

    protected int c(int i8, int i9, int i10, int i11, int i12, int i13) {
        if (i10 != 0) {
            if (i10 != 1) {
                if (i10 == 2) {
                    return f(i9, i13);
                }
                throw new IllegalArgumentException();
            }
            return e(i9);
        }
        return g(i8, i12, i11);
    }

    protected int e(int i8) {
        return com.google.common.primitives.g.d((this.f9383f * d(i8)) / 1000000);
    }

    protected int f(int i8, int i9) {
        int i10 = this.f9382e;
        if (i8 == 5) {
            i10 *= this.f9384g;
        }
        return com.google.common.primitives.g.d((i10 * (i9 != -1 ? b8.b.b(i9, 8, RoundingMode.CEILING) : d(i8))) / 1000000);
    }

    protected int g(int i8, int i9, int i10) {
        return l0.q(i8 * this.f9381d, b(this.f9379b, i9, i10), b(this.f9380c, i9, i10));
    }
}
