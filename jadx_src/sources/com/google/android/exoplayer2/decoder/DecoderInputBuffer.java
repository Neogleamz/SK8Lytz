package com.google.android.exoplayer2.decoder;

import i4.q;
import java.nio.ByteBuffer;
import l4.a;
import l4.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class DecoderInputBuffer extends a {

    /* renamed from: b  reason: collision with root package name */
    public final c f9511b;

    /* renamed from: c  reason: collision with root package name */
    public ByteBuffer f9512c;

    /* renamed from: d  reason: collision with root package name */
    public boolean f9513d;

    /* renamed from: e  reason: collision with root package name */
    public long f9514e;

    /* renamed from: f  reason: collision with root package name */
    public ByteBuffer f9515f;

    /* renamed from: g  reason: collision with root package name */
    private final int f9516g;

    /* renamed from: h  reason: collision with root package name */
    private final int f9517h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class InsufficientCapacityException extends IllegalStateException {

        /* renamed from: a  reason: collision with root package name */
        public final int f9518a;

        /* renamed from: b  reason: collision with root package name */
        public final int f9519b;

        public InsufficientCapacityException(int i8, int i9) {
            super("Buffer too small (" + i8 + " < " + i9 + ")");
            this.f9518a = i8;
            this.f9519b = i9;
        }
    }

    static {
        q.a("goog.exo.decoder");
    }

    public DecoderInputBuffer(int i8) {
        this(i8, 0);
    }

    public DecoderInputBuffer(int i8, int i9) {
        this.f9511b = new c();
        this.f9516g = i8;
        this.f9517h = i9;
    }

    public static DecoderInputBuffer C() {
        return new DecoderInputBuffer(0);
    }

    private ByteBuffer y(int i8) {
        int i9 = this.f9516g;
        if (i9 == 1) {
            return ByteBuffer.allocate(i8);
        }
        if (i9 == 2) {
            return ByteBuffer.allocateDirect(i8);
        }
        ByteBuffer byteBuffer = this.f9512c;
        throw new InsufficientCapacityException(byteBuffer == null ? 0 : byteBuffer.capacity(), i8);
    }

    public final void A() {
        ByteBuffer byteBuffer = this.f9512c;
        if (byteBuffer != null) {
            byteBuffer.flip();
        }
        ByteBuffer byteBuffer2 = this.f9515f;
        if (byteBuffer2 != null) {
            byteBuffer2.flip();
        }
    }

    public final boolean B() {
        return q(1073741824);
    }

    public void D(int i8) {
        ByteBuffer byteBuffer = this.f9515f;
        if (byteBuffer == null || byteBuffer.capacity() < i8) {
            this.f9515f = ByteBuffer.allocate(i8);
        } else {
            this.f9515f.clear();
        }
    }

    @Override // l4.a
    public void k() {
        super.k();
        ByteBuffer byteBuffer = this.f9512c;
        if (byteBuffer != null) {
            byteBuffer.clear();
        }
        ByteBuffer byteBuffer2 = this.f9515f;
        if (byteBuffer2 != null) {
            byteBuffer2.clear();
        }
        this.f9513d = false;
    }

    public void z(int i8) {
        int i9 = i8 + this.f9517h;
        ByteBuffer byteBuffer = this.f9512c;
        if (byteBuffer == null) {
            this.f9512c = y(i9);
            return;
        }
        int capacity = byteBuffer.capacity();
        int position = byteBuffer.position();
        int i10 = i9 + position;
        if (capacity >= i10) {
            this.f9512c = byteBuffer;
            return;
        }
        ByteBuffer y8 = y(i10);
        y8.order(byteBuffer.order());
        if (position > 0) {
            byteBuffer.flip();
            y8.put(byteBuffer);
        }
        this.f9512c = y8;
    }
}
