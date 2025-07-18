package com.food.localresto.util;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class DecimalEditText extends AppCompatEditText {

    // Maximum number of decimal digits (for example, 2 for currency)
    private int decimalDigits = 2;

    public DecimalEditText(Context context) {
        super(context);
        init();
    }

    public DecimalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DecimalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setFilters(new InputFilter[]{new DecimalDigitsInputFilter(decimalDigits)});
        setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
        setFilters(new InputFilter[]{new DecimalDigitsInputFilter(decimalDigits)});
    }

    // InputFilter to allow only a limited number of decimal digits
    private static class DecimalDigitsInputFilter implements InputFilter {
        private final int decimalDigits;

        public DecimalDigitsInputFilter(int decimalDigits) {
            this.decimalDigits = decimalDigits;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            String result = dest.subSequence(0, dstart)
                    + source.toString()
                    + dest.subSequence(dend, dest.length());
            if (result.equals(".")) {
                return "0.";
            }
            if (result.contains(".")) {
                int index = result.indexOf(".");
                int decimalCount = result.length() - index - 1;
                if (decimalCount > decimalDigits) {
                    return "";
                }
            }
            // Only allow numbers and one dot
            if (!result.matches("^\\d*(\\.\\d*)?$")) {
                return "";
            }
            return null;
        }
    }
}