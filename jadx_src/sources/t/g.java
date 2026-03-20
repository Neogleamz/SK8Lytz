package t;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.p1;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import t.b;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class g implements b.a {

    /* renamed from: a  reason: collision with root package name */
    final Object f22818a;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        final List<Surface> f22819a;

        /* renamed from: b  reason: collision with root package name */
        final Size f22820b;

        /* renamed from: c  reason: collision with root package name */
        final int f22821c;

        /* renamed from: d  reason: collision with root package name */
        final int f22822d;

        /* renamed from: e  reason: collision with root package name */
        String f22823e;

        /* renamed from: f  reason: collision with root package name */
        boolean f22824f = false;

        a(Surface surface) {
            androidx.core.util.h.i(surface, "Surface must not be null");
            this.f22819a = Collections.singletonList(surface);
            this.f22820b = c(surface);
            this.f22821c = a(surface);
            this.f22822d = b(surface);
        }

        @SuppressLint({"BlockedPrivateApi", "BanUncheckedReflection"})
        private static int a(Surface surface) {
            try {
                Method declaredMethod = Class.forName("android.hardware.camera2.legacy.LegacyCameraDevice").getDeclaredMethod("detectSurfaceType", Surface.class);
                if (Build.VERSION.SDK_INT < 22) {
                    declaredMethod.setAccessible(true);
                }
                return ((Integer) declaredMethod.invoke(null, surface)).intValue();
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e8) {
                p1.d("OutputConfigCompat", "Unable to retrieve surface format.", e8);
                return 0;
            }
        }

        @SuppressLint({"SoonBlockedPrivateApi", "BlockedPrivateApi", "BanUncheckedReflection"})
        private static int b(Surface surface) {
            try {
                return ((Integer) Surface.class.getDeclaredMethod("getGenerationId", new Class[0]).invoke(surface, new Object[0])).intValue();
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e8) {
                p1.d("OutputConfigCompat", "Unable to retrieve surface generation id.", e8);
                return -1;
            }
        }

        @SuppressLint({"BlockedPrivateApi", "BanUncheckedReflection"})
        private static Size c(Surface surface) {
            try {
                Method declaredMethod = Class.forName("android.hardware.camera2.legacy.LegacyCameraDevice").getDeclaredMethod("getSurfaceSize", Surface.class);
                declaredMethod.setAccessible(true);
                return (Size) declaredMethod.invoke(null, surface);
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e8) {
                p1.d("OutputConfigCompat", "Unable to retrieve surface size.", e8);
                return null;
            }
        }

        public boolean equals(Object obj) {
            if (obj instanceof a) {
                a aVar = (a) obj;
                if (this.f22820b.equals(aVar.f22820b) && this.f22821c == aVar.f22821c && this.f22822d == aVar.f22822d && this.f22824f == aVar.f22824f && Objects.equals(this.f22823e, aVar.f22823e)) {
                    int min = Math.min(this.f22819a.size(), aVar.f22819a.size());
                    for (int i8 = 0; i8 < min; i8++) {
                        if (this.f22819a.get(i8) != aVar.f22819a.get(i8)) {
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            int hashCode = this.f22819a.hashCode() ^ 31;
            int i8 = this.f22822d ^ ((hashCode << 5) - hashCode);
            int hashCode2 = this.f22820b.hashCode() ^ ((i8 << 5) - i8);
            int i9 = this.f22821c ^ ((hashCode2 << 5) - hashCode2);
            int i10 = (this.f22824f ? 1 : 0) ^ ((i9 << 5) - i9);
            int i11 = (i10 << 5) - i10;
            String str = this.f22823e;
            return (str == null ? 0 : str.hashCode()) ^ i11;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(Surface surface) {
        this.f22818a = new a(surface);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(Object obj) {
        this.f22818a = obj;
    }

    @Override // t.b.a
    public void a(long j8) {
    }

    @Override // t.b.a
    public void b(Surface surface) {
        androidx.core.util.h.i(surface, "Surface must not be null");
        if (getSurface() == surface) {
            throw new IllegalStateException("Surface is already added!");
        }
        if (!g()) {
            throw new IllegalStateException("Cannot have 2 surfaces for a non-sharing configuration");
        }
        throw new IllegalArgumentException("Exceeds maximum number of surfaces");
    }

    @Override // t.b.a
    public String c() {
        return ((a) this.f22818a).f22823e;
    }

    @Override // t.b.a
    public void d() {
        ((a) this.f22818a).f22824f = true;
    }

    @Override // t.b.a
    public void e(String str) {
        ((a) this.f22818a).f22823e = str;
    }

    public boolean equals(Object obj) {
        if (obj instanceof g) {
            return Objects.equals(this.f22818a, ((g) obj).f22818a);
        }
        return false;
    }

    @Override // t.b.a
    public Object f() {
        return null;
    }

    boolean g() {
        return ((a) this.f22818a).f22824f;
    }

    @Override // t.b.a
    public Surface getSurface() {
        List<Surface> list = ((a) this.f22818a).f22819a;
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public int hashCode() {
        return this.f22818a.hashCode();
    }
}
