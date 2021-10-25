package core;

public enum Browsers {
    CHROME("chrome"),
    FIREFOX("firefox"),
    WEBKIT("webkit");

    private final String text;

    Browsers(String text) {
        this.text = text;
    }

    public String get() {
        return this.text;
    }

    @Override
    public String toString() {
        return this.text;
    }

    public static Browsers findByString(String str) {
        for (Browsers browser : values()) {
            if (browser.get().equals(str)) {
                return browser;
            }
        }
        throw new IllegalArgumentException();
    }
}
