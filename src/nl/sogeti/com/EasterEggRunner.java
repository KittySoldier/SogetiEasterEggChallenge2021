package nl.sogeti.com;

import nl.sogeti.logo.SogetiLogoDrawer;

import java.io.IOException;
import java.util.Random;

public class EasterEggRunner {

    public static void main(String[] args) throws InterruptedException {
        EggMetrics eggMetrics = createEggMetrics(); //create egg metrics to be used over entire animation
        EasterEgg.animator.initialiseAnimation(eggMetrics); //initialises animation code

        EasterEgg.drawEgg(eggMetrics); //draws initial egg to the screen

        Random random = new Random(); //creates random for later

        Thread.sleep(3000);// pauses for 3 seconds

        int crackAmount = 5; //set initial crack amount

        for(int currentFrame = 1; currentFrame < 6; currentFrame++){
            //creates cracks in egg
            EasterEgg.animator.prepareCracks(crackAmount, 14, 25, eggMetrics);

            //runs frame specific code:
            if(currentFrame == 4){
                //at frame 4, display main crack
                EasterEgg.animator.createMainCrack(eggMetrics);
            } else if (currentFrame == 5){
                //at frame 5, reveal chicken!
                EasterEgg.animator.chickenDisplay = true;
            }

            //draws egg to screen in new state
            EasterEgg.drawEgg(eggMetrics); //draws egg to screen

            //adds more cracks to next frame
            crackAmount += 3;

            //pauses thread for random amount
            Thread.sleep(1000 + random.nextInt(2000));
        }

        //pauses thread for short while
        Thread.sleep(3000);

        // Please don't change the following code:
        new SogetiLogoDrawer().printSogetiLogo();
    }

    private static EggMetrics createEggMetrics() {
        return new EggMetrics(30, 22, 50, 20, Colors.CYAN.getColor(), Colors.GREEN.getColor());
    }
}
