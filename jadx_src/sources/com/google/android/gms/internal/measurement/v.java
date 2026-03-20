package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class v implements Iterator<r> {

    /* renamed from: a  reason: collision with root package name */
    private int f12571a = 0;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ t f12572b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v(t tVar) {
        this.f12572b = tVar;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        String str;
        int i8 = this.f12571a;
        str = this.f12572b.f12514a;
        return i8 < str.length();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ r next() {
        String str;
        String str2;
        int i8 = this.f12571a;
        str = this.f12572b.f12514a;
        if (i8 < str.length()) {
            str2 = this.f12572b.f12514a;
            int i9 = this.f12571a;
            this.f12571a = i9 + 1;
            return new t(String.valueOf(str2.charAt(i9)));
        }
        throw new NoSuchElementException();
    }
}
