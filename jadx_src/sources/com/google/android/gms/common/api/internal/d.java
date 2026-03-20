package com.google.android.gms.common.api.internal;

import android.os.Looper;
import com.google.android.gms.common.api.internal.c;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d {
    public static <L> c<L> a(L l8, Looper looper, String str) {
        n6.j.m(l8, "Listener must not be null");
        n6.j.m(looper, "Looper must not be null");
        n6.j.m(str, "Listener type must not be null");
        return new c<>(looper, l8, str);
    }

    public static <L> c<L> b(L l8, Executor executor, String str) {
        n6.j.m(l8, "Listener must not be null");
        n6.j.m(executor, "Executor must not be null");
        n6.j.m(str, "Listener type must not be null");
        return new c<>(executor, l8, str);
    }

    public static <L> c.a<L> c(L l8, String str) {
        n6.j.m(l8, "Listener must not be null");
        n6.j.m(str, "Listener type must not be null");
        n6.j.g(str, "Listener type must not be empty");
        return new c.a<>(l8, str);
    }
}
