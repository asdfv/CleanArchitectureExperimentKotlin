package by.grodno.vasili.presentation.feature.base;

import android.databinding.BindingConversion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Util class for bindings
 */
public class BindingsUtils {
    private static final String DATE_FORMAT ="yyyy-MM-dd HH:mm:ss";
    private static final String NO_DATE = "No date";

    @BindingConversion
    public static String convertDate(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        simpleDateFormat.setTimeZone(Calendar.getInstance().getTimeZone());
        return timestamp == 0 ? NO_DATE : simpleDateFormat.format(new Date(timestamp));
    }
}
