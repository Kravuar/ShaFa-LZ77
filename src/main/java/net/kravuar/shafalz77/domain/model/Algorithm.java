package net.kravuar.shafalz77.domain.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.security.InvalidParameterException;

public interface Algorithm {
    boolean compress(BufferedInputStream in, BufferedOutputStream out);
    boolean decompress(BufferedInputStream in, BufferedOutputStream out) throws InvalidParameterException;
}

