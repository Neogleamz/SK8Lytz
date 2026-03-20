package l7;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.util.Property;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h {

    /* renamed from: a  reason: collision with root package name */
    private final k0.g<String, i> f21799a = new k0.g<>();

    /* renamed from: b  reason: collision with root package name */
    private final k0.g<String, PropertyValuesHolder[]> f21800b = new k0.g<>();

    private static void a(h hVar, Animator animator) {
        if (animator instanceof ObjectAnimator) {
            ObjectAnimator objectAnimator = (ObjectAnimator) animator;
            hVar.l(objectAnimator.getPropertyName(), objectAnimator.getValues());
            hVar.m(objectAnimator.getPropertyName(), i.b(objectAnimator));
            return;
        }
        throw new IllegalArgumentException("Animator must be an ObjectAnimator: " + animator);
    }

    private PropertyValuesHolder[] b(PropertyValuesHolder[] propertyValuesHolderArr) {
        PropertyValuesHolder[] propertyValuesHolderArr2 = new PropertyValuesHolder[propertyValuesHolderArr.length];
        for (int i8 = 0; i8 < propertyValuesHolderArr.length; i8++) {
            propertyValuesHolderArr2[i8] = propertyValuesHolderArr[i8].clone();
        }
        return propertyValuesHolderArr2;
    }

    public static h c(Context context, TypedArray typedArray, int i8) {
        int resourceId;
        if (!typedArray.hasValue(i8) || (resourceId = typedArray.getResourceId(i8, 0)) == 0) {
            return null;
        }
        return d(context, resourceId);
    }

    public static h d(Context context, int i8) {
        try {
            Animator loadAnimator = AnimatorInflater.loadAnimator(context, i8);
            if (loadAnimator instanceof AnimatorSet) {
                return e(((AnimatorSet) loadAnimator).getChildAnimations());
            }
            if (loadAnimator != null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(loadAnimator);
                return e(arrayList);
            }
            return null;
        } catch (Exception e8) {
            Log.w("MotionSpec", "Can't load animation resource ID #0x" + Integer.toHexString(i8), e8);
            return null;
        }
    }

    private static h e(List<Animator> list) {
        h hVar = new h();
        int size = list.size();
        for (int i8 = 0; i8 < size; i8++) {
            a(hVar, list.get(i8));
        }
        return hVar;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof h) {
            return this.f21799a.equals(((h) obj).f21799a);
        }
        return false;
    }

    public <T> ObjectAnimator f(String str, T t8, Property<T, ?> property) {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(t8, g(str));
        ofPropertyValuesHolder.setProperty(property);
        h(str).a(ofPropertyValuesHolder);
        return ofPropertyValuesHolder;
    }

    public PropertyValuesHolder[] g(String str) {
        if (j(str)) {
            return b(this.f21800b.get(str));
        }
        throw new IllegalArgumentException();
    }

    public i h(String str) {
        if (k(str)) {
            return this.f21799a.get(str);
        }
        throw new IllegalArgumentException();
    }

    public int hashCode() {
        return this.f21799a.hashCode();
    }

    public long i() {
        int size = this.f21799a.size();
        long j8 = 0;
        for (int i8 = 0; i8 < size; i8++) {
            i o5 = this.f21799a.o(i8);
            j8 = Math.max(j8, o5.c() + o5.d());
        }
        return j8;
    }

    public boolean j(String str) {
        return this.f21800b.get(str) != null;
    }

    public boolean k(String str) {
        return this.f21799a.get(str) != null;
    }

    public void l(String str, PropertyValuesHolder[] propertyValuesHolderArr) {
        this.f21800b.put(str, propertyValuesHolderArr);
    }

    public void m(String str, i iVar) {
        this.f21799a.put(str, iVar);
    }

    public String toString() {
        return '\n' + getClass().getName() + '{' + Integer.toHexString(System.identityHashCode(this)) + " timings: " + this.f21799a + "}\n";
    }
}
