package x;

import android.graphics.Matrix;
import android.graphics.Rect;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.e1;
import androidx.camera.core.l1;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import y.u;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a0 {

    /* renamed from: a  reason: collision with root package name */
    private final e1.m f23688a;

    /* renamed from: b  reason: collision with root package name */
    private final Rect f23689b;

    /* renamed from: c  reason: collision with root package name */
    private final int f23690c;

    /* renamed from: d  reason: collision with root package name */
    private final int f23691d;

    /* renamed from: e  reason: collision with root package name */
    private final Matrix f23692e;

    /* renamed from: f  reason: collision with root package name */
    private final g0 f23693f;

    /* renamed from: g  reason: collision with root package name */
    private final String f23694g;

    /* renamed from: h  reason: collision with root package name */
    private final List<Integer> f23695h = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public a0(u uVar, e1.m mVar, Rect rect, int i8, int i9, Matrix matrix, g0 g0Var) {
        this.f23688a = mVar;
        this.f23691d = i9;
        this.f23690c = i8;
        this.f23689b = rect;
        this.f23692e = matrix;
        this.f23693f = g0Var;
        this.f23694g = String.valueOf(uVar.hashCode());
        List<androidx.camera.core.impl.g> a9 = uVar.a();
        Objects.requireNonNull(a9);
        for (androidx.camera.core.impl.g gVar : a9) {
            this.f23695h.add(Integer.valueOf(gVar.e()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect a() {
        return this.f23689b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.f23691d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e1.m c() {
        return this.f23688a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int d() {
        return this.f23690c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Matrix e() {
        return this.f23692e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Integer> f() {
        return this.f23695h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String g() {
        return this.f23694g;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean h() {
        return this.f23693f.a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean i() {
        return c() == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j(e1.n nVar) {
        this.f23693f.d(nVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(l1 l1Var) {
        this.f23693f.f(l1Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l() {
        this.f23693f.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m(ImageCaptureException imageCaptureException) {
        this.f23693f.e(imageCaptureException);
    }
}
