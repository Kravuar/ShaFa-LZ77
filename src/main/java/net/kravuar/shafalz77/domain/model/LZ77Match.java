package net.kravuar.shafalz77.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LZ77Match {
    private short offset;
    private short length;
    private byte next;

    public void incLength() { length++; }
    @Override
    public String toString() {
        return String.format("[%d, %d] -> %d", offset, length, next);
    }
}
