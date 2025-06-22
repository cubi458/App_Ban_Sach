package nlu.hmuaf.android_bookapp.enums;

public enum EBookFormat {
    HARDCOVER("HARDCOVER"),
    PAPERBACK("PAPERBACK");

    private final String value;

    EBookFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EBookFormat fromString(String text) {
        for (EBookFormat format : EBookFormat.values()) {
            if (format.value.equalsIgnoreCase(text)) {
                return format;
            }
        }
        return PAPERBACK; // default value
    }
} 