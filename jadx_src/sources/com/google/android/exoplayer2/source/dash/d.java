package com.google.android.exoplayer2.source.dash;

import b6.l0;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.w0;
import h5.r;
import i4.s;
import l5.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d implements r {

    /* renamed from: a  reason: collision with root package name */
    private final w0 f10396a;

    /* renamed from: c  reason: collision with root package name */
    private long[] f10398c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f10399d;

    /* renamed from: e  reason: collision with root package name */
    private f f10400e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f10401f;

    /* renamed from: g  reason: collision with root package name */
    private int f10402g;

    /* renamed from: b  reason: collision with root package name */
    private final c5.b f10397b = new c5.b();

    /* renamed from: h  reason: collision with root package name */
    private long f10403h = -9223372036854775807L;

    public d(f fVar, w0 w0Var, boolean z4) {
        this.f10396a = w0Var;
        this.f10400e = fVar;
        this.f10398c = fVar.f21665b;
        d(fVar, z4);
    }

    @Override // h5.r
    public void a() {
    }

    public String b() {
        return this.f10400e.a();
    }

    public void c(long j8) {
        boolean z4 = true;
        int e8 = l0.e(this.f10398c, j8, true, false);
        this.f10402g = e8;
        if (!this.f10399d || e8 != this.f10398c.length) {
            z4 = false;
        }
        if (!z4) {
            j8 = -9223372036854775807L;
        }
        this.f10403h = j8;
    }

    public void d(f fVar, boolean z4) {
        int i8 = this.f10402g;
        long j8 = i8 == 0 ? -9223372036854775807L : this.f10398c[i8 - 1];
        this.f10399d = z4;
        this.f10400e = fVar;
        long[] jArr = fVar.f21665b;
        this.f10398c = jArr;
        long j9 = this.f10403h;
        if (j9 != -9223372036854775807L) {
            c(j9);
        } else if (j8 != -9223372036854775807L) {
            this.f10402g = l0.e(jArr, j8, false, false);
        }
    }

    @Override // h5.r
    public boolean e() {
        return true;
    }

    @Override // h5.r
    public int m(long j8) {
        int max = Math.max(this.f10402g, l0.e(this.f10398c, j8, true, false));
        int i8 = max - this.f10402g;
        this.f10402g = max;
        return i8;
    }

    @Override // h5.r
    public int o(s sVar, DecoderInputBuffer decoderInputBuffer, int i8) {
        int i9 = this.f10402g;
        boolean z4 = i9 == this.f10398c.length;
        if (z4 && !this.f10399d) {
            decoderInputBuffer.x(4);
            return -4;
        } else if ((i8 & 2) != 0 || !this.f10401f) {
            sVar.f20512b = this.f10396a;
            this.f10401f = true;
            return -5;
        } else if (z4) {
            return -3;
        } else {
            if ((i8 & 1) == 0) {
                this.f10402g = i9 + 1;
            }
            if ((i8 & 4) == 0) {
                byte[] a9 = this.f10397b.a(this.f10400e.f21664a[i9]);
                decoderInputBuffer.z(a9.length);
                decoderInputBuffer.f9512c.put(a9);
            }
            decoderInputBuffer.f9514e = this.f10398c[i9];
            decoderInputBuffer.x(1);
            return -4;
        }
    }
}
