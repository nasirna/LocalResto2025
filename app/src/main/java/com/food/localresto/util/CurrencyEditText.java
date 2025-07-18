package com.food.localresto.util;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * CurrencyEditText for AndroidX with configurable maximal decimal digits.
 */
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

    /**
     * Set the maximal number of decimal digits allowed.
     * @param digits Number of decimal digits.
     */
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

    /**
     * Returns the numeric value as double.
     */
    public double getNumericValue() {
        String clean = getText().toString().replaceAll("[^\\d.]", "");
        if (clean.isEmpty()) return 0.0;
        return Double.parseDouble(clean);
    }
}
