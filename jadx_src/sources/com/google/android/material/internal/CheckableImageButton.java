package com.google.android.material.internal;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Checkable;
import android.widget.ImageButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.view.c0;
import androidx.customview.view.AbsSavedState;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class CheckableImageButton extends AppCompatImageButton implements Checkable {

    /* renamed from: g  reason: collision with root package name */
    private static final int[] f18025g = {16842912};

    /* renamed from: d  reason: collision with root package name */
    private boolean f18026d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f18027e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f18028f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: c  reason: collision with root package name */
        boolean f18029c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            b(parcel);
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private void b(Parcel parcel) {
            this.f18029c = parcel.readInt() == 1;
        }

        @Override // androidx.customview.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            super.writeToParcel(parcel, i8);
            parcel.writeInt(this.f18029c ? 1 : 0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends androidx.core.view.a {
        a() {
        }

        @Override // androidx.core.view.a
        public void f(View view, AccessibilityEvent accessibilityEvent) {
            super.f(view, accessibilityEvent);
            accessibilityEvent.setChecked(CheckableImageButton.this.isChecked());
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            super.g(view, cVar);
            cVar.a0(CheckableImageButton.this.a());
            cVar.b0(CheckableImageButton.this.isChecked());
        }
    }

    public CheckableImageButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.F);
    }

    public CheckableImageButton(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f18027e = true;
        this.f18028f = true;
        c0.t0(this, new a());
    }

    public boolean a() {
        return this.f18027e;
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        return this.f18026d;
    }

    @Override // android.widget.ImageView, android.view.View
    public int[] onCreateDrawableState(int i8) {
        if (this.f18026d) {
            int[] iArr = f18025g;
            return ImageButton.mergeDrawableStates(super.onCreateDrawableState(i8 + iArr.length), iArr);
        }
        return super.onCreateDrawableState(i8);
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.a());
        setChecked(savedState.f18029c);
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.f18029c = this.f18026d;
        return savedState;
    }

    public void setCheckable(boolean z4) {
        if (this.f18027e != z4) {
            this.f18027e = z4;
            sendAccessibilityEvent(0);
        }
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z4) {
        if (!this.f18027e || this.f18026d == z4) {
            return;
        }
        this.f18026d = z4;
        refreshDrawableState();
        sendAccessibilityEvent(RecognitionOptions.PDF417);
    }

    public void setPressable(boolean z4) {
        this.f18028f = z4;
    }

    @Override // android.view.View
    public void setPressed(boolean z4) {
        if (this.f18028f) {
            super.setPressed(z4);
        }
    }

    @Override // android.widget.Checkable
    public void toggle() {
        setChecked(!this.f18026d);
    }
}
