package net.kravuar.shafalz77.application.services;

import lombok.RequiredArgsConstructor;
import net.kravuar.shafalz77.domain.model.*;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.LinkedHashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ShaFa implements Algorithm {
    private final Utils utils;
    @Override
    public boolean compress(BufferedInputStream in, BufferedOutputStream out) {
        try {
            var bytes = in.readAllBytes();
            var countedMappings = new CountedMappings(bytes);
            var tree = buildTree(countedMappings);
            for (var leaf: tree.getLeaves())
                countedMappings.put(leaf.mapping);

            out.write(utils.asBytes(countedMappings.getOldToCount().size()));
            for (var entry: countedMappings.getOldToCount().entrySet()) {
                out.write(entry.getKey());
                out.write(utils.asBytes(entry.getValue()));
            }

//            for (var mapping: countedMappings.getOldToNew().entrySet())
//                System.out.printf("Old: %c %-24s   New:%-48s   Count: %d\n",
//                        mapping.getKey(),
//                        Integer.toBinaryString(mapping.getKey()),
//                        mapping.getValue(),
//                        countedMappings.getOldToCount().get(mapping.getKey()));

            out.write(utils.asBytes(countedMappings.getTotal()));

            var byteBuilder = new BigEndianBitSet();
            for (byte b: bytes) {
                byteBuilder.append(countedMappings.getNewCode(b));
                if (byteBuilder.length() % 8 == 0) {
                    out.write(byteBuilder.toByteArray());
                    byteBuilder = new BigEndianBitSet();
                }
            }
            if (byteBuilder.length() != 0)
                out.write(byteBuilder.toByteArray());
            out.flush();
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }
    @Override
    public boolean  decompress(BufferedInputStream in, BufferedOutputStream out) throws InvalidParameterException {
        try {
            int freqSize = utils.toInt(in.readNBytes(4));
            var oldToCount = new LinkedHashMap<Byte, Integer>(freqSize);
            for (int i = 0; i < freqSize; ++i)
                oldToCount.put(
                        (byte) in.read(),
                        utils.toInt(in.readNBytes(4)));

            int nSymbols = utils.toInt(in.readNBytes(4));

            var tree = buildTree(new CountedMappings(oldToCount));

            var node = tree;
            int mask = 128;

            byte currentByte;
            int res = in.read();
            while (res != -1) {
                currentByte = (byte) res;
                for (int j = 0; j < 8; ++j) {
                    if ((currentByte & (mask >>> j)) == 0)
                        node = node.left;
                    else
                        node = node.right;
                    if (node == null)
                        throw new InvalidParameterException("Reached tree leaf, but no mapping was found.");
                    if (node.left == null && node.right == null) {
                        if (node.mapping == null)
                            throw new InvalidParameterException("Found leaf with no mapping.");
                        out.write(node.mapping.getOldCode());
                        if (--nSymbols == 0)
                            break;
                        node = tree;
                    }
                }
                res = in.read();
            }
            out.flush();
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    private Tree buildTree(CountedMappings mappings) {
        var root = new Tree();
        buildTree(root,
                mappings.getOldToCount().values().stream().toList(),
                mappings.getOldToCount().keySet().stream()
                        .map(b -> new SymbolMapping(b, new BigEndianBitSet()))
                        .toList(),
                mappings.getTotal());
        return root;
    }
    private void buildTree(Tree node, List<Integer> counted, List<SymbolMapping> mappings, int total) {
        int n = 0;
        int sum = 0;
        int minDif = total;
        int dif;
        for (int next : counted) {
            dif = Math.abs((total - 2 * (sum + next)));
            if (dif >= minDif)
                break;
            sum += next;
            minDif = dif;
            n++;
        }

        var p1 = mappings.subList(0, n);
        var p2 = mappings.subList(n, mappings.size());
        var c1 = counted.subList(0, n);
        var c2 = counted.subList(n, counted.size());

        for (var mapping: p1)
            mapping.getNewCode().append(false);
        for (var mapping: p2)
            mapping.getNewCode().append(true);

        if (p1.size() > 1) {
            node.left = new Tree();
            buildTree(node.left, c1, p1, sum);
        }
        else if (p1.size() == 1) {
            node.left = new Tree();
            node.left.mapping = p1.get(0);
        }

        if (p2.size() > 1) {
            node.right = new Tree();
            buildTree(node.right, c2, p2, total - sum);
        }
        else if (p2.size() == 1) {
            node.right = new Tree();
            node.right.mapping = p2.get(0);
        }
    }
}
