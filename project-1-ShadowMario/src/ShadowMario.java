import bagel.*;
import java.util.Properties;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 1, 2024
 *
 * Please enter your name below
 * @author Wingyee He (1461821)
 */

public class ShadowMario extends AbstractGame {
    //read Properties files here, so that it can be used to reset the game
    private final static Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
    private final static Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");

    private final Image BACKGROUND_IMAGE, PLATFORM_IMAGE, ENDFLAG_IMAGE;
    private final Image PLAYER_IMAGE_RIGHT, PLAYER_IMAGE_LEFT;
    private final Image ENEMY_IMAGE, COIN_IMAGE;

    private final Font TITLE_FONT, MESSAGE_FONT, INSTRUCTION_FONT, SCORE_FONT, HEALTH_FONT;

    private final double INITIAL_HEALTH;

    private static boolean gameStartFlag, gameEndFlag, gameWinStatus;
    private boolean playerFaceRightStatus;

    private double gameScore, healthPoint;
    private double coinValue, enemyDamageSize;
    private double enemySpeed, coinSpeed, coinVerticalSpeed, platformSpeed, endFlagSpeed, playerVerticalSpeed;
    private final double enemyCollisionRange, coinCollisionRange, endFlagCollisionRange;

    private final int enemyCount, coinCount;

    //Below are positions of entities that "don't move", its position won't change
    private final double titleX, titleY, messageY, instructionY, scoreX, scoreY, healthX, healthY;
    //Below are positions of entities that will "move" during the game
    private double playerX, playerY, initialPlayerX, initialPlayerY;
    private double platformX, platformY, initialPlatformX;
    private double enemyX, enemyY, coinX, coinY, endFlagX, endFlagY;

    private final String title, instruction, gameOverMessage, gameWonMessage;

    private int csvNumRow = 50, csvNumCol = 3;
    // a 50x3 string matrix storing data received from csv file
    // E.g row 3: ["Enemy", "500", "600"]
    private String[][] entitiesAndPositions = new String[csvNumRow][csvNumCol];
    private double[][] allEnemyPositions, allCoinPositions;
    private double[] allEnemyDamageSizes, allCoinValues;


