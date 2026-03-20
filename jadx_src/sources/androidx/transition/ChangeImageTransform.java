package androidx.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.transition.t;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ChangeImageTransform extends Transition {
    private static final String[] Y = {"android:changeImageTransform:matrix", "android:changeImageTransform:bounds"};
    private static final TypeEvaluator<Matrix> Z = new a();

    /* renamed from: a0  reason: collision with root package name */
    private static final Property<ImageView, Matrix> f7413a0 = new b(Matrix.class, "animatedTransform");

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements TypeEvaluator<Matrix> {
        a() {
        }

        @Override // android.animation.TypeEvaluator
        /* renamed from: a */
        public Matrix evaluate(float f5, Matrix matrix, Matrix matrix2) {
            return null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b extends Property<ImageView, Matrix> {
        b(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Matrix get(ImageView imageView) {
            return null;
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(ImageView imageView, Matrix matrix) {
            j.a(imageView, matrix);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class c {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f7414a;

        static {
            int[] iArr = new int[ImageView.ScaleType.values().length];
            f7414a = iArr;
            try {
                iArr[ImageView.ScaleType.FIT_XY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f7414a[ImageView.ScaleType.CENTER_CROP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public ChangeImageTransform() {
    }

    public ChangeImageTransform(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void o0(u uVar) {
        View view = uVar.f7620b;
        if ((view instanceof ImageView) && view.getVisibility() == 0) {
            ImageView imageView = (ImageView) view;
            if (imageView.getDrawable() == null) {
                return;
            }
            Map<String, Object> map = uVar.f7619a;
            map.put("android:changeImageTransform:bounds", new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom()));
            map.put("android:changeImageTransform:matrix", q0(imageView));
        }
    }

    private static Matrix p0(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        float width = imageView.getWidth();
        float f5 = intrinsicWidth;
        int intrinsicHeight = drawable.getIntrinsicHeight();
        float height = imageView.getHeight();
        float f8 = intrinsicHeight;
        float max = Math.max(width / f5, height / f8);
        int round = Math.round((width - (f5 * max)) / 2.0f);
        int round2 = Math.round((height - (f8 * max)) / 2.0f);
        Matrix matrix = new Matrix();
        matrix.postScale(max, max);
        matrix.postTranslate(round, round2);
        return matrix;
    }

    private static Matrix q0(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable.getIntrinsicWidth() > 0 && drawable.getIntrinsicHeight() > 0) {
            int i8 = c.f7414a[imageView.getScaleType().ordinal()];
            if (i8 == 1) {
                return t0(imageView);
            }
            if (i8 == 2) {
                return p0(imageView);
            }
        }
        return new Matrix(imageView.getImageMatrix());
    }

    private ObjectAnimator r0(ImageView imageView, Matrix matrix, Matrix matrix2) {
        return ObjectAnimator.ofObject(imageView, (Property<ImageView, V>) f7413a0, (TypeEvaluator) new t.a(), (Object[]) new Matrix[]{matrix, matrix2});
    }

    private ObjectAnimator s0(ImageView imageView) {
        Property<ImageView, Matrix> property = f7413a0;
        TypeEvaluator<Matrix> typeEvaluator = Z;
        Matrix matrix = k.f7578a;
        return ObjectAnimator.ofObject(imageView, (Property<ImageView, V>) property, (TypeEvaluator) typeEvaluator, (Object[]) new Matrix[]{matrix, matrix});
    }

    private static Matrix t0(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Matrix matrix = new Matrix();
        matrix.postScale(imageView.getWidth() / drawable.getIntrinsicWidth(), imageView.getHeight() / drawable.getIntrinsicHeight());
        return matrix;
    }

    @Override // androidx.transition.Transition
    public String[] L() {
        return Y;
    }

    @Override // androidx.transition.Transition
    public void j(u uVar) {
        o0(uVar);
    }

    @Override // androidx.transition.Transition
    public void m(u uVar) {
        o0(uVar);
    }

    @Override // androidx.transition.Transition
    public Animator s(ViewGroup viewGroup, u uVar, u uVar2) {
        if (uVar == null || uVar2 == null) {
            return null;
        }
        Rect rect = (Rect) uVar.f7619a.get("android:changeImageTransform:bounds");
        Rect rect2 = (Rect) uVar2.f7619a.get("android:changeImageTransform:bounds");
        if (rect == null || rect2 == null) {
            return null;
        }
        Matrix matrix = (Matrix) uVar.f7619a.get("android:changeImageTransform:matrix");
        Matrix matrix2 = (Matrix) uVar2.f7619a.get("android:changeImageTransform:matrix");
        boolean z4 = (matrix == null && matrix2 == null) || (matrix != null && matrix.equals(matrix2));
        if (rect.equals(rect2) && z4) {
            return null;
        }
        ImageView imageView = (ImageView) uVar2.f7620b;
        Drawable drawable = imageView.getDrawable();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            return s0(imageView);
        }
        if (matrix == null) {
            matrix = k.f7578a;
        }
        if (matrix2 == null) {
            matrix2 = k.f7578a;
        }
        f7413a0.set(imageView, matrix);
        return r0(imageView, matrix, matrix2);
    }
}
