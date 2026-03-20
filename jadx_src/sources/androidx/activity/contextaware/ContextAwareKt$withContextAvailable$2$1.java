package androidx.activity.contextaware;

import cj.a0;
import e.a;
import e.c;
import kotlin.jvm.internal.Lambda;
import mj.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ContextAwareKt$withContextAvailable$2$1 extends Lambda implements l<Throwable, a0> {

    /* renamed from: a  reason: collision with root package name */
    final /* synthetic */ a f389a;

    /* renamed from: b  reason: collision with root package name */
    final /* synthetic */ c f390b;

    public final void c(Throwable th) {
        this.f389a.removeOnContextAvailableListener(this.f390b);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        c((Throwable) obj);
        return a0.a;
    }
}
