package com.google.android.gms.internal.mlkit_common;

import android.content.Context;
import com.google.android.gms.dynamite.DynamiteModule;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k0 {

    /* renamed from: k  reason: collision with root package name */
    private static final zzau f12976k = zzau.c("optional-module-barcode", "com.google.android.gms.vision.barcode");

    /* renamed from: a  reason: collision with root package name */
    private final String f12977a;

    /* renamed from: b  reason: collision with root package name */
    private final String f12978b;

    /* renamed from: c  reason: collision with root package name */
    private final e0 f12979c;

    /* renamed from: d  reason: collision with root package name */
    private final i9.m f12980d;

    /* renamed from: e  reason: collision with root package name */
    private final j7.j f12981e;

    /* renamed from: f  reason: collision with root package name */
    private final j7.j f12982f;

    /* renamed from: g  reason: collision with root package name */
    private final String f12983g;

    /* renamed from: h  reason: collision with root package name */
    private final int f12984h;

    /* renamed from: i  reason: collision with root package name */
    private final Map f12985i = new HashMap();

    /* renamed from: j  reason: collision with root package name */
    private final Map f12986j = new HashMap();

    public k0(Context context, final i9.m mVar, e0 e0Var, String str) {
        this.f12977a = context.getPackageName();
        this.f12978b = i9.c.a(context);
        this.f12980d = mVar;
        this.f12979c = e0Var;
        u0.a();
        this.f12983g = str;
        this.f12981e = i9.g.a().b(new Callable() { // from class: com.google.android.gms.internal.mlkit_common.i0
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return k0.this.a();
            }
        });
        i9.g a9 = i9.g.a();
        mVar.getClass();
        this.f12982f = a9.b(new Callable() { // from class: com.google.android.gms.internal.mlkit_common.j0
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return mVar.a();
            }
        });
        zzau zzauVar = f12976k;
        this.f12984h = zzauVar.containsKey(str) ? DynamiteModule.b(context, (String) zzauVar.get(str)) : -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ String a() {
        return n6.h.a().b(this.f12983g);
    }
}
