package androidx.camera.core.impl;

import android.util.Pair;
import android.util.Size;
import androidx.camera.core.impl.Config;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface l extends q {

    /* renamed from: g  reason: collision with root package name */
    public static final Config.a<Integer> f2576g = Config.a.a("camerax.core.imageOutput.targetAspectRatio", androidx.camera.core.e.class);

    /* renamed from: h  reason: collision with root package name */
    public static final Config.a<Integer> f2577h;

    /* renamed from: i  reason: collision with root package name */
    public static final Config.a<Integer> f2578i;

    /* renamed from: j  reason: collision with root package name */
    public static final Config.a<Size> f2579j;

    /* renamed from: k  reason: collision with root package name */
    public static final Config.a<Size> f2580k;

    /* renamed from: l  reason: collision with root package name */
    public static final Config.a<Size> f2581l;

    /* renamed from: m  reason: collision with root package name */
    public static final Config.a<List<Pair<Integer, Size[]>>> f2582m;

    static {
        Class cls = Integer.TYPE;
        f2577h = Config.a.a("camerax.core.imageOutput.targetRotation", cls);
        f2578i = Config.a.a("camerax.core.imageOutput.appTargetRotation", cls);
        f2579j = Config.a.a("camerax.core.imageOutput.targetResolution", Size.class);
        f2580k = Config.a.a("camerax.core.imageOutput.defaultResolution", Size.class);
        f2581l = Config.a.a("camerax.core.imageOutput.maxResolution", Size.class);
        f2582m = Config.a.a("camerax.core.imageOutput.supportedResolutions", List.class);
    }

    default boolean B() {
        return b(f2576g);
    }

    default int E() {
        return ((Integer) a(f2576g)).intValue();
    }

    default int K(int i8) {
        return ((Integer) f(f2577h, Integer.valueOf(i8))).intValue();
    }

    default Size i(Size size) {
        return (Size) f(f2581l, size);
    }

    default List<Pair<Integer, Size[]>> k(List<Pair<Integer, Size[]>> list) {
        return (List) f(f2582m, list);
    }

    default Size q(Size size) {
        return (Size) f(f2580k, size);
    }

    default Size u(Size size) {
        return (Size) f(f2579j, size);
    }

    default int v(int i8) {
        return ((Integer) f(f2578i, Integer.valueOf(i8))).intValue();
    }
}
