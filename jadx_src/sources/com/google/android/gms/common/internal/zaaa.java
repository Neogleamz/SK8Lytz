package com.google.android.gms.common.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zaaa extends Button {
    public zaaa(Context context, AttributeSet attributeSet) {
        super(context, null, 16842824);
    }

    private static final int b(int i8, int i9, int i10, int i11) {
        if (i8 != 0) {
            if (i8 != 1) {
                if (i8 == 2) {
                    return i11;
                }
                throw new IllegalStateException("Unknown color scheme: " + i8);
            }
            return i10;
        }
        return i9;
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void a(android.content.res.Resources r6, int r7, int r8) {
        /*
            r5 = this;
            android.graphics.Typeface r0 = android.graphics.Typeface.DEFAULT_BOLD
            r5.setTypeface(r0)
            r0 = 1096810496(0x41600000, float:14.0)
            r5.setTextSize(r0)
            android.util.DisplayMetrics r0 = r6.getDisplayMetrics()
            float r0 = r0.density
            r1 = 1111490560(0x42400000, float:48.0)
            float r0 = r0 * r1
            r1 = 1056964608(0x3f000000, float:0.5)
            float r0 = r0 + r1
            int r0 = (int) r0
            r5.setMinHeight(r0)
            r5.setMinWidth(r0)
            int r0 = h6.b.f20323b
            int r1 = h6.b.f20324c
            int r0 = b(r8, r0, r1, r1)
            int r1 = h6.b.f20325d
            int r2 = h6.b.f20326e
            int r1 = b(r8, r1, r2, r2)
            java.lang.String r2 = "Unknown button size: "
            r3 = 2
            r4 = 1
            if (r7 == 0) goto L4d
            if (r7 == r4) goto L4d
            if (r7 != r3) goto L38
            goto L4e
        L38:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r2)
            r8.append(r7)
            java.lang.String r7 = r8.toString()
            r6.<init>(r7)
            throw r6
        L4d:
            r0 = r1
        L4e:
            android.graphics.drawable.Drawable r0 = r6.getDrawable(r0)
            android.graphics.drawable.Drawable r0 = androidx.core.graphics.drawable.a.r(r0)
            int r1 = h6.a.f20321c
            android.content.res.ColorStateList r1 = r6.getColorStateList(r1)
            androidx.core.graphics.drawable.a.o(r0, r1)
            android.graphics.PorterDuff$Mode r1 = android.graphics.PorterDuff.Mode.SRC_ATOP
            androidx.core.graphics.drawable.a.p(r0, r1)
            r5.setBackgroundDrawable(r0)
            int r0 = h6.a.f20319a
            int r1 = h6.a.f20320b
            int r8 = b(r8, r0, r1, r1)
            android.content.res.ColorStateList r8 = r6.getColorStateList(r8)
            java.lang.Object r8 = n6.j.l(r8)
            android.content.res.ColorStateList r8 = (android.content.res.ColorStateList) r8
            r5.setTextColor(r8)
            r8 = 0
            if (r7 == 0) goto L9f
            if (r7 == r4) goto L9c
            if (r7 != r3) goto L87
            r5.setText(r8)
            goto La8
        L87:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r2)
            r8.append(r7)
            java.lang.String r7 = r8.toString()
            r6.<init>(r7)
            throw r6
        L9c:
            int r7 = h6.c.q
            goto La1
        L9f:
            int r7 = h6.c.f20342p
        La1:
            java.lang.String r6 = r6.getString(r7)
            r5.setText(r6)
        La8:
            r5.setTransformationMethod(r8)
            android.content.Context r6 = r5.getContext()
            boolean r6 = u6.h.c(r6)
            if (r6 == 0) goto Lba
            r6 = 19
            r5.setGravity(r6)
        Lba:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zaaa.a(android.content.res.Resources, int, int):void");
    }
}
