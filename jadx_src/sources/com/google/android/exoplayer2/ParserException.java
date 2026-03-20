package com.google.android.exoplayer2;

import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ParserException extends IOException {

    /* renamed from: a  reason: collision with root package name */
    public final boolean f9141a;

    /* renamed from: b  reason: collision with root package name */
    public final int f9142b;

    /* JADX INFO: Access modifiers changed from: protected */
    public ParserException(String str, Throwable th, boolean z4, int i8) {
        super(str, th);
        this.f9141a = z4;
        this.f9142b = i8;
    }

    public static ParserException a(String str, Throwable th) {
        return new ParserException(str, th, true, 1);
    }

    public static ParserException b(String str, Throwable th) {
        return new ParserException(str, th, true, 0);
    }

    public static ParserException c(String str, Throwable th) {
        return new ParserException(str, th, true, 4);
    }

    public static ParserException d(String str) {
        return new ParserException(str, null, false, 1);
    }
}
