package su.dynamicimage;

public class NativeTextLoaderJNI {
    static {
        System.loadLibrary("native-textloader");
    }

    public native String getText();

}
