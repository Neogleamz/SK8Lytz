package com.google.android.exoplayer2.mediacodec;

import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f extends DecoderInputBuffer {

    /* renamed from: j  reason: collision with root package name */
    private long f10011j;

    /* renamed from: k  reason: collision with root package name */
    private int f10012k;

    /* renamed from: l  reason: collision with root package name */
    private int f10013l;

    public f() {
        super(2);
        this.f10013l = 32;
    }

    private boolean F(DecoderInputBuffer decoderInputBuffer) {
        ByteBuffer byteBuffer;
        if (J()) {
            if (this.f10012k < this.f10013l && decoderInputBuffer.s() == s()) {
                ByteBuffer byteBuffer2 = decoderInputBuffer.f9512c;
                return byteBuffer2 == null || (byteBuffer = this.f9512c) == null || byteBuffer.position() + byteBuffer2.remaining() <= 3072000;
            }
            return false;
        }
        return true;
    }

    public boolean E(DecoderInputBuffer decoderInputBuffer) {
        b6.a.a(!decoderInputBuffer.B());
        b6.a.a(!decoderInputBuffer.r());
        b6.a.a(!decoderInputBuffer.t());
        if (F(decoderInputBuffer)) {
            int i8 = this.f10012k;
            this.f10012k = i8 + 1;
            if (i8 == 0) {
                this.f9514e = decoderInputBuffer.f9514e;
                if (decoderInputBuffer.v()) {
                    x(1);
                }
            }
            if (decoderInputBuffer.s()) {
                x(Integer.MIN_VALUE);
            }
            ByteBuffer byteBuffer = decoderInputBuffer.f9512c;
            if (byteBuffer != null) {
                z(byteBuffer.remaining());
                this.f9512c.put(byteBuffer);
            }
            this.f10011j = decoderInputBuffer.f9514e;
            return true;
        }
        return false;
    }

    public long G() {
        return this.f9514e;
    }

    public long H() {
        return this.f10011j;
    }

    public int I() {
        return this.f10012k;
    }

    public boolean J() {
        return this.f10012k > 0;
    }

    public void K(int i8) {
        b6.a.a(i8 > 0);
        this.f10013l = i8;
    }

    @Override // com.google.android.exoplayer2.decoder.DecoderInputBuffer, l4.a
    public void k() {
        super.k();
        this.f10012k = 0;
    }
}
