package net.kravuar.shafalz77.domain.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CountedMappings {
    private final Map<Byte, Integer> oldToCount;
    private final BiMap<Byte, BigEndianBitSet> oldToNew;
    private final int total;

    public CountedMappings(byte[] bytes) {
        var counted = new HashMap<Byte, Integer>();
        oldToNew = HashBiMap.create();
        for (byte b: bytes)
            counted.merge(b, 1, Integer::sum);

        this.oldToCount = counted.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldV, newV) -> oldV,
                        LinkedHashMap::new
                ));
        this.total = oldToCount.values().stream()
                .reduce(0, Integer::sum);
    }
    public CountedMappings(Map<Byte, Integer> countedSorted) {
        this.oldToCount = countedSorted.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldV, newV) -> oldV,
                        LinkedHashMap::new
                ));
        this.total = oldToCount.values().stream()
                .reduce(0, Integer::sum);
        this.oldToNew = HashBiMap.create();
    }

    public int getOldCode(BigEndianBitSet newCode) {
        return oldToNew.inverse().get(newCode);
    }
    public BigEndianBitSet getNewCode(byte oldCode) {
        return oldToNew.get(oldCode);
    }
    public void put(SymbolMapping mapping) {
        oldToNew.put(mapping.getOldCode(), mapping.getNewCode());
    }
    public int size() {
        return oldToCount.size();
    }
}
