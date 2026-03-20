package androidx.navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.UUID;
/* JADX INFO: Access modifiers changed from: package-private */
@SuppressLint({"BanParcelableUsage"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class NavBackStackEntryState implements Parcelable {
    public static final Parcelable.Creator<NavBackStackEntryState> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    private final UUID f6277a;

    /* renamed from: b  reason: collision with root package name */
    private final int f6278b;

    /* renamed from: c  reason: collision with root package name */
    private final Bundle f6279c;

    /* renamed from: d  reason: collision with root package name */
    private final Bundle f6280d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<NavBackStackEntryState> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public NavBackStackEntryState createFromParcel(Parcel parcel) {
            return new NavBackStackEntryState(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public NavBackStackEntryState[] newArray(int i8) {
            return new NavBackStackEntryState[i8];
        }
    }

    NavBackStackEntryState(Parcel parcel) {
        this.f6277a = UUID.fromString(parcel.readString());
        this.f6278b = parcel.readInt();
        this.f6279c = parcel.readBundle(NavBackStackEntryState.class.getClassLoader());
        this.f6280d = parcel.readBundle(NavBackStackEntryState.class.getClassLoader());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NavBackStackEntryState(e eVar) {
        this.f6277a = eVar.f6320f;
        this.f6278b = eVar.b().q();
        this.f6279c = eVar.a();
        Bundle bundle = new Bundle();
        this.f6280d = bundle;
        eVar.g(bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle a() {
        return this.f6279c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return this.f6278b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bundle c() {
        return this.f6280d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public UUID d() {
        return this.f6277a;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f6277a.toString());
        parcel.writeInt(this.f6278b);
        parcel.writeBundle(this.f6279c);
        parcel.writeBundle(this.f6280d);
    }
}
