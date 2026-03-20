package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.b;
/* JADX INFO: Access modifiers changed from: package-private */
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ReflectiveGenericLifecycleObserver implements h {

    /* renamed from: a  reason: collision with root package name */
    private final Object f5836a;

    /* renamed from: b  reason: collision with root package name */
    private final b.a f5837b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ReflectiveGenericLifecycleObserver(Object obj) {
        this.f5836a = obj;
        this.f5837b = b.f5848c.c(obj.getClass());
    }

    @Override // androidx.lifecycle.h
    public void c(j jVar, Lifecycle.Event event) {
        this.f5837b.a(jVar, event, this.f5836a);
    }
}
