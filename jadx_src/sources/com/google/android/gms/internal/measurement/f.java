package com.google.android.gms.internal.measurement;

import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f implements Iterator<r> {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ Iterator f12169a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ Iterator f12170b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(g gVar, Iterator it, Iterator it2) {
        this.f12169a = it;
        this.f12170b = it2;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        if (this.f12169a.hasNext()) {
            return true;
        }
        return this.f12170b.hasNext();
    }

    @Override // java.util.Iterator
    public final /* synthetic */ r next() {
        if (this.f12169a.hasNext()) {
            return new t(((Integer) this.f12169a.next()).toString());
        }
        if (this.f12170b.hasNext()) {
            return new t((String) this.f12170b.next());
        }
        throw new NoSuchElementException();
    }
}
