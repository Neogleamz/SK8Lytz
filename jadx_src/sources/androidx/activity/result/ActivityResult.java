package androidx.activity.result;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
@SuppressLint({"BanParcelableUsage"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ActivityResult implements Parcelable {
    public static final Parcelable.Creator<ActivityResult> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    private final int f413a;

    /* renamed from: b  reason: collision with root package name */
    private final Intent f414b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<ActivityResult> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public ActivityResult createFromParcel(Parcel parcel) {
            return new ActivityResult(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public ActivityResult[] newArray(int i8) {
            return new ActivityResult[i8];
        }
    }

    public ActivityResult(int i8, Intent intent) {
        this.f413a = i8;
        this.f414b = intent;
    }

    ActivityResult(Parcel parcel) {
        this.f413a = parcel.readInt();
        this.f414b = parcel.readInt() == 0 ? null : (Intent) Intent.CREATOR.createFromParcel(parcel);
    }

    public static String c(int i8) {
        return i8 != -1 ? i8 != 0 ? String.valueOf(i8) : "RESULT_CANCELED" : "RESULT_OK";
    }

    public Intent a() {
        return this.f414b;
    }

    public int b() {
        return this.f413a;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "ActivityResult{resultCode=" + c(this.f413a) + ", data=" + this.f414b + '}';
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeInt(this.f413a);
        parcel.writeInt(this.f414b == null ? 0 : 1);
        Intent intent = this.f414b;
        if (intent != null) {
            intent.writeToParcel(parcel, i8);
        }
    }
}
