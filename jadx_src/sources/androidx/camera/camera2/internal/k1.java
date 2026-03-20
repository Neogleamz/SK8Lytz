package androidx.camera.camera2.internal;

import androidx.camera.core.CameraState;
import androidx.camera.core.impl.CameraInternal;
import androidx.lifecycle.LiveData;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class k1 {

    /* renamed from: a  reason: collision with root package name */
    private final androidx.camera.core.impl.e f1911a;

    /* renamed from: b  reason: collision with root package name */
    private final androidx.lifecycle.p<CameraState> f1912b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f1913a;

        static {
            int[] iArr = new int[CameraInternal.State.values().length];
            f1913a = iArr;
            try {
                iArr[CameraInternal.State.PENDING_OPEN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f1913a[CameraInternal.State.OPENING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f1913a[CameraInternal.State.OPEN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f1913a[CameraInternal.State.CLOSING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f1913a[CameraInternal.State.RELEASING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f1913a[CameraInternal.State.CLOSED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f1913a[CameraInternal.State.RELEASED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k1(androidx.camera.core.impl.e eVar) {
        this.f1911a = eVar;
        androidx.lifecycle.p<CameraState> pVar = new androidx.lifecycle.p<>();
        this.f1912b = pVar;
        pVar.l(CameraState.a(CameraState.Type.CLOSED));
    }

    private CameraState b() {
        return CameraState.a(this.f1911a.a() ? CameraState.Type.OPENING : CameraState.Type.PENDING_OPEN);
    }

    public LiveData<CameraState> a() {
        return this.f1912b;
    }

    public void c(CameraInternal.State state, CameraState.a aVar) {
        CameraState b9;
        CameraState.Type type;
        switch (a.f1913a[state.ordinal()]) {
            case 1:
                b9 = b();
                break;
            case 2:
                type = CameraState.Type.OPENING;
                b9 = CameraState.b(type, aVar);
                break;
            case 3:
                type = CameraState.Type.OPEN;
                b9 = CameraState.b(type, aVar);
                break;
            case 4:
            case 5:
                type = CameraState.Type.CLOSING;
                b9 = CameraState.b(type, aVar);
                break;
            case 6:
            case 7:
                type = CameraState.Type.CLOSED;
                b9 = CameraState.b(type, aVar);
                break;
            default:
                throw new IllegalStateException("Unknown internal camera state: " + state);
        }
        androidx.camera.core.p1.a("CameraStateMachine", "New public camera state " + b9 + " from " + state + " and " + aVar);
        if (Objects.equals(this.f1912b.e(), b9)) {
            return;
        }
        androidx.camera.core.p1.a("CameraStateMachine", "Publishing new public camera state " + b9);
        this.f1912b.l(b9);
    }
}
