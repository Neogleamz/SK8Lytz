package com.google.android.gms.internal.mlkit_vision_barcode;

import com.google.firebase.encoders.EncodingException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p2 implements j8.f {

    /* renamed from: a  reason: collision with root package name */
    private boolean f13880a = false;

    /* renamed from: b  reason: collision with root package name */
    private boolean f13881b = false;

    /* renamed from: c  reason: collision with root package name */
    private j8.b f13882c;

    /* renamed from: d  reason: collision with root package name */
    private final l2 f13883d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public p2(l2 l2Var) {
        this.f13883d = l2Var;
    }

    private final void b() {
        if (this.f13880a) {
            throw new EncodingException("Cannot encode a second value in the ValueEncoderContext");
        }
        this.f13880a = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(j8.b bVar, boolean z4) {
        this.f13880a = false;
        this.f13882c = bVar;
        this.f13881b = z4;
    }

    public final j8.f add(String str) {
        b();
        this.f13883d.f(this.f13882c, str, this.f13881b);
        return this;
    }

    public final j8.f d(boolean z4) {
        b();
        this.f13883d.g(this.f13882c, z4 ? 1 : 0, this.f13881b);
        return this;
    }
}
