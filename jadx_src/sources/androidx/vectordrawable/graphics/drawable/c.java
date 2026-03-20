package androidx.vectordrawable.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import androidx.core.content.res.k;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c extends h implements Animatable {

    /* renamed from: b  reason: collision with root package name */
    private b f7648b;

    /* renamed from: c  reason: collision with root package name */
    private Context f7649c;

    /* renamed from: d  reason: collision with root package name */
    private ArgbEvaluator f7650d;

    /* renamed from: e  reason: collision with root package name */
    private Animator.AnimatorListener f7651e;

    /* renamed from: f  reason: collision with root package name */
    ArrayList<androidx.vectordrawable.graphics.drawable.b> f7652f;

    /* renamed from: g  reason: collision with root package name */
    final Drawable.Callback f7653g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Drawable.Callback {
        a() {
        }

        @Override // android.graphics.drawable.Drawable.Callback
        public void invalidateDrawable(Drawable drawable) {
            c.this.invalidateSelf();
        }

        @Override // android.graphics.drawable.Drawable.Callback
        public void scheduleDrawable(Drawable drawable, Runnable runnable, long j8) {
            c.this.scheduleSelf(runnable, j8);
        }

        @Override // android.graphics.drawable.Drawable.Callback
        public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
            c.this.unscheduleSelf(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends Drawable.ConstantState {

        /* renamed from: a  reason: collision with root package name */
        int f7655a;

        /* renamed from: b  reason: collision with root package name */
        i f7656b;

        /* renamed from: c  reason: collision with root package name */
        AnimatorSet f7657c;

        /* renamed from: d  reason: collision with root package name */
        ArrayList<Animator> f7658d;

        /* renamed from: e  reason: collision with root package name */
        k0.a<Animator, String> f7659e;

        public b(Context context, b bVar, Drawable.Callback callback, Resources resources) {
            if (bVar != null) {
                this.f7655a = bVar.f7655a;
                i iVar = bVar.f7656b;
                if (iVar != null) {
                    Drawable.ConstantState constantState = iVar.getConstantState();
                    this.f7656b = (i) (resources != null ? constantState.newDrawable(resources) : constantState.newDrawable());
                    i iVar2 = (i) this.f7656b.mutate();
                    this.f7656b = iVar2;
                    iVar2.setCallback(callback);
                    this.f7656b.setBounds(bVar.f7656b.getBounds());
                    this.f7656b.h(false);
                }
                ArrayList<Animator> arrayList = bVar.f7658d;
                if (arrayList != null) {
                    int size = arrayList.size();
                    this.f7658d = new ArrayList<>(size);
                    this.f7659e = new k0.a<>(size);
                    for (int i8 = 0; i8 < size; i8++) {
                        Animator animator = bVar.f7658d.get(i8);
                        Animator clone = animator.clone();
                        String str = bVar.f7659e.get(animator);
                        clone.setTarget(this.f7656b.d(str));
                        this.f7658d.add(clone);
                        this.f7659e.put(clone, str);
                    }
                    a();
                }
            }
        }

        public void a() {
            if (this.f7657c == null) {
                this.f7657c = new AnimatorSet();
            }
            this.f7657c.playTogether(this.f7658d);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.f7655a;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }
    }

    /* renamed from: androidx.vectordrawable.graphics.drawable.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class C0085c extends Drawable.ConstantState {

        /* renamed from: a  reason: collision with root package name */
        private final Drawable.ConstantState f7660a;

        public C0085c(Drawable.ConstantState constantState) {
            this.f7660a = constantState;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return this.f7660a.canApplyTheme();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.f7660a.getChangingConfigurations();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            c cVar = new c();
            Drawable newDrawable = this.f7660a.newDrawable();
            cVar.f7665a = newDrawable;
            newDrawable.setCallback(cVar.f7653g);
            return cVar;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            c cVar = new c();
            Drawable newDrawable = this.f7660a.newDrawable(resources);
            cVar.f7665a = newDrawable;
            newDrawable.setCallback(cVar.f7653g);
            return cVar;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            c cVar = new c();
            Drawable newDrawable = this.f7660a.newDrawable(resources, theme);
            cVar.f7665a = newDrawable;
            newDrawable.setCallback(cVar.f7653g);
            return cVar;
        }
    }

    c() {
        this(null, null, null);
    }

    private c(Context context) {
        this(context, null, null);
    }

    private c(Context context, b bVar, Resources resources) {
        this.f7650d = null;
        this.f7651e = null;
        this.f7652f = null;
        a aVar = new a();
        this.f7653g = aVar;
        this.f7649c = context;
        if (bVar != null) {
            this.f7648b = bVar;
        } else {
            this.f7648b = new b(context, bVar, aVar, resources);
        }
    }

    public static c a(Context context, Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        c cVar = new c(context);
        cVar.inflate(resources, xmlPullParser, attributeSet, theme);
        return cVar;
    }

    private void b(String str, Animator animator) {
        animator.setTarget(this.f7648b.f7656b.d(str));
        if (Build.VERSION.SDK_INT < 21) {
            c(animator);
        }
        b bVar = this.f7648b;
        if (bVar.f7658d == null) {
            bVar.f7658d = new ArrayList<>();
            this.f7648b.f7659e = new k0.a<>();
        }
        this.f7648b.f7658d.add(animator);
        this.f7648b.f7659e.put(animator, str);
    }

    private void c(Animator animator) {
        ArrayList<Animator> childAnimations;
        if ((animator instanceof AnimatorSet) && (childAnimations = ((AnimatorSet) animator).getChildAnimations()) != null) {
            for (int i8 = 0; i8 < childAnimations.size(); i8++) {
                c(childAnimations.get(i8));
            }
        }
        if (animator instanceof ObjectAnimator) {
            ObjectAnimator objectAnimator = (ObjectAnimator) animator;
            String propertyName = objectAnimator.getPropertyName();
            if ("fillColor".equals(propertyName) || "strokeColor".equals(propertyName)) {
                if (this.f7650d == null) {
                    this.f7650d = new ArgbEvaluator();
                }
                objectAnimator.setEvaluator(this.f7650d);
            }
        }
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme theme) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.a(drawable, theme);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            return androidx.core.graphics.drawable.a.b(drawable);
        }
        return false;
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void clearColorFilter() {
        super.clearColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.draw(canvas);
            return;
        }
        this.f7648b.f7656b.draw(canvas);
        if (this.f7648b.f7657c.isStarted()) {
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        Drawable drawable = this.f7665a;
        return drawable != null ? androidx.core.graphics.drawable.a.d(drawable) : this.f7648b.f7656b.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.getChangingConfigurations() : super.getChangingConfigurations() | this.f7648b.f7655a;
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        Drawable drawable = this.f7665a;
        return drawable != null ? androidx.core.graphics.drawable.a.e(drawable) : this.f7648b.f7656b.getColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        if (this.f7665a == null || Build.VERSION.SDK_INT < 24) {
            return null;
        }
        return new C0085c(this.f7665a.getConstantState());
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ Drawable getCurrent() {
        return super.getCurrent();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.getIntrinsicHeight() : this.f7648b.f7656b.getIntrinsicHeight();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.getIntrinsicWidth() : this.f7648b.f7656b.getIntrinsicWidth();
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getMinimumHeight() {
        return super.getMinimumHeight();
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int getMinimumWidth() {
        return super.getMinimumWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.getOpacity() : this.f7648b.f7656b.getOpacity();
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ boolean getPadding(Rect rect) {
        return super.getPadding(rect);
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ int[] getState() {
        return super.getState();
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ Region getTransparentRegion() {
        return super.getTransparentRegion();
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet) {
        inflate(resources, xmlPullParser, attributeSet, null);
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) {
        TypedArray obtainAttributes;
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.g(drawable, resources, xmlPullParser, attributeSet, theme);
            return;
        }
        int eventType = xmlPullParser.getEventType();
        int depth = xmlPullParser.getDepth() + 1;
        while (eventType != 1 && (xmlPullParser.getDepth() >= depth || eventType != 3)) {
            if (eventType == 2) {
                String name = xmlPullParser.getName();
                if ("animated-vector".equals(name)) {
                    obtainAttributes = k.k(resources, theme, attributeSet, androidx.vectordrawable.graphics.drawable.a.f7640e);
                    int resourceId = obtainAttributes.getResourceId(0, 0);
                    if (resourceId != 0) {
                        i b9 = i.b(resources, resourceId, theme);
                        b9.h(false);
                        b9.setCallback(this.f7653g);
                        i iVar = this.f7648b.f7656b;
                        if (iVar != null) {
                            iVar.setCallback(null);
                        }
                        this.f7648b.f7656b = b9;
                    }
                } else if ("target".equals(name)) {
                    obtainAttributes = resources.obtainAttributes(attributeSet, androidx.vectordrawable.graphics.drawable.a.f7641f);
                    String string = obtainAttributes.getString(0);
                    int resourceId2 = obtainAttributes.getResourceId(1, 0);
                    if (resourceId2 != 0) {
                        Context context = this.f7649c;
                        if (context == null) {
                            obtainAttributes.recycle();
                            throw new IllegalStateException("Context can't be null when inflating animators");
                        }
                        b(string, e.i(context, resourceId2));
                    }
                } else {
                    continue;
                }
                obtainAttributes.recycle();
            }
            eventType = xmlPullParser.next();
        }
        this.f7648b.a();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        Drawable drawable = this.f7665a;
        return drawable != null ? androidx.core.graphics.drawable.a.h(drawable) : this.f7648b.f7656b.isAutoMirrored();
    }

    @Override // android.graphics.drawable.Animatable
    public boolean isRunning() {
        Drawable drawable = this.f7665a;
        return drawable != null ? ((AnimatedVectorDrawable) drawable).isRunning() : this.f7648b.f7657c.isRunning();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.isStateful() : this.f7648b.f7656b.isStateful();
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void jumpToCurrentState() {
        super.jumpToCurrentState();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.mutate();
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    protected void onBoundsChange(Rect rect) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.setBounds(rect);
        } else {
            this.f7648b.f7656b.setBounds(rect);
        }
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    protected boolean onLevelChange(int i8) {
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.setLevel(i8) : this.f7648b.f7656b.setLevel(i8);
    }

    @Override // android.graphics.drawable.Drawable
    protected boolean onStateChange(int[] iArr) {
        Drawable drawable = this.f7665a;
        return drawable != null ? drawable.setState(iArr) : this.f7648b.f7656b.setState(iArr);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.setAlpha(i8);
        } else {
            this.f7648b.f7656b.setAlpha(i8);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean z4) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.j(drawable, z4);
        } else {
            this.f7648b.f7656b.setAutoMirrored(z4);
        }
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setChangingConfigurations(int i8) {
        super.setChangingConfigurations(i8);
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setColorFilter(int i8, PorterDuff.Mode mode) {
        super.setColorFilter(i8, mode);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
        } else {
            this.f7648b.f7656b.setColorFilter(colorFilter);
        }
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setFilterBitmap(boolean z4) {
        super.setFilterBitmap(z4);
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setHotspot(float f5, float f8) {
        super.setHotspot(f5, f8);
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ void setHotspotBounds(int i8, int i9, int i10, int i11) {
        super.setHotspotBounds(i8, i9, i10, i11);
    }

    @Override // androidx.vectordrawable.graphics.drawable.h, android.graphics.drawable.Drawable
    public /* bridge */ /* synthetic */ boolean setState(int[] iArr) {
        return super.setState(iArr);
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTint(int i8) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.n(drawable, i8);
        } else {
            this.f7648b.f7656b.setTint(i8);
        }
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintList(ColorStateList colorStateList) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.o(drawable, colorStateList);
        } else {
            this.f7648b.f7656b.setTintList(colorStateList);
        }
    }

    @Override // android.graphics.drawable.Drawable, androidx.core.graphics.drawable.b
    public void setTintMode(PorterDuff.Mode mode) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.p(drawable, mode);
        } else {
            this.f7648b.f7656b.setTintMode(mode);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean z4, boolean z8) {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            return drawable.setVisible(z4, z8);
        }
        this.f7648b.f7656b.setVisible(z4, z8);
        return super.setVisible(z4, z8);
    }

    @Override // android.graphics.drawable.Animatable
    public void start() {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            ((AnimatedVectorDrawable) drawable).start();
        } else if (this.f7648b.f7657c.isStarted()) {
        } else {
            this.f7648b.f7657c.start();
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Animatable
    public void stop() {
        Drawable drawable = this.f7665a;
        if (drawable != null) {
            ((AnimatedVectorDrawable) drawable).stop();
        } else {
            this.f7648b.f7657c.end();
        }
    }
}
