package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.f7;
import com.google.android.gms.internal.measurement.g7;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class f7<MessageType extends g7<MessageType, BuilderType>, BuilderType extends f7<MessageType, BuilderType>> implements ha {
    @Override // com.google.android.gms.internal.measurement.ha
    public final /* synthetic */ ha E0(byte[] bArr, l8 l8Var) {
        return l(bArr, 0, bArr.length, l8Var);
    }

    @Override // com.google.android.gms.internal.measurement.ha
    public final /* synthetic */ ha Z(byte[] bArr) {
        return k(bArr, 0, bArr.length);
    }

    public abstract /* synthetic */ Object clone();

    public abstract BuilderType k(byte[] bArr, int i8, int i9);

    public abstract BuilderType l(byte[] bArr, int i8, int i9, l8 l8Var);
}
