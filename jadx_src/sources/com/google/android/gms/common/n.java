package com.google.android.gms.common;

import android.os.RemoteException;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import n6.a0;
import n6.o0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class n extends o0 {

    /* renamed from: a  reason: collision with root package name */
    private final int f11929a;

    /* JADX INFO: Access modifiers changed from: protected */
    public n(byte[] bArr) {
        n6.j.a(bArr.length == 25);
        this.f11929a = Arrays.hashCode(bArr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static byte[] f(String str) {
        try {
            return str.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e8) {
            throw new AssertionError(e8);
        }
    }

    @Override // n6.a0
    public final int a() {
        return this.f11929a;
    }

    @Override // n6.a0
    public final x6.a b() {
        return x6.b.g(g());
    }

    public final boolean equals(Object obj) {
        x6.a b9;
        if (obj != null && (obj instanceof a0)) {
            try {
                a0 a0Var = (a0) obj;
                if (a0Var.a() == this.f11929a && (b9 = a0Var.b()) != null) {
                    return Arrays.equals(g(), (byte[]) x6.b.f(b9));
                }
                return false;
            } catch (RemoteException e8) {
                Log.e("GoogleCertificates", "Failed to get Google certificates from remote", e8);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract byte[] g();

    public final int hashCode() {
        return this.f11929a;
    }
}
