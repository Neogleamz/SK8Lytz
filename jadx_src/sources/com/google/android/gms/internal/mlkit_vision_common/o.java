package com.google.android.gms.internal.mlkit_vision_common;

import com.google.firebase.encoders.EncodingException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class o implements j8.f {

    /* renamed from: a  reason: collision with root package name */
    private boolean f15728a = false;

    /* renamed from: b  reason: collision with root package name */
    private boolean f15729b = false;

    /* renamed from: c  reason: collision with root package name */
    private j8.b f15730c;

    /* renamed from: d  reason: collision with root package name */
    private final k f15731d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public o(k kVar) {
        this.f15731d = kVar;
    }

    private final void b() {
        if (this.f15728a) {
            throw new EncodingException("Cannot encode a second value in the ValueEncoderContext");
        }
        this.f15728a = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void a(j8.b bVar, boolean z4) {
        this.f15728a = false;
        this.f15730c = bVar;
        this.f15729b = z4;
    }

    public final j8.f add(String str) {
        b();
        this.f15731d.f(this.f15730c, str, this.f15729b);
        return this;
    }

    public final j8.f d(boolean z4) {
        b();
        this.f15731d.g(this.f15730c, z4 ? 1 : 0, this.f15729b);
        return this;
    }
}
