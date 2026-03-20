package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ConstraintAttribute {

    /* renamed from: a  reason: collision with root package name */
    String f3913a;

    /* renamed from: b  reason: collision with root package name */
    private AttributeType f3914b;

    /* renamed from: c  reason: collision with root package name */
    private int f3915c;

    /* renamed from: d  reason: collision with root package name */
    private float f3916d;

    /* renamed from: e  reason: collision with root package name */
    private String f3917e;

    /* renamed from: f  reason: collision with root package name */
    boolean f3918f;

    /* renamed from: g  reason: collision with root package name */
    private int f3919g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum AttributeType {
        INT_TYPE,
        FLOAT_TYPE,
        COLOR_TYPE,
        COLOR_DRAWABLE_TYPE,
        STRING_TYPE,
        BOOLEAN_TYPE,
        DIMENSION_TYPE
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f3928a;

        static {
            int[] iArr = new int[AttributeType.values().length];
            f3928a = iArr;
            try {
                iArr[AttributeType.COLOR_TYPE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3928a[AttributeType.COLOR_DRAWABLE_TYPE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3928a[AttributeType.INT_TYPE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3928a[AttributeType.FLOAT_TYPE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f3928a[AttributeType.STRING_TYPE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f3928a[AttributeType.BOOLEAN_TYPE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f3928a[AttributeType.DIMENSION_TYPE.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public ConstraintAttribute(ConstraintAttribute constraintAttribute, Object obj) {
        this.f3913a = constraintAttribute.f3913a;
        this.f3914b = constraintAttribute.f3914b;
        j(obj);
    }

    public ConstraintAttribute(String str, AttributeType attributeType, Object obj) {
        this.f3913a = str;
        this.f3914b = attributeType;
        j(obj);
    }

    private static int a(int i8) {
        int i9 = (i8 & (~(i8 >> 31))) - 255;
        return (i9 & (i9 >> 31)) + 255;
    }

    public static HashMap<String, ConstraintAttribute> b(HashMap<String, ConstraintAttribute> hashMap, View view) {
        HashMap<String, ConstraintAttribute> hashMap2 = new HashMap<>();
        Class<?> cls = view.getClass();
        for (String str : hashMap.keySet()) {
            ConstraintAttribute constraintAttribute = hashMap.get(str);
            try {
                hashMap2.put(str, str.equals("BackgroundColor") ? new ConstraintAttribute(constraintAttribute, Integer.valueOf(((ColorDrawable) view.getBackground()).getColor())) : new ConstraintAttribute(constraintAttribute, cls.getMethod("getMap" + str, new Class[0]).invoke(view, new Object[0])));
            } catch (IllegalAccessException e8) {
                e8.printStackTrace();
            } catch (NoSuchMethodException e9) {
                e9.printStackTrace();
            } catch (InvocationTargetException e10) {
                e10.printStackTrace();
            }
        }
        return hashMap2;
    }

    public static void g(Context context, XmlPullParser xmlPullParser, HashMap<String, ConstraintAttribute> hashMap) {
        AttributeType attributeType;
        Object string;
        int integer;
        float f5;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(Xml.asAttributeSet(xmlPullParser), e.f4139c3);
        int indexCount = obtainStyledAttributes.getIndexCount();
        String str = null;
        Object obj = null;
        AttributeType attributeType2 = null;
        for (int i8 = 0; i8 < indexCount; i8++) {
            int index = obtainStyledAttributes.getIndex(i8);
            if (index == e.f4149d3) {
                str = obtainStyledAttributes.getString(index);
                if (str != null && str.length() > 0) {
                    str = Character.toUpperCase(str.charAt(0)) + str.substring(1);
                }
            } else if (index == e.f4158e3) {
                obj = Boolean.valueOf(obtainStyledAttributes.getBoolean(index, false));
                attributeType2 = AttributeType.BOOLEAN_TYPE;
            } else {
                if (index == e.f4175g3) {
                    attributeType = AttributeType.COLOR_TYPE;
                } else if (index == e.f4167f3) {
                    attributeType = AttributeType.COLOR_DRAWABLE_TYPE;
                } else {
                    if (index == e.f4211k3) {
                        attributeType = AttributeType.DIMENSION_TYPE;
                        f5 = TypedValue.applyDimension(1, obtainStyledAttributes.getDimension(index, 0.0f), context.getResources().getDisplayMetrics());
                    } else if (index == e.f4184h3) {
                        attributeType = AttributeType.DIMENSION_TYPE;
                        f5 = obtainStyledAttributes.getDimension(index, 0.0f);
                    } else if (index == e.f4193i3) {
                        attributeType = AttributeType.FLOAT_TYPE;
                        f5 = obtainStyledAttributes.getFloat(index, Float.NaN);
                    } else if (index == e.f4202j3) {
                        attributeType = AttributeType.INT_TYPE;
                        integer = obtainStyledAttributes.getInteger(index, -1);
                        string = Integer.valueOf(integer);
                        Object obj2 = string;
                        attributeType2 = attributeType;
                        obj = obj2;
                    } else if (index == e.f4220l3) {
                        attributeType = AttributeType.STRING_TYPE;
                        string = obtainStyledAttributes.getString(index);
                        Object obj22 = string;
                        attributeType2 = attributeType;
                        obj = obj22;
                    }
                    string = Float.valueOf(f5);
                    Object obj222 = string;
                    attributeType2 = attributeType;
                    obj = obj222;
                }
                integer = obtainStyledAttributes.getColor(index, 0);
                string = Integer.valueOf(integer);
                Object obj2222 = string;
                attributeType2 = attributeType;
                obj = obj2222;
            }
        }
        if (str != null && obj != null) {
            hashMap.put(str, new ConstraintAttribute(str, attributeType2, obj));
        }
        obtainStyledAttributes.recycle();
    }

    public static void h(View view, HashMap<String, ConstraintAttribute> hashMap) {
        Class<?> cls = view.getClass();
        for (String str : hashMap.keySet()) {
            ConstraintAttribute constraintAttribute = hashMap.get(str);
            String str2 = "set" + str;
            try {
                switch (a.f3928a[constraintAttribute.f3914b.ordinal()]) {
                    case 1:
                        cls.getMethod(str2, Integer.TYPE).invoke(view, Integer.valueOf(constraintAttribute.f3919g));
                        break;
                    case 2:
                        Method method = cls.getMethod(str2, Drawable.class);
                        ColorDrawable colorDrawable = new ColorDrawable();
                        colorDrawable.setColor(constraintAttribute.f3919g);
                        method.invoke(view, colorDrawable);
                        break;
                    case 3:
                        cls.getMethod(str2, Integer.TYPE).invoke(view, Integer.valueOf(constraintAttribute.f3915c));
                        break;
                    case 4:
                        cls.getMethod(str2, Float.TYPE).invoke(view, Float.valueOf(constraintAttribute.f3916d));
                        break;
                    case 5:
                        cls.getMethod(str2, CharSequence.class).invoke(view, constraintAttribute.f3917e);
                        break;
                    case 6:
                        cls.getMethod(str2, Boolean.TYPE).invoke(view, Boolean.valueOf(constraintAttribute.f3918f));
                        break;
                    case 7:
                        cls.getMethod(str2, Float.TYPE).invoke(view, Float.valueOf(constraintAttribute.f3916d));
                        break;
                }
            } catch (IllegalAccessException e8) {
                Log.e("TransitionLayout", " Custom Attribute \"" + str + "\" not found on " + cls.getName());
                e8.printStackTrace();
            } catch (NoSuchMethodException e9) {
                Log.e("TransitionLayout", e9.getMessage());
                Log.e("TransitionLayout", " Custom Attribute \"" + str + "\" not found on " + cls.getName());
                StringBuilder sb = new StringBuilder();
                sb.append(cls.getName());
                sb.append(" must have a method ");
                sb.append(str2);
                Log.e("TransitionLayout", sb.toString());
            } catch (InvocationTargetException e10) {
                Log.e("TransitionLayout", " Custom Attribute \"" + str + "\" not found on " + cls.getName());
                e10.printStackTrace();
            }
        }
    }

    public AttributeType c() {
        return this.f3914b;
    }

    public float d() {
        switch (a.f3928a[this.f3914b.ordinal()]) {
            case 1:
            case 2:
                throw new RuntimeException("Color does not have a single color to interpolate");
            case 3:
                return this.f3915c;
            case 4:
                return this.f3916d;
            case 5:
                throw new RuntimeException("Cannot interpolate String");
            case 6:
                return this.f3918f ? 0.0f : 1.0f;
            case 7:
                return this.f3916d;
            default:
                return Float.NaN;
        }
    }

    public void e(float[] fArr) {
        switch (a.f3928a[this.f3914b.ordinal()]) {
            case 1:
            case 2:
                int i8 = this.f3919g;
                float pow = (float) Math.pow(((i8 >> 16) & 255) / 255.0f, 2.2d);
                float pow2 = (float) Math.pow(((i8 >> 8) & 255) / 255.0f, 2.2d);
                fArr[0] = pow;
                fArr[1] = pow2;
                fArr[2] = (float) Math.pow((i8 & 255) / 255.0f, 2.2d);
                fArr[3] = ((i8 >> 24) & 255) / 255.0f;
                return;
            case 3:
                fArr[0] = this.f3915c;
                return;
            case 4:
                fArr[0] = this.f3916d;
                return;
            case 5:
                throw new RuntimeException("Color does not have a single color to interpolate");
            case 6:
                fArr[0] = this.f3918f ? 0.0f : 1.0f;
                return;
            case 7:
                fArr[0] = this.f3916d;
                return;
            default:
                return;
        }
    }

    public int f() {
        int i8 = a.f3928a[this.f3914b.ordinal()];
        return (i8 == 1 || i8 == 2) ? 4 : 1;
    }

    public void i(View view, float[] fArr) {
        Class<?> cls = view.getClass();
        String str = "set" + this.f3913a;
        try {
            boolean z4 = true;
            switch (a.f3928a[this.f3914b.ordinal()]) {
                case 1:
                    cls.getMethod(str, Integer.TYPE).invoke(view, Integer.valueOf((a((int) (((float) Math.pow(fArr[0], 0.45454545454545453d)) * 255.0f)) << 16) | (a((int) (fArr[3] * 255.0f)) << 24) | (a((int) (((float) Math.pow(fArr[1], 0.45454545454545453d)) * 255.0f)) << 8) | a((int) (((float) Math.pow(fArr[2], 0.45454545454545453d)) * 255.0f))));
                    return;
                case 2:
                    Method method = cls.getMethod(str, Drawable.class);
                    int a9 = a((int) (((float) Math.pow(fArr[0], 0.45454545454545453d)) * 255.0f));
                    int a10 = a((int) (((float) Math.pow(fArr[1], 0.45454545454545453d)) * 255.0f));
                    ColorDrawable colorDrawable = new ColorDrawable();
                    colorDrawable.setColor((a9 << 16) | (a((int) (fArr[3] * 255.0f)) << 24) | (a10 << 8) | a((int) (((float) Math.pow(fArr[2], 0.45454545454545453d)) * 255.0f)));
                    method.invoke(view, colorDrawable);
                    return;
                case 3:
                    cls.getMethod(str, Integer.TYPE).invoke(view, Integer.valueOf((int) fArr[0]));
                    return;
                case 4:
                    cls.getMethod(str, Float.TYPE).invoke(view, Float.valueOf(fArr[0]));
                    return;
                case 5:
                    throw new RuntimeException("unable to interpolate strings " + this.f3913a);
                case 6:
                    Method method2 = cls.getMethod(str, Boolean.TYPE);
                    Object[] objArr = new Object[1];
                    if (fArr[0] <= 0.5f) {
                        z4 = false;
                    }
                    objArr[0] = Boolean.valueOf(z4);
                    method2.invoke(view, objArr);
                    return;
                case 7:
                    cls.getMethod(str, Float.TYPE).invoke(view, Float.valueOf(fArr[0]));
                    return;
                default:
                    return;
            }
        } catch (IllegalAccessException e8) {
            Log.e("TransitionLayout", "cannot access method " + str + "on View \"" + androidx.constraintlayout.motion.widget.a.c(view) + "\"");
            e8.printStackTrace();
        } catch (NoSuchMethodException e9) {
            Log.e("TransitionLayout", "no method " + str + "on View \"" + androidx.constraintlayout.motion.widget.a.c(view) + "\"");
            e9.printStackTrace();
        } catch (InvocationTargetException e10) {
            e10.printStackTrace();
        }
    }

    public void j(Object obj) {
        switch (a.f3928a[this.f3914b.ordinal()]) {
            case 1:
            case 2:
                this.f3919g = ((Integer) obj).intValue();
                return;
            case 3:
                this.f3915c = ((Integer) obj).intValue();
                return;
            case 4:
            case 7:
                this.f3916d = ((Float) obj).floatValue();
                return;
            case 5:
                this.f3917e = (String) obj;
                return;
            case 6:
                this.f3918f = ((Boolean) obj).booleanValue();
                return;
            default:
                return;
        }
    }
}
