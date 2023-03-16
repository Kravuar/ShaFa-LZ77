package net.kravuar.shafalz77.domain.model;

import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;

public class ByteBuffer {
    private final byte[] data;
    private int start;
    private int end;
    @Getter
    private int size;
    @Getter
    private final int capacity;

    public ByteBuffer(int size, int sizeMultiplier) {
        this.data = new byte[size * sizeMultiplier];
        this.capacity = size;
        this.start = 0;
        this.end = 0;
        this.size = 0;
    }


    public void shift(int n) {
        if (n < 0 || n > size)
            return;

        if (end + n <= data.length) {
            start += n;
            size -= n;
        }
        else {
            size -= n;
            System.arraycopy(data, start + n, data, 0, size);
            start = 0;
            end = size;
        }
    }
    public void shiftOnFull(int n) {
        if (size + n >= capacity)
            shift(n);
    }
    public void read(byte[] dest, int from, int n) {
        if (from + n > end)
            throw new IndexOutOfBoundsException();
        System.arraycopy(data, start + from, dest, 0, n);
    }
    public int append(BufferedInputStream in, int n) {
        if (n <= 0)
            return 0;

        int res;
        try {
            res = Math.max(in.read(data, end, n), 0);
            end += res;
            size += res;
        } catch (IOException e) { return 0; }
        return res;
    }
    public void append(byte[] source, int from, int n) {
        System.arraycopy(source, from, data, end, n);
        end += n;
        size += n;
    }
    public void append(ByteBuffer other, int from, int n) {
        append(other.data, other.start + from, n);
    }

    public byte at(int i) {
        if (i >= size || i < 0)
            throw new IndexOutOfBoundsException();
        return data[start + i];
    }
    @Override
    public String toString() {
        return Arrays.toString(ArrayUtils.subarray(data, start, end));
    }
}
