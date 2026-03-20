package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.r;
import androidx.lifecycle.Lifecycle;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"BanParcelableUsage"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class BackStackState implements Parcelable {
    public static final Parcelable.Creator<BackStackState> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    final int[] f5374a;

    /* renamed from: b  reason: collision with root package name */
    final ArrayList<String> f5375b;

    /* renamed from: c  reason: collision with root package name */
    final int[] f5376c;

    /* renamed from: d  reason: collision with root package name */
    final int[] f5377d;

    /* renamed from: e  reason: collision with root package name */
    final int f5378e;

    /* renamed from: f  reason: collision with root package name */
    final String f5379f;

    /* renamed from: g  reason: collision with root package name */
    final int f5380g;

    /* renamed from: h  reason: collision with root package name */
    final int f5381h;

    /* renamed from: j  reason: collision with root package name */
    final CharSequence f5382j;

    /* renamed from: k  reason: collision with root package name */
    final int f5383k;

    /* renamed from: l  reason: collision with root package name */
    final CharSequence f5384l;

    /* renamed from: m  reason: collision with root package name */
    final ArrayList<String> f5385m;

    /* renamed from: n  reason: collision with root package name */
    final ArrayList<String> f5386n;

    /* renamed from: p  reason: collision with root package name */
    final boolean f5387p;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<BackStackState> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public BackStackState createFromParcel(Parcel parcel) {
            return new BackStackState(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public BackStackState[] newArray(int i8) {
            return new BackStackState[i8];
        }
    }

    public BackStackState(Parcel parcel) {
        this.f5374a = parcel.createIntArray();
        this.f5375b = parcel.createStringArrayList();
        this.f5376c = parcel.createIntArray();
        this.f5377d = parcel.createIntArray();
        this.f5378e = parcel.readInt();
        this.f5379f = parcel.readString();
        this.f5380g = parcel.readInt();
        this.f5381h = parcel.readInt();
        this.f5382j = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.f5383k = parcel.readInt();
        this.f5384l = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.f5385m = parcel.createStringArrayList();
        this.f5386n = parcel.createStringArrayList();
        this.f5387p = parcel.readInt() != 0;
    }

    public BackStackState(androidx.fragment.app.a aVar) {
        int size = aVar.f5665c.size();
        this.f5374a = new int[size * 5];
        if (!aVar.f5671i) {
            throw new IllegalStateException("Not on back stack");
        }
        this.f5375b = new ArrayList<>(size);
        this.f5376c = new int[size];
        this.f5377d = new int[size];
        int i8 = 0;
        int i9 = 0;
        while (i8 < size) {
            r.a aVar2 = aVar.f5665c.get(i8);
            int i10 = i9 + 1;
            this.f5374a[i9] = aVar2.f5681a;
            ArrayList<String> arrayList = this.f5375b;
            Fragment fragment = aVar2.f5682b;
            arrayList.add(fragment != null ? fragment.f5399f : null);
            int[] iArr = this.f5374a;
            int i11 = i10 + 1;
            iArr[i10] = aVar2.f5683c;
            int i12 = i11 + 1;
            iArr[i11] = aVar2.f5684d;
            int i13 = i12 + 1;
            iArr[i12] = aVar2.f5685e;
            iArr[i13] = aVar2.f5686f;
            this.f5376c[i8] = aVar2.f5687g.ordinal();
            this.f5377d[i8] = aVar2.f5688h.ordinal();
            i8++;
            i9 = i13 + 1;
        }
        this.f5378e = aVar.f5670h;
        this.f5379f = aVar.f5673k;
        this.f5380g = aVar.f5547v;
        this.f5381h = aVar.f5674l;
        this.f5382j = aVar.f5675m;
        this.f5383k = aVar.f5676n;
        this.f5384l = aVar.f5677o;
        this.f5385m = aVar.f5678p;
        this.f5386n = aVar.q;
        this.f5387p = aVar.f5679r;
    }

    public androidx.fragment.app.a a(FragmentManager fragmentManager) {
        androidx.fragment.app.a aVar = new androidx.fragment.app.a(fragmentManager);
        int i8 = 0;
        int i9 = 0;
        while (i8 < this.f5374a.length) {
            r.a aVar2 = new r.a();
            int i10 = i8 + 1;
            aVar2.f5681a = this.f5374a[i8];
            if (FragmentManager.F0(2)) {
                Log.v("FragmentManager", "Instantiate " + aVar + " op #" + i9 + " base fragment #" + this.f5374a[i10]);
            }
            String str = this.f5375b.get(i9);
            aVar2.f5682b = str != null ? fragmentManager.g0(str) : null;
            aVar2.f5687g = Lifecycle.State.values()[this.f5376c[i9]];
            aVar2.f5688h = Lifecycle.State.values()[this.f5377d[i9]];
            int[] iArr = this.f5374a;
            int i11 = i10 + 1;
            int i12 = iArr[i10];
            aVar2.f5683c = i12;
            int i13 = i11 + 1;
            int i14 = iArr[i11];
            aVar2.f5684d = i14;
            int i15 = i13 + 1;
            int i16 = iArr[i13];
            aVar2.f5685e = i16;
            int i17 = iArr[i15];
            aVar2.f5686f = i17;
            aVar.f5666d = i12;
            aVar.f5667e = i14;
            aVar.f5668f = i16;
            aVar.f5669g = i17;
            aVar.e(aVar2);
            i9++;
            i8 = i15 + 1;
        }
        aVar.f5670h = this.f5378e;
        aVar.f5673k = this.f5379f;
        aVar.f5547v = this.f5380g;
        aVar.f5671i = true;
        aVar.f5674l = this.f5381h;
        aVar.f5675m = this.f5382j;
        aVar.f5676n = this.f5383k;
        aVar.f5677o = this.f5384l;
        aVar.f5678p = this.f5385m;
        aVar.q = this.f5386n;
        aVar.f5679r = this.f5387p;
        aVar.w(1);
        return aVar;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeIntArray(this.f5374a);
        parcel.writeStringList(this.f5375b);
        parcel.writeIntArray(this.f5376c);
        parcel.writeIntArray(this.f5377d);
        parcel.writeInt(this.f5378e);
        parcel.writeString(this.f5379f);
        parcel.writeInt(this.f5380g);
        parcel.writeInt(this.f5381h);
        TextUtils.writeToParcel(this.f5382j, parcel, 0);
        parcel.writeInt(this.f5383k);
        TextUtils.writeToParcel(this.f5384l, parcel, 0);
        parcel.writeStringList(this.f5385m);
        parcel.writeStringList(this.f5386n);
        parcel.writeInt(this.f5387p ? 1 : 0);
    }
}
