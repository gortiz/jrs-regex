package io.questdb.jni.example.rust;

public interface RsCaptures extends AutoCloseable {

    /**
     * Returns the number of captures in this object.
     *
     * @return the number of captures
     */
    int size();

    /**
     * Returns the capture at the specified index.
     *
     * @param index the index of the capture to return
     * @return the capture at the specified index
     */
    String get(int index);

    /**
     * Returns the capture with the specified name.
     *
     * @param name the name of the capture to return
     * @return the capture with the specified name, or null if not found
     */
    String get(String name);

    /**
     * Closes this captures object, releasing any resources it holds.
     */
    @Override
    void close();
}
