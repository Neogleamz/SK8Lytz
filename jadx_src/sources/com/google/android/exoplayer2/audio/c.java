package com.google.android.exoplayer2.audio;

import android.annotation.TargetApi;
import android.media.AudioTimestamp;
import android.media.AudioTrack;
import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class c {

    /* renamed from: a  reason: collision with root package name */
    private final a f9334a;

    /* renamed from: b  reason: collision with root package name */
    private int f9335b;

    /* renamed from: c  reason: collision with root package name */
    private long f9336c;

    /* renamed from: d  reason: collision with root package name */
    private long f9337d;

    /* renamed from: e  reason: collision with root package name */
    private long f9338e;

    /* renamed from: f  reason: collision with root package name */
    private long f9339f;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final AudioTrack f9340a;

        /* renamed from: b  reason: collision with root package name */
        private final AudioTimestamp f9341b = new AudioTimestamp();

        /* renamed from: c  reason: collision with root package name */
        private long f9342c;

        /* renamed from: d  reason: collision with root package name */
        private long f9343d;

        /* renamed from: e  reason: collision with root package name */
        private long f9344e;

        public a(AudioTrack audioTrack) {
            this.f9340a = audioTrack;
        }

        public long a() {
            return this.f9344e;
        }

        public long b() {
            return this.f9341b.nanoTime / 1000;
        }

        public boolean c() {
            boolean timestamp = this.f9340a.getTimestamp(this.f9341b);
            if (timestamp) {
                long j8 = this.f9341b.framePosition;
                if (this.f9343d > j8) {
                    this.f9342c++;
                }
                this.f9343d = j8;
                this.f9344e = j8 + (this.f9342c << 32);
            }
            return timestamp;
        }
    }

    public c(AudioTrack audioTrack) {
        if (l0.f8063a >= 19) {
            this.f9334a = new a(audioTrack);
            g();
            return;
        }
        this.f9334a = null;
        h(3);
    }

    private void h(int i8) {
        this.f9335b = i8;
        long j8 = 10000;
        if (i8 == 0) {
            this.f9338e = 0L;
            this.f9339f = -1L;
            this.f9336c = System.nanoTime() / 1000;
        } else if (i8 != 1) {
            if (i8 == 2 || i8 == 3) {
                j8 = 10000000;
            } else if (i8 != 4) {
                throw new IllegalStateException();
            } else {
                j8 = 500000;
            }
        }
        this.f9337d = j8;
    }

    public void a() {
        if (this.f9335b == 4) {
            g();
        }
    }

    @TargetApi(19)
    public long b() {
        a aVar = this.f9334a;
        if (aVar != null) {
            return aVar.a();
        }
        return -1L;
    }

    @TargetApi(19)
    public long c() {
        a aVar = this.f9334a;
        if (aVar != null) {
            return aVar.b();
        }
        return -9223372036854775807L;
    }

    public boolean d() {
        return this.f9335b == 2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x002d, code lost:
        if (r0 != false) goto L19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0030, code lost:
        if (r0 == false) goto L19;
     */
    @android.annotation.TargetApi(19)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean e(long r7) {
        /*
            r6 = this;
            com.google.android.exoplayer2.audio.c$a r0 = r6.f9334a
            r1 = 0
            if (r0 == 0) goto L71
            long r2 = r6.f9338e
            long r2 = r7 - r2
            long r4 = r6.f9337d
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r2 >= 0) goto L10
            goto L71
        L10:
            r6.f9338e = r7
            boolean r0 = r0.c()
            int r2 = r6.f9335b
            r3 = 3
            r4 = 1
            if (r2 == 0) goto L49
            r7 = 2
            if (r2 == r4) goto L33
            if (r2 == r7) goto L30
            if (r2 == r3) goto L2d
            r7 = 4
            if (r2 != r7) goto L27
            goto L70
        L27:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            r7.<init>()
            throw r7
        L2d:
            if (r0 == 0) goto L70
            goto L45
        L30:
            if (r0 != 0) goto L70
            goto L45
        L33:
            if (r0 == 0) goto L45
            com.google.android.exoplayer2.audio.c$a r8 = r6.f9334a
            long r1 = r8.a()
            long r3 = r6.f9339f
            int r8 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r8 <= 0) goto L70
            r6.h(r7)
            goto L70
        L45:
            r6.g()
            goto L70
        L49:
            if (r0 == 0) goto L63
            com.google.android.exoplayer2.audio.c$a r7 = r6.f9334a
            long r7 = r7.b()
            long r2 = r6.f9336c
            int r7 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r7 < 0) goto L71
            com.google.android.exoplayer2.audio.c$a r7 = r6.f9334a
            long r7 = r7.a()
            r6.f9339f = r7
            r6.h(r4)
            goto L70
        L63:
            long r1 = r6.f9336c
            long r7 = r7 - r1
            r1 = 500000(0x7a120, double:2.47033E-318)
            int r7 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1))
            if (r7 <= 0) goto L70
            r6.h(r3)
        L70:
            r1 = r0
        L71:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.audio.c.e(long):boolean");
    }

    public void f() {
        h(4);
    }

    public void g() {
        if (this.f9334a != null) {
            h(0);
        }
    }
}
