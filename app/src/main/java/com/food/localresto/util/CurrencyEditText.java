package com.food.localresto.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.food.localresto.R;

import java.math.BigDecimal;

/**
 * CurrencyEditText for AndroidX with configurable maximal decimal digits.
 *
 *
 * CurrencyEditText currencyEditText = findViewById(R.id.currencyEditText);
 * currencyEditText.setOnDownOrEnterListener(new CurrencyEditText.OnDownOrEnterListener() {
 *     @Override
 *     public void onDownOrEnter(CurrencyEditText view) {
 *         // Handle ACTION_DOWN or KEYCODE_ENTER event here
 *         BigDecimal value = view.getNumericValue();
 *         // Do something with the BigDecimal value
 *     }
 * });

public class CurrencyEditText extends AppCompatEditText {
    private String current = "";
    private int maxDecimalDigits = 2;

    public CurrencyEditText(Context context) {
        super(context);
        init();
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMaxDecimalDigits(int digits) {
        this.maxDecimalDigits = digits;
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                removeTextChangedListener(this);
                String input = s.toString();

                // Remove currency formatting and non-digit/non-dot
                String clean = input.replaceAll("[^\\d.]", "");

                // Handle multiple dots
                int firstDot = clean.indexOf('.');
                if (firstDot != -1) {
                    int secondDot = clean.indexOf('.', firstDot + 1);
                    while (secondDot != -1) {
                        clean = clean.substring(0, secondDot) + clean.substring(secondDot + 1);
                        secondDot = clean.indexOf('.', firstDot + 1);
                    }
                }

                // Restrict decimal digits
                if (firstDot != -1 && clean.length() > firstDot + 1 + maxDecimalDigits) {
                    clean = clean.substring(0, firstDot + 1 + maxDecimalDigits);
                }

                // Format as currency
                try {
                    double parsed = clean.isEmpty() ? 0.0 : Double.parseDouble(clean);
                    NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
                    formatter.setMaximumFractionDigits(maxDecimalDigits);
                    formatter.setMinimumFractionDigits(0);
                    String formatted = formatter.format(parsed);

                    current = formatted;
                    setText(formatted);
                    setSelection(formatted.length());
                } catch (Exception e) {
                    // fallback in case of exception
                    setText(current);
                    setSelection(current.length());
                }
                addTextChangedListener(this);
            }
        });
    }

    public double getNumericValue() {
        String clean = getText().toString().replaceAll("[^\\d.]", "");
        if (clean.isEmpty()) return 0.0;
        return Double.parseDouble(clean);
    }
}
*/



/*public class CurrencyEditText extends AppCompatEditText {

    private boolean isEditing;
    private String currencySymbol = "";
    private boolean showCurrencySymbol = false;
    private int maxDecimalDigits = 2;

    private String lastFormatted = "";

    public CurrencyEditText(Context context) {
        super(context);
        init(null);
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CurrencyEditText);
            currencySymbol = a.getString(R.styleable.CurrencyEditText_currencySymbol);
            showCurrencySymbol = a.getBoolean(R.styleable.CurrencyEditText_showCurrencySymbol, false);
            maxDecimalDigits = a.getInt(R.styleable.CurrencyEditText_maxDecimalDigits, 2);
            a.recycle();
        }

        if (currencySymbol == null) currencySymbol = "";

        this.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;

                String clean = s.toString().replaceAll("[^\\d]", "");

                if (clean.isEmpty()) {
                    setText("");
                    isEditing = false;
                    return;
                }

                try {
                    BigDecimal parsed = new BigDecimal(clean).movePointLeft(maxDecimalDigits);
                    NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
                    format.setMinimumFractionDigits(maxDecimalDigits);
                    format.setMaximumFractionDigits(maxDecimalDigits);
                    String formatted = format.format(parsed);

                    lastFormatted = formatted;

                    if (showCurrencySymbol) {
                        formatted = currencySymbol + " " + formatted;
                    }

                    setText(formatted);
                    setSelection(formatted.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                isEditing = false;
            }
        });

        setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
    }

    // ✅ Public method to get numeric value as BigDecimal
    public BigDecimal getNumericValue() {
        String raw = getText() != null ? getText().toString() : "";
        String clean = raw.replaceAll("[^\\d]", "");

        if (clean.isEmpty()) return BigDecimal.ZERO;

        try {
            return new BigDecimal(clean).movePointLeft(maxDecimalDigits);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}*/


