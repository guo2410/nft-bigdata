package com.tj.bigdata.analysis.util;

import java.util.Random;

public class RandomUtils {

    public static String getStringRandom(int length) {
        Random random = new Random();
        String val = "";
        for (int i = 0; i < length; i++) {
            //下一个数字是字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equals(charOrNum)) {
                //产生一个字母
                //产生一个asci码值   65-90是大写，97-122是小写
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                //总共有26个字母，产生26以内的数字+asci码值的开头就可以，随机产生大小写字母了
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equals(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    public static Long getLongRandom(int min, int max) {
        long randomNum = System.currentTimeMillis();
        Long val = (Long) (randomNum % (max - min) + min);
        return val;
    }

    public static void main(String args[]) {
        String randomStr = RandomUtils.getStringRandom(14);
        String tokenTemp = SymmetricEncoderUtils.AESEncode("AES", "caoyue" + "_" + randomStr + "_" + "24448");
        System.out.println(tokenTemp);

//    String randomStr = RandomUtils.getStringRandom(14);
//    String aes = SymmetricEncoderUtils.AESEncode("AES", "15517803291" + "_" + randomStr + "_" + 17371L);
//    System.out.println(aes);
    }
}
