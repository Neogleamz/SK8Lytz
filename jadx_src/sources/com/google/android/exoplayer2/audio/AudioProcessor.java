package com.google.android.exoplayer2.audio;

import b6.l0;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface AudioProcessor {

    /* renamed from: a  reason: collision with root package name */
    public static final ByteBuffer f9231a = ByteBuffer.allocateDirect(0).order(ByteOrder.nativeOrder());

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class UnhandledAudioFormatException extends Exception {
        public UnhandledAudioFormatException(a aVar) {
            super("Unhandled format: " + aVar);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: e  reason: collision with root package name */
        public static final a f9232e = new a(-1, -1, -1);

        /* renamed from: a  reason: collision with root package name */
        public final int f9233a;

        /* renamed from: b  reason: collision with root package name */
        public final int f9234b;

        /* renamed from: c  reason: collision with root package name */
        public final int f9235c;

        /* renamed from: d  reason: collision with root package name */
        public final int f9236d;

        public a(int i8, int i9, int i10) {
            this.f9233a = i8;
            this.f9234b = i9;
            this.f9235c = i10;
            this.f9236d = l0.u0(i10) ? l0.d0(i10, i9) : -1;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof a) {
                a aVar = (a) obj;
                return this.f9233a == aVar.f9233a && this.f9234b == aVar.f9234b && this.f9235c == aVar.f9235c;
            }
            return false;
        }

        public int hashCode() {
            return com.google.common.base.k.b(Integer.valueOf(this.f9233a), Integer.valueOf(this.f9234b), Integer.valueOf(this.f9235c));
        }

        public String toString() {
            return "AudioFormat[sampleRate=" + this.f9233a + ", channelCount=" + this.f9234b + ", encoding=" + this.f9235c + ']';
        }
    }

    boolean b();

    boolean c();

    ByteBuffer d();

    void e(ByteBuffer byteBuffer);

    a f(a aVar);

    void flush();

    void g();

    void reset();
}
