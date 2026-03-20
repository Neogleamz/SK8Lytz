package androidx.recyclerview.widget;

import android.graphics.Canvas;
import android.os.Build;
import android.view.View;
import androidx.core.view.c0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class n implements m {

    /* renamed from: a  reason: collision with root package name */
    static final m f7003a = new n();

    n() {
    }

    private static float e(RecyclerView recyclerView, View view) {
        int childCount = recyclerView.getChildCount();
        float f5 = 0.0f;
        for (int i8 = 0; i8 < childCount; i8++) {
            View childAt = recyclerView.getChildAt(i8);
            if (childAt != view) {
                float y8 = c0.y(childAt);
                if (y8 > f5) {
                    f5 = y8;
                }
            }
        }
        return f5;
    }

    @Override // androidx.recyclerview.widget.m
    public void a(View view) {
        if (Build.VERSION.SDK_INT >= 21) {
            int i8 = p1.c.f22271a;
            Object tag = view.getTag(i8);
            if (tag instanceof Float) {
                c0.B0(view, ((Float) tag).floatValue());
            }
            view.setTag(i8, null);
        }
        view.setTranslationX(0.0f);
        view.setTranslationY(0.0f);
    }

    @Override // androidx.recyclerview.widget.m
    public void b(View view) {
    }

    @Override // androidx.recyclerview.widget.m
    public void c(Canvas canvas, RecyclerView recyclerView, View view, float f5, float f8, int i8, boolean z4) {
    }

    @Override // androidx.recyclerview.widget.m
    public void d(Canvas canvas, RecyclerView recyclerView, View view, float f5, float f8, int i8, boolean z4) {
        if (Build.VERSION.SDK_INT >= 21 && z4) {
            int i9 = p1.c.f22271a;
            if (view.getTag(i9) == null) {
                Float valueOf = Float.valueOf(c0.y(view));
                c0.B0(view, e(recyclerView, view) + 1.0f);
                view.setTag(i9, valueOf);
            }
        }
        view.setTranslationX(f5);
        view.setTranslationY(f8);
    }
}
