package com.google.android.material.badge;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.internal.ParcelableSparseArray;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: a  reason: collision with root package name */
    public static final boolean f17456a;

    static {
        f17456a = Build.VERSION.SDK_INT < 18;
    }

    public static void a(BadgeDrawable badgeDrawable, View view, FrameLayout frameLayout) {
        e(badgeDrawable, view, frameLayout);
        if (badgeDrawable.i() != null) {
            badgeDrawable.i().setForeground(badgeDrawable);
        } else if (f17456a) {
            throw new IllegalArgumentException("Trying to reference null customBadgeParent");
        } else {
            view.getOverlay().add(badgeDrawable);
        }
    }

    public static SparseArray<BadgeDrawable> b(Context context, ParcelableSparseArray parcelableSparseArray) {
        SparseArray<BadgeDrawable> sparseArray = new SparseArray<>(parcelableSparseArray.size());
        for (int i8 = 0; i8 < parcelableSparseArray.size(); i8++) {
            int keyAt = parcelableSparseArray.keyAt(i8);
            BadgeDrawable.SavedState savedState = (BadgeDrawable.SavedState) parcelableSparseArray.valueAt(i8);
            if (savedState == null) {
                throw new IllegalArgumentException("BadgeDrawable's savedState cannot be null");
            }
            sparseArray.put(keyAt, BadgeDrawable.e(context, savedState));
        }
        return sparseArray;
    }

    public static ParcelableSparseArray c(SparseArray<BadgeDrawable> sparseArray) {
        ParcelableSparseArray parcelableSparseArray = new ParcelableSparseArray();
        for (int i8 = 0; i8 < sparseArray.size(); i8++) {
            int keyAt = sparseArray.keyAt(i8);
            BadgeDrawable valueAt = sparseArray.valueAt(i8);
            if (valueAt == null) {
                throw new IllegalArgumentException("badgeDrawable cannot be null");
            }
            parcelableSparseArray.put(keyAt, valueAt.m());
        }
        return parcelableSparseArray;
    }

    public static void d(BadgeDrawable badgeDrawable, View view) {
        if (badgeDrawable == null) {
            return;
        }
        if (f17456a || badgeDrawable.i() != null) {
            badgeDrawable.i().setForeground(null);
        } else {
            view.getOverlay().remove(badgeDrawable);
        }
    }

    public static void e(BadgeDrawable badgeDrawable, View view, FrameLayout frameLayout) {
        Rect rect = new Rect();
        view.getDrawingRect(rect);
        badgeDrawable.setBounds(rect);
        badgeDrawable.F(view, frameLayout);
    }

    public static void f(Rect rect, float f5, float f8, float f9, float f10) {
        rect.set((int) (f5 - f9), (int) (f8 - f10), (int) (f5 + f9), (int) (f8 + f10));
    }
}
