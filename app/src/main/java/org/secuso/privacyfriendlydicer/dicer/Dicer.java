package org.secuso.privacyfriendlydicer.dicer;

import java.security.SecureRandom;

/**
 * Created by yonjuni on 5/6/15.
 */
public class Dicer {
    private static final SecureRandom random = new SecureRandom();

    public int[] rollDice(int poolSize, int faceNum){
        int[] dice = new int[poolSize];

        for (int i=0;i<dice.length;i++){
            dice[i] = random.nextInt(faceNum) +1;
        }

        return dice;
    }
}
