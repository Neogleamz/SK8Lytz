package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import java.util.ArrayDeque;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m4 {

    /* renamed from: a */
    private final ArrayDeque f14812a = new ArrayDeque();

    public /* synthetic */ m4(l4 l4Var) {
    }

    public static /* bridge */ /* synthetic */ zzdb a(m4 m4Var, zzdb zzdbVar, zzdb zzdbVar2) {
        m4Var.b(zzdbVar);
        m4Var.b(zzdbVar2);
        zzdb zzdbVar3 = (zzdb) m4Var.f14812a.pop();
        while (!m4Var.f14812a.isEmpty()) {
            zzdbVar3 = new q4((zzdb) m4Var.f14812a.pop(), zzdbVar3);
        }
        return zzdbVar3;
    }

    private final void b(zzdb zzdbVar) {
        zzdb zzdbVar2;
        zzdb zzdbVar3;
        if (!zzdbVar.p()) {
            if (!(zzdbVar instanceof q4)) {
                throw new IllegalArgumentException("Has a new type of ByteString been created? Found ".concat(String.valueOf(zzdbVar.getClass())));
            }
            q4 q4Var = (q4) zzdbVar;
            zzdbVar2 = q4Var.f14840f;
            b(zzdbVar2);
            zzdbVar3 = q4Var.f14841g;
            b(zzdbVar3);
            return;
        }
        int c9 = c(zzdbVar.i());
        int O = q4.O(c9 + 1);
        if (this.f14812a.isEmpty() || ((zzdb) this.f14812a.peek()).i() >= O) {
            this.f14812a.push(zzdbVar);
            return;
        }
        int O2 = q4.O(c9);
        zzdb zzdbVar4 = (zzdb) this.f14812a.pop();
        while (!this.f14812a.isEmpty() && ((zzdb) this.f14812a.peek()).i() < O2) {
            zzdbVar4 = new q4((zzdb) this.f14812a.pop(), zzdbVar4);
        }
        q4 q4Var2 = new q4(zzdbVar4, zzdbVar);
        while (!this.f14812a.isEmpty()) {
            if (((zzdb) this.f14812a.peek()).i() >= q4.O(c(q4Var2.i()) + 1)) {
                break;
            }
            q4Var2 = new q4((zzdb) this.f14812a.pop(), q4Var2);
        }
        this.f14812a.push(q4Var2);
    }

    private static final int c(int i8) {
        int binarySearch = Arrays.binarySearch(q4.f14838k, i8);
        return binarySearch < 0 ? (-(binarySearch + 1)) - 1 : binarySearch;
    }
}
