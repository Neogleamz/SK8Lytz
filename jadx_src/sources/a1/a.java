package a1;

import android.os.Build;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    private final b f38a;

    /* renamed from: b  reason: collision with root package name */
    private int f39b = Integer.MAX_VALUE;

    /* renamed from: c  reason: collision with root package name */
    private int f40c = 0;

    /* renamed from: a1.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class C0001a extends b {

        /* renamed from: a  reason: collision with root package name */
        private final EditText f41a;

        /* renamed from: b  reason: collision with root package name */
        private final g f42b;

        C0001a(EditText editText, boolean z4) {
            this.f41a = editText;
            g gVar = new g(editText, z4);
            this.f42b = gVar;
            editText.addTextChangedListener(gVar);
            editText.setEditableFactory(a1.b.getInstance());
        }

        @Override // a1.a.b
        KeyListener a(KeyListener keyListener) {
            if (keyListener instanceof e) {
                return keyListener;
            }
            if (keyListener == null) {
                return null;
            }
            return keyListener instanceof NumberKeyListener ? keyListener : new e(keyListener);
        }

        @Override // a1.a.b
        InputConnection b(InputConnection inputConnection, EditorInfo editorInfo) {
            return inputConnection instanceof c ? inputConnection : new c(this.f41a, inputConnection, editorInfo);
        }

        @Override // a1.a.b
        void c(boolean z4) {
            this.f42b.c(z4);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {
        b() {
        }

        KeyListener a(KeyListener keyListener) {
            return keyListener;
        }

        InputConnection b(InputConnection inputConnection, EditorInfo editorInfo) {
            return inputConnection;
        }

        void c(boolean z4) {
        }
    }

    public a(EditText editText, boolean z4) {
        androidx.core.util.h.i(editText, "editText cannot be null");
        if (Build.VERSION.SDK_INT < 19) {
            this.f38a = new b();
        } else {
            this.f38a = new C0001a(editText, z4);
        }
    }

    public KeyListener a(KeyListener keyListener) {
        return this.f38a.a(keyListener);
    }

    public InputConnection b(InputConnection inputConnection, EditorInfo editorInfo) {
        if (inputConnection == null) {
            return null;
        }
        return this.f38a.b(inputConnection, editorInfo);
    }

    public void c(boolean z4) {
        this.f38a.c(z4);
    }
}
