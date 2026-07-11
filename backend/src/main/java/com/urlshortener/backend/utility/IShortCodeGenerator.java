package com.urlshortener.backend.utility;

import java.math.BigInteger;
import java.time.LocalDateTime;

public interface IShortCodeGenerator {

    BigInteger generateId(LocalDateTime now);
    String generateSC(BigInteger id);
}
