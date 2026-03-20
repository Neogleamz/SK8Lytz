package androidx.camera.camera2.internal;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.camera.core.InitializationException;
import androidx.camera.core.impl.CameraInternal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v implements y.p {

    /* renamed from: a  reason: collision with root package name */
    private final y.t f2135a;

    /* renamed from: c  reason: collision with root package name */
    private final s.l0 f2137c;

    /* renamed from: d  reason: collision with root package name */
    private final List<String> f2138d;

    /* renamed from: e  reason: collision with root package name */
    private final x1 f2139e;

    /* renamed from: f  reason: collision with root package name */
    private final Map<String, j0> f2140f = new HashMap();

    /* renamed from: b  reason: collision with root package name */
    private final androidx.camera.core.impl.e f2136b = new androidx.camera.core.impl.e(1);

    public v(Context context, y.t tVar, androidx.camera.core.t tVar2) {
        this.f2135a = tVar;
        this.f2137c = s.l0.b(context, tVar.c());
        this.f2139e = x1.b(context);
        this.f2138d = d(j1.b(this, tVar2));
    }

    private List<String> d(List<String> list) {
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            if (str.equals("0") || str.equals("1") || g(str)) {
                arrayList.add(str);
            } else {
                androidx.camera.core.p1.a("Camera2CameraFactory", "Camera " + str + " is filtered out because its capabilities do not contain REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE.");
            }
        }
        return arrayList;
    }

    private boolean g(String str) {
        if ("robolectric".equals(Build.FINGERPRINT)) {
            return true;
        }
        try {
            int[] iArr = (int[]) this.f2137c.c(str).a(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
            if (iArr != null) {
                for (int i8 : iArr) {
                    if (i8 == 0) {
                        return true;
                    }
                }
            }
            return false;
        } catch (CameraAccessExceptionCompat e8) {
            throw new InitializationException(l1.a(e8));
        }
    }

    @Override // y.p
    public Set<String> a() {
        return new LinkedHashSet(this.f2138d);
    }

    @Override // y.p
    public CameraInternal b(String str) {
        if (this.f2138d.contains(str)) {
            return new g0(this.f2137c, str, e(str), this.f2136b, this.f2135a.b(), this.f2135a.c(), this.f2139e);
        }
        throw new IllegalArgumentException("The given camera id is not on the available camera id list.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public j0 e(String str) {
        try {
            j0 j0Var = this.f2140f.get(str);
            if (j0Var == null) {
                j0 j0Var2 = new j0(str, this.f2137c);
                this.f2140f.put(str, j0Var2);
                return j0Var2;
            }
            return j0Var;
        } catch (CameraAccessExceptionCompat e8) {
            throw l1.a(e8);
        }
    }

    @Override // y.p
    /* renamed from: f */
    public s.l0 c() {
        return this.f2137c;
    }
}
