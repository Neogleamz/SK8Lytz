package com.google.android.material.navigation;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.view.menu.i;
import androidx.appcompat.view.menu.m;
import androidx.appcompat.view.menu.r;
import com.google.android.material.internal.ParcelableSparseArray;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class NavigationBarPresenter implements m {

    /* renamed from: a  reason: collision with root package name */
    private g f18170a;

    /* renamed from: b  reason: collision with root package name */
    private c f18171b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f18172c = false;

    /* renamed from: d  reason: collision with root package name */
    private int f18173d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        int f18174a;

        /* renamed from: b  reason: collision with root package name */
        ParcelableSparseArray f18175b;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.Creator<SavedState> {
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

        SavedState() {
        }

        SavedState(Parcel parcel) {
            this.f18174a = parcel.readInt();
            this.f18175b = (ParcelableSparseArray) parcel.readParcelable(getClass().getClassLoader());
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeInt(this.f18174a);
            parcel.writeParcelable(this.f18175b, 0);
        }
    }

    public void a(int i8) {
        this.f18173d = i8;
    }

    public void b(c cVar) {
        this.f18171b = cVar;
    }

    @Override // androidx.appcompat.view.menu.m
    public void c(g gVar, boolean z4) {
    }

    public void d(boolean z4) {
        this.f18172c = z4;
    }

    @Override // androidx.appcompat.view.menu.m
    public int e() {
        return this.f18173d;
    }

    @Override // androidx.appcompat.view.menu.m
    public void f(boolean z4) {
        if (this.f18172c) {
            return;
        }
        if (z4) {
            this.f18171b.d();
        } else {
            this.f18171b.k();
        }
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean g() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean h(g gVar, i iVar) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean i(g gVar, i iVar) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public void k(Context context, g gVar) {
        this.f18170a = gVar;
        this.f18171b.b(gVar);
    }

    @Override // androidx.appcompat.view.menu.m
    public void l(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            SavedState savedState = (SavedState) parcelable;
            this.f18171b.j(savedState.f18174a);
            this.f18171b.setBadgeDrawables(com.google.android.material.badge.a.b(this.f18171b.getContext(), savedState.f18175b));
        }
    }

    @Override // androidx.appcompat.view.menu.m
    public boolean n(r rVar) {
        return false;
    }

    @Override // androidx.appcompat.view.menu.m
    public Parcelable o() {
        SavedState savedState = new SavedState();
        savedState.f18174a = this.f18171b.getSelectedItemId();
        savedState.f18175b = com.google.android.material.badge.a.c(this.f18171b.getBadgeDrawables());
        return savedState;
    }
}
