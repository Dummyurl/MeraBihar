package app.zingo.merabihars.VideoComperssor.Video;

/**
 * Created by ZingoHotels Tech on 05-10-2018.
 */

public class Sample {

    private long offset = 0;
    private long size = 0;

    public Sample(long offset, long size) {
        this.offset = offset;
        this.size = size;
    }

    public long getOffset() {
        return offset;
    }

    public long getSize() {
        return size;
    }
}