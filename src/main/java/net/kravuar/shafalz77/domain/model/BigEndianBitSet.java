package net.kravuar.shafalz77.domain.model;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.BitSet;
import java.util.stream.IntStream;

@EqualsAndHashCode
@NoArgsConstructor
public class BigEndianBitSet {
    private final BitSet bitSet = new BitSet();
    private int actualLength = 0;

    public void append(boolean bit) {
        set(actualLength, bit);
        actualLength++;
    }
    public void append(final BigEndianBitSet other) {
        int bit = other.bitSet.length();
        while ((bit = other.bitSet.previousSetBit(bit - 1)) != -1)
            set(actualLength + translate(bit), true);
        actualLength+= other.actualLength;
    }

    public int length() {return actualLength;}

    public void set(int bitIndex, boolean bit) {
        bitSet.set(translate(bitIndex), bit);
    }
    public boolean get(int bitIndex) {
        return bitSet.get(translate(bitIndex));
    }
    public byte[] toByteArray() {
        // Convenien(st) API in Jaba d_(*_*)
        var cleverArray = new byte[(actualLength + 8 - 1 ) / 8];
        var retardArray = bitSet.toByteArray();
        System.arraycopy(retardArray, 0, cleverArray, 0, retardArray.length);

        return cleverArray;
    }

    private int translate(int idx) {
        return idx - 2 * (idx % 8) + 7;
    }
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder(actualLength);
        for (int i = 0; i < actualLength; i+=4) {
            IntStream.range(i, i + 4).limit(actualLength - i)
                    .mapToObj(j -> get(j) ? '1' : '0').forEach(buffer::append);
            buffer.append(" ");
        }
        return buffer.toString();
    }
}
