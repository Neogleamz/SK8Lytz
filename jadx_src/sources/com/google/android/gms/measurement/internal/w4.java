package com.google.android.gms.measurement.internal;

import com.google.android.libraries.barhopper.RecognitionOptions;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w4 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ int f17065a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ String f17066b;

    /* renamed from: c  reason: collision with root package name */
    private final /* synthetic */ Object f17067c;

    /* renamed from: d  reason: collision with root package name */
    private final /* synthetic */ Object f17068d;

    /* renamed from: e  reason: collision with root package name */
    private final /* synthetic */ Object f17069e;

    /* renamed from: f  reason: collision with root package name */
    private final /* synthetic */ x4 f17070f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w4(x4 x4Var, int i8, String str, Object obj, Object obj2, Object obj3) {
        this.f17065a = i8;
        this.f17066b = str;
        this.f17067c = obj;
        this.f17068d = obj2;
        this.f17069e = obj3;
        this.f17070f = x4Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        char c9;
        long j8;
        char c10;
        long j9;
        x4 x4Var;
        char c11;
        h5 D = this.f17070f.f16485a.D();
        if (!D.q()) {
            this.f17070f.w(6, "Persisted config not initialized. Not logging error/warn");
            return;
        }
        c9 = this.f17070f.f17111c;
        if (c9 == 0) {
            if (this.f17070f.a().U()) {
                x4Var = this.f17070f;
                c11 = 'C';
            } else {
                x4Var = this.f17070f;
                c11 = 'c';
            }
            x4Var.f17111c = c11;
        }
        j8 = this.f17070f.f17112d;
        if (j8 < 0) {
            this.f17070f.f17112d = 87000L;
        }
        char charAt = "01VDIWEA?".charAt(this.f17065a);
        c10 = this.f17070f.f17111c;
        j9 = this.f17070f.f17112d;
        String str = "2" + charAt + c10 + j9 + ":" + x4.v(true, this.f17066b, this.f17067c, this.f17068d, this.f17069e);
        if (str.length() > 1024) {
            str = this.f17066b.substring(0, RecognitionOptions.UPC_E);
        }
        l5 l5Var = D.f16604f;
        if (l5Var != null) {
            l5Var.b(str, 1L);
        }
    }
}
