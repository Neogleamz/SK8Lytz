package j5;

import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface o {

    /* renamed from: a  reason: collision with root package name */
    public static final o f20788a = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements o {
        a() {
        }

        @Override // j5.o
        public long a() {
            throw new NoSuchElementException();
        }

        @Override // j5.o
        public long b() {
            throw new NoSuchElementException();
        }

        @Override // j5.o
        public boolean next() {
            return false;
        }
    }

    long a();

    long b();

    boolean next();
}
