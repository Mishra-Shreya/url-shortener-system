package com.urlshortener.backend.utility;

import com.urlshortener.backend.utility.service.SequenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Slf4j
@Service
public class ShortCodeGenerator implements IShortCodeGenerator {

    private final SequenceService seqService;

    public ShortCodeGenerator(SequenceService seqService){
        this.seqService = seqService;
    }

    private final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_~".toCharArray();
    private final BigInteger BASE = BigInteger.valueOf(CHARS.length);

    @Override
    public String generateSC(BigInteger id){
        StringBuilder sb = new StringBuilder();
        while(id.compareTo(BigInteger.ZERO) > 0){

            BigInteger[] dnr = id.divideAndRemainder(BASE);
            log.debug("divideAndRemainder[]: d={}, r={}", dnr[0], dnr[1]);
            sb.append(CHARS[dnr[1].intValue()]);
            log.debug("sb={}", sb);
            id = dnr[0];
        }

        log.info("short code={}", sb);
        return new String(sb);
    }

    @Override
    public BigInteger generateId(LocalDateTime now){

        int day = now.getDayOfYear();
        String dayf = String.format("%03d", day);
        log.debug("dayf={}", dayf);
        int year = now.getYear();
        String yearf = String.format("%04d", year);
        log.debug("yearf={}", yearf);
        String serverId = System.getProperty("server.id");
        String serverIdf = String.format("%03d", Integer.parseInt(serverId));
        log.debug("serverIdf={}", serverIdf);
        long seq = seqService.getNextSeq();
        String seqf = String.format("%09d", seq);
        log.debug("seqf={}", seqf);
        String sid =  yearf + dayf +serverIdf + seqf;
        log.info("sid={}", sid);
        return new BigInteger(sid);
    }
}
