package androidx.camera.camera2.internal;

import androidx.camera.camera2.internal.s1;
import androidx.camera.core.impl.SessionConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d1 implements y.u0 {

    /* renamed from: a  reason: collision with root package name */
    private final s1 f1773a;

    /* renamed from: b  reason: collision with root package name */
    private final List<y.w0> f1774b;

    /* renamed from: c  reason: collision with root package name */
    private volatile boolean f1775c = false;

    /* renamed from: d  reason: collision with root package name */
    private volatile SessionConfig f1776d;

    public d1(s1 s1Var, List<y.w0> list) {
        boolean z4 = s1Var.f2065l == s1.e.OPENED;
        androidx.core.util.h.b(z4, "CaptureSession state must be OPENED. Current state:" + s1Var.f2065l);
        this.f1773a = s1Var;
        this.f1774b = Collections.unmodifiableList(new ArrayList(list));
    }

    public void a() {
        this.f1775c = true;
    }

    public void b(SessionConfig sessionConfig) {
        this.f1776d = sessionConfig;
    }
}
