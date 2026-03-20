package androidx.activity.result;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
@SuppressLint({"BanParcelableUsage"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class IntentSenderRequest implements Parcelable {

    /* renamed from: a  reason: collision with root package name */
    private final IntentSender f438a;

    /* renamed from: b  reason: collision with root package name */
    private final Intent f439b;

    /* renamed from: c  reason: collision with root package name */
    private final int f440c;

    /* renamed from: d  reason: collision with root package name */
    private final int f441d;

    /* renamed from: e  reason: collision with root package name */
    public static final c f437e = new c(null);
    public static final Parcelable.Creator<IntentSenderRequest> CREATOR = new b();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final IntentSender f442a;

        /* renamed from: b  reason: collision with root package name */
        private Intent f443b;

        /* renamed from: c  reason: collision with root package name */
        private int f444c;

        /* renamed from: d  reason: collision with root package name */
        private int f445d;

        public a(IntentSender intentSender) {
            p.e(intentSender, "intentSender");
            this.f442a = intentSender;
        }

        public final IntentSenderRequest a() {
            return new IntentSenderRequest(this.f442a, this.f443b, this.f444c, this.f445d);
        }

        public final a b(Intent intent) {
            this.f443b = intent;
            return this;
        }

        public final a c(int i8, int i9) {
            this.f445d = i8;
            this.f444c = i9;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements Parcelable.Creator<IntentSenderRequest> {
        b() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public IntentSenderRequest createFromParcel(Parcel parcel) {
            p.e(parcel, "inParcel");
            return new IntentSenderRequest(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public IntentSenderRequest[] newArray(int i8) {
            return new IntentSenderRequest[i8];
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class c {
        private c() {
        }

        public /* synthetic */ c(i iVar) {
            this();
        }
    }

    public IntentSenderRequest(IntentSender intentSender, Intent intent, int i8, int i9) {
        p.e(intentSender, "intentSender");
        this.f438a = intentSender;
        this.f439b = intent;
        this.f440c = i8;
        this.f441d = i9;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public IntentSenderRequest(android.os.Parcel r4) {
        /*
            r3 = this;
            java.lang.String r0 = "parcel"
            kotlin.jvm.internal.p.e(r4, r0)
            java.lang.Class<android.content.IntentSender> r0 = android.content.IntentSender.class
            java.lang.ClassLoader r0 = r0.getClassLoader()
            android.os.Parcelable r0 = r4.readParcelable(r0)
            kotlin.jvm.internal.p.b(r0)
            android.content.IntentSender r0 = (android.content.IntentSender) r0
            java.lang.Class<android.content.Intent> r1 = android.content.Intent.class
            java.lang.ClassLoader r1 = r1.getClassLoader()
            android.os.Parcelable r1 = r4.readParcelable(r1)
            android.content.Intent r1 = (android.content.Intent) r1
            int r2 = r4.readInt()
            int r4 = r4.readInt()
            r3.<init>(r0, r1, r2, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.activity.result.IntentSenderRequest.<init>(android.os.Parcel):void");
    }

    public final Intent a() {
        return this.f439b;
    }

    public final int b() {
        return this.f440c;
    }

    public final int c() {
        return this.f441d;
    }

    public final IntentSender d() {
        return this.f438a;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        p.e(parcel, "dest");
        parcel.writeParcelable(this.f438a, i8);
        parcel.writeParcelable(this.f439b, i8);
        parcel.writeInt(this.f440c);
        parcel.writeInt(this.f441d);
    }
}
