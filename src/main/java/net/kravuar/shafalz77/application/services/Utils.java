package net.kravuar.shafalz77.application.services;

import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service
public class Utils {
    private final ByteBuffer ib = ByteBuffer.allocate(4);
    private final ByteBuffer sb = ByteBuffer.allocate(2);

    public int toInt(byte[] arr) {
        ib.put(arr);
        ib.rewind();
        var res = ib.getInt();
        ib.clear();
        return res;
    }
    public byte[] asBytes(int val) {
        ib.putInt(val);
        var res = ib.array();
        ib.clear();
        return res;
    }

    public short toShort(byte[] arr) {
        sb.put(arr);
        sb.rewind();
        short res = sb.getShort();
        sb.clear();
        return res;
    }
    public byte[] asBytes(short val) {
        sb.putShort(val);
        var res = sb.array();
        sb.clear();
        return res;
    }
}
