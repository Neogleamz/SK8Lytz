package android.support.v4.media.session;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
@SuppressLint({"BanParcelableUsage"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ParcelableVolumeInfo implements Parcelable {
    public static final Parcelable.Creator<ParcelableVolumeInfo> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public int f316a;

    /* renamed from: b  reason: collision with root package name */
    public int f317b;

    /* renamed from: c  reason: collision with root package name */
    public int f318c;

    /* renamed from: d  reason: collision with root package name */
    public int f319d;

    /* renamed from: e  reason: collision with root package name */
    public int f320e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<ParcelableVolumeInfo> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public ParcelableVolumeInfo createFromParcel(Parcel parcel) {
            return new ParcelableVolumeInfo(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public ParcelableVolumeInfo[] newArray(int i8) {
            return new ParcelableVolumeInfo[i8];
        }
    }

    public ParcelableVolumeInfo(Parcel parcel) {
        this.f316a = parcel.readInt();
        this.f318c = parcel.readInt();
        this.f319d = parcel.readInt();
        this.f320e = parcel.readInt();
        this.f317b = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeInt(this.f316a);
        parcel.writeInt(this.f318c);
        parcel.writeInt(this.f319d);
        parcel.writeInt(this.f320e);
        parcel.writeInt(this.f317b);
    }
}
