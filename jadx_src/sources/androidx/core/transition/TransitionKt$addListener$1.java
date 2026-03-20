package androidx.core.transition;

import android.transition.Transition;
import cj.a0;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.p;
import mj.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class TransitionKt$addListener$1 extends Lambda implements l<Transition, a0> {

    /* renamed from: a  reason: collision with root package name */
    public static final TransitionKt$addListener$1 f4881a = new TransitionKt$addListener$1();

    public TransitionKt$addListener$1() {
        super(1);
    }

    public final void c(Transition transition) {
        p.e(transition, "it");
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        c((Transition) obj);
        return a0.a;
    }
}
