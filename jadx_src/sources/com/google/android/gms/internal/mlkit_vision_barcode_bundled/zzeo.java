package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class zzeo extends IOException {

    /* renamed from: a  reason: collision with root package name */
    private x3 f15018a;

    public zzeo(IOException iOException) {
        super(iOException.getMessage(), iOException);
        this.f15018a = null;
    }

    public zzeo(String str) {
        super(str);
        this.f15018a = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzen a() {
        return new zzen("Protocol message tag had invalid wire type.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzeo b() {
        return new zzeo("Protocol message contained an invalid tag (zero).");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzeo c() {
        return new zzeo("Protocol message had invalid UTF-8.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzeo d() {
        return new zzeo("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzeo e() {
        return new zzeo("Failed to parse the message.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzeo g() {
        return new zzeo("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either that the input has been truncated or that an embedded message misreported its own length.");
    }

    public final zzeo f(x3 x3Var) {
        this.f15018a = x3Var;
        return this;
    }
}
