package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"BanParcelableUsage"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class FragmentState implements Parcelable {
    public static final Parcelable.Creator<FragmentState> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    final String f5520a;

    /* renamed from: b  reason: collision with root package name */
    final String f5521b;

    /* renamed from: c  reason: collision with root package name */
    final boolean f5522c;

    /* renamed from: d  reason: collision with root package name */
    final int f5523d;

    /* renamed from: e  reason: collision with root package name */
    final int f5524e;

    /* renamed from: f  reason: collision with root package name */
    final String f5525f;

    /* renamed from: g  reason: collision with root package name */
    final boolean f5526g;

    /* renamed from: h  reason: collision with root package name */
    final boolean f5527h;

    /* renamed from: j  reason: collision with root package name */
    final boolean f5528j;

    /* renamed from: k  reason: collision with root package name */
    final Bundle f5529k;

    /* renamed from: l  reason: collision with root package name */
    final boolean f5530l;

    /* renamed from: m  reason: collision with root package name */
    final int f5531m;

    /* renamed from: n  reason: collision with root package name */
    Bundle f5532n;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<FragmentState> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public FragmentState createFromParcel(Parcel parcel) {
            return new FragmentState(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public FragmentState[] newArray(int i8) {
            return new FragmentState[i8];
        }
    }

    FragmentState(Parcel parcel) {
        this.f5520a = parcel.readString();
        this.f5521b = parcel.readString();
        this.f5522c = parcel.readInt() != 0;
        this.f5523d = parcel.readInt();
        this.f5524e = parcel.readInt();
        this.f5525f = parcel.readString();
        this.f5526g = parcel.readInt() != 0;
        this.f5527h = parcel.readInt() != 0;
        this.f5528j = parcel.readInt() != 0;
        this.f5529k = parcel.readBundle();
        this.f5530l = parcel.readInt() != 0;
        this.f5532n = parcel.readBundle();
        this.f5531m = parcel.readInt();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentState(Fragment fragment) {
        this.f5520a = fragment.getClass().getName();
        this.f5521b = fragment.f5399f;
        this.f5522c = fragment.f5416p;
        this.f5523d = fragment.C;
        this.f5524e = fragment.E;
        this.f5525f = fragment.F;
        this.f5526g = fragment.K;
        this.f5527h = fragment.f5414n;
        this.f5528j = fragment.H;
        this.f5529k = fragment.f5401g;
        this.f5530l = fragment.G;
        this.f5531m = fragment.f5400f0.ordinal();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder((int) RecognitionOptions.ITF);
        sb.append("FragmentState{");
        sb.append(this.f5520a);
        sb.append(" (");
        sb.append(this.f5521b);
        sb.append(")}:");
        if (this.f5522c) {
            sb.append(" fromLayout");
        }
        if (this.f5524e != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.f5524e));
        }
        String str = this.f5525f;
        if (str != null && !str.isEmpty()) {
            sb.append(" tag=");
            sb.append(this.f5525f);
        }
        if (this.f5526g) {
            sb.append(" retainInstance");
        }
        if (this.f5527h) {
            sb.append(" removing");
        }
        if (this.f5528j) {
            sb.append(" detached");
        }
        if (this.f5530l) {
            sb.append(" hidden");
        }
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f5520a);
        parcel.writeString(this.f5521b);
        parcel.writeInt(this.f5522c ? 1 : 0);
        parcel.writeInt(this.f5523d);
        parcel.writeInt(this.f5524e);
        parcel.writeString(this.f5525f);
        parcel.writeInt(this.f5526g ? 1 : 0);
        parcel.writeInt(this.f5527h ? 1 : 0);
        parcel.writeInt(this.f5528j ? 1 : 0);
        parcel.writeBundle(this.f5529k);
        parcel.writeInt(this.f5530l ? 1 : 0);
        parcel.writeBundle(this.f5532n);
        parcel.writeInt(this.f5531m);
    }
}
