package com.google.android.exoplayer2.upstream;

import h5.h;
import h5.i;
import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface c {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final int f10964a;

        /* renamed from: b  reason: collision with root package name */
        public final int f10965b;

        /* renamed from: c  reason: collision with root package name */
        public final int f10966c;

        /* renamed from: d  reason: collision with root package name */
        public final int f10967d;

        public a(int i8, int i9, int i10, int i11) {
            this.f10964a = i8;
            this.f10965b = i9;
            this.f10966c = i10;
            this.f10967d = i11;
        }

        public boolean a(int i8) {
            if (i8 == 1) {
                if (this.f10964a - this.f10965b <= 1) {
                    return false;
                }
            } else if (this.f10966c - this.f10967d <= 1) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final int f10968a;

        /* renamed from: b  reason: collision with root package name */
        public final long f10969b;

        public b(int i8, long j8) {
            b6.a.a(j8 >= 0);
            this.f10968a = i8;
            this.f10969b = j8;
        }
    }

    /* renamed from: com.google.android.exoplayer2.upstream.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0117c {

        /* renamed from: a  reason: collision with root package name */
        public final h f10970a;

        /* renamed from: b  reason: collision with root package name */
        public final i f10971b;

        /* renamed from: c  reason: collision with root package name */
        public final IOException f10972c;

        /* renamed from: d  reason: collision with root package name */
        public final int f10973d;

        public C0117c(h hVar, i iVar, IOException iOException, int i8) {
            this.f10970a = hVar;
            this.f10971b = iVar;
            this.f10972c = iOException;
            this.f10973d = i8;
        }
    }

    long a(C0117c c0117c);

    b b(a aVar, C0117c c0117c);

    default void c(long j8) {
    }

    int d(int i8);
}
