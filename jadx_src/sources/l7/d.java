package l7;

import android.util.Property;
import android.view.ViewGroup;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d extends Property<ViewGroup, Float> {

    /* renamed from: a  reason: collision with root package name */
    public static final Property<ViewGroup, Float> f21792a = new d("childrenAlpha");

    private d(String str) {
        super(Float.class, str);
    }

    @Override // android.util.Property
    /* renamed from: a */
    public Float get(ViewGroup viewGroup) {
        Float f5 = (Float) viewGroup.getTag(k7.f.E);
        return f5 != null ? f5 : Float.valueOf(1.0f);
    }

    @Override // android.util.Property
    /* renamed from: b */
    public void set(ViewGroup viewGroup, Float f5) {
        float floatValue = f5.floatValue();
        viewGroup.setTag(k7.f.E, Float.valueOf(floatValue));
        int childCount = viewGroup.getChildCount();
        for (int i8 = 0; i8 < childCount; i8++) {
            viewGroup.getChildAt(i8).setAlpha(floatValue);
        }
    }
}
