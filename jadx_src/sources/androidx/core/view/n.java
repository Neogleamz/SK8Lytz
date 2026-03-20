package androidx.core.view;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface n {
    boolean a(MenuItem menuItem);

    default void b(Menu menu) {
    }

    void c(Menu menu, MenuInflater menuInflater);

    default void d(Menu menu) {
    }
}