    /**
     * The constructor
     */
    public ShadowMario(Properties game_props, Properties message_props) {
        super(Integer.parseInt(game_props.getProperty("windowWidth")),
              Integer.parseInt(game_props.getProperty("windowHeight")),
              message_props.getProperty("title"));

        BACKGROUND_IMAGE = new Image(game_props.getProperty("backgroundImage"));
        PLAYER_IMAGE_RIGHT = new Image(game_props.getProperty("gameObjects.player.imageRight"));
        PLAYER_IMAGE_LEFT = new Image(game_props.getProperty("gameObjects.player.imageLeft"));
        PLATFORM_IMAGE = new Image(game_props.getProperty("gameObjects.platform.image"));
        ENEMY_IMAGE = new Image(game_props.getProperty("gameObjects.enemy.image"));
        COIN_IMAGE = new Image(game_props.getProperty("gameObjects.coin.image"));
        ENDFLAG_IMAGE = new Image(game_props.getProperty("gameObjects.endFlag.image"));
        TITLE_FONT = new Font(game_props.getProperty("font"),
                Integer.parseInt(game_props.getProperty("title.fontSize")));
        INSTRUCTION_FONT = new Font(game_props.getProperty("font"),
                Integer.parseInt(game_props.getProperty("instruction.fontSize")));
        MESSAGE_FONT = new Font(game_props.getProperty("font"),
                                Integer.parseInt(game_props.getProperty("message.fontSize")));
        SCORE_FONT = new Font(game_props.getProperty("font"),
                                Integer.parseInt(game_props.getProperty("score.fontSize")));
        HEALTH_FONT = new Font(game_props.getProperty("font"),
                Integer.parseInt(game_props.getProperty("score.fontSize")));

        INITIAL_HEALTH = Double.parseDouble(game_props.getProperty("gameObjects.player.health")); // value 1

        scoreX = Double.parseDouble(game_props.getProperty("score.x"));
        scoreY = Double.parseDouble(game_props.getProperty("score.y"));
        healthX = Double.parseDouble(game_props.getProperty("health.x"));
        healthY = Double.parseDouble(game_props.getProperty("health.y"));
        titleX = Double.parseDouble(game_props.getProperty("title.x"));
        titleY = Double.parseDouble(game_props.getProperty("title.y"));
        messageY = Double.parseDouble(game_props.getProperty("message.y"));
        instructionY = Double.parseDouble(game_props.getProperty("instruction.y"));

        title = message_props.getProperty("title");
        instruction = message_props.getProperty("instruction");
        gameOverMessage = message_props.getProperty("gameOver");
        gameWonMessage = message_props.getProperty("gameWon");

        enemyDamageSize = Double.parseDouble(game_props.getProperty("gameObjects.enemy.damageSize"));
        enemySpeed = Double.parseDouble(game_props.getProperty("gameObjects.enemy.speed"));
        enemyCount = Integer.parseInt(game_props.getProperty("gameObjects.enemy.enemyCount"));
        enemyCollisionRange = Double.parseDouble(game_props.getProperty("gameObjects.player.radius"))
                + Double.parseDouble(game_props.getProperty("gameObjects.enemy.radius"));

        coinValue = Double.parseDouble(game_props.getProperty("gameObjects.coin.value"));
        coinCount = Integer.parseInt(game_props.getProperty("gameObjects.coin.coinCount"));
        coinSpeed = Double.parseDouble(game_props.getProperty("gameObjects.coin.speed"));
        coinCollisionRange = Double.parseDouble(game_props.getProperty("gameObjects.player.radius"))
                + Double.parseDouble(game_props.getProperty("gameObjects.coin.radius"));

        platformSpeed = Double.parseDouble(game_props.getProperty("gameObjects.platform.speed"));

        endFlagSpeed = Double.parseDouble(game_props.getProperty("gameObjects.endFlag.speed"));
        endFlagCollisionRange = Double.parseDouble(game_props.getProperty("gameObjects.player.radius"))
                + Double.parseDouble(game_props.getProperty("gameObjects.endFlag.radius"));

        //above are variables that don't change during the game
        //therefore no need to reset

        this.reset(game_props, message_props);
    }
    public void reset(Properties game_props, Properties message_props) {
        //some boolean variables help to decide when to reset
        gameStartFlag = false;
        gameEndFlag = false;
        playerFaceRightStatus = true;

        gameScore = 0;
        playerVerticalSpeed = 0;
        coinVerticalSpeed = 0;

        healthPoint = Double.parseDouble(game_props.getProperty("gameObjects.player.health"));

        String text;
        int enemyEncountered = 0, coinEncountered = 0;
        //2 represent the two number x and y that composite the position
        allEnemyPositions = new double[enemyCount][2];
        allEnemyDamageSizes = new double[enemyCount];
        allCoinPositions = new double[coinCount][2];
        allCoinValues =  new double[coinCount];
        //take position (x, y) data for entities from level1.csv
        try (BufferedReader br = new BufferedReader(new FileReader(game_props.getProperty("levelFile")))) {
            int i = 0;
            while((text = br.readLine()) != null) {
                entitiesAndPositions[i] = text.split(",");

                if (entitiesAndPositions[i][0].equals("PLATFORM")) {
                    platformX = Double.parseDouble(entitiesAndPositions[i][1]);
                    platformY = Double.parseDouble(entitiesAndPositions[i][2]);
                    initialPlatformX = platformX;
                }
                if (entitiesAndPositions[i][0].equals("PLAYER")) {
                    initialPlayerX = Double.parseDouble(entitiesAndPositions[i][1]);
                    initialPlayerY = Double.parseDouble(entitiesAndPositions[i][2]);
                }
                if (entitiesAndPositions[i][0].equals("ENEMY")){
                    allEnemyPositions[enemyEncountered][0] = Double.parseDouble(entitiesAndPositions[i][1]);
                    allEnemyPositions[enemyEncountered][1] = Double.parseDouble(entitiesAndPositions[i][2]);
                    allEnemyDamageSizes[enemyEncountered] = enemyDamageSize;
                    enemyEncountered += 1;
                }
                if (entitiesAndPositions[i][0].equals("COIN")) {
                    allCoinPositions[coinEncountered][0] = Double.parseDouble(entitiesAndPositions[i][1]);
                    allCoinPositions[coinEncountered][1] = Double.parseDouble(entitiesAndPositions[i][2]);
                    allCoinValues[coinEncountered] = coinValue;
                    coinEncountered += 1;
                }
                if (entitiesAndPositions[i][0].equals("END_FLAG")){
                    endFlagX = Double.parseDouble(entitiesAndPositions[i][1]);
                    endFlagY = Double.parseDouble(entitiesAndPositions[i][2]);
                }
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        playerX = initialPlayerX;
        playerY = initialPlayerY;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {

        ShadowMario game = new ShadowMario(game_props, message_props);
        game.run();

    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        // close window
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        /* There are two possible intentions when the user press 'SPACE',
        * One is to start the game, screen should change from START page to GAME page
        * Another one is to reset the game, screen should change from END page to START page
        */
        if (input.wasPressed(Keys.SPACE)){

            /*if a game just ended, the user is looking to reset the game
             * reset flag status, so that START page can be shown
             */
            if (gameStartFlag == true && gameEndFlag == true) {
                gameStartFlag = false;
                gameEndFlag = false;
            /*else if the player is looking to start the game, reset everything,
             * and change flag status to show the GAME page.
             */
            } else if (gameStartFlag == false && gameEndFlag == false){
                reset(game_props, message_props);
                gameStartFlag = true;
                gameEndFlag = false;
            }
        }


        //if the game has not started yet, load the start page
        if (gameStartFlag == false) {
            //divide window width by 3 to make it roughly fit into the middle
            TITLE_FONT.drawString(title, titleX, titleY);
            INSTRUCTION_FONT.drawString(instruction, Window.getWidth() / 3.0, instructionY);
        }

        //if the game has started and the player has not won or lost (the game not end yet), continue the game
        if (gameStartFlag == true && gameEndFlag == false) {

            //Position of entities changed according to the motion directed by the player of the game
            //moveing entities include: enemies, coins, platform, endflag
            if (input.isDown(Keys.RIGHT)){
                playerFaceRightStatus = true;
                for (int i = 0; i < enemyCount; i++){
                    allEnemyPositions[i][0] += -1 * enemySpeed;
                }
                for (int i = 0; i < coinCount; i++){
                    allCoinPositions[i][0] += -1 * coinSpeed;
                }
                platformX += -1 * platformSpeed;
                endFlagX += -1 * endFlagSpeed;
            }

            if (input.isDown(Keys.LEFT)){
                playerFaceRightStatus = false;
                for (int l = 0; l < enemyCount; l++){
                    allEnemyPositions[l][0] += enemySpeed;
                }
                for (int l = 0; l < coinCount; l++){
                    allCoinPositions[l][0] += coinSpeed;
                }
                if (platformX < initialPlatformX){
                    platformX += platformSpeed;
                }
                endFlagX += endFlagSpeed;
            }

            //if the player wants to move up in vertical direction, change the position of player only
            if (input.wasPressed(Keys.UP)) {
                playerVerticalSpeed = -20;
                playerY += playerVerticalSpeed;
            }
            if (playerY < initialPlayerY){
                //during player's jumping motion (the player has not touched ground yet),
                //vertical speed should increase by 1 each frame
                playerVerticalSpeed += 1;
                playerY += playerVerticalSpeed;
            } else if (playerY == initialPlayerY){
                //once the player touched ground, vertical speed should back to 0
                playerY = initialPlayerY;
                playerVerticalSpeed = 0;
            }

            //draw all enemies
            for (int i = 0; i < enemyCount; i++){
                enemyX = allEnemyPositions[i][0];
                enemyY = allEnemyPositions[i][1];
                ENEMY_IMAGE.draw(enemyX, enemyY);
                //if player collide with an enemy, decrease health point, and that enemy should no more
                //contain damage size
                if (distance(enemyX, enemyY, playerX, playerY) <= enemyCollisionRange){
                    healthPoint -= allEnemyDamageSizes[i];
                    allEnemyDamageSizes[i] = 0;
                }
            }

            //draw all coins
            for (int j = 0; j < coinCount; j++){
                coinX = allCoinPositions[j][0];
                coinY = allCoinPositions[j][1];
                //if a coin is collided with the player, increase gameScore
                if (distance(coinX, coinY, playerX, playerY) <= coinCollisionRange){
                    gameScore += allCoinValues[j];
                    allCoinValues[j] = 0;
                }
                //if coin has been collided by the player, it should move upwards, and disappear off screen
                if (allCoinValues[j] == 0){
                    coinVerticalSpeed = -10;
                    allCoinPositions[j][1] += coinVerticalSpeed;
                }
                COIN_IMAGE.draw(coinX, coinY);
            }

            PLATFORM_IMAGE.draw(platformX, platformY);

            //draw the player with the correct facing direction
            if (playerFaceRightStatus == true) {
                PLAYER_IMAGE_RIGHT.draw(playerX, playerY);
            } else {
                PLAYER_IMAGE_LEFT.draw(playerX, playerY);
            }

            ENDFLAG_IMAGE.draw(endFlagX, endFlagY);
            SCORE_FONT.drawString("SCORE " + (int)gameScore, scoreX, scoreY);
            HEALTH_FONT.drawString("HEALTH" + " " + (int)((healthPoint/INITIAL_HEALTH) * 100), healthX, healthY);

            //detect whether the game should end or not (lose condition)
            if (healthPoint <= 0){
                //if health point <= 0, the player move vertically down off the screen, then the game ends
                if (playerY <= Window.getHeight()){
                    playerVerticalSpeed = 2;
                    playerY += playerVerticalSpeed;
                    if (playerFaceRightStatus == true) {
                        PLAYER_IMAGE_RIGHT.draw(playerX, playerY);
                    } else {
                        PLAYER_IMAGE_LEFT.draw(playerX, playerY);
                    }
                }
                //wait until player disappear from screen, then we end the game and display the game over message
                if (playerY > Window.getHeight()){
                    gameEndFlag = true;
                    gameWinStatus = false;
                }

            }

            //detect whether the game should end or not (win condition)
            if (distance(endFlagX, endFlagY, playerX, playerY) <= endFlagCollisionRange){
                gameWinStatus = true;
                gameEndFlag = true;
            }
        }


        //if the game ended, display message according to the result of the game
        if (gameStartFlag == true && gameEndFlag == true){
            if (gameWinStatus == true){
                MESSAGE_FONT.drawString(gameWonMessage, Window.getWidth() / 3.0, messageY);
            } else {
                MESSAGE_FONT.drawString(gameOverMessage, Window.getWidth() / 3.0, messageY);
            }
        }
    }

    //a method created to calculate the distance between two point (x1, y1), (x2, y2)
    public double distance(double x1, double y1, double x2, double y2) {
        double distance;
        distance = Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
        return distance;
    }
}
