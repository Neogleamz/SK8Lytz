package androidx.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Explode extends Visibility {

    /* renamed from: b0  reason: collision with root package name */
    private static final TimeInterpolator f7443b0 = new DecelerateInterpolator();

    /* renamed from: c0  reason: collision with root package name */
    private static final TimeInterpolator f7444c0 = new AccelerateInterpolator();

    /* renamed from: a0  reason: collision with root package name */
    private int[] f7445a0;

    public Explode() {
        this.f7445a0 = new int[2];
        k0(new x1.a());
    }

    public Explode(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f7445a0 = new int[2];
        k0(new x1.a());
    }

    private void o0(u uVar) {
        View view = uVar.f7620b;
        view.getLocationOnScreen(this.f7445a0);
        int[] iArr = this.f7445a0;
        int i8 = iArr[0];
        int i9 = iArr[1];
        uVar.f7619a.put("android:explode:screenBounds", new Rect(i8, i9, view.getWidth() + i8, view.getHeight() + i9));
    }

    private static float w0(float f5, float f8) {
        return (float) Math.sqrt((f5 * f5) + (f8 * f8));
    }

    private static float x0(View view, int i8, int i9) {
        return w0(Math.max(i8, view.getWidth() - i8), Math.max(i9, view.getHeight() - i9));
    }

    private void y0(View view, Rect rect, int[] iArr) {
        int centerY;
        int i8;
        view.getLocationOnScreen(this.f7445a0);
        int[] iArr2 = this.f7445a0;
        int i9 = iArr2[0];
        int i10 = iArr2[1];
        Rect y8 = y();
        if (y8 == null) {
            i8 = (view.getWidth() / 2) + i9 + Math.round(view.getTranslationX());
            centerY = (view.getHeight() / 2) + i10 + Math.round(view.getTranslationY());
        } else {
            int centerX = y8.centerX();
            centerY = y8.centerY();
            i8 = centerX;
        }
        float centerX2 = rect.centerX() - i8;
        float centerY2 = rect.centerY() - centerY;
        if (centerX2 == 0.0f && centerY2 == 0.0f) {
            centerX2 = ((float) (Math.random() * 2.0d)) - 1.0f;
            centerY2 = ((float) (Math.random() * 2.0d)) - 1.0f;
        }
        float w02 = w0(centerX2, centerY2);
        float x02 = x0(view, i8 - i9, centerY - i10);
        iArr[0] = Math.round((centerX2 / w02) * x02);
        iArr[1] = Math.round(x02 * (centerY2 / w02));
    }

    @Override // androidx.transition.Visibility, androidx.transition.Transition
    public void j(u uVar) {
        super.j(uVar);
        o0(uVar);
    }

    @Override // androidx.transition.Visibility, androidx.transition.Transition
    public void m(u uVar) {
        super.m(uVar);
        o0(uVar);
    }

    @Override // androidx.transition.Visibility
    public Animator r0(ViewGroup viewGroup, View view, u uVar, u uVar2) {
        if (uVar2 == null) {
            return null;
        }
        Rect rect = (Rect) uVar2.f7619a.get("android:explode:screenBounds");
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        y0(viewGroup, rect, this.f7445a0);
        int[] iArr = this.f7445a0;
        return w.a(view, uVar2, rect.left, rect.top, translationX + iArr[0], translationY + iArr[1], translationX, translationY, f7443b0, this);
    }

    @Override // androidx.transition.Visibility
    public Animator t0(ViewGroup viewGroup, View view, u uVar, u uVar2) {
        float f5;
        float f8;
        if (uVar == null) {
            return null;
        }
        Rect rect = (Rect) uVar.f7619a.get("android:explode:screenBounds");
        int i8 = rect.left;
        int i9 = rect.top;
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        int[] iArr = (int[]) uVar.f7620b.getTag(x1.b.f23767h);
        if (iArr != null) {
            f5 = (iArr[0] - rect.left) + translationX;
            f8 = (iArr[1] - rect.top) + translationY;
            rect.offsetTo(iArr[0], iArr[1]);
        } else {
            f5 = translationX;
            f8 = translationY;
        }
        y0(viewGroup, rect, this.f7445a0);
        int[] iArr2 = this.f7445a0;
        return w.a(view, uVar, i8, i9, translationX, translationY, f5 + iArr2[0], f8 + iArr2[1], f7444c0, this);
    }
}
