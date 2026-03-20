package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"BanParcelableUsage"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class FragmentManagerState implements Parcelable {
    public static final Parcelable.Creator<FragmentManagerState> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    ArrayList<FragmentState> f5512a;

    /* renamed from: b  reason: collision with root package name */
    ArrayList<String> f5513b;

    /* renamed from: c  reason: collision with root package name */
    BackStackState[] f5514c;

    /* renamed from: d  reason: collision with root package name */
    int f5515d;

    /* renamed from: e  reason: collision with root package name */
    String f5516e;

    /* renamed from: f  reason: collision with root package name */
    ArrayList<String> f5517f;

    /* renamed from: g  reason: collision with root package name */
    ArrayList<Bundle> f5518g;

    /* renamed from: h  reason: collision with root package name */
    ArrayList<FragmentManager.LaunchedFragmentInfo> f5519h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<FragmentManagerState> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public FragmentManagerState createFromParcel(Parcel parcel) {
            return new FragmentManagerState(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public FragmentManagerState[] newArray(int i8) {
            return new FragmentManagerState[i8];
        }
    }

    public FragmentManagerState() {
        this.f5516e = null;
        this.f5517f = new ArrayList<>();
        this.f5518g = new ArrayList<>();
    }

    public FragmentManagerState(Parcel parcel) {
        this.f5516e = null;
        this.f5517f = new ArrayList<>();
        this.f5518g = new ArrayList<>();
        this.f5512a = parcel.createTypedArrayList(FragmentState.CREATOR);
        this.f5513b = parcel.createStringArrayList();
        this.f5514c = (BackStackState[]) parcel.createTypedArray(BackStackState.CREATOR);
        this.f5515d = parcel.readInt();
        this.f5516e = parcel.readString();
        this.f5517f = parcel.createStringArrayList();
        this.f5518g = parcel.createTypedArrayList(Bundle.CREATOR);
        this.f5519h = parcel.createTypedArrayList(FragmentManager.LaunchedFragmentInfo.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeTypedList(this.f5512a);
        parcel.writeStringList(this.f5513b);
        parcel.writeTypedArray(this.f5514c, i8);
        parcel.writeInt(this.f5515d);
        parcel.writeString(this.f5516e);
        parcel.writeStringList(this.f5517f);
        parcel.writeTypedList(this.f5518g);
        parcel.writeTypedList(this.f5519h);
    }
}
