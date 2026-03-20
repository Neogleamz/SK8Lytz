package com.google.android.exoplayer2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ExoTimeoutException extends RuntimeException {

    /* renamed from: a  reason: collision with root package name */
    public final int f9137a;

    public ExoTimeoutException(int i8) {
        super(a(i8));
        this.f9137a = i8;
    }

    private static String a(int i8) {
        return i8 != 1 ? i8 != 2 ? i8 != 3 ? "Undefined timeout." : "Detaching surface timed out." : "Setting foreground mode timed out." : "Player release timed out.";
    }
}
