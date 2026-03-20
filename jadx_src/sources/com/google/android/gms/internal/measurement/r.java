package com.google.android.gms.internal.measurement;

import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface r {

    /* renamed from: r  reason: collision with root package name */
    public static final r f12463r = new y();

    /* renamed from: s  reason: collision with root package name */
    public static final r f12464s = new p();

    /* renamed from: u  reason: collision with root package name */
    public static final r f12465u = new k("continue");

    /* renamed from: v  reason: collision with root package name */
    public static final r f12466v = new k("break");
    public static final r D = new k("return");
    public static final r I = new h(Boolean.TRUE);
    public static final r J = new h(Boolean.FALSE);
    public static final r M = new t(BuildConfig.FLAVOR);

    r a();

    Boolean b();

    Double d();

    String e();

    Iterator<r> f();

    r g(String str, g6 g6Var, List<r> list);
}
