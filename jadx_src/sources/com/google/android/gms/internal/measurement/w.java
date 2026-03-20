package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class w implements Iterator<r> {

    /* renamed from: a  reason: collision with root package name */
    private int f12626a = 0;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ t f12627b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w(t tVar) {
        this.f12627b = tVar;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        String str;
        int i8 = this.f12626a;
        str = this.f12627b.f12514a;
        return i8 < str.length();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ r next() {
        String str;
        int i8 = this.f12626a;
        str = this.f12627b.f12514a;
        if (i8 < str.length()) {
            int i9 = this.f12626a;
            this.f12626a = i9 + 1;
            return new t(String.valueOf(i9));
        }
        throw new NoSuchElementException();
    }
}
