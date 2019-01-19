package by.grodno.vasili.presentation.feature.base;

import static org.apache.commons.lang3.StringUtils.length;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Class for application utilities
 */
public class Utils {
    public static final int MAX_TITLE_LENGTH = 256;
    public static final int MAX_DESCRIPTION_LENGTH = 512;

    /**
     * Validate note Title
     */
    public static boolean validateTitle(String title) {
        int length = length(trim(title));
        return length > 0 && length <= MAX_TITLE_LENGTH;
    }

    /**
     * Validate note Description
     */
    public static boolean validateDescription(String description) {
        int length = length(trim(description));
        return length > 0 && length <= MAX_DESCRIPTION_LENGTH;
    }
}
