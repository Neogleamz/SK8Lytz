package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TabHost;
import java.util.ArrayList;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class FragmentTabHost extends TabHost implements TabHost.OnTabChangeListener {

    /* renamed from: a  reason: collision with root package name */
    private final ArrayList<a> f5533a;

    /* renamed from: b  reason: collision with root package name */
    private Context f5534b;

    /* renamed from: c  reason: collision with root package name */
    private FragmentManager f5535c;

    /* renamed from: d  reason: collision with root package name */
    private int f5536d;

    /* renamed from: e  reason: collision with root package name */
    private TabHost.OnTabChangeListener f5537e;

    /* renamed from: f  reason: collision with root package name */
    private a f5538f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f5539g;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        String f5540a;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        SavedState(Parcel parcel) {
            super(parcel);
            this.f5540a = parcel.readString();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            return "FragmentTabHost.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " curTab=" + this.f5540a + "}";
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeString(this.f5540a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        final String f5541a;

        /* renamed from: b  reason: collision with root package name */
        final Class<?> f5542b;

        /* renamed from: c  reason: collision with root package name */
        final Bundle f5543c;

        /* renamed from: d  reason: collision with root package name */
        Fragment f5544d;
    }

    @Deprecated
    public FragmentTabHost(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f5533a = new ArrayList<>();
        c(context, attributeSet);
    }

    private r a(String str, r rVar) {
        Fragment fragment;
        a b9 = b(str);
        if (this.f5538f != b9) {
            if (rVar == null) {
                rVar = this.f5535c.l();
            }
            a aVar = this.f5538f;
            if (aVar != null && (fragment = aVar.f5544d) != null) {
                rVar.m(fragment);
            }
            if (b9 != null) {
                Fragment fragment2 = b9.f5544d;
                if (fragment2 == null) {
                    Fragment a9 = this.f5535c.q0().a(this.f5534b.getClassLoader(), b9.f5542b.getName());
                    b9.f5544d = a9;
                    a9.t1(b9.f5543c);
                    rVar.b(this.f5536d, b9.f5544d, b9.f5541a);
                } else {
                    rVar.h(fragment2);
                }
            }
            this.f5538f = b9;
        }
        return rVar;
    }

    private a b(String str) {
        int size = this.f5533a.size();
        for (int i8 = 0; i8 < size; i8++) {
            a aVar = this.f5533a.get(i8);
            if (aVar.f5541a.equals(str)) {
                return aVar;
            }
        }
        return null;
    }

    private void c(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, new int[]{16842995}, 0, 0);
        this.f5536d = obtainStyledAttributes.getResourceId(0, 0);
        obtainStyledAttributes.recycle();
        super.setOnTabChangedListener(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    @Deprecated
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        String currentTabTag = getCurrentTabTag();
        int size = this.f5533a.size();
        r rVar = null;
        for (int i8 = 0; i8 < size; i8++) {
            a aVar = this.f5533a.get(i8);
            Fragment i02 = this.f5535c.i0(aVar.f5541a);
            aVar.f5544d = i02;
            if (i02 != null && !i02.a0()) {
                if (aVar.f5541a.equals(currentTabTag)) {
                    this.f5538f = aVar;
                } else {
                    if (rVar == null) {
                        rVar = this.f5535c.l();
                    }
                    rVar.m(aVar.f5544d);
                }
            }
        }
        this.f5539g = true;
        r a9 = a(currentTabTag, rVar);
        if (a9 != null) {
            a9.i();
            this.f5535c.e0();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    @Deprecated
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.f5539g = false;
    }

    @Override // android.view.View
    @Deprecated
    protected void onRestoreInstanceState(@SuppressLint({"UnknownNullness"}) Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setCurrentTabByTag(savedState.f5540a);
    }

    @Override // android.view.View
    @Deprecated
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f5540a = getCurrentTabTag();
        return savedState;
    }

    @Override // android.widget.TabHost.OnTabChangeListener
    @Deprecated
    public void onTabChanged(String str) {
        r a9;
        if (this.f5539g && (a9 = a(str, null)) != null) {
            a9.i();
        }
        TabHost.OnTabChangeListener onTabChangeListener = this.f5537e;
        if (onTabChangeListener != null) {
            onTabChangeListener.onTabChanged(str);
        }
    }

    @Override // android.widget.TabHost
    @Deprecated
    public void setOnTabChangedListener(TabHost.OnTabChangeListener onTabChangeListener) {
        this.f5537e = onTabChangeListener;
    }

    @Override // android.widget.TabHost
    @Deprecated
    public void setup() {
        throw new IllegalStateException("Must call setup() that takes a Context and FragmentManager");
    }
}
