package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class t {

    /* renamed from: a  reason: collision with root package name */
    private static final boolean f7613a;

    /* renamed from: b  reason: collision with root package name */
    private static final boolean f7614b;

    /* renamed from: c  reason: collision with root package name */
    private static final boolean f7615c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements TypeEvaluator<Matrix> {

        /* renamed from: a  reason: collision with root package name */
        final float[] f7616a = new float[9];

        /* renamed from: b  reason: collision with root package name */
        final float[] f7617b = new float[9];

        /* renamed from: c  reason: collision with root package name */
        final Matrix f7618c = new Matrix();

        @Override // android.animation.TypeEvaluator
        /* renamed from: a */
        public Matrix evaluate(float f5, Matrix matrix, Matrix matrix2) {
            matrix.getValues(this.f7616a);
            matrix2.getValues(this.f7617b);
            for (int i8 = 0; i8 < 9; i8++) {
                float[] fArr = this.f7617b;
                float f8 = fArr[i8];
                float[] fArr2 = this.f7616a;
                fArr[i8] = fArr2[i8] + ((f8 - fArr2[i8]) * f5);
            }
            this.f7618c.setValues(this.f7617b);
            return this.f7618c;
        }
    }

    static {
        int i8 = Build.VERSION.SDK_INT;
        f7613a = i8 >= 19;
        f7614b = i8 >= 18;
        f7615c = i8 >= 28;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static View a(ViewGroup viewGroup, View view, View view2) {
        Matrix matrix = new Matrix();
        matrix.setTranslate(-view2.getScrollX(), -view2.getScrollY());
        f0.j(view, matrix);
        f0.k(viewGroup, matrix);
        RectF rectF = new RectF(0.0f, 0.0f, view.getWidth(), view.getHeight());
        matrix.mapRect(rectF);
        int round = Math.round(rectF.left);
        int round2 = Math.round(rectF.top);
        int round3 = Math.round(rectF.right);
        int round4 = Math.round(rectF.bottom);
        ImageView imageView = new ImageView(view.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap b9 = b(view, matrix, rectF, viewGroup);
        if (b9 != null) {
            imageView.setImageBitmap(b9);
        }
        imageView.measure(View.MeasureSpec.makeMeasureSpec(round3 - round, 1073741824), View.MeasureSpec.makeMeasureSpec(round4 - round2, 1073741824));
        imageView.layout(round, round2, round3, round4);
        return imageView;
    }

    private static Bitmap b(View view, Matrix matrix, RectF rectF, ViewGroup viewGroup) {
        boolean z4;
        boolean z8;
        int i8;
        ViewGroup viewGroup2;
        if (f7613a) {
            z4 = !view.isAttachedToWindow();
            z8 = viewGroup == null ? false : viewGroup.isAttachedToWindow();
        } else {
            z4 = false;
            z8 = false;
        }
        boolean z9 = f7614b;
        Bitmap bitmap = null;
        if (!z9 || !z4) {
            i8 = 0;
            viewGroup2 = null;
        } else if (!z8) {
            return null;
        } else {
            viewGroup2 = (ViewGroup) view.getParent();
            i8 = viewGroup2.indexOfChild(view);
            viewGroup.getOverlay().add(view);
        }
        int round = Math.round(rectF.width());
        int round2 = Math.round(rectF.height());
        if (round > 0 && round2 > 0) {
            float min = Math.min(1.0f, 1048576.0f / (round * round2));
            int round3 = Math.round(round * min);
            int round4 = Math.round(round2 * min);
            matrix.postTranslate(-rectF.left, -rectF.top);
            matrix.postScale(min, min);
            if (f7615c) {
                Picture picture = new Picture();
                Canvas beginRecording = picture.beginRecording(round3, round4);
                beginRecording.concat(matrix);
                view.draw(beginRecording);
                picture.endRecording();
                bitmap = Bitmap.createBitmap(picture);
            } else {
                bitmap = Bitmap.createBitmap(round3, round4, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.concat(matrix);
                view.draw(canvas);
            }
        }
        if (z9 && z4) {
            viewGroup.getOverlay().remove(view);
            viewGroup2.addView(view, i8);
        }
        return bitmap;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Animator c(Animator animator, Animator animator2) {
        if (animator == null) {
            return animator2;
        }
        if (animator2 == null) {
            return animator;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator, animator2);
        return animatorSet;
    }
}
