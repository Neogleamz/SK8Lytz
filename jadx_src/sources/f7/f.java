package f7;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.internal.measurement.x0;
import com.google.android.gms.internal.measurement.y0;
import com.google.android.gms.measurement.internal.zzac;
import com.google.android.gms.measurement.internal.zzal;
import com.google.android.gms.measurement.internal.zzbf;
import com.google.android.gms.measurement.internal.zzn;
import com.google.android.gms.measurement.internal.zzno;
import java.util.Collection;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class f extends y0 implements d {
    public f() {
        super("com.google.android.gms.measurement.internal.IMeasurementService");
    }

    @Override // com.google.android.gms.internal.measurement.y0
    protected final boolean d(int i8, Parcel parcel, Parcel parcel2, int i9) {
        Collection K0;
        switch (i8) {
            case 1:
                x0.f(parcel);
                f1((zzbf) x0.a(parcel, zzbf.CREATOR), (zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                return true;
            case 2:
                x0.f(parcel);
                z0((zzno) x0.a(parcel, zzno.CREATOR), (zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                return true;
            case 3:
            case 8:
            case 22:
            case 23:
            default:
                return false;
            case 4:
                x0.f(parcel);
                Z((zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                return true;
            case 5:
                String readString = parcel.readString();
                String readString2 = parcel.readString();
                x0.f(parcel);
                a1((zzbf) x0.a(parcel, zzbf.CREATOR), readString, readString2);
                parcel2.writeNoException();
                return true;
            case 6:
                x0.f(parcel);
                w((zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                return true;
            case 7:
                boolean h8 = x0.h(parcel);
                x0.f(parcel);
                K0 = K0((zzn) x0.a(parcel, zzn.CREATOR), h8);
                parcel2.writeNoException();
                parcel2.writeTypedList(K0);
                return true;
            case 9:
                String readString3 = parcel.readString();
                x0.f(parcel);
                byte[] K1 = K1((zzbf) x0.a(parcel, zzbf.CREATOR), readString3);
                parcel2.writeNoException();
                parcel2.writeByteArray(K1);
                return true;
            case 10:
                long readLong = parcel.readLong();
                String readString4 = parcel.readString();
                String readString5 = parcel.readString();
                String readString6 = parcel.readString();
                x0.f(parcel);
                m0(readLong, readString4, readString5, readString6);
                parcel2.writeNoException();
                return true;
            case 11:
                x0.f(parcel);
                String n12 = n1((zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                parcel2.writeString(n12);
                return true;
            case 12:
                x0.f(parcel);
                B((zzac) x0.a(parcel, zzac.CREATOR), (zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                return true;
            case 13:
                x0.f(parcel);
                u1((zzac) x0.a(parcel, zzac.CREATOR));
                parcel2.writeNoException();
                return true;
            case 14:
                x0.f(parcel);
                K0 = I0(parcel.readString(), parcel.readString(), x0.h(parcel), (zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                parcel2.writeTypedList(K0);
                return true;
            case 15:
                String readString7 = parcel.readString();
                String readString8 = parcel.readString();
                String readString9 = parcel.readString();
                boolean h9 = x0.h(parcel);
                x0.f(parcel);
                K0 = s(readString7, readString8, readString9, h9);
                parcel2.writeNoException();
                parcel2.writeTypedList(K0);
                return true;
            case 16:
                x0.f(parcel);
                K0 = t0(parcel.readString(), parcel.readString(), (zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                parcel2.writeTypedList(K0);
                return true;
            case 17:
                String readString10 = parcel.readString();
                String readString11 = parcel.readString();
                String readString12 = parcel.readString();
                x0.f(parcel);
                K0 = s0(readString10, readString11, readString12);
                parcel2.writeNoException();
                parcel2.writeTypedList(K0);
                return true;
            case 18:
                x0.f(parcel);
                r0((zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                return true;
            case 19:
                x0.f(parcel);
                G1((Bundle) x0.a(parcel, Bundle.CREATOR), (zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                return true;
            case 20:
                x0.f(parcel);
                m((zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                return true;
            case 21:
                x0.f(parcel);
                zzal M0 = M0((zzn) x0.a(parcel, zzn.CREATOR));
                parcel2.writeNoException();
                x0.g(parcel2, M0);
                return true;
            case 24:
                x0.f(parcel);
                K0 = K((zzn) x0.a(parcel, zzn.CREATOR), (Bundle) x0.a(parcel, Bundle.CREATOR));
                parcel2.writeNoException();
                parcel2.writeTypedList(K0);
                return true;
        }
    }
}
