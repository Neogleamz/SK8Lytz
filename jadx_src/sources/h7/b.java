package h7;

import android.os.Parcel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.signin.internal.zaa;
import com.google.android.gms.signin.internal.zag;
import com.google.android.gms.signin.internal.zak;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b extends a7.b implements c {
    public b() {
        super("com.google.android.gms.signin.internal.ISignInCallbacks");
    }

    @Override // a7.b
    protected final boolean g(int i8, Parcel parcel, Parcel parcel2, int i9) {
        switch (i8) {
            case 3:
                ConnectionResult connectionResult = (ConnectionResult) a7.c.a(parcel, ConnectionResult.CREATOR);
                zaa zaaVar = (zaa) a7.c.a(parcel, zaa.CREATOR);
                a7.c.b(parcel);
                break;
            case 4:
            case 6:
                Status status = (Status) a7.c.a(parcel, Status.CREATOR);
                a7.c.b(parcel);
                break;
            case 5:
            default:
                return false;
            case 7:
                Status status2 = (Status) a7.c.a(parcel, Status.CREATOR);
                GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount) a7.c.a(parcel, GoogleSignInAccount.CREATOR);
                a7.c.b(parcel);
                break;
            case 8:
                a7.c.b(parcel);
                z((zak) a7.c.a(parcel, zak.CREATOR));
                break;
            case 9:
                zag zagVar = (zag) a7.c.a(parcel, zag.CREATOR);
                a7.c.b(parcel);
                break;
        }
        parcel2.writeNoException();
        return true;
    }
}
