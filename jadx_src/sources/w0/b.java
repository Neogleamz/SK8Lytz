package w0;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a<T> {
        void a(T t8, Rect rect);
    }

    /* renamed from: w0.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0220b<T, V> {
        V a(T t8, int i8);

        int b(T t8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c<T> implements Comparator<T> {

        /* renamed from: a  reason: collision with root package name */
        private final Rect f23388a = new Rect();

        /* renamed from: b  reason: collision with root package name */
        private final Rect f23389b = new Rect();

        /* renamed from: c  reason: collision with root package name */
        private final boolean f23390c;

        /* renamed from: d  reason: collision with root package name */
        private final a<T> f23391d;

        c(boolean z4, a<T> aVar) {
            this.f23390c = z4;
            this.f23391d = aVar;
        }

        @Override // java.util.Comparator
        public int compare(T t8, T t9) {
            Rect rect = this.f23388a;
            Rect rect2 = this.f23389b;
            this.f23391d.a(t8, rect);
            this.f23391d.a(t9, rect2);
            int i8 = rect.top;
            int i9 = rect2.top;
            if (i8 < i9) {
                return -1;
            }
            if (i8 > i9) {
                return 1;
            }
            int i10 = rect.left;
            int i11 = rect2.left;
            if (i10 < i11) {
                return this.f23390c ? 1 : -1;
            } else if (i10 > i11) {
                return this.f23390c ? -1 : 1;
            } else {
                int i12 = rect.bottom;
                int i13 = rect2.bottom;
                if (i12 < i13) {
                    return -1;
                }
                if (i12 > i13) {
                    return 1;
                }
                int i14 = rect.right;
                int i15 = rect2.right;
                if (i14 < i15) {
                    return this.f23390c ? 1 : -1;
                } else if (i14 > i15) {
                    return this.f23390c ? -1 : 1;
                } else {
                    return 0;
                }
            }
        }
    }

    private static boolean a(int i8, Rect rect, Rect rect2, Rect rect3) {
        boolean b9 = b(i8, rect, rect2);
        if (b(i8, rect, rect3) || !b9) {
            return false;
        }
        return !j(i8, rect, rect3) || i8 == 17 || i8 == 66 || k(i8, rect, rect2) < m(i8, rect, rect3);
    }

    private static boolean b(int i8, Rect rect, Rect rect2) {
        if (i8 != 17) {
            if (i8 != 33) {
                if (i8 != 66) {
                    if (i8 != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                }
            }
            return rect2.right >= rect.left && rect2.left <= rect.right;
        }
        return rect2.bottom >= rect.top && rect2.top <= rect.bottom;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x004d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static <L, T> T c(L r7, w0.b.InterfaceC0220b<L, T> r8, w0.b.a<T> r9, T r10, android.graphics.Rect r11, int r12) {
        /*
            android.graphics.Rect r0 = new android.graphics.Rect
            r0.<init>(r11)
            r1 = 17
            r2 = 0
            if (r12 == r1) goto L38
            r1 = 33
            if (r12 == r1) goto L2e
            r1 = 66
            if (r12 == r1) goto L26
            r1 = 130(0x82, float:1.82E-43)
            if (r12 != r1) goto L1e
            int r1 = r11.height()
            int r1 = r1 + 1
            int r1 = -r1
            goto L34
        L1e:
            java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException
            java.lang.String r8 = "direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}."
            r7.<init>(r8)
            throw r7
        L26:
            int r1 = r11.width()
            int r1 = r1 + 1
            int r1 = -r1
            goto L3e
        L2e:
            int r1 = r11.height()
            int r1 = r1 + 1
        L34:
            r0.offset(r2, r1)
            goto L41
        L38:
            int r1 = r11.width()
            int r1 = r1 + 1
        L3e:
            r0.offset(r1, r2)
        L41:
            r1 = 0
            int r3 = r8.b(r7)
            android.graphics.Rect r4 = new android.graphics.Rect
            r4.<init>()
        L4b:
            if (r2 >= r3) goto L64
            java.lang.Object r5 = r8.a(r7, r2)
            if (r5 != r10) goto L54
            goto L61
        L54:
            r9.a(r5, r4)
            boolean r6 = h(r12, r11, r4, r0)
            if (r6 == 0) goto L61
            r0.set(r4)
            r1 = r5
        L61:
            int r2 = r2 + 1
            goto L4b
        L64:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: w0.b.c(java.lang.Object, w0.b$b, w0.b$a, java.lang.Object, android.graphics.Rect, int):java.lang.Object");
    }

    public static <L, T> T d(L l8, InterfaceC0220b<L, T> interfaceC0220b, a<T> aVar, T t8, int i8, boolean z4, boolean z8) {
        int b9 = interfaceC0220b.b(l8);
        ArrayList arrayList = new ArrayList(b9);
        for (int i9 = 0; i9 < b9; i9++) {
            arrayList.add(interfaceC0220b.a(l8, i9));
        }
        Collections.sort(arrayList, new c(z4, aVar));
        if (i8 != 1) {
            if (i8 == 2) {
                return (T) e(t8, arrayList, z8);
            }
            throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
        }
        return (T) f(t8, arrayList, z8);
    }

    private static <T> T e(T t8, ArrayList<T> arrayList, boolean z4) {
        int size = arrayList.size();
        int lastIndexOf = (t8 == null ? -1 : arrayList.lastIndexOf(t8)) + 1;
        if (lastIndexOf < size) {
            return arrayList.get(lastIndexOf);
        }
        if (!z4 || size <= 0) {
            return null;
        }
        return arrayList.get(0);
    }

    private static <T> T f(T t8, ArrayList<T> arrayList, boolean z4) {
        int size = arrayList.size();
        int indexOf = (t8 == null ? size : arrayList.indexOf(t8)) - 1;
        if (indexOf >= 0) {
            return arrayList.get(indexOf);
        }
        if (!z4 || size <= 0) {
            return null;
        }
        return arrayList.get(size - 1);
    }

    private static int g(int i8, int i9) {
        return (i8 * 13 * i8) + (i9 * i9);
    }

    private static boolean h(int i8, Rect rect, Rect rect2, Rect rect3) {
        if (i(rect, rect2, i8)) {
            if (i(rect, rect3, i8) && !a(i8, rect, rect2, rect3)) {
                return !a(i8, rect, rect3, rect2) && g(k(i8, rect, rect2), o(i8, rect, rect2)) < g(k(i8, rect, rect3), o(i8, rect, rect3));
            }
            return true;
        }
        return false;
    }

    private static boolean i(Rect rect, Rect rect2, int i8) {
        if (i8 == 17) {
            int i9 = rect.right;
            int i10 = rect2.right;
            return (i9 > i10 || rect.left >= i10) && rect.left > rect2.left;
        } else if (i8 == 33) {
            int i11 = rect.bottom;
            int i12 = rect2.bottom;
            return (i11 > i12 || rect.top >= i12) && rect.top > rect2.top;
        } else if (i8 == 66) {
            int i13 = rect.left;
            int i14 = rect2.left;
            return (i13 < i14 || rect.right <= i14) && rect.right < rect2.right;
        } else if (i8 == 130) {
            int i15 = rect.top;
            int i16 = rect2.top;
            return (i15 < i16 || rect.bottom <= i16) && rect.bottom < rect2.bottom;
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private static boolean j(int i8, Rect rect, Rect rect2) {
        if (i8 == 17) {
            return rect.left >= rect2.right;
        } else if (i8 == 33) {
            return rect.top >= rect2.bottom;
        } else if (i8 == 66) {
            return rect.right <= rect2.left;
        } else if (i8 == 130) {
            return rect.bottom <= rect2.top;
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private static int k(int i8, Rect rect, Rect rect2) {
        return Math.max(0, l(i8, rect, rect2));
    }

    private static int l(int i8, Rect rect, Rect rect2) {
        int i9;
        int i10;
        if (i8 == 17) {
            i9 = rect.left;
            i10 = rect2.right;
        } else if (i8 == 33) {
            i9 = rect.top;
            i10 = rect2.bottom;
        } else if (i8 == 66) {
            i9 = rect2.left;
            i10 = rect.right;
        } else if (i8 != 130) {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        } else {
            i9 = rect2.top;
            i10 = rect.bottom;
        }
        return i9 - i10;
    }

    private static int m(int i8, Rect rect, Rect rect2) {
        return Math.max(1, n(i8, rect, rect2));
    }

    private static int n(int i8, Rect rect, Rect rect2) {
        int i9;
        int i10;
        if (i8 == 17) {
            i9 = rect.left;
            i10 = rect2.left;
        } else if (i8 == 33) {
            i9 = rect.top;
            i10 = rect2.top;
        } else if (i8 == 66) {
            i9 = rect2.right;
            i10 = rect.right;
        } else if (i8 != 130) {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        } else {
            i9 = rect2.bottom;
            i10 = rect.bottom;
        }
        return i9 - i10;
    }

    private static int o(int i8, Rect rect, Rect rect2) {
        int height;
        int i9;
        int height2;
        if (i8 != 17) {
            if (i8 != 33) {
                if (i8 != 66) {
                    if (i8 != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                }
            }
            height = rect.left + (rect.width() / 2);
            i9 = rect2.left;
            height2 = rect2.width();
            return Math.abs(height - (i9 + (height2 / 2)));
        }
        height = rect.top + (rect.height() / 2);
        i9 = rect2.top;
        height2 = rect2.height();
        return Math.abs(height - (i9 + (height2 / 2)));
    }
}
