package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.c;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class c<T extends IInterface> extends b<T> implements a.f {
    private final n6.c Q;
    private final Set R;
    private final Account T;

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public c(Context context, Looper looper, int i8, n6.c cVar, c.a aVar, c.b bVar) {
        this(context, looper, i8, cVar, (l6.c) aVar, (l6.h) bVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public c(Context context, Looper looper, int i8, n6.c cVar, l6.c cVar2, l6.h hVar) {
        this(context, looper, d.b(context), com.google.android.gms.common.a.m(), i8, cVar, (l6.c) n6.j.l(cVar2), (l6.h) n6.j.l(hVar));
    }

    @VisibleForTesting
    protected c(Context context, Looper looper, d dVar, com.google.android.gms.common.a aVar, int i8, n6.c cVar, l6.c cVar2, l6.h hVar) {
        super(context, looper, dVar, aVar, i8, cVar2 == null ? null : new f(cVar2), hVar == null ? null : new g(hVar), cVar.h());
        this.Q = cVar;
        this.T = cVar.a();
        this.R = i0(cVar.c());
    }

    private final Set i0(Set set) {
        Set<Scope> h02 = h0(set);
        for (Scope scope : h02) {
            if (!set.contains(scope)) {
                throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
            }
        }
        return h02;
    }

    @Override // com.google.android.gms.common.internal.b
    protected final Set<Scope> A() {
        return this.R;
    }

    @Override // com.google.android.gms.common.api.a.f
    public Set<Scope> a() {
        return m() ? this.R : Collections.emptySet();
    }

    protected Set<Scope> h0(Set<Scope> set) {
        return set;
    }

    @Override // com.google.android.gms.common.internal.b
    public final Account s() {
        return this.T;
    }

    @Override // com.google.android.gms.common.internal.b
    protected final Executor u() {
        return null;
    }
}
