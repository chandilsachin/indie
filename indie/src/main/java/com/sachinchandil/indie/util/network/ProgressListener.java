package com.sachinchandil.indie.util.network;

public interface ProgressListener
{

    /**
     * <h1>public void onProgress(int progress, int total);</h1>
     * <p>Called when download in progress.</p>
     *
     * @param progress - progress value.
     * @param total    - total value.
     */
    public void onProgress(long progress, long total);
}
