package androidx.appcompat.widget;

import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.WeakHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class d0 extends v0.c implements View.OnClickListener {
    private int A;
    private int B;
    private int C;
    private int E;
    private int F;

    /* renamed from: m  reason: collision with root package name */
    private final SearchView f1448m;

    /* renamed from: n  reason: collision with root package name */
    private final SearchableInfo f1449n;

    /* renamed from: p  reason: collision with root package name */
    private final Context f1450p;
    private final WeakHashMap<String, Drawable.ConstantState> q;

    /* renamed from: t  reason: collision with root package name */
    private final int f1451t;

    /* renamed from: w  reason: collision with root package name */
    private boolean f1452w;

    /* renamed from: x  reason: collision with root package name */
    private int f1453x;

    /* renamed from: y  reason: collision with root package name */
    private ColorStateList f1454y;

    /* renamed from: z  reason: collision with root package name */
    private int f1455z;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final TextView f1456a;

        /* renamed from: b  reason: collision with root package name */
        public final TextView f1457b;

        /* renamed from: c  reason: collision with root package name */
        public final ImageView f1458c;

        /* renamed from: d  reason: collision with root package name */
        public final ImageView f1459d;

        /* renamed from: e  reason: collision with root package name */
        public final ImageView f1460e;

        public a(View view) {
            this.f1456a = (TextView) view.findViewById(16908308);
            this.f1457b = (TextView) view.findViewById(16908309);
            this.f1458c = (ImageView) view.findViewById(16908295);
            this.f1459d = (ImageView) view.findViewById(16908296);
            this.f1460e = (ImageView) view.findViewById(g.f.f19953s);
        }
    }

    public d0(Context context, SearchView searchView, SearchableInfo searchableInfo, WeakHashMap<String, Drawable.ConstantState> weakHashMap) {
        super(context, searchView.getSuggestionRowLayout(), null, true);
        this.f1452w = false;
        this.f1453x = 1;
        this.f1455z = -1;
        this.A = -1;
        this.B = -1;
        this.C = -1;
        this.E = -1;
        this.F = -1;
        this.f1448m = searchView;
        this.f1449n = searchableInfo;
        this.f1451t = searchView.getSuggestionCommitIconResId();
        this.f1450p = context;
        this.q = weakHashMap;
    }

    private void A(String str, Drawable drawable) {
        if (drawable != null) {
            this.q.put(str, drawable.getConstantState());
        }
    }

    private void B(Cursor cursor) {
        Bundle extras = cursor != null ? cursor.getExtras() : null;
        if (extras != null) {
            extras.getBoolean("in_progress");
        }
    }

    private Drawable k(String str) {
        Drawable.ConstantState constantState = this.q.get(str);
        if (constantState == null) {
            return null;
        }
        return constantState.newDrawable();
    }

    private CharSequence l(CharSequence charSequence) {
        if (this.f1454y == null) {
            TypedValue typedValue = new TypedValue();
            this.f1450p.getTheme().resolveAttribute(g.a.P, typedValue, true);
            this.f1454y = this.f1450p.getResources().getColorStateList(typedValue.resourceId);
        }
        SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan(new TextAppearanceSpan(null, 0, 0, this.f1454y, null), 0, charSequence.length(), 33);
        return spannableString;
    }

    private Drawable m(ComponentName componentName) {
        String nameNotFoundException;
        ActivityInfo activityInfo;
        int iconResource;
        PackageManager packageManager = this.f1450p.getPackageManager();
        try {
            activityInfo = packageManager.getActivityInfo(componentName, RecognitionOptions.ITF);
            iconResource = activityInfo.getIconResource();
        } catch (PackageManager.NameNotFoundException e8) {
            nameNotFoundException = e8.toString();
        }
        if (iconResource == 0) {
            return null;
        }
        Drawable drawable = packageManager.getDrawable(componentName.getPackageName(), iconResource, activityInfo.applicationInfo);
        if (drawable == null) {
            nameNotFoundException = "Invalid icon resource " + iconResource + " for " + componentName.flattenToShortString();
            Log.w("SuggestionsAdapter", nameNotFoundException);
            return null;
        }
        return drawable;
    }

    private Drawable n(ComponentName componentName) {
        String flattenToShortString = componentName.flattenToShortString();
        if (!this.q.containsKey(flattenToShortString)) {
            Drawable m8 = m(componentName);
            this.q.put(flattenToShortString, m8 != null ? m8.getConstantState() : null);
            return m8;
        }
        Drawable.ConstantState constantState = this.q.get(flattenToShortString);
        if (constantState == null) {
            return null;
        }
        return constantState.newDrawable(this.f1450p.getResources());
    }

    public static String o(Cursor cursor, String str) {
        return w(cursor, cursor.getColumnIndex(str));
    }

    private Drawable p() {
        Drawable n8 = n(this.f1449n.getSearchActivity());
        return n8 != null ? n8 : this.f1450p.getPackageManager().getDefaultActivityIcon();
    }

    private Drawable q(Uri uri) {
        try {
            if ("android.resource".equals(uri.getScheme())) {
                try {
                    return r(uri);
                } catch (Resources.NotFoundException unused) {
                    throw new FileNotFoundException("Resource does not exist: " + uri);
                }
            }
            InputStream openInputStream = this.f1450p.getContentResolver().openInputStream(uri);
            if (openInputStream == null) {
                throw new FileNotFoundException("Failed to open " + uri);
            }
            Drawable createFromStream = Drawable.createFromStream(openInputStream, null);
            try {
                openInputStream.close();
            } catch (IOException e8) {
                Log.e("SuggestionsAdapter", "Error closing icon stream for " + uri, e8);
            }
            return createFromStream;
        } catch (FileNotFoundException e9) {
            Log.w("SuggestionsAdapter", "Icon not found: " + uri + ", " + e9.getMessage());
            return null;
        }
        Log.w("SuggestionsAdapter", "Icon not found: " + uri + ", " + e9.getMessage());
        return null;
    }

    private Drawable s(String str) {
        if (str == null || str.isEmpty() || "0".equals(str)) {
            return null;
        }
        try {
            int parseInt = Integer.parseInt(str);
            String str2 = "android.resource://" + this.f1450p.getPackageName() + "/" + parseInt;
            Drawable k8 = k(str2);
            if (k8 != null) {
                return k8;
            }
            Drawable f5 = androidx.core.content.a.f(this.f1450p, parseInt);
            A(str2, f5);
            return f5;
        } catch (Resources.NotFoundException unused) {
            Log.w("SuggestionsAdapter", "Icon resource not found: " + str);
            return null;
        } catch (NumberFormatException unused2) {
            Drawable k9 = k(str);
            if (k9 != null) {
                return k9;
            }
            Drawable q = q(Uri.parse(str));
            A(str, q);
            return q;
        }
    }

    private Drawable t(Cursor cursor) {
        int i8 = this.C;
        if (i8 == -1) {
            return null;
        }
        Drawable s8 = s(cursor.getString(i8));
        return s8 != null ? s8 : p();
    }

    private Drawable u(Cursor cursor) {
        int i8 = this.E;
        if (i8 == -1) {
            return null;
        }
        return s(cursor.getString(i8));
    }

    private static String w(Cursor cursor, int i8) {
        if (i8 == -1) {
            return null;
        }
        try {
            return cursor.getString(i8);
        } catch (Exception e8) {
            Log.e("SuggestionsAdapter", "unexpected error retrieving valid column from cursor, did the remote process die?", e8);
            return null;
        }
    }

    private void y(ImageView imageView, Drawable drawable, int i8) {
        imageView.setImageDrawable(drawable);
        if (drawable == null) {
            imageView.setVisibility(i8);
            return;
        }
        imageView.setVisibility(0);
        drawable.setVisible(false, false);
        drawable.setVisible(true, false);
    }

    private void z(TextView textView, CharSequence charSequence) {
        textView.setText(charSequence);
        textView.setVisibility(TextUtils.isEmpty(charSequence) ? 8 : 0);
    }

    @Override // v0.a, v0.b.a
    public void a(Cursor cursor) {
        if (this.f1452w) {
            Log.w("SuggestionsAdapter", "Tried to change cursor after adapter was closed.");
            if (cursor != null) {
                cursor.close();
                return;
            }
            return;
        }
        try {
            super.a(cursor);
            if (cursor != null) {
                this.f1455z = cursor.getColumnIndex("suggest_text_1");
                this.A = cursor.getColumnIndex("suggest_text_2");
                this.B = cursor.getColumnIndex("suggest_text_2_url");
                this.C = cursor.getColumnIndex("suggest_icon_1");
                this.E = cursor.getColumnIndex("suggest_icon_2");
                this.F = cursor.getColumnIndex("suggest_flags");
            }
        } catch (Exception e8) {
            Log.e("SuggestionsAdapter", "error changing cursor and caching columns", e8);
        }
    }

    @Override // v0.a, v0.b.a
    public CharSequence c(Cursor cursor) {
        String o5;
        String o8;
        if (cursor == null) {
            return null;
        }
        String o9 = o(cursor, "suggest_intent_query");
        if (o9 != null) {
            return o9;
        }
        if (!this.f1449n.shouldRewriteQueryFromData() || (o8 = o(cursor, "suggest_intent_data")) == null) {
            if (!this.f1449n.shouldRewriteQueryFromText() || (o5 = o(cursor, "suggest_text_1")) == null) {
                return null;
            }
            return o5;
        }
        return o8;
    }

    @Override // v0.b.a
    public Cursor d(CharSequence charSequence) {
        String charSequence2 = charSequence == null ? BuildConfig.FLAVOR : charSequence.toString();
        if (this.f1448m.getVisibility() == 0 && this.f1448m.getWindowVisibility() == 0) {
            try {
                Cursor v8 = v(this.f1449n, charSequence2, 50);
                if (v8 != null) {
                    v8.getCount();
                    return v8;
                }
            } catch (RuntimeException e8) {
                Log.w("SuggestionsAdapter", "Search suggestions query threw an exception.", e8);
            }
        }
        return null;
    }

    @Override // v0.a
    public void e(View view, Context context, Cursor cursor) {
        a aVar = (a) view.getTag();
        int i8 = this.F;
        int i9 = i8 != -1 ? cursor.getInt(i8) : 0;
        if (aVar.f1456a != null) {
            z(aVar.f1456a, w(cursor, this.f1455z));
        }
        if (aVar.f1457b != null) {
            String w8 = w(cursor, this.B);
            CharSequence l8 = w8 != null ? l(w8) : w(cursor, this.A);
            if (TextUtils.isEmpty(l8)) {
                TextView textView = aVar.f1456a;
                if (textView != null) {
                    textView.setSingleLine(false);
                    aVar.f1456a.setMaxLines(2);
                }
            } else {
                TextView textView2 = aVar.f1456a;
                if (textView2 != null) {
                    textView2.setSingleLine(true);
                    aVar.f1456a.setMaxLines(1);
                }
            }
            z(aVar.f1457b, l8);
        }
        ImageView imageView = aVar.f1458c;
        if (imageView != null) {
            y(imageView, t(cursor), 4);
        }
        ImageView imageView2 = aVar.f1459d;
        if (imageView2 != null) {
            y(imageView2, u(cursor), 8);
        }
        int i10 = this.f1453x;
        if (i10 != 2 && (i10 != 1 || (i9 & 1) == 0)) {
            aVar.f1460e.setVisibility(8);
            return;
        }
        aVar.f1460e.setVisibility(0);
        aVar.f1460e.setTag(aVar.f1456a.getText());
        aVar.f1460e.setOnClickListener(this);
    }

    @Override // v0.a, android.widget.BaseAdapter, android.widget.SpinnerAdapter
    public View getDropDownView(int i8, View view, ViewGroup viewGroup) {
        try {
            return super.getDropDownView(i8, view, viewGroup);
        } catch (RuntimeException e8) {
            Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", e8);
            View g8 = g(this.f1450p, b(), viewGroup);
            if (g8 != null) {
                ((a) g8.getTag()).f1456a.setText(e8.toString());
            }
            return g8;
        }
    }

    @Override // v0.a, android.widget.Adapter
    public View getView(int i8, View view, ViewGroup viewGroup) {
        try {
            return super.getView(i8, view, viewGroup);
        } catch (RuntimeException e8) {
            Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", e8);
            View h8 = h(this.f1450p, b(), viewGroup);
            if (h8 != null) {
                ((a) h8.getTag()).f1456a.setText(e8.toString());
            }
            return h8;
        }
    }

    @Override // v0.c, v0.a
    public View h(Context context, Cursor cursor, ViewGroup viewGroup) {
        View h8 = super.h(context, cursor, viewGroup);
        h8.setTag(new a(h8));
        ((ImageView) h8.findViewById(g.f.f19953s)).setImageResource(this.f1451t);
        return h8;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public boolean hasStableIds() {
        return false;
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        B(b());
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        B(b());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Object tag = view.getTag();
        if (tag instanceof CharSequence) {
            this.f1448m.U((CharSequence) tag);
        }
    }

    Drawable r(Uri uri) {
        int parseInt;
        String authority = uri.getAuthority();
        if (TextUtils.isEmpty(authority)) {
            throw new FileNotFoundException("No authority: " + uri);
        }
        try {
            Resources resourcesForApplication = this.f1450p.getPackageManager().getResourcesForApplication(authority);
            List<String> pathSegments = uri.getPathSegments();
            if (pathSegments == null) {
                throw new FileNotFoundException("No path: " + uri);
            }
            int size = pathSegments.size();
            if (size == 1) {
                try {
                    parseInt = Integer.parseInt(pathSegments.get(0));
                } catch (NumberFormatException unused) {
                    throw new FileNotFoundException("Single path segment is not a resource ID: " + uri);
                }
            } else if (size != 2) {
                throw new FileNotFoundException("More than two path segments: " + uri);
            } else {
                parseInt = resourcesForApplication.getIdentifier(pathSegments.get(1), pathSegments.get(0), authority);
            }
            if (parseInt != 0) {
                return resourcesForApplication.getDrawable(parseInt);
            }
            throw new FileNotFoundException("No resource found for: " + uri);
        } catch (PackageManager.NameNotFoundException unused2) {
            throw new FileNotFoundException("No package found for authority: " + uri);
        }
    }

    Cursor v(SearchableInfo searchableInfo, String str, int i8) {
        String suggestAuthority;
        String[] strArr = null;
        if (searchableInfo == null || (suggestAuthority = searchableInfo.getSuggestAuthority()) == null) {
            return null;
        }
        Uri.Builder fragment = new Uri.Builder().scheme("content").authority(suggestAuthority).query(BuildConfig.FLAVOR).fragment(BuildConfig.FLAVOR);
        String suggestPath = searchableInfo.getSuggestPath();
        if (suggestPath != null) {
            fragment.appendEncodedPath(suggestPath);
        }
        fragment.appendPath("search_suggest_query");
        String suggestSelection = searchableInfo.getSuggestSelection();
        if (suggestSelection != null) {
            strArr = new String[]{str};
        } else {
            fragment.appendPath(str);
        }
        String[] strArr2 = strArr;
        if (i8 > 0) {
            fragment.appendQueryParameter("limit", String.valueOf(i8));
        }
        return this.f1450p.getContentResolver().query(fragment.build(), null, suggestSelection, strArr2, null);
    }

    public void x(int i8) {
        this.f1453x = i8;
    }
}
