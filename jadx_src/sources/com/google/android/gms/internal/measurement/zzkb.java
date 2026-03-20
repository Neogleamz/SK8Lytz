package com.google.android.gms.internal.measurement;

import java.io.IOException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class zzkb extends IOException {

    /* renamed from: a  reason: collision with root package name */
    private ia f12903a;

    public zzkb(String str) {
        super(str);
        this.f12903a = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzke a() {
        return new zzke("Protocol message tag had invalid wire type.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzkb b() {
        return new zzkb("Protocol message contained an invalid tag (zero).");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzkb c() {
        return new zzkb("Protocol message had invalid UTF-8.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzkb d() {
        return new zzkb("CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzkb e() {
        return new zzkb("Failed to parse the message.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzkb f() {
        return new zzkb("While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either that the input has been truncated or that an embedded message misreported its own length.");
    }
}
