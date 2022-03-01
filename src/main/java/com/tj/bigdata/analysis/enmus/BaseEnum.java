package com.tj.bigdata.analysis.enmus;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Stream;

//@NoArgsConstructor
@AllArgsConstructor
public  class     BaseEnum {
    public static void main(String[] args) {

        int value = 1;

        ChainType chainType1 = Arrays.stream(ChainType.values()).filter(chainType -> chainType.getCode() == value).findAny().orElse(null);
        System.out.println(chainType1.getCode());
        System.out.println(chainType1.getValue());
//        Stream<ChainType> chainTypeStream = (Stream<ChainType>) chainType1

//        for (ChainType value : ChainType.values()) {
//
//            System.out.println(value.getCode());
//            System.out.println(value.getValue());
//        }
    }

}
