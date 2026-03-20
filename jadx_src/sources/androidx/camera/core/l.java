package androidx.camera.core;

import android.graphics.Rect;
import androidx.camera.core.z2;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l extends z2.g {

    /* renamed from: a  reason: collision with root package name */
    private final Rect f2701a;

    /* renamed from: b  reason: collision with root package name */
    private final int f2702b;

    /* renamed from: c  reason: collision with root package name */
    private final int f2703c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(Rect rect, int i8, int i9) {
        Objects.requireNonNull(rect, "Null cropRect");
        this.f2701a = rect;
        this.f2702b = i8;
        this.f2703c = i9;
    }

    @Override // androidx.camera.core.z2.g
    public Rect a() {
        return this.f2701a;
    }

    @Override // androidx.camera.core.z2.g
    public int b() {
        return this.f2702b;
    }

    @Override // androidx.camera.core.z2.g
    public int c() {
        return this.f2703c;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof z2.g) {
            z2.g gVar = (z2.g) obj;
            return this.f2701a.equals(gVar.a()) && this.f2702b == gVar.b() && this.f2703c == gVar.c();
        }
        return false;
    }

    public int hashCode() {
        return ((((this.f2701a.hashCode() ^ 1000003) * 1000003) ^ this.f2702b) * 1000003) ^ this.f2703c;
    }

    public String toString() {
        return "TransformationInfo{cropRect=" + this.f2701a + ", rotationDegrees=" + this.f2702b + ", targetRotation=" + this.f2703c + "}";
    }
}
