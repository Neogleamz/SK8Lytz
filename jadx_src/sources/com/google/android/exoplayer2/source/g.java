package com.google.android.exoplayer2.source;

import android.net.Uri;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g implements a6.h {

    /* renamed from: a  reason: collision with root package name */
    private final a6.h f10434a;

    /* renamed from: b  reason: collision with root package name */
    private final int f10435b;

    /* renamed from: c  reason: collision with root package name */
    private final a f10436c;

    /* renamed from: d  reason: collision with root package name */
    private final byte[] f10437d;

    /* renamed from: e  reason: collision with root package name */
    private int f10438e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void b(b6.z zVar);
    }

    public g(a6.h hVar, int i8, a aVar) {
        b6.a.a(i8 > 0);
        this.f10434a = hVar;
        this.f10435b = i8;
        this.f10436c = aVar;
        this.f10437d = new byte[1];
        this.f10438e = i8;
    }

    private boolean l() {
        if (this.f10434a.read(this.f10437d, 0, 1) == -1) {
            return false;
        }
        int i8 = (this.f10437d[0] & 255) << 4;
        if (i8 == 0) {
            return true;
        }
        byte[] bArr = new byte[i8];
        int i9 = i8;
        int i10 = 0;
        while (i9 > 0) {
            int read = this.f10434a.read(bArr, i10, i9);
            if (read == -1) {
                return false;
            }
            i10 += read;
            i9 -= read;
        }
        while (i8 > 0 && bArr[i8 - 1] == 0) {
            i8--;
        }
        if (i8 > 0) {
            this.f10436c.b(new b6.z(bArr, i8));
        }
        return true;
    }

    @Override // a6.h
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override // a6.f
    public int read(byte[] bArr, int i8, int i9) {
        if (this.f10438e == 0) {
            if (!l()) {
                return -1;
            }
            this.f10438e = this.f10435b;
        }
        int read = this.f10434a.read(bArr, i8, Math.min(this.f10438e, i9));
        if (read != -1) {
            this.f10438e -= read;
        }
        return read;
    }

    @Override // a6.h
    public Uri v() {
        return this.f10434a.v();
    }

    @Override // a6.h
    public void w(a6.y yVar) {
        b6.a.e(yVar);
        this.f10434a.w(yVar);
    }

    @Override // a6.h
    public long x(com.google.android.exoplayer2.upstream.a aVar) {
        throw new UnsupportedOperationException();
    }

    @Override // a6.h
    public Map<String, List<String>> y() {
        return this.f10434a.y();
    }
}
