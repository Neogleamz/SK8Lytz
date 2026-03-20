package androidx.appcompat.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.g0;
import androidx.core.view.c0;
import com.daimajia.numberprogressbar.BuildConfig;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k {

    /* renamed from: b  reason: collision with root package name */
    private static final Class<?>[] f671b = {Context.class, AttributeSet.class};

    /* renamed from: c  reason: collision with root package name */
    private static final int[] f672c = {16843375};

    /* renamed from: d  reason: collision with root package name */
    private static final int[] f673d = {16844160};

    /* renamed from: e  reason: collision with root package name */
    private static final int[] f674e = {16844156};

    /* renamed from: f  reason: collision with root package name */
    private static final int[] f675f = {16844148};

    /* renamed from: g  reason: collision with root package name */
    private static final String[] f676g = {"android.widget.", "android.view.", "android.webkit."};

    /* renamed from: h  reason: collision with root package name */
    private static final k0.g<String, Constructor<? extends View>> f677h = new k0.g<>();

    /* renamed from: a  reason: collision with root package name */
    private final Object[] f678a = new Object[2];

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements View.OnClickListener {

        /* renamed from: a  reason: collision with root package name */
        private final View f679a;

        /* renamed from: b  reason: collision with root package name */
        private final String f680b;

        /* renamed from: c  reason: collision with root package name */
        private Method f681c;

        /* renamed from: d  reason: collision with root package name */
        private Context f682d;

        public a(View view, String str) {
            this.f679a = view;
            this.f680b = str;
        }

        private void a(Context context) {
            int id;
            String str;
            Method method;
            while (context != null) {
                try {
                    if (!context.isRestricted() && (method = context.getClass().getMethod(this.f680b, View.class)) != null) {
                        this.f681c = method;
                        this.f682d = context;
                        return;
                    }
                } catch (NoSuchMethodException unused) {
                }
                context = context instanceof ContextWrapper ? ((ContextWrapper) context).getBaseContext() : null;
            }
            if (this.f679a.getId() == -1) {
                str = BuildConfig.FLAVOR;
            } else {
                str = " with id '" + this.f679a.getContext().getResources().getResourceEntryName(id) + "'";
            }
            throw new IllegalStateException("Could not find method " + this.f680b + "(View) in a parent or ancestor Context for android:onClick attribute defined on view " + this.f679a.getClass() + str);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (this.f681c == null) {
                a(this.f679a.getContext());
            }
            try {
                this.f681c.invoke(this.f682d, view);
            } catch (IllegalAccessException e8) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", e8);
            } catch (InvocationTargetException e9) {
                throw new IllegalStateException("Could not execute method for android:onClick", e9);
            }
        }
    }

    private void a(Context context, View view, AttributeSet attributeSet) {
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 19 || i8 > 28) {
            return;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f673d);
        if (obtainStyledAttributes.hasValue(0)) {
            c0.u0(view, obtainStyledAttributes.getBoolean(0, false));
        }
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, f674e);
        if (obtainStyledAttributes2.hasValue(0)) {
            c0.w0(view, obtainStyledAttributes2.getString(0));
        }
        obtainStyledAttributes2.recycle();
        TypedArray obtainStyledAttributes3 = context.obtainStyledAttributes(attributeSet, f675f);
        if (obtainStyledAttributes3.hasValue(0)) {
            c0.L0(view, obtainStyledAttributes3.getBoolean(0, false));
        }
        obtainStyledAttributes3.recycle();
    }

    private void b(View view, AttributeSet attributeSet) {
        Context context = view.getContext();
        if (context instanceof ContextWrapper) {
            if (Build.VERSION.SDK_INT < 15 || c0.R(view)) {
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, f672c);
                String string = obtainStyledAttributes.getString(0);
                if (string != null) {
                    view.setOnClickListener(new a(view, string));
                }
                obtainStyledAttributes.recycle();
            }
        }
    }

    private View s(Context context, String str, String str2) {
        String str3;
        k0.g<String, Constructor<? extends View>> gVar = f677h;
        Constructor<? extends View> constructor = gVar.get(str);
        if (constructor == null) {
            if (str2 != null) {
                try {
                    str3 = str2 + str;
                } catch (Exception unused) {
                    return null;
                }
            } else {
                str3 = str;
            }
            constructor = Class.forName(str3, false, context.getClassLoader()).asSubclass(View.class).getConstructor(f671b);
            gVar.put(str, constructor);
        }
        constructor.setAccessible(true);
        return constructor.newInstance(this.f678a);
    }

    private View t(Context context, String str, AttributeSet attributeSet) {
        if (str.equals("view")) {
            str = attributeSet.getAttributeValue(null, "class");
        }
        try {
            Object[] objArr = this.f678a;
            objArr[0] = context;
            objArr[1] = attributeSet;
            if (-1 != str.indexOf(46)) {
                return s(context, str, null);
            }
            int i8 = 0;
            while (true) {
                String[] strArr = f676g;
                if (i8 >= strArr.length) {
                    return null;
                }
                View s8 = s(context, str, strArr[i8]);
                if (s8 != null) {
                    return s8;
                }
                i8++;
            }
        } catch (Exception unused) {
            return null;
        } finally {
            Object[] objArr2 = this.f678a;
            objArr2[0] = null;
            objArr2[1] = null;
        }
    }

    private static Context u(Context context, AttributeSet attributeSet, boolean z4, boolean z8) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, g.j.T3, 0, 0);
        int resourceId = z4 ? obtainStyledAttributes.getResourceId(g.j.U3, 0) : 0;
        if (z8 && resourceId == 0 && (resourceId = obtainStyledAttributes.getResourceId(g.j.V3, 0)) != 0) {
            Log.i("AppCompatViewInflater", "app:theme is now deprecated. Please move to using android:theme instead.");
        }
        obtainStyledAttributes.recycle();
        return resourceId != 0 ? ((context instanceof androidx.appcompat.view.d) && ((androidx.appcompat.view.d) context).c() == resourceId) ? context : new androidx.appcompat.view.d(context, resourceId) : context;
    }

    private void v(View view, String str) {
        if (view != null) {
            return;
        }
        throw new IllegalStateException(getClass().getName() + " asked to inflate view for <" + str + ">, but returned null");
    }

    protected AppCompatAutoCompleteTextView c(Context context, AttributeSet attributeSet) {
        return new AppCompatAutoCompleteTextView(context, attributeSet);
    }

    protected AppCompatButton d(Context context, AttributeSet attributeSet) {
        return new AppCompatButton(context, attributeSet);
    }

    protected AppCompatCheckBox e(Context context, AttributeSet attributeSet) {
        return new AppCompatCheckBox(context, attributeSet);
    }

    protected AppCompatCheckedTextView f(Context context, AttributeSet attributeSet) {
        return new AppCompatCheckedTextView(context, attributeSet);
    }

    protected AppCompatEditText g(Context context, AttributeSet attributeSet) {
        return new AppCompatEditText(context, attributeSet);
    }

    protected AppCompatImageButton h(Context context, AttributeSet attributeSet) {
        return new AppCompatImageButton(context, attributeSet);
    }

    protected AppCompatImageView i(Context context, AttributeSet attributeSet) {
        return new AppCompatImageView(context, attributeSet);
    }

    protected AppCompatMultiAutoCompleteTextView j(Context context, AttributeSet attributeSet) {
        return new AppCompatMultiAutoCompleteTextView(context, attributeSet);
    }

    protected AppCompatRadioButton k(Context context, AttributeSet attributeSet) {
        return new AppCompatRadioButton(context, attributeSet);
    }

    protected AppCompatRatingBar l(Context context, AttributeSet attributeSet) {
        return new AppCompatRatingBar(context, attributeSet);
    }

    protected AppCompatSeekBar m(Context context, AttributeSet attributeSet) {
        return new AppCompatSeekBar(context, attributeSet);
    }

    protected AppCompatSpinner n(Context context, AttributeSet attributeSet) {
        return new AppCompatSpinner(context, attributeSet);
    }

    protected AppCompatTextView o(Context context, AttributeSet attributeSet) {
        return new AppCompatTextView(context, attributeSet);
    }

    protected AppCompatToggleButton p(Context context, AttributeSet attributeSet) {
        return new AppCompatToggleButton(context, attributeSet);
    }

    protected View q(Context context, String str, AttributeSet attributeSet) {
        return null;
    }

    public final View r(View view, String str, Context context, AttributeSet attributeSet, boolean z4, boolean z8, boolean z9, boolean z10) {
        View l8;
        Context context2 = (!z4 || view == null) ? context : view.getContext();
        if (z8 || z9) {
            context2 = u(context2, attributeSet, z8, z9);
        }
        if (z10) {
            context2 = g0.b(context2);
        }
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1946472170:
                if (str.equals("RatingBar")) {
                    c9 = 0;
                    break;
                }
                break;
            case -1455429095:
                if (str.equals("CheckedTextView")) {
                    c9 = 1;
                    break;
                }
                break;
            case -1346021293:
                if (str.equals("MultiAutoCompleteTextView")) {
                    c9 = 2;
                    break;
                }
                break;
            case -938935918:
                if (str.equals("TextView")) {
                    c9 = 3;
                    break;
                }
                break;
            case -937446323:
                if (str.equals("ImageButton")) {
                    c9 = 4;
                    break;
                }
                break;
            case -658531749:
                if (str.equals("SeekBar")) {
                    c9 = 5;
                    break;
                }
                break;
            case -339785223:
                if (str.equals("Spinner")) {
                    c9 = 6;
                    break;
                }
                break;
            case 776382189:
                if (str.equals("RadioButton")) {
                    c9 = 7;
                    break;
                }
                break;
            case 799298502:
                if (str.equals("ToggleButton")) {
                    c9 = '\b';
                    break;
                }
                break;
            case 1125864064:
                if (str.equals("ImageView")) {
                    c9 = '\t';
                    break;
                }
                break;
            case 1413872058:
                if (str.equals("AutoCompleteTextView")) {
                    c9 = '\n';
                    break;
                }
                break;
            case 1601505219:
                if (str.equals("CheckBox")) {
                    c9 = 11;
                    break;
                }
                break;
            case 1666676343:
                if (str.equals("EditText")) {
                    c9 = '\f';
                    break;
                }
                break;
            case 2001146706:
                if (str.equals("Button")) {
                    c9 = '\r';
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                l8 = l(context2, attributeSet);
                v(l8, str);
                break;
            case 1:
                l8 = f(context2, attributeSet);
                v(l8, str);
                break;
            case 2:
                l8 = j(context2, attributeSet);
                v(l8, str);
                break;
            case 3:
                l8 = o(context2, attributeSet);
                v(l8, str);
                break;
            case 4:
                l8 = h(context2, attributeSet);
                v(l8, str);
                break;
            case 5:
                l8 = m(context2, attributeSet);
                v(l8, str);
                break;
            case 6:
                l8 = n(context2, attributeSet);
                v(l8, str);
                break;
            case 7:
                l8 = k(context2, attributeSet);
                v(l8, str);
                break;
            case '\b':
                l8 = p(context2, attributeSet);
                v(l8, str);
                break;
            case '\t':
                l8 = i(context2, attributeSet);
                v(l8, str);
                break;
            case '\n':
                l8 = c(context2, attributeSet);
                v(l8, str);
                break;
            case 11:
                l8 = e(context2, attributeSet);
                v(l8, str);
                break;
            case '\f':
                l8 = g(context2, attributeSet);
                v(l8, str);
                break;
            case '\r':
                l8 = d(context2, attributeSet);
                v(l8, str);
                break;
            default:
                l8 = q(context2, str, attributeSet);
                break;
        }
        if (l8 == null && context != context2) {
            l8 = t(context2, str, attributeSet);
        }
        if (l8 != null) {
            b(l8, attributeSet);
            a(context2, l8, attributeSet);
        }
        return l8;
    }
}
