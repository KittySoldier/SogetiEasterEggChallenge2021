package nl.sogeti.com;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EasterEggAnimator {

    /**
     *
     * this should be called every point to decide what color that specific point should be given
     *
     * @param eggMetrics egg metrics
     * @param xCoordinate current X position
     * @param yCoordinate current Y position
     */
    public void drawAnimatorPoint(EggMetrics eggMetrics, int xCoordinate, int yCoordinate) {
        //gets the pixel status of a particulair coordinate
        int state = crackList.get(yCoordinate).get(xCoordinate);

        //gets the color of a particulair coordinate for direct draw
        Colors color = colorList.get(yCoordinate).get(xCoordinate);

        //if chicken needs to be displayed:
        if(chickenDisplay){
            //removes top half of the egg, not drawing anything above the main crack
            if(isPositionAboveState(xCoordinate, yCoordinate, 2)) {
                //allows direct coloring. any color that is not null is drawn directly to the screen, else background is drawn
                if(color == null){
                    System.out.print(eggMetrics.getBackgroundColor());
                } else {
                    System.out.print(color.getColor());
                }
                //return to avoid all other coloring code
                return;
            }
        }
        //full egg needs to be drawn:

        //check which state which pixel is in:
        //1 = side crack
        //2 = main crack
        //3 = top color
        //4 = middle color
        //anything else uses the egg metric default color
        if(state == 1){
            //place has been selected! draw crack
            System.out.print(Colors.BLACK.getColor());
        } else if(state == 2) {
            System.out.print(Colors.BLACK.getColor());
        } else  if(state == 3) {
            System.out.print(Colors.RED.getColor());
        } else  if(state == 4) {
            System.out.print(Colors.PURPLE.getColor());
        } else {
            System.out.print(eggMetrics.getColor());
        }
    }

    List<List<Integer>> crackList = new ArrayList<>(); //crack list: saves the state for every pixel position weither its a side crack, main crack, or neither

    List<List<Colors>> colorList = new ArrayList<>(); //color list: saves the state for every pixel position. if set to anything other than null, color is drawn to screen when egg is not

    /**
     *
     * returns a boolean whena given position is above a position with a given state number.
     *
     * @param posX x position of current location
     * @param posY y position of current location
     * @param goalState goal state that need to be checked against
     * @return boolean weither the given position is above a position with a given state.
     */

    private boolean isPositionAboveState(int posX, int posY, int goalState){

        //loops though every Y position till a matching state is found
        for(int currentPosY = 0; currentPosY < crackList.size(); currentPosY++){
            int state = crackList.get(currentPosY).get(posX);

            if(state == goalState){
                //if a matching goal state is found, the goal state's y position is checked against current. if above, return true, else return false
                return (posY < currentPosY);
            }
        }

        //no matching states are found
        return false;
    }

    public boolean chickenDisplay = false; //weither to display full egg or broken egg with chicken

    boolean alreadyInitialised = false; //set to true after initialisation. prevents code from running twice

    /**
     *
     * initialise code. must be run before anything else!
     *
     * @param eggMetrics required egg metrics
     */
    void initialiseAnimation(EggMetrics eggMetrics){
        if(alreadyInitialised)return; //animation has already been set up. return

        //create a nested list to store a boolean for every X and Y position
        for(int currentY = 0; currentY < eggMetrics.getFrameHeight(); currentY++){
            List<Integer> stateList = new ArrayList<>(); //create a new list for every Y position
            for(int currentX = 0; currentX < eggMetrics.getFrameWidth(); currentX++){
                //create a boolean for every X position
                stateList.add(0);
            }
            crackList.add(stateList);
        }

        //create a nested list to store a boolean for every X and Y position
        for(int currentY = 0; currentY < eggMetrics.getFrameHeight(); currentY++){
            List<Colors> stateList = new ArrayList<>(); //create a new list for every Y position
            for(int currentX = 0; currentX < eggMetrics.getFrameWidth(); currentX++){
                //create a boolean for every X position
                stateList.add(null);
            }
            colorList.add(stateList);
        }
        //draw sway
        createColorSway(eggMetrics, 30, 4, 2);
        createColorSway(eggMetrics, 20, 3, 2);

        //draw chicken to color list:
        //draw body
        drawCircle(Colors.YELLOW, 53, 44,14);
        drawCircle(Colors.YELLOW, 53, 64,18);
        //draw eyes
        drawCircle(Colors.BLACK, 47, 38,1);
        drawCircle(Colors.BLACK, 59, 38,1);
        //draw beak
        drawLine(Colors.RED, 51, 17, 53, 19);
        drawLine(Colors.RED, 53, 19, 55, 17);
        //draw top feathers
        drawLine(Colors.YELLOW, 51, 10, 55, 13);
        drawLine(Colors.YELLOW, 55, 10, 58, 13);

        //end initialisation
        alreadyInitialised = true;
    }

    /**
     *
     * Calculates the distance between two given points
     *
     * @param posX1 Location start
     * @param posY1 ^
     * @param posX2 Location end
     * @param posY2 ^
     * @return distance between two positions
     */
    private int calculateDistance(int posX1, int posY1, int posX2, int posY2){
       return (int) Math.sqrt(Math.pow((posX1 - posX2), 2) + Math.pow((posY1 - posY2), 2));
    }

    /**
     *
     * draws circle directly to color memory.
     *
     * @param color center position of circle
     * @param centerX center position of circle
     * @param centerY center position of circle
     * @param circleSize circle diameter
     */
    private void drawCircle(Colors color, int centerX, int centerY, int circleSize){
        for(int currentY = 0; currentY < colorList.size(); currentY++){
            for(int currentX = 0; currentX < colorList.get(currentY).size(); currentX++){
                //loops though every position
                if(calculateDistance(centerX, centerY, currentX, (int) (currentY * 2.5)) <= circleSize){
                    colorList.get(currentY).set(currentX, color);
                }
            }
        }
    }

    /**
     *
     * draws a line directly to color memory
     *
     * @param color center position of circle
     * @param startX Location start
     * @param startY ^
     * @param endX Location end
     * @param endY ^
     */
    private void drawLine(Colors color, int startX, int startY, int endX, int endY){
        double slope = ((double) endY - startY) / ((double) endX - startX);
        int offset = (int) (endY - (slope * endX));
        for(int posX = startX; posX <= endX; posX++){
            int posY = (int) (slope * posX + offset);
            colorList.get(posY).set(posX, color);
        }
    }

    /**
     *
     * crack chances: numbers used to determine how cracks can grow
     * VL: Very Low Chance
     * L: Low Chance
     * M: Medium Chance
     * H: High chance
     *
     * see Directions for more information
     *
     */

    static final int VL = 1;
    static final int L = 3;
    static final int M = 5;
    static final int H = 16;

    /**
     *
     * Direction enum: determines which directions side cracks can grow into
     *
     */

    enum Directions {
        LEFT(new int[] {L, 0, L, H}),
        LEFTUP(new int[] {M, 0, VL, H}),
        LEFTDOWN(new int[] {VL, 0, M, H}),
        RIGHT(new int[] {L, H, L, 0}),
        RIGHTUP(new int[] {M, H, VL, 0}),
        RIGHTDOWN(new int[] {VL, H, M, 0});

        /**
         *  chances:
         * VL: Very Low
         * L: Low
         * M: Medium
         * H: High
         *
         *
         *
         LEFT(new int[] {3, 1, 3, 10}),
         LEFTUP(new int[] {5, 1, 1, 10}),
         LEFTDOWN(new int[] {1, 1, 5, 10}),
         RIGHT(new int[] {3, 10, 3, 1}),
         RIGHTUP(new int[] {5, 10, 1, 1}),
         RIGHTDOWN(new int[] {1, 10, 5, 1});


         *         LEFT(new int[] {L, VL, L, H}),
         *         LEFTUP(new int[] {M, VL, VL, H}),
         *         LEFTDOWN(new int[] {VL, VL, M, H}),
         *         RIGHT(new int[] {L, H, L, VL}),
         *         RIGHTUP(new int[] {M, H, VL, VL}),
         *         RIGHTDOWN(new int[] {VL, H, M, VL});
         */

        private int[] availableDirections; //follows compass directions to determine chance of going in certain direction

        Directions(final int[] availableDirections) {
            this.availableDirections = availableDirections;
        }

        public int[] getDirection() {return availableDirections;}

        public int getTotal(){return availableDirections[0] + availableDirections[1] + availableDirections[2] + availableDirections[3];
        }
    }

    /**
     *
     * generates a certain amount of cracks to memory to be displayed on egg next time it is drawn
     *
     * @param amount amount of total cracks added
     * @param minimumSize minimum size per crack
     * @param maximumSize maximum size per crack
     * @param eggMetrics required egg metrics
     */
    public void prepareCracks(int amount, int minimumSize, int maximumSize, EggMetrics eggMetrics){

        Random random = new Random();
        for(int currentIndex = 0; currentIndex < amount; currentIndex++){
            //generate random crack length
            int crackSize = minimumSize + random.nextInt(maximumSize - minimumSize);

            //choose random direction:
            Directions direction = Directions.values()[random.nextInt(Directions.values().length)];


            //select random origin position
            int posX = random.nextInt(eggMetrics.getFrameWidth());
            int posY = random.nextInt(eggMetrics.getFrameHeight());

            for(int crackSegment = 0; crackSegment < crackSize; crackSegment++){
                //save current position into array:
                if(crackList.get(posY).get(posX) != 2){
                    crackList.get(posY).set(posX, 1);
                }

                //seek new offset for next position:
                int chosenDirection = random.nextInt(direction.getTotal());


                boolean running = true;
                int directionOffset = 0; //compass direction: 0 = north, 1= east, 2 = south, 3 = west
                while(running && directionOffset < 4){

                    //check if direction has any chance:
                    if(direction.getDirection()[directionOffset] > 0) {
                        //direction has above zero chance to be used
                        //deduct direction chance from randomized number:
                        chosenDirection -= direction.getDirection()[directionOffset];
                    }

                    //if chosen direction is below 0, a direction has been chosen and we can leave the while loop
                    if(chosenDirection < 0){
                        running = false; //direction has been chosen!
                    } else {
                        //direction was not chosen. check next direction:
                        directionOffset++;
                    }
                }

                //use next direction to create offset!
                switch(directionOffset){
                    case(0):posY = boundsCheck(posY - 1, 0, eggMetrics.getFrameHeight() - 1); break; //go upwards. remove one from Y as long as it keeps within bound
                    case(1):posX = boundsCheck(posX + 1, 0, eggMetrics.getFrameWidth() - 1); break;//go Right. add one from X as long as it keeps within bound
                    case(2):posY = boundsCheck(posY + 1, 0, eggMetrics.getFrameHeight() - 1); break;//go downwards. add one from Y as long as it keeps within bound
                    case(3):posX = boundsCheck(posX - 1, 0, eggMetrics.getFrameWidth() - 1); break;//go Left. remove one from X as long as it keeps within bound
                }
            }
        }
    }


    /**
     *
     * creates main crack in egg. also strips egg in half
     *
     * @param eggMetrics required egg metrics
     */
    public void createMainCrack(EggMetrics eggMetrics){
        //start at pos X 0, randomly go up or down but never 2+ above origin pos y
        int originY = 24;
        int boundLimit = 3;

        int posX = 0;
        int posY = originY;

        Random random = new Random();

        while(posX < eggMetrics.getFrameWidth()){
            //while no full crack has been drawn, draw crack!~
            crackList.get(posY).set(posX, 2);

            if(posX % 2 == 0){
                posY = boundsCheck(posY + (-1 + random.nextInt(3)), originY - boundLimit, originY + boundLimit);
            }
            posX++;
        }
    }

    /**
     * Creates color spray. sets sway line to certain state and then sets all positions above to the new state also!
     *
     * @param eggMetrics required egg metrics
     * @param startY start position of line
     * @param crackState what state to set each pixel to
     * @param boundLimit what upper and lower limit the y position should sway into
     */

    private void createColorSway(EggMetrics eggMetrics, int startY, int crackState, int boundLimit){
        //start at pos X 0, sway up and down between bounds
        int posX = 0;
        int posY = startY;
        boolean goingUp = true;

        while(posX < eggMetrics.getFrameWidth()){
            //while no full crack has been drawn, draw crack!~
            for(int lineY = posY; lineY > 0; lineY--){
                crackList.get(lineY).set(posX, crackState);

            }

            if(posX % 2 == 0){
                if(goingUp){
                    posY = boundsCheck(posY + 1, startY - boundLimit, startY + boundLimit);
                } else {
                    posY = boundsCheck(posY - 1, startY - boundLimit, startY + boundLimit);
                }

                if(posY == startY + boundLimit){
                    //top boundery reached!
                    goingUp = false;
                } else if(posY == startY - boundLimit){
                    //bottom boundery reached!
                    goingUp = true;
                }
            }
            posX++;
        }
    }

    /**
     *
     * outputs the input, shifted to fit within bounds given
     * @param input input given
     * @param minimum the minimum bounds the input needs to have. input is shifted up to fit this number
     * @param maximum the maximum bounds the input needs to have. input is shifted down to fit this number
     * @return int that is within the minimum and maximum bounds given. int is shifted up or down to fit within the bounds
     */
    private int boundsCheck(int input, int minimum, int maximum){
        return Math.max(Math.min(maximum, input), minimum);
    }




}
