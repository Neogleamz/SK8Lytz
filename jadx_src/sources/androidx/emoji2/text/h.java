package androidx.emoji2.text;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import androidx.emoji2.text.e;
import androidx.emoji2.text.m;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {

    /* renamed from: a  reason: collision with root package name */
    private final e.i f5261a;

    /* renamed from: b  reason: collision with root package name */
    private final m f5262b;

    /* renamed from: c  reason: collision with root package name */
    private e.d f5263c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f5264d;

    /* renamed from: e  reason: collision with root package name */
    private final int[] f5265e;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        static int a(CharSequence charSequence, int i8, int i9) {
            int length = charSequence.length();
            if (i8 < 0 || length < i8 || i9 < 0) {
                return -1;
            }
            while (true) {
                boolean z4 = false;
                while (i9 != 0) {
                    i8--;
                    if (i8 < 0) {
                        return z4 ? -1 : 0;
                    }
                    char charAt = charSequence.charAt(i8);
                    if (z4) {
                        if (!Character.isHighSurrogate(charAt)) {
                            return -1;
                        }
                        i9--;
                    } else if (!Character.isSurrogate(charAt)) {
                        i9--;
                    } else if (Character.isHighSurrogate(charAt)) {
                        return -1;
                    } else {
                        z4 = true;
                    }
                }
                return i8;
            }
        }

        static int b(CharSequence charSequence, int i8, int i9) {
            int length = charSequence.length();
            if (i8 < 0 || length < i8 || i9 < 0) {
                return -1;
            }
            while (true) {
                boolean z4 = false;
                while (i9 != 0) {
                    if (i8 >= length) {
                        if (z4) {
                            return -1;
                        }
                        return length;
                    }
                    char charAt = charSequence.charAt(i8);
                    if (z4) {
                        if (!Character.isLowSurrogate(charAt)) {
                            return -1;
                        }
                        i9--;
                        i8++;
                    } else if (!Character.isSurrogate(charAt)) {
                        i9--;
                        i8++;
                    } else if (Character.isLowSurrogate(charAt)) {
                        return -1;
                    } else {
                        i8++;
                        z4 = true;
                    }
                }
                return i8;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private int f5266a = 1;

        /* renamed from: b  reason: collision with root package name */
        private final m.a f5267b;

        /* renamed from: c  reason: collision with root package name */
        private m.a f5268c;

        /* renamed from: d  reason: collision with root package name */
        private m.a f5269d;

        /* renamed from: e  reason: collision with root package name */
        private int f5270e;

        /* renamed from: f  reason: collision with root package name */
        private int f5271f;

        /* renamed from: g  reason: collision with root package name */
        private final boolean f5272g;

        /* renamed from: h  reason: collision with root package name */
        private final int[] f5273h;

        b(m.a aVar, boolean z4, int[] iArr) {
            this.f5267b = aVar;
            this.f5268c = aVar;
            this.f5272g = z4;
            this.f5273h = iArr;
        }

        private static boolean d(int i8) {
            return i8 == 65039;
        }

        private static boolean f(int i8) {
            return i8 == 65038;
        }

        private int g() {
            this.f5266a = 1;
            this.f5268c = this.f5267b;
            this.f5271f = 0;
            return 1;
        }

        private boolean h() {
            if (this.f5268c.b().j() || d(this.f5270e)) {
                return true;
            }
            if (this.f5272g) {
                if (this.f5273h == null) {
                    return true;
                }
                if (Arrays.binarySearch(this.f5273h, this.f5268c.b().b(0)) < 0) {
                    return true;
                }
            }
            return false;
        }

        int a(int i8) {
            m.a a9 = this.f5268c.a(i8);
            int i9 = 3;
            if (this.f5266a != 2) {
                if (a9 != null) {
                    this.f5266a = 2;
                    this.f5268c = a9;
                    this.f5271f = 1;
                    i9 = 2;
                }
                i9 = g();
            } else {
                if (a9 != null) {
                    this.f5268c = a9;
                    this.f5271f++;
                } else {
                    if (!f(i8)) {
                        if (!d(i8)) {
                            if (this.f5268c.b() != null && (this.f5271f != 1 || h())) {
                                this.f5269d = this.f5268c;
                                g();
                            }
                        }
                    }
                    i9 = g();
                }
                i9 = 2;
            }
            this.f5270e = i8;
            return i9;
        }

        g b() {
            return this.f5268c.b();
        }

        g c() {
            return this.f5269d.b();
        }

        boolean e() {
            return this.f5266a == 2 && this.f5268c.b() != null && (this.f5271f > 1 || h());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(m mVar, e.i iVar, e.d dVar, boolean z4, int[] iArr) {
        this.f5261a = iVar;
        this.f5262b = mVar;
        this.f5263c = dVar;
        this.f5264d = z4;
        this.f5265e = iArr;
    }

    private void a(Spannable spannable, g gVar, int i8, int i9) {
        spannable.setSpan(this.f5261a.a(gVar), i8, i9, 33);
    }

    private static boolean b(Editable editable, KeyEvent keyEvent, boolean z4) {
        i[] iVarArr;
        if (g(keyEvent)) {
            return false;
        }
        int selectionStart = Selection.getSelectionStart(editable);
        int selectionEnd = Selection.getSelectionEnd(editable);
        if (!f(selectionStart, selectionEnd) && (iVarArr = (i[]) editable.getSpans(selectionStart, selectionEnd, i.class)) != null && iVarArr.length > 0) {
            for (i iVar : iVarArr) {
                int spanStart = editable.getSpanStart(iVar);
                int spanEnd = editable.getSpanEnd(iVar);
                if ((z4 && spanStart == selectionStart) || ((!z4 && spanEnd == selectionStart) || (selectionStart > spanStart && selectionStart < spanEnd))) {
                    editable.delete(spanStart, spanEnd);
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean c(InputConnection inputConnection, Editable editable, int i8, int i9, boolean z4) {
        int max;
        int min;
        if (editable != null && inputConnection != null && i8 >= 0 && i9 >= 0) {
            int selectionStart = Selection.getSelectionStart(editable);
            int selectionEnd = Selection.getSelectionEnd(editable);
            if (f(selectionStart, selectionEnd)) {
                return false;
            }
            if (z4) {
                max = a.a(editable, selectionStart, Math.max(i8, 0));
                min = a.b(editable, selectionEnd, Math.max(i9, 0));
                if (max == -1 || min == -1) {
                    return false;
                }
            } else {
                max = Math.max(selectionStart - i8, 0);
                min = Math.min(selectionEnd + i9, editable.length());
            }
            i[] iVarArr = (i[]) editable.getSpans(max, min, i.class);
            if (iVarArr != null && iVarArr.length > 0) {
                for (i iVar : iVarArr) {
                    int spanStart = editable.getSpanStart(iVar);
                    int spanEnd = editable.getSpanEnd(iVar);
                    max = Math.min(spanStart, max);
                    min = Math.max(spanEnd, min);
                }
                int max2 = Math.max(max, 0);
                int min2 = Math.min(min, editable.length());
                inputConnection.beginBatchEdit();
                editable.delete(max2, min2);
                inputConnection.endBatchEdit();
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean d(Editable editable, int i8, KeyEvent keyEvent) {
        if (i8 != 67 ? i8 != 112 ? false : b(editable, keyEvent, true) : b(editable, keyEvent, false)) {
            MetaKeyKeyListener.adjustMetaAfterKeypress(editable);
            return true;
        }
        return false;
    }

    private boolean e(CharSequence charSequence, int i8, int i9, g gVar) {
        if (gVar.d() == 0) {
            gVar.k(this.f5263c.a(charSequence, i8, i9, gVar.h()));
        }
        return gVar.d() == 2;
    }

    private static boolean f(int i8, int i9) {
        return i8 == -1 || i9 == -1 || i8 != i9;
    }

    private static boolean g(KeyEvent keyEvent) {
        return !KeyEvent.metaStateHasNoModifiers(keyEvent.getMetaState());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0125, code lost:
        ((androidx.emoji2.text.n) r10).d();
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x00a2 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0047 A[Catch: all -> 0x012c, TryCatch #0 {all -> 0x012c, blocks: (B:7:0x000d, B:10:0x0012, B:12:0x0016, B:14:0x0025, B:18:0x0036, B:20:0x0040, B:22:0x0043, B:24:0x0047, B:26:0x0053, B:27:0x0056, B:29:0x0063, B:35:0x0072, B:36:0x0080, B:40:0x009b, B:48:0x00ab, B:51:0x00b7, B:52:0x00c1, B:53:0x00cb, B:55:0x00d2, B:56:0x00d7, B:58:0x00e2, B:60:0x00e9, B:64:0x00f3, B:67:0x00ff, B:68:0x0105, B:70:0x010e, B:15:0x002b), top: B:84:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x00ff A[Catch: all -> 0x012c, TryCatch #0 {all -> 0x012c, blocks: (B:7:0x000d, B:10:0x0012, B:12:0x0016, B:14:0x0025, B:18:0x0036, B:20:0x0040, B:22:0x0043, B:24:0x0047, B:26:0x0053, B:27:0x0056, B:29:0x0063, B:35:0x0072, B:36:0x0080, B:40:0x009b, B:48:0x00ab, B:51:0x00b7, B:52:0x00c1, B:53:0x00cb, B:55:0x00d2, B:56:0x00d7, B:58:0x00e2, B:60:0x00e9, B:64:0x00f3, B:67:0x00ff, B:68:0x0105, B:70:0x010e, B:15:0x002b), top: B:84:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x010e A[Catch: all -> 0x012c, TRY_LEAVE, TryCatch #0 {all -> 0x012c, blocks: (B:7:0x000d, B:10:0x0012, B:12:0x0016, B:14:0x0025, B:18:0x0036, B:20:0x0040, B:22:0x0043, B:24:0x0047, B:26:0x0053, B:27:0x0056, B:29:0x0063, B:35:0x0072, B:36:0x0080, B:40:0x009b, B:48:0x00ab, B:51:0x00b7, B:52:0x00c1, B:53:0x00cb, B:55:0x00d2, B:56:0x00d7, B:58:0x00e2, B:60:0x00e9, B:64:0x00f3, B:67:0x00ff, B:68:0x0105, B:70:0x010e, B:15:0x002b), top: B:84:0x000d }] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x00d7 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.CharSequence h(java.lang.CharSequence r10, int r11, int r12, int r13, boolean r14) {
        /*
            Method dump skipped, instructions count: 309
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.emoji2.text.h.h(java.lang.CharSequence, int, int, int, boolean):java.lang.CharSequence");
    }
}
