package com.google.android.exoplayer2.audio;

import android.media.AudioDeviceInfo;
import com.google.android.exoplayer2.w0;
import com.google.android.exoplayer2.x1;
import j4.t1;
import java.nio.ByteBuffer;
import k4.q;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface AudioSink {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class ConfigurationException extends Exception {

        /* renamed from: a  reason: collision with root package name */
        public final w0 f9237a;

        public ConfigurationException(String str, w0 w0Var) {
            super(str);
            this.f9237a = w0Var;
        }

        public ConfigurationException(Throwable th, w0 w0Var) {
            super(th);
            this.f9237a = w0Var;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class InitializationException extends Exception {

        /* renamed from: a  reason: collision with root package name */
        public final int f9238a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f9239b;

        /* renamed from: c  reason: collision with root package name */
        public final w0 f9240c;

        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public InitializationException(int r4, int r5, int r6, int r7, com.google.android.exoplayer2.w0 r8, boolean r9, java.lang.Exception r10) {
            /*
                r3 = this;
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "AudioTrack init failed "
                r0.append(r1)
                r0.append(r4)
                java.lang.String r1 = " "
                r0.append(r1)
                java.lang.String r2 = "Config("
                r0.append(r2)
                r0.append(r5)
                java.lang.String r5 = ", "
                r0.append(r5)
                r0.append(r6)
                r0.append(r5)
                r0.append(r7)
                java.lang.String r5 = ")"
                r0.append(r5)
                r0.append(r1)
                r0.append(r8)
                if (r9 == 0) goto L38
                java.lang.String r5 = " (recoverable)"
                goto L3a
            L38:
                java.lang.String r5 = ""
            L3a:
                r0.append(r5)
                java.lang.String r5 = r0.toString()
                r3.<init>(r5, r10)
                r3.f9238a = r4
                r3.f9239b = r9
                r3.f9240c = r8
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.audio.AudioSink.InitializationException.<init>(int, int, int, int, com.google.android.exoplayer2.w0, boolean, java.lang.Exception):void");
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class UnexpectedDiscontinuityException extends Exception {

        /* renamed from: a  reason: collision with root package name */
        public final long f9241a;

        /* renamed from: b  reason: collision with root package name */
        public final long f9242b;

        public UnexpectedDiscontinuityException(long j8, long j9) {
            super("Unexpected audio track timestamp discontinuity: expected " + j9 + ", got " + j8);
            this.f9241a = j8;
            this.f9242b = j9;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class WriteException extends Exception {

        /* renamed from: a  reason: collision with root package name */
        public final int f9243a;

        /* renamed from: b  reason: collision with root package name */
        public final boolean f9244b;

        /* renamed from: c  reason: collision with root package name */
        public final w0 f9245c;

        public WriteException(int i8, w0 w0Var, boolean z4) {
            super("AudioTrack write failed: " + i8);
            this.f9244b = z4;
            this.f9243a = i8;
            this.f9245c = w0Var;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(boolean z4);

        default void b(Exception exc) {
        }

        default void c(long j8) {
        }

        default void d() {
        }

        void e(int i8, long j8, long j9);

        void f();

        default void g() {
        }
    }

    boolean a(w0 w0Var);

    boolean b();

    x1 c();

    void d(x1 x1Var);

    void e(float f5);

    void f(boolean z4);

    void flush();

    default void g(AudioDeviceInfo audioDeviceInfo) {
    }

    void h();

    boolean i();

    void j(int i8);

    void k(q qVar);

    long l(boolean z4);

    void m();

    void n(com.google.android.exoplayer2.audio.a aVar);

    default void o(long j8) {
    }

    void p();

    void pause();

    void play();

    default void q(t1 t1Var) {
    }

    void r();

    void reset();

    boolean s(ByteBuffer byteBuffer, long j8, int i8);

    void t(a aVar);

    int u(w0 w0Var);

    void v(w0 w0Var, int i8, int[] iArr);

    void w();
}
