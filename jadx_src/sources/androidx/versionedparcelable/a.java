package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseIntArray;
import com.daimajia.numberprogressbar.BuildConfig;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class a extends VersionedParcel {

    /* renamed from: d  reason: collision with root package name */
    private final SparseIntArray f7739d;

    /* renamed from: e  reason: collision with root package name */
    private final Parcel f7740e;

    /* renamed from: f  reason: collision with root package name */
    private final int f7741f;

    /* renamed from: g  reason: collision with root package name */
    private final int f7742g;

    /* renamed from: h  reason: collision with root package name */
    private final String f7743h;

    /* renamed from: i  reason: collision with root package name */
    private int f7744i;

    /* renamed from: j  reason: collision with root package name */
    private int f7745j;

    /* renamed from: k  reason: collision with root package name */
    private int f7746k;

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(Parcel parcel) {
        this(parcel, parcel.dataPosition(), parcel.dataSize(), BuildConfig.FLAVOR, new k0.a(), new k0.a(), new k0.a());
    }

    private a(Parcel parcel, int i8, int i9, String str, k0.a<String, Method> aVar, k0.a<String, Method> aVar2, k0.a<String, Class> aVar3) {
        super(aVar, aVar2, aVar3);
        this.f7739d = new SparseIntArray();
        this.f7744i = -1;
        this.f7745j = 0;
        this.f7746k = -1;
        this.f7740e = parcel;
        this.f7741f = i8;
        this.f7742g = i9;
        this.f7745j = i8;
        this.f7743h = str;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public String D() {
        return this.f7740e.readString();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public IBinder F() {
        return this.f7740e.readStrongBinder();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void J(int i8) {
        a();
        this.f7744i = i8;
        this.f7739d.put(i8, this.f7740e.dataPosition());
        X(0);
        X(i8);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void L(boolean z4) {
        this.f7740e.writeInt(z4 ? 1 : 0);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void N(Bundle bundle) {
        this.f7740e.writeBundle(bundle);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void P(byte[] bArr) {
        if (bArr == null) {
            this.f7740e.writeInt(-1);
            return;
        }
        this.f7740e.writeInt(bArr.length);
        this.f7740e.writeByteArray(bArr);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected void R(CharSequence charSequence) {
        TextUtils.writeToParcel(charSequence, this.f7740e, 0);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void V(float f5) {
        this.f7740e.writeFloat(f5);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void X(int i8) {
        this.f7740e.writeInt(i8);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void a() {
        int i8 = this.f7744i;
        if (i8 >= 0) {
            int i9 = this.f7739d.get(i8);
            int dataPosition = this.f7740e.dataPosition();
            this.f7740e.setDataPosition(i9);
            this.f7740e.writeInt(dataPosition - i9);
            this.f7740e.setDataPosition(dataPosition);
        }
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void a0(long j8) {
        this.f7740e.writeLong(j8);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected VersionedParcel b() {
        Parcel parcel = this.f7740e;
        int dataPosition = parcel.dataPosition();
        int i8 = this.f7745j;
        if (i8 == this.f7741f) {
            i8 = this.f7742g;
        }
        int i9 = i8;
        return new a(parcel, dataPosition, i9, this.f7743h + "  ", this.f7735a, this.f7736b, this.f7737c);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void c0(Parcelable parcelable) {
        this.f7740e.writeParcelable(parcelable, 0);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void g0(String str) {
        this.f7740e.writeString(str);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public boolean h() {
        return this.f7740e.readInt() != 0;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public void i0(IBinder iBinder) {
        this.f7740e.writeStrongBinder(iBinder);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public Bundle j() {
        return this.f7740e.readBundle(getClass().getClassLoader());
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public byte[] l() {
        int readInt = this.f7740e.readInt();
        if (readInt < 0) {
            return null;
        }
        byte[] bArr = new byte[readInt];
        this.f7740e.readByteArray(bArr);
        return bArr;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    protected CharSequence n() {
        return (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this.f7740e);
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public boolean q(int i8) {
        while (this.f7745j < this.f7742g) {
            int i9 = this.f7746k;
            if (i9 == i8) {
                return true;
            }
            if (String.valueOf(i9).compareTo(String.valueOf(i8)) > 0) {
                return false;
            }
            this.f7740e.setDataPosition(this.f7745j);
            int readInt = this.f7740e.readInt();
            this.f7746k = this.f7740e.readInt();
            this.f7745j += readInt;
        }
        return this.f7746k == i8;
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public float r() {
        return this.f7740e.readFloat();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public int u() {
        return this.f7740e.readInt();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public long x() {
        return this.f7740e.readLong();
    }

    @Override // androidx.versionedparcelable.VersionedParcel
    public <T extends Parcelable> T z() {
        return (T) this.f7740e.readParcelable(getClass().getClassLoader());
    }
}
