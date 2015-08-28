package privacyfriendlydicer.secuso.informatik.tudarmstadt.de.privacyfriendlydicer;

/**
 * Created by yonjuni on 5/6/15.
 */
public class Dicer {

    public int[] rollDice(int poolSize){
        int[] dice = new int[poolSize];

        for (int i=0;i<dice.length;i++){
            dice[i] = (int) (Math.random() * 6) + 1;
        }
        return dice;
    }

}
