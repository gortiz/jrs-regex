package io.questdb.jni.example.rust;

import io.questdb.jar.jni.JarJniLoader;


public class JniRsRegex implements RsRegex {
  static {
    JarJniLoader.loadLib(
        Main.class,

        // A platform-specific path is automatically suffixed to path below.
        "/io/questdb/jni/example/rust/libs",

        // The "lib" prefix and ".so|.dynlib|.dll" suffix are added automatically as needed.
        "str_reverse");
  }

  private long rsRegexPtr;

  private JniRsRegex(long rsRegexPtr) {
    if (rsRegexPtr <= 0) {
      throw new IllegalArgumentException("rsRegexPtr must not be zero");
    }
    this.rsRegexPtr = rsRegexPtr;
  }

  @Override
  public boolean isMatch(String haystack) {
    if (rsRegexPtr <= 0) {
      throw new IllegalStateException("Regex object has been closed");
    }
    return isMatchNative(rsRegexPtr, haystack);
  }

  @Override
  public Match find(String haystack, int start) {
    if (rsRegexPtr <= 0) {
      throw new IllegalStateException("Regex object has been closed");
    }
    if (start < 0) {
      throw new IllegalArgumentException("Start index cannot be negative");
    }
    if (start > haystack.length()) {
      throw new IllegalArgumentException("Start index cannot be greater than haystack length");
    }
    return findNative(rsRegexPtr, haystack, start);
  }

  @Override
  public void close() {
    if (rsRegexPtr > 0) {
      closeNative(rsRegexPtr);
      rsRegexPtr = 0;
    }
  }

  private static native boolean isMatchNative(long rsRegexPtr, String haystack);

  private static native Match findNative(long rsRegexPtr, String haystack, int start);

  private static native void closeNative(long rsRegexPtr);

  private static native long createNative(String pattern);

  public static class Factory implements RsRegex.Factory {
    @Override
    public RsRegex create(String pattern) {
      long rsRegexPtr = createNative(pattern);
      return new JniRsRegex(rsRegexPtr);
    }
  }
}
