package com.google.android.gms.common.internal;

import android.util.Log;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class q {

    /* renamed from: a  reason: collision with root package name */
    private Object f11857a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f11858b = false;

    /* renamed from: c  reason: collision with root package name */
    final /* synthetic */ b f11859c;

    public q(b bVar, Object obj) {
        this.f11859c = bVar;
        this.f11857a = obj;
    }

    protected abstract void a(Object obj);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void b();

    public final void c() {
        Object obj;
        synchronized (this) {
            obj = this.f11857a;
            if (this.f11858b) {
                String obj2 = toString();
                Log.w("GmsClient", "Callback proxy " + obj2 + " being reused. This is not safe.");
            }
        }
        if (obj != null) {
            try {
                a(obj);
            } catch (RuntimeException e8) {
                throw e8;
            }
        }
        synchronized (this) {
            this.f11858b = true;
        }
        e();
    }

    public final void d() {
        synchronized (this) {
            this.f11857a = null;
        }
    }

    public final void e() {
        ArrayList arrayList;
        ArrayList arrayList2;
        d();
        arrayList = this.f11859c.f11837x;
        synchronized (arrayList) {
            arrayList2 = this.f11859c.f11837x;
            arrayList2.remove(this);
        }
    }
}
