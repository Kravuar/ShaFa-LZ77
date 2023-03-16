package net.kravuar.shafalz77.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.shafalz77.application.props.AppProps;
import net.kravuar.shafalz77.domain.model.Algorithm;
import net.kravuar.shafalz77.domain.model.ByteBuffer;
import net.kravuar.shafalz77.domain.model.LZ77Match;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;

@RequiredArgsConstructor
@Service
public class LZ77 implements Algorithm {
    private final AppProps props;
    private final Utils utils;

    public boolean compress(BufferedInputStream in, BufferedOutputStream out) {
        int dLength = props.getBufferSize();
        int bLength = (int) (props.getBufferSize() * 0.125);
        var dict = new ByteBuffer(dLength, 2);
        var buff = new ByteBuffer(bLength, 2);
        LZ77Match match;

        buff.append(in, bLength);
        if (buff.getSize() != bLength)
            return false;
        try {
            do {
                match = findMatch(dict, buff);

                out.write(utils.asBytes(match.getOffset()));
                out.write(utils.asBytes(match.getLength()));
                out.write(match.getNext());

                int length = match.getLength() + 1;
                dict.shiftOnFull(length);
                dict.append(buff, 0, length);

                buff.shift(length);
                buff.append(in, length);
            } while (buff.getSize() != 0);
            out.flush();
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }
    @Override
    public boolean decompress(BufferedInputStream in, BufferedOutputStream out) throws InvalidParameterException {
        var dict = new ByteBuffer(props.getBufferSize(), 2);
        LZ77Match match = LZ77Match.builder().build();

        try {
            while (parseMatch(in, match)) {
                var sequence = new byte[match.getLength() + 1];
                dict.read(sequence, match.getOffset(), match.getLength());
                sequence[match.getLength()] = match.getNext();

                dict.shiftOnFull(sequence.length);
                dict.append(sequence, 0, sequence.length);

                out.write(sequence);
            }
            out.flush();
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    private boolean parseMatch(BufferedInputStream in, LZ77Match match) {
        try {
            match.setOffset(utils.toShort(in.readNBytes(2)));
            match.setLength(utils.toShort(in.readNBytes(2)));
            int next = in.read();
            match.setNext((byte) next);

            return next != -1;
        } catch (IOException ignored) { return false; }
    }
    private LZ77Match findMatch(ByteBuffer dict, ByteBuffer buff) {
        int dSize = dict.getSize();
        int bSize = buff.getSize();

        if (bSize < 1)
            throw new IllegalArgumentException("No match can be found with empty buffer");
        LZ77Match longestMatch =
                LZ77Match.builder()
                        .offset((short) 0)
                        .length((short) 0)
                        .next(buff.at(0))
                        .build();
        for (int i = 0; i < dSize; ++i)
            if (dict.at(i) == buff.at(0) && bSize > 1) {
                LZ77Match match = LZ77Match.builder()
                        .offset((short) (i))
                        .length((short) 1)
                        .next(buff.at(1))
                        .build();
                int j = 1;
                while (i + j < dSize && j < bSize - 1 && dict.at(i + j) == buff.at(j)) {
                    match.incLength();
                    j++;
                }
                match.setNext(buff.at(j));
                i += match.getLength();
                if (match.getLength() > longestMatch.getLength())
                    longestMatch = match;
            }
        return longestMatch;
    }
}
