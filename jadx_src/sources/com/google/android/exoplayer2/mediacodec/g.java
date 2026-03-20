package com.google.android.exoplayer2.mediacodec;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.w0;
import java.nio.ByteBuffer;
import k4.u;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g {

    /* renamed from: a  reason: collision with root package name */
    private long f10014a;

    /* renamed from: b  reason: collision with root package name */
    private long f10015b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f10016c;

    private long a(long j8) {
        return this.f10014a + Math.max(0L, ((this.f10015b - 529) * 1000000) / j8);
    }

    public long b(w0 w0Var) {
        return a(w0Var.G);
    }

    public void c() {
        this.f10014a = 0L;
        this.f10015b = 0L;
        this.f10016c = false;
    }

    public long d(w0 w0Var, DecoderInputBuffer decoderInputBuffer) {
        if (this.f10015b == 0) {
            this.f10014a = decoderInputBuffer.f9514e;
        }
        if (this.f10016c) {
            return decoderInputBuffer.f9514e;
        }
        ByteBuffer byteBuffer = (ByteBuffer) b6.a.e(decoderInputBuffer.f9512c);
        int i8 = 0;
        for (int i9 = 0; i9 < 4; i9++) {
            i8 = (i8 << 8) | (byteBuffer.get(i9) & 255);
        }
        int m8 = u.m(i8);
        if (m8 != -1) {
            long a9 = a(w0Var.G);
            this.f10015b += m8;
            return a9;
        }
        this.f10016c = true;
        this.f10015b = 0L;
        this.f10014a = decoderInputBuffer.f9514e;
        b6.p.i("C2Mp3TimestampTracker", "MPEG audio header is invalid.");
        return decoderInputBuffer.f9514e;
    }
}
