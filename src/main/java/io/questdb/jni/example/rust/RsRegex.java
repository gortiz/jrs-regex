package io.questdb.jni.example.rust;

import java.util.Objects;


public interface RsRegex extends AutoCloseable {

  boolean isMatch(String haystack);

  Match find(String haystack, int start);

  default Match find(String haystack) {
    return find(haystack, 0);
  }

  @Override
  void close();

  interface Factory {
    RsRegex create(String pattern);
  }

  class Match {
    private final int start;
    private final int end;
    public Match(int start, int end) {
      if (start < 0 || end < start) {
        throw new IllegalArgumentException("Invalid match indices: start=" + start + ", end=" + end);
      }
      this.start = start;
      this.end = end;
    }

    public int getStart() {
      return start;
    }

    public int getEnd() {
      return end;
    }

    public String getSubstring(String haystack) {
      return haystack.substring(start, end);
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Match match)) {
        return false;
      }
      return getStart() == match.getStart() && getEnd() == match.getEnd();
    }

    @Override
    public int hashCode() {
      return Objects.hash(getStart(), getEnd());
    }
  }
}
