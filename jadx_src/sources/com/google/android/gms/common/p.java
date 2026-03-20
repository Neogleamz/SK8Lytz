package com.google.android.gms.common;

import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class p extends n {

    /* renamed from: c  reason: collision with root package name */
    private static final WeakReference f11931c = new WeakReference(null);

    /* renamed from: b  reason: collision with root package name */
    private WeakReference f11932b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(byte[] bArr) {
        super(bArr);
        this.f11932b = f11931c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.common.n
    public final byte[] g() {
        byte[] bArr;
        synchronized (this) {
            bArr = (byte[]) this.f11932b.get();
            if (bArr == null) {
                bArr = k();
                this.f11932b = new WeakReference(bArr);
            }
        }
        return bArr;
    }

    protected abstract byte[] k();
}
