package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import com.google.android.gms.internal.mlkit_vision_barcode_bundled.j2;
import com.google.android.gms.internal.mlkit_vision_barcode_bundled.p2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j2<MessageType extends p2<MessageType, BuilderType>, BuilderType extends j2<MessageType, BuilderType>> extends x0<MessageType, BuilderType> {

    /* renamed from: a  reason: collision with root package name */
    private final p2 f14797a;

    /* renamed from: b  reason: collision with root package name */
    protected p2 f14798b;

    /* JADX INFO: Access modifiers changed from: protected */
    public j2(MessageType messagetype) {
        this.f14797a = messagetype;
        if (messagetype.E()) {
            throw new IllegalArgumentException("Default instance must be immutable.");
        }
        this.f14798b = messagetype.o();
    }

    private static void g(Object obj, Object obj2) {
        g4.a().b(obj.getClass()).g(obj, obj2);
    }

    /* renamed from: j */
    public final j2 clone() {
        j2 j2Var = (j2) this.f14797a.H(5, null, null);
        j2Var.f14798b = u();
        return j2Var;
    }

    public final j2 k(p2 p2Var) {
        if (!this.f14797a.equals(p2Var)) {
            if (!this.f14798b.E()) {
                r();
            }
            g(this.f14798b, p2Var);
        }
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w3
    /* renamed from: l */
    public final MessageType i() {
        MessageType u8 = u();
        if (u8.m()) {
            return u8;
        }
        throw new zzgx(u8);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.y3
    public final boolean m() {
        return p2.D(this.f14798b, false);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.w3
    /* renamed from: o */
    public MessageType u() {
        if (this.f14798b.E()) {
            this.f14798b.z();
            return (MessageType) this.f14798b;
        }
        return (MessageType) this.f14798b;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void q() {
        if (this.f14798b.E()) {
            return;
        }
        r();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void r() {
        p2 o5 = this.f14797a.o();
        g(o5, this.f14798b);
        this.f14798b = o5;
    }
}
