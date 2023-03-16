package net.kravuar.shafalz77.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SymbolMapping {
    private final byte oldCode;
    private final BigEndianBitSet newCode;
}
