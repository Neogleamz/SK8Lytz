package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class o4 implements Iterator {

    /* renamed from: a  reason: collision with root package name */
    private final ArrayDeque f14829a;

    /* renamed from: b  reason: collision with root package name */
    private l1 f14830b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ o4(zzdb zzdbVar, n4 n4Var) {
        l1 l1Var;
        zzdb zzdbVar2;
        if (zzdbVar instanceof q4) {
            q4 q4Var = (q4) zzdbVar;
            ArrayDeque arrayDeque = new ArrayDeque(q4Var.n());
            this.f14829a = arrayDeque;
            arrayDeque.push(q4Var);
            zzdbVar2 = q4Var.f14840f;
            l1Var = b(zzdbVar2);
        } else {
            this.f14829a = null;
            l1Var = (l1) zzdbVar;
        }
        this.f14830b = l1Var;
    }

    private final l1 b(zzdb zzdbVar) {
        while (zzdbVar instanceof q4) {
            q4 q4Var = (q4) zzdbVar;
            this.f14829a.push(q4Var);
            zzdbVar = q4Var.f14840f;
        }
        return (l1) zzdbVar;
    }

    @Override // java.util.Iterator
    /* renamed from: a */
    public final l1 next() {
        l1 l1Var;
        zzdb zzdbVar;
        l1 l1Var2 = this.f14830b;
        if (l1Var2 != null) {
            do {
                ArrayDeque arrayDeque = this.f14829a;
                l1Var = null;
                if (arrayDeque == null || arrayDeque.isEmpty()) {
                    break;
                }
                zzdbVar = ((q4) this.f14829a.pop()).f14841g;
                l1Var = b(zzdbVar);
            } while (l1Var.i() == 0);
            this.f14830b = l1Var;
            return l1Var2;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return this.f14830b != null;
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