/**
 * CurrencyEditText is an EditText for currency input with:
 * - configurable maximal decimal digits (via XML attr)
 * - configurable currency symbol (via XML attr)
 * - configurable display of currency symbol (via XML attr)
 * - requests focus when activity/view is up
 * - provides OnDownOrEnterListener for ACTION_DOWN or KEYCODE_ENTER events
 */

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatEditText;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * CurrencyEditText is an EditText for currency input with:
 * - Configurable maximal decimal digits (via XML attr)
 * - Configurable currency symbol (via XML attr)
 * - Configurable display of currency symbol (via XML attr)
 * - Provides events for ACTION_DOWN or KEYCODE_ENTER
 */

/**
 * CurrencyEditText is an EditText for currency input with:
 * - Configurable maximal decimal digits (via XML attr)
 * - Configurable currency symbol (via XML attr)
 * - Configurable display of currency symbol (via XML attr)
 * - Provides events for ACTION_DOWN or KEYCODE_ENTER
 * - Public method to get numeric value as BigDecimal
 */
public class CurrencyEditText extends AppCompatEditText {

    public interface OnDownOrEnterListener {
        void onDownOrEnter(CurrencyEditText view);
    }

    private int maxDecimalDigits = 2;
    private String currencySymbol = "$";
    private boolean displayCurrencySymbol = true;
    private DecimalFormat decimalFormat;
    private boolean isEditing = false;
    private OnDownOrEnterListener onDownOrEnterListener;

    public CurrencyEditText(Context context) {
        super(context);
        init(null);
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        if (attrs != null) {
            android.content.res.TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CurrencyEditText);
            maxDecimalDigits = a.getInt(R.styleable.CurrencyEditText_maxDecimalDigits, 2);
            currencySymbol = a.getString(R.styleable.CurrencyEditText_currencySymbol);
            if (currencySymbol == null) currencySymbol = "$";
            displayCurrencySymbol = a.getBoolean(R.styleable.CurrencyEditText_displayCurrencySymbol, true);
            a.recycle();
        }

        updateFormatter();

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;
                String cleanString = s.toString().replaceAll("[^\\d.]", "");
                if (cleanString.contains(".")) {
                    String[] parts = cleanString.split("\\.");
                    if (parts.length > 1 && maxDecimalDigits > 0) {
                        cleanString = parts[0] + "." + parts[1].substring(0, Math.min(parts[1].length(), maxDecimalDigits));
                    }
                }
                BigDecimal value = BigDecimal.ZERO;
                try { value = new BigDecimal(cleanString); } catch (Exception ignored) { }
                String formatted = (displayCurrencySymbol ? currencySymbol : "") + decimalFormat.format(value);
                setText(formatted);
                setSelection(formatted.length());
                isEditing = false;
            }
        });
    }

    private void updateFormatter() {
        StringBuilder pattern = new StringBuilder("#,##0");
        if (maxDecimalDigits > 0) {
            pattern.append(".");
            for (int i = 0; i < maxDecimalDigits; i++) pattern.append("0");
        }
        decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.getDefault());
        decimalFormat.applyPattern(pattern.toString());
        decimalFormat.setParseBigDecimal(true);
    }

    /**
     * Get the numeric value as BigDecimal.
     * @return BigDecimal representation of the input value
     */
    public BigDecimal getNumericValue() {
        String txt = getText() == null ? "" : getText().toString();
        txt = txt.replace(currencySymbol, "").replaceAll("[^\\d.]", "");
        try { return new BigDecimal(txt); } catch (Exception e) { return BigDecimal.ZERO; }
    }

    public void setMaxDecimalDigits(int digits) {
        maxDecimalDigits = digits;
        updateFormatter();
    }

    public void setCurrencySymbol(String symbol) {
        currencySymbol = symbol;
        updateFormatter();
    }

    public void setDisplayCurrencySymbol(boolean display) {
        displayCurrencySymbol = display;
        updateFormatter();
    }

    public void setOnDownOrEnterListener(OnDownOrEnterListener listener) {
        onDownOrEnterListener = listener;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER || event.getAction() == KeyEvent.ACTION_DOWN) {
            if (onDownOrEnterListener != null) {
                onDownOrEnterListener.onDownOrEnter(this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (onDownOrEnterListener != null) {
                onDownOrEnterListener.onDownOrEnter(this);
            }
        }
        return super.onTouchEvent(event);
    }
}