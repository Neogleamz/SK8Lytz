package x1;

import android.graphics.Rect;
import android.view.ViewGroup;
import androidx.transition.Transition;
import androidx.transition.u;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c extends e {

    /* renamed from: b  reason: collision with root package name */
    private float f23769b = 3.0f;

    /* renamed from: c  reason: collision with root package name */
    private int f23770c = 80;

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0017, code lost:
        r0 = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0026, code lost:
        if ((androidx.core.view.c0.E(r7) == 1) != false) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0013, code lost:
        if ((androidx.core.view.c0.E(r7) == 1) != false) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0015, code lost:
        r0 = 5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int h(android.view.View r7, int r8, int r9, int r10, int r11, int r12, int r13, int r14, int r15) {
        /*
            r6 = this;
            int r0 = r6.f23770c
            r1 = 5
            r2 = 3
            r3 = 0
            r4 = 1
            r5 = 8388611(0x800003, float:1.1754948E-38)
            if (r0 != r5) goto L19
            int r7 = androidx.core.view.c0.E(r7)
            if (r7 != r4) goto L12
            goto L13
        L12:
            r4 = r3
        L13:
            if (r4 == 0) goto L17
        L15:
            r0 = r1
            goto L29
        L17:
            r0 = r2
            goto L29
        L19:
            r5 = 8388613(0x800005, float:1.175495E-38)
            if (r0 != r5) goto L29
            int r7 = androidx.core.view.c0.E(r7)
            if (r7 != r4) goto L25
            goto L26
        L25:
            r4 = r3
        L26:
            if (r4 == 0) goto L15
            goto L17
        L29:
            if (r0 == r2) goto L51
            if (r0 == r1) goto L48
            r7 = 48
            if (r0 == r7) goto L3f
            r7 = 80
            if (r0 == r7) goto L36
            goto L59
        L36:
            int r9 = r9 - r13
            int r10 = r10 - r8
            int r7 = java.lang.Math.abs(r10)
            int r3 = r9 + r7
            goto L59
        L3f:
            int r15 = r15 - r9
            int r10 = r10 - r8
            int r7 = java.lang.Math.abs(r10)
            int r3 = r15 + r7
            goto L59
        L48:
            int r8 = r8 - r12
            int r11 = r11 - r9
            int r7 = java.lang.Math.abs(r11)
            int r3 = r8 + r7
            goto L59
        L51:
            int r14 = r14 - r8
            int r11 = r11 - r9
            int r7 = java.lang.Math.abs(r11)
            int r3 = r14 + r7
        L59:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: x1.c.h(android.view.View, int, int, int, int, int, int, int, int):int");
    }

    private int i(ViewGroup viewGroup) {
        int i8 = this.f23770c;
        return (i8 == 3 || i8 == 5 || i8 == 8388611 || i8 == 8388613) ? viewGroup.getWidth() : viewGroup.getHeight();
    }

    @Override // x1.d
    public long c(ViewGroup viewGroup, Transition transition, u uVar, u uVar2) {
        int i8;
        int i9;
        int i10;
        u uVar3 = uVar;
        if (uVar3 == null && uVar2 == null) {
            return 0L;
        }
        Rect y8 = transition.y();
        if (uVar2 == null || e(uVar3) == 0) {
            i8 = -1;
        } else {
            uVar3 = uVar2;
            i8 = 1;
        }
        int f5 = f(uVar3);
        int g8 = g(uVar3);
        int[] iArr = new int[2];
        viewGroup.getLocationOnScreen(iArr);
        int round = iArr[0] + Math.round(viewGroup.getTranslationX());
        int round2 = iArr[1] + Math.round(viewGroup.getTranslationY());
        int width = round + viewGroup.getWidth();
        int height = round2 + viewGroup.getHeight();
        if (y8 != null) {
            i9 = y8.centerX();
            i10 = y8.centerY();
        } else {
            i9 = (round + width) / 2;
            i10 = (round2 + height) / 2;
        }
        float h8 = h(viewGroup, f5, g8, i9, i10, round, round2, width, height) / i(viewGroup);
        long x8 = transition.x();
        if (x8 < 0) {
            x8 = 300;
        }
        return Math.round((((float) (x8 * i8)) / this.f23769b) * h8);
    }

    public void j(int i8) {
        this.f23770c = i8;
    }
}
