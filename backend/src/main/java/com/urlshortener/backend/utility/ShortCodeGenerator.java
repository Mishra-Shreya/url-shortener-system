package com.urlshortener.backend.utility;

import com.urlshortener.backend.utility.service.SequenceService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;

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
            System.out.println("dnr = " + dnr[0] + " , " + dnr[1]);
            sb.append(CHARS[dnr[1].intValue()]);
            System.out.println("sb = " + sb);
            id = dnr[0];
        }
        System.out.println("finall sb = " + sb);
        return new String(sb);
    }

    @Override
    public BigInteger generateId(LocalDateTime now){

        int day = now.getDayOfYear();
        String dayf = String.format("%03d", day);
        System.out.println("dayf=" + dayf);
        int year = now.getYear();
        String yearf = String.format("%04d", year);
        System.out.println("yearf=" + yearf);
        String serverId = System.getProperty("server.id");
        String serverIdf = String.format("%03d", Integer.parseInt(serverId));
        System.out.println("serverIdf=" + serverIdf);
        long seq = seqService.getNextSeq();
        String seqf = String.format("%09d", seq);
        System.out.println("seqf=" + seqf);
        String sid =  yearf + dayf +serverIdf + seqf;
        System.out.println("sid=" + sid);
        return new BigInteger(sid);
    }
}
