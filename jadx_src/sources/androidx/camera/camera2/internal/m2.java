package androidx.camera.camera2.internal;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Pair;
import android.util.Rational;
import android.util.Size;
import androidx.camera.camera2.internal.compat.CameraAccessExceptionCompat;
import androidx.camera.core.impl.SurfaceConfig;
import androidx.camera.core.impl.utils.a;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class m2 {

    /* renamed from: c  reason: collision with root package name */
    private final String f1953c;

    /* renamed from: d  reason: collision with root package name */
    private final c f1954d;

    /* renamed from: e  reason: collision with root package name */
    private final s.y f1955e;

    /* renamed from: f  reason: collision with root package name */
    private final v.d f1956f;

    /* renamed from: g  reason: collision with root package name */
    private final v.e f1957g;

    /* renamed from: h  reason: collision with root package name */
    private final int f1958h;

    /* renamed from: i  reason: collision with root package name */
    private final boolean f1959i;

    /* renamed from: k  reason: collision with root package name */
    private boolean f1961k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f1962l;

    /* renamed from: m  reason: collision with root package name */
    y.z0 f1963m;

    /* renamed from: o  reason: collision with root package name */
    private final x1 f1965o;

    /* renamed from: a  reason: collision with root package name */
    private final List<y.y0> f1951a = new ArrayList();

    /* renamed from: b  reason: collision with root package name */
    private final Map<Integer, Size> f1952b = new HashMap();

    /* renamed from: j  reason: collision with root package name */
    private final Map<Integer, List<Size>> f1960j = new HashMap();

    /* renamed from: n  reason: collision with root package name */
    private Map<Integer, Size[]> f1964n = new HashMap();

    /* renamed from: p  reason: collision with root package name */
    private final v.n f1966p = new v.n();

    /* JADX INFO: Access modifiers changed from: package-private */
    public m2(Context context, String str, s.l0 l0Var, c cVar) {
        this.f1961k = false;
        this.f1962l = false;
        String str2 = (String) androidx.core.util.h.h(str);
        this.f1953c = str2;
        this.f1954d = (c) androidx.core.util.h.h(cVar);
        this.f1956f = new v.d(str);
        this.f1957g = new v.e();
        this.f1965o = x1.b(context);
        try {
            s.y c9 = l0Var.c(str2);
            this.f1955e = c9;
            Integer num = (Integer) c9.a(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
            this.f1958h = num != null ? num.intValue() : 2;
            this.f1959i = x();
            int[] iArr = (int[]) c9.a(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
            if (iArr != null) {
                for (int i8 : iArr) {
                    if (i8 == 3) {
                        this.f1961k = true;
                    } else if (i8 == 6) {
                        this.f1962l = true;
                    }
                }
            }
            h();
            i();
            a();
        } catch (CameraAccessExceptionCompat e8) {
            throw l1.a(e8);
        }
    }

    private void a() {
    }

    private Size[] c(int i8) {
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) this.f1955e.a(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (streamConfigurationMap != null) {
            Size[] outputSizes = (Build.VERSION.SDK_INT >= 23 || i8 != 34) ? streamConfigurationMap.getOutputSizes(i8) : streamConfigurationMap.getOutputSizes(SurfaceTexture.class);
            if (outputSizes != null) {
                Size[] d8 = d(outputSizes, i8);
                Arrays.sort(d8, new androidx.camera.core.impl.utils.d(true));
                return d8;
            }
            throw new IllegalArgumentException("Can not get supported output size for the format: " + i8);
        }
        throw new IllegalArgumentException("Can not retrieve SCALER_STREAM_CONFIGURATION_MAP");
    }

    private Size[] d(Size[] sizeArr, int i8) {
        List<Size> e8 = e(i8);
        ArrayList arrayList = new ArrayList(Arrays.asList(sizeArr));
        arrayList.removeAll(e8);
        return (Size[]) arrayList.toArray(new Size[0]);
    }

    private List<Size> e(int i8) {
        List<Size> list = this.f1960j.get(Integer.valueOf(i8));
        if (list == null) {
            List<Size> a9 = this.f1956f.a(i8);
            this.f1960j.put(Integer.valueOf(i8), a9);
            return a9;
        }
        return list;
    }

    private Size f(int i8) {
        Size size = this.f1952b.get(Integer.valueOf(i8));
        if (size != null) {
            return size;
        }
        Size m8 = m(i8);
        this.f1952b.put(Integer.valueOf(i8), m8);
        return m8;
    }

    private Size g(Size size, int i8) {
        return (size == null || !w(i8)) ? size : new Size(size.getHeight(), size.getWidth());
    }

    private void h() {
        this.f1951a.addAll(c2.a(this.f1958h, this.f1961k, this.f1962l));
        this.f1951a.addAll(this.f1957g.a(this.f1953c, this.f1958h));
    }

    private void i() {
        this.f1963m = y.z0.a(new Size(640, 480), this.f1965o.d(), n());
    }

    private Size[] j(int i8) {
        Size[] sizeArr = this.f1964n.get(Integer.valueOf(i8));
        if (sizeArr == null) {
            Size[] c9 = c(i8);
            this.f1964n.put(Integer.valueOf(i8), c9);
            return c9;
        }
        return sizeArr;
    }

    private List<List<Size>> k(List<List<Size>> list) {
        int i8 = 1;
        for (List<Size> list2 : list) {
            i8 *= list2.size();
        }
        if (i8 != 0) {
            ArrayList arrayList = new ArrayList();
            for (int i9 = 0; i9 < i8; i9++) {
                arrayList.add(new ArrayList());
            }
            int size = i8 / list.get(0).size();
            int i10 = i8;
            for (int i11 = 0; i11 < list.size(); i11++) {
                List<Size> list3 = list.get(i11);
                for (int i12 = 0; i12 < i8; i12++) {
                    ((List) arrayList.get(i12)).add(list3.get((i12 % i10) / size));
                }
                if (i11 < list.size() - 1) {
                    i10 = size;
                    size /= list.get(i11 + 1).size();
                }
            }
            return arrayList;
        }
        throw new IllegalArgumentException("Failed to find supported resolutions.");
    }

    private Size[] l(int i8, androidx.camera.core.impl.l lVar) {
        Size[] sizeArr = null;
        List<Pair<Integer, Size[]>> k8 = lVar.k(null);
        if (k8 != null) {
            Iterator<Pair<Integer, Size[]>> it = k8.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Pair<Integer, Size[]> next = it.next();
                if (((Integer) next.first).intValue() == i8) {
                    sizeArr = (Size[]) next.second;
                    break;
                }
            }
        }
        if (sizeArr != null) {
            Size[] d8 = d(sizeArr, i8);
            Arrays.sort(d8, new androidx.camera.core.impl.utils.d(true));
            return d8;
        }
        return sizeArr;
    }

    private Size n() {
        try {
            int parseInt = Integer.parseInt(this.f1953c);
            CamcorderProfile a9 = this.f1954d.b(parseInt, 1) ? this.f1954d.a(parseInt, 1) : null;
            return a9 != null ? new Size(a9.videoFrameWidth, a9.videoFrameHeight) : o(parseInt);
        } catch (NumberFormatException unused) {
            return p();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.util.Size o(int r4) {
        /*
            r3 = this;
            android.util.Size r0 = f0.c.f19833c
            androidx.camera.camera2.internal.c r1 = r3.f1954d
            r2 = 10
            boolean r1 = r1.b(r4, r2)
            if (r1 == 0) goto L13
        Lc:
            androidx.camera.camera2.internal.c r1 = r3.f1954d
            android.media.CamcorderProfile r4 = r1.a(r4, r2)
            goto L48
        L13:
            androidx.camera.camera2.internal.c r1 = r3.f1954d
            r2 = 8
            boolean r1 = r1.b(r4, r2)
            if (r1 == 0) goto L1e
            goto Lc
        L1e:
            androidx.camera.camera2.internal.c r1 = r3.f1954d
            r2 = 12
            boolean r1 = r1.b(r4, r2)
            if (r1 == 0) goto L29
            goto Lc
        L29:
            androidx.camera.camera2.internal.c r1 = r3.f1954d
            r2 = 6
            boolean r1 = r1.b(r4, r2)
            if (r1 == 0) goto L33
            goto Lc
        L33:
            androidx.camera.camera2.internal.c r1 = r3.f1954d
            r2 = 5
            boolean r1 = r1.b(r4, r2)
            if (r1 == 0) goto L3d
            goto Lc
        L3d:
            androidx.camera.camera2.internal.c r1 = r3.f1954d
            r2 = 4
            boolean r1 = r1.b(r4, r2)
            if (r1 == 0) goto L47
            goto Lc
        L47:
            r4 = 0
        L48:
            if (r4 == 0) goto L53
            android.util.Size r0 = new android.util.Size
            int r1 = r4.videoFrameWidth
            int r4 = r4.videoFrameHeight
            r0.<init>(r1, r4)
        L53:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.internal.m2.o(int):android.util.Size");
    }

    private Size p() {
        StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap) this.f1955e.a(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (streamConfigurationMap != null) {
            Size[] outputSizes = streamConfigurationMap.getOutputSizes(MediaRecorder.class);
            if (outputSizes == null) {
                return f0.c.f19833c;
            }
            Arrays.sort(outputSizes, new androidx.camera.core.impl.utils.d(true));
            for (Size size : outputSizes) {
                int width = size.getWidth();
                Size size2 = f0.c.f19834d;
                if (width <= size2.getWidth() && size.getHeight() <= size2.getHeight()) {
                    return size;
                }
            }
            return f0.c.f19833c;
        }
        throw new IllegalArgumentException("Can not retrieve SCALER_STREAM_CONFIGURATION_MAP");
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0046, code lost:
        if (r4.f1959i != false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x004b, code lost:
        if (r4.f1959i != false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0076, code lost:
        if (r4.f1959i != false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0078, code lost:
        r5 = androidx.camera.core.impl.utils.a.f2620c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x007b, code lost:
        r5 = androidx.camera.core.impl.utils.a.f2621d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0080, code lost:
        if (r4.f1959i != false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0082, code lost:
        r5 = androidx.camera.core.impl.utils.a.f2618a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0085, code lost:
        r5 = androidx.camera.core.impl.utils.a.f2619b;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.util.Rational s(androidx.camera.core.impl.l r5) {
        /*
            r4 = this;
            v.q r0 = new v.q
            r0.<init>()
            java.lang.String r1 = r4.f1953c
            s.y r2 = r4.f1955e
            int r0 = r0.a(r1, r2)
            r1 = 0
            if (r0 == 0) goto L7e
            r2 = 1
            if (r0 == r2) goto L74
            r3 = 2
            if (r0 == r3) goto L60
            r3 = 3
            if (r0 == r3) goto L1b
            goto L88
        L1b:
            android.util.Size r0 = r4.t(r5)
            boolean r3 = r5.B()
            if (r3 == 0) goto L50
            int r5 = r5.E()
            if (r5 == 0) goto L49
            if (r5 == r2) goto L44
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r2 = "Undefined target aspect ratio: "
            r0.append(r2)
            r0.append(r5)
            java.lang.String r5 = r0.toString()
            java.lang.String r0 = "SupportedSurfaceCombination"
            androidx.camera.core.p1.c(r0, r5)
            goto L88
        L44:
            boolean r5 = r4.f1959i
            if (r5 == 0) goto L7b
            goto L78
        L49:
            boolean r5 = r4.f1959i
            if (r5 == 0) goto L85
            goto L82
        L4e:
            r1 = r5
            goto L88
        L50:
            if (r0 == 0) goto L88
            android.util.Rational r1 = new android.util.Rational
            int r5 = r0.getWidth()
            int r0 = r0.getHeight()
            r1.<init>(r5, r0)
            goto L88
        L60:
            r5 = 256(0x100, float:3.59E-43)
            android.util.Size r5 = r4.f(r5)
            android.util.Rational r1 = new android.util.Rational
            int r0 = r5.getWidth()
            int r5 = r5.getHeight()
            r1.<init>(r0, r5)
            goto L88
        L74:
            boolean r5 = r4.f1959i
            if (r5 == 0) goto L7b
        L78:
            android.util.Rational r5 = androidx.camera.core.impl.utils.a.f2620c
            goto L4e
        L7b:
            android.util.Rational r5 = androidx.camera.core.impl.utils.a.f2621d
            goto L4e
        L7e:
            boolean r5 = r4.f1959i
            if (r5 == 0) goto L85
        L82:
            android.util.Rational r5 = androidx.camera.core.impl.utils.a.f2618a
            goto L4e
        L85:
            android.util.Rational r5 = androidx.camera.core.impl.utils.a.f2619b
            goto L4e
        L88:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.camera2.internal.m2.s(androidx.camera.core.impl.l):android.util.Rational");
    }

    private Size t(androidx.camera.core.impl.l lVar) {
        return g(lVar.u(null), lVar.K(0));
    }

    private List<Integer> u(List<androidx.camera.core.impl.v<?>> list) {
        ArrayList arrayList = new ArrayList();
        ArrayList<Integer> arrayList2 = new ArrayList();
        for (androidx.camera.core.impl.v<?> vVar : list) {
            int D = vVar.D(0);
            if (!arrayList2.contains(Integer.valueOf(D))) {
                arrayList2.add(Integer.valueOf(D));
            }
        }
        Collections.sort(arrayList2);
        Collections.reverse(arrayList2);
        for (Integer num : arrayList2) {
            int intValue = num.intValue();
            for (androidx.camera.core.impl.v<?> vVar2 : list) {
                if (intValue == vVar2.D(0)) {
                    arrayList.add(Integer.valueOf(list.indexOf(vVar2)));
                }
            }
        }
        return arrayList;
    }

    private Map<Rational, List<Size>> v(List<Size> list) {
        HashMap hashMap = new HashMap();
        hashMap.put(androidx.camera.core.impl.utils.a.f2618a, new ArrayList());
        hashMap.put(androidx.camera.core.impl.utils.a.f2620c, new ArrayList());
        for (Size size : list) {
            Rational rational = null;
            for (Rational rational2 : hashMap.keySet()) {
                if (androidx.camera.core.impl.utils.a.a(size, rational2)) {
                    List list2 = (List) hashMap.get(rational2);
                    if (!list2.contains(size)) {
                        list2.add(size);
                    }
                    rational = rational2;
                }
            }
            if (rational == null) {
                hashMap.put(new Rational(size.getWidth(), size.getHeight()), new ArrayList(Collections.singleton(size)));
            }
        }
        return hashMap;
    }

    private boolean w(int i8) {
        Integer num = (Integer) this.f1955e.a(CameraCharacteristics.SENSOR_ORIENTATION);
        androidx.core.util.h.i(num, "Camera HAL in bad state, unable to retrieve the SENSOR_ORIENTATION");
        int b9 = androidx.camera.core.impl.utils.c.b(i8);
        Integer num2 = (Integer) this.f1955e.a(CameraCharacteristics.LENS_FACING);
        androidx.core.util.h.i(num2, "Camera HAL in bad state, unable to retrieve the LENS_FACING");
        int a9 = androidx.camera.core.impl.utils.c.a(b9, num.intValue(), 1 == num2.intValue());
        return a9 == 90 || a9 == 270;
    }

    private boolean x() {
        Size size = (Size) this.f1955e.a(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE);
        return size == null || size.getWidth() >= size.getHeight();
    }

    private void y() {
        this.f1965o.e();
        if (this.f1963m == null) {
            i();
            return;
        }
        this.f1963m = y.z0.a(this.f1963m.b(), this.f1965o.d(), this.f1963m.d());
    }

    private void z(List<Size> list, Size size) {
        if (list == null || list.isEmpty()) {
            return;
        }
        int i8 = -1;
        ArrayList arrayList = new ArrayList();
        int i9 = 0;
        while (true) {
            int i10 = i9;
            int i11 = i8;
            i8 = i10;
            if (i8 >= list.size()) {
                break;
            }
            Size size2 = list.get(i8);
            if (size2.getWidth() < size.getWidth() || size2.getHeight() < size.getHeight()) {
                break;
            }
            if (i11 >= 0) {
                arrayList.add(list.get(i11));
            }
            i9 = i8 + 1;
        }
        list.removeAll(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SurfaceConfig A(int i8, Size size) {
        return SurfaceConfig.f(i8, size, this.f1963m);
    }

    boolean b(List<SurfaceConfig> list) {
        Iterator<y.y0> it = this.f1951a.iterator();
        boolean z4 = false;
        while (it.hasNext() && !(z4 = it.next().d(list))) {
        }
        return z4;
    }

    Size m(int i8) {
        return (Size) Collections.max(Arrays.asList(j(i8)), new androidx.camera.core.impl.utils.d());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<androidx.camera.core.impl.v<?>, Size> q(List<y.a> list, List<androidx.camera.core.impl.v<?>> list2) {
        y();
        ArrayList arrayList = new ArrayList();
        for (y.a aVar : list) {
            arrayList.add(aVar.d());
        }
        for (androidx.camera.core.impl.v<?> vVar : list2) {
            arrayList.add(SurfaceConfig.f(vVar.m(), new Size(640, 480), this.f1963m));
        }
        if (!b(arrayList)) {
            throw new IllegalArgumentException("No supported surface combination is found for camera device - Id : " + this.f1953c + ".  May be attempting to bind too many use cases. Existing surfaces: " + list + " New configs: " + list2);
        }
        List<Integer> u8 = u(list2);
        ArrayList arrayList2 = new ArrayList();
        for (Integer num : u8) {
            arrayList2.add(r(list2.get(num.intValue())));
        }
        HashMap hashMap = null;
        Iterator<List<Size>> it = k(arrayList2).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            List<Size> next = it.next();
            ArrayList arrayList3 = new ArrayList();
            for (y.a aVar2 : list) {
                arrayList3.add(aVar2.d());
            }
            for (int i8 = 0; i8 < next.size(); i8++) {
                arrayList3.add(SurfaceConfig.f(list2.get(u8.get(i8).intValue()).m(), next.get(i8), this.f1963m));
            }
            if (b(arrayList3)) {
                hashMap = new HashMap();
                for (androidx.camera.core.impl.v<?> vVar2 : list2) {
                    hashMap.put(vVar2, next.get(u8.indexOf(Integer.valueOf(list2.indexOf(vVar2)))));
                }
            }
        }
        if (hashMap != null) {
            return hashMap;
        }
        throw new IllegalArgumentException("No supported surface combination is found for camera device - Id : " + this.f1953c + " and Hardware level: " + this.f1958h + ". May be the specified resolution is too large and not supported. Existing surfaces: " + list + " New configs: " + list2);
    }

    List<Size> r(androidx.camera.core.impl.v<?> vVar) {
        int m8 = vVar.m();
        androidx.camera.core.impl.l lVar = (androidx.camera.core.impl.l) vVar;
        Size[] l8 = l(m8, lVar);
        if (l8 == null) {
            l8 = j(m8);
        }
        ArrayList arrayList = new ArrayList();
        Size i8 = lVar.i(null);
        Size m9 = m(m8);
        if (i8 == null || f0.c.a(m9) < f0.c.a(i8)) {
            i8 = m9;
        }
        Arrays.sort(l8, new androidx.camera.core.impl.utils.d(true));
        Size t8 = t(lVar);
        Size size = f0.c.f19832b;
        int a9 = f0.c.a(size);
        if (f0.c.a(i8) < a9) {
            size = f0.c.f19831a;
        } else if (t8 != null && f0.c.a(t8) < a9) {
            size = t8;
        }
        for (Size size2 : l8) {
            if (f0.c.a(size2) <= f0.c.a(i8) && f0.c.a(size2) >= f0.c.a(size) && !arrayList.contains(size2)) {
                arrayList.add(size2);
            }
        }
        if (arrayList.isEmpty()) {
            throw new IllegalArgumentException("Can not get supported output size under supported maximum for the format: " + m8);
        }
        Rational s8 = s(lVar);
        if (t8 == null) {
            t8 = lVar.q(null);
        }
        List<Size> arrayList2 = new ArrayList<>();
        new HashMap();
        if (s8 == null) {
            arrayList2.addAll(arrayList);
            if (t8 != null) {
                z(arrayList2, t8);
            }
        } else {
            Map<Rational, List<Size>> v8 = v(arrayList);
            if (t8 != null) {
                for (Rational rational : v8.keySet()) {
                    z(v8.get(rational), t8);
                }
            }
            ArrayList<Rational> arrayList3 = new ArrayList(v8.keySet());
            Collections.sort(arrayList3, new a.C0019a(s8));
            for (Rational rational2 : arrayList3) {
                for (Size size3 : v8.get(rational2)) {
                    if (!arrayList2.contains(size3)) {
                        arrayList2.add(size3);
                    }
                }
            }
        }
        return this.f1966p.a(SurfaceConfig.d(vVar.m()), arrayList2);
    }
}
