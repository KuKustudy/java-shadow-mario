import bagel.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.abs;

/**
 * This is a class that depends on the class ShadowMario, each instance of this class represents a game level in the
 * game ShadowMario. The game currently has three game levels
 * Description: To win each level, player need to reach the end flag. The second level features flying platforms that
 *              the player can jump on to, extra powers such as invincibility and double score. The third level includes
 *              the enemy boss that the player must defeat by shooting fireballs
 */
public class GameLevel {
    private final Properties PROPS;
    private final Properties MESSAGE_PROPS;
    private final int FIREBALL_ATTACK_RANGE = 500;
    private final int LEVEL;

    private boolean isOn = false;
    private boolean gameEnd = false, gameWon = false;

    Map<String, GameMessage> allMessages = new HashMap<>();
    private DrawOptions redOption = new DrawOptions();

    private Platform platform;
    private Player player;
    private EnemyBoss enemyBoss;
    private Endflag endflag;
    private ArrayList<Enemy> allEnemies = new ArrayList<>();
    private ArrayList<Coin> allCoins = new ArrayList<>();
    private ArrayList<FlyingPlatform> allFlyingPlatforms = new ArrayList<>();
    private ArrayList<InvinciblePower> allInvinciblePowers = new ArrayList<>();
    private ArrayList<DoubleScorePower> allDoubleScorePowers = new ArrayList<>();

    //attributes that calculate the elapsed time
    private long startTime, endTime, elapsedTime;
    private long fastestGameWonTime;
    private boolean recordBreak = false;


    /**
     * This is a constructor of the class GameLevel
     * @param levelNumber This is the id of the gameLevel
     * @param props This is a file that contains information for constructing gameEntities of each game level
     * @param message_props This is a file that contains information for constructing gameMessages of each game level
     */
    public GameLevel(int levelNumber, Properties props, Properties message_props){
        this.LEVEL = levelNumber;
        this.PROPS = props;
        this.MESSAGE_PROPS = message_props;
        this.set();
    }

    /**
     * This is a setter method
     * @param on boolean This set the gameLevel's ON status
     */
    public void setOn(boolean on) {
        this.isOn = on;
    }
    /**
     * This is setter method that construct all gameEntities for a game level using CSV file provided for each
     * game level
     */
    public void set(){
        this.startTime = System.nanoTime();
        allEnemies.clear();
        allCoins.clear();
        allFlyingPlatforms.clear();
        allInvinciblePowers.clear();
        allDoubleScorePowers.clear();
        this.gameWon = false;
        this.gameEnd = false;
        this.recordBreak = false;


        allMessages.put("score", new GameMessage("score", MESSAGE_PROPS, PROPS));
        allMessages.put("playerHealth", new GameMessage("playerHealth", MESSAGE_PROPS, PROPS));
        allMessages.put("enemyBossHealth", new GameMessage("enemyBossHealth", MESSAGE_PROPS, PROPS));
        redOption.setBlendColour(255,0,0);

        String text;
        String[] line;
        try (BufferedReader br = new BufferedReader(new FileReader(PROPS.getProperty("level" + this.LEVEL + "File")))){

            while ((text = br.readLine()) != null){

                line = text.split(",");
                double x = Double.parseDouble(line[1]);
                double y = Double.parseDouble(line[2]);

                if (line[0].equals("PLAYER")) {
                    player = new Player(x, y, PROPS);
                } else if (line[0].equals("PLATFORM")){
                    platform = new Platform(x, y, PROPS);
                } else if (line[0].equals("ENEMY")){
                    allEnemies.add(new Enemy(x, y, PROPS));
                } else if (line[0].equals("COIN")){
                    allCoins.add(new Coin(x, y, PROPS));
                } else if (line[0].equals("FLYING_PLATFORM")){
                    allFlyingPlatforms.add(new FlyingPlatform(x, y, PROPS));
                } else if (line[0].equals("INVINCIBLE_POWER")){
                    allInvinciblePowers.add(new InvinciblePower(x, y, PROPS));
                } else if (line[0].equals("DOUBLE_SCORE")){
                    allDoubleScorePowers.add(new DoubleScorePower(x, y, PROPS));
                } else if (line[0].equals("ENEMY_BOSS")){
                    enemyBoss = new EnemyBoss(x, y, PROPS);
                } else if (line[0].equals("END_FLAG")){
                    endflag = new Endflag(x, y, PROPS);
                }
            }
        } catch(FileNotFoundException e){
            System.err.println("CSV file not found: " + "level" + this.LEVEL + "File");
        } catch (IOException e) {
            System.err.println("Error when reading CSV file: " + "level" + this.LEVEL + "File");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    /**
     * This is a getter method
     * @return boolean This returns true if the game level is on, meaning the player is currently playing this game
     *                  level. This returns false otherwise
     */
    public boolean isOn() {
        return this.isOn;
    }


    /**
     * This is a getter method
     * @return boolean This returns true if the game is ended, and returns false otherwise.
     */
    public boolean isGameEnd() {
        return gameEnd;
    }

    /**
     * This is a getter method
     * @return boolean This returns true if player won the game, and returns false otherwise.
     */
    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * Depending on which game level the player is playing, this updates the game in response to the player's keyboard
     * input
     * @param input This is the user/player's keyboard input
     */
    public void play(Input input){
        //implement game level 1
        if (this.LEVEL == 1){
            playLevel1(input);
        }
        if (this.LEVEL == 2){
            playLevel2(input);
        }
        if (this.LEVEL == 3){
            playLevel3(input);
        }

    }


    /**
     * This is where game logic for level 1 applied
     * @param input This is the user/player's keyboard input
     */
    public void playLevel1(Input input){
        if (input.isDown(Keys.RIGHT)){
            moveLevel1EntitiesToRight();
        }

        if (input.isDown(Keys.LEFT)){
            moveLevel1EntitiesToLeft();
        }

        platform.draw();
        drawAllEnemies();

        drawAllCoins();

        winLogicOfEndflag();
        lostLogic();

        player.draw(input);
        endflag.draw();
        allMessages.get("score").drawMessage(player.getScore());
        allMessages.get("playerHealth").drawMessage(player.getHealthPercentage());

    }


    /**
     * This is where game logic for level 2 applied
     * @param input This is the user/player's keyboard input
     */
    public void playLevel2(Input input){
        //implement game level 2
        if (input.isDown(Keys.RIGHT)){
            moveLevel2EntitiesToRight();
        }

        if (input.isDown(Keys.LEFT)){
            moveLevel2EntitiesToLeft();
        }

        platform.draw();
        drawAllEnemies();
        drawAllCoins();
        drawAllFlyingPlatforms();
        drawAllInvinciblePowers();
        drawAllDoubleScorePowers();

        player.draw(input);
        endflag.draw();
        allMessages.get("score").drawMessage(player.getScore());
        allMessages.get("playerHealth").drawMessage(player.getHealthPercentage());

        winLogicOfEndflag();
        lostLogic();
    }


    /**
     * This is where game logic for level 3 applied
     * @param input This is the user/player's keyboard input
     */
    public void playLevel3(Input input){
        //implement game level 3
        if (input.isDown(Keys.RIGHT)){
            moveLevel3EntitiesToRight();
        }

        if (input.isDown(Keys.LEFT)){
            moveLevel3EntitiesToLeft();
        }

        platform.draw();
        drawAllEnemies();
        drawAllCoins();
        drawAllFlyingPlatforms();
        drawAllInvinciblePowers();
        drawAllDoubleScorePowers();

        shootFireballLogic(input);
        drawAllPlayerFireballs();
        drawAllEnemybossFireballs();

        winLogicOfEndflageAndEnemyboss();
        lostLogic();

        player.draw(input);
        enemyBoss.draw();
        endflag.draw();
        allMessages.get("score").drawMessage(player.getScore());
        allMessages.get("playerHealth").drawMessage(player.getHealthPercentage());
        allMessages.get("enemyBossHealth").drawMessage(enemyBoss.getHealthPercentage(), redOption);
    }

    /**
     * This method move all relevant entities to Right in game level 1, this is called when user pressed the key Right
     */
    public void moveLevel1EntitiesToRight(){
        for (Enemy enemy: allEnemies){
            enemy.moveRight();
        }
        for (Coin coin: allCoins) {
            coin.moveRight();
        }
        platform.moveRight();
        endflag.moveRight();
    }

    /**
     * This method move all relevant entities to left in game level 1, this is called when user pressed the key Left
     */
    public void moveLevel1EntitiesToLeft(){
        for (Enemy enemy: allEnemies){
            enemy.moveLeft();
        }
        for (Coin coin: allCoins) {
            coin.moveLeft();
        }
        if (platform.getPositionX() < platform.getPosition().getINITIALX()){
            platform.moveLeft();
        }

        endflag.moveLeft();
    }

    /**
     * This method move all relevant entities to Right in game level 2, this is called when user pressed the key Right
     * Since level 2 includes entities from level 1, it calls moveLevel1EntitiesToRight
     */
    public void moveLevel2EntitiesToRight(){
        moveLevel1EntitiesToRight();
        for (FlyingPlatform flyingPlatform: allFlyingPlatforms){
            flyingPlatform.moveRight();
        }
        for (InvinciblePower invinciblePower: allInvinciblePowers){
            invinciblePower.moveRight();
        }
        for (DoubleScorePower doubleScorePower: allDoubleScorePowers){
            doubleScorePower.moveRight();
        }
    }

    /**
     * This method move all relevant entities to Left in game level 2, this is called when user pressed the key Left
     * Since level 2 includes entities from level 1, it calls moveLevel1EntitiesToLeft
     */
    public void moveLevel2EntitiesToLeft(){
        moveLevel1EntitiesToLeft();
        for (FlyingPlatform flyingPlatform: allFlyingPlatforms){
            flyingPlatform.moveLeft();
        }
        for (InvinciblePower invinciblePower: allInvinciblePowers){
            invinciblePower.moveLeft();
        }
        for (DoubleScorePower doubleScorePower: allDoubleScorePowers){
            doubleScorePower.moveLeft();
        }
    }

    /**
     * This method move all relevant entities to Right in game level 3, this is called when user pressed the key Right
     * Since level 3 includes entities from level 2, it calls moveLevel2EntitiesToRight
     */
    public void moveLevel3EntitiesToRight(){
        moveLevel2EntitiesToRight();
        for (Fireball playerFireball: player.getAllPlayerFireballs()){
            if (playerFireball.isActive()){
                playerFireball.moveRight();
            }
        }
        for (Fireball enemyFireball: enemyBoss.getAllEnemyFireballs()){
            if (enemyFireball.isActive()){
                enemyFireball.moveRight();
            }
        }

        enemyBoss.moveRight();
    }

    /**
     * This method move all relevant entities to Left in game level 3, this is called when user pressed the key Left
     * Since level 3 includes entities from level 1, it calls moveLevel1EntitiesToLeft
     */
    public void moveLevel3EntitiesToLeft(){
        moveLevel2EntitiesToLeft();
        for (Fireball playerFireball: player.getAllPlayerFireballs()){
            if (playerFireball.isActive()){
                playerFireball.moveLeft();
            }
        }
        for (Fireball enemyFireball: enemyBoss.getAllEnemyFireballs()) {
            if (enemyFireball.isActive()) {
                enemyFireball.moveLeft();
            }
        }
        enemyBoss.moveLeft();
    }

    /**
     * This method draws all coins and applied logic that if player currently has the gift of double score, every score
     * collected will be doubled
     */
    public void drawAllCoins(){
        for (Coin coin: allCoins) {
            coin.draw();
            if (!coin.isCollected()
                    && collision(player, coin, player.getRadius() + coin.getRadius())){

                /*when player collected Double Score Power, for every coin collected, score will
                 *be doubled, this score-doubling effect will last for a certain period of time
                 */
                if (player.getActiveDoubleScoreFrames() > 0) {

                    player.gainScore(coin.getCoinValue() * 2);
                } else {
                    player.gainScore(coin.getCoinValue());
                }

                //mark the coin as collected
                coin.hasBeenCollected();
            }
            if (coin.isCollected()) {
                coin.disappearOffScreen();
            }
        }
    }
    /**
     * This method draws all enemies and applied logic that if player currently has the gift of invincibility, every
     * attack received will not cause damage to the player
     */
    public void drawAllEnemies(){
        //draw all enemies
        for (Enemy enemy: allEnemies) {
            enemy.moveHorizontalRandomly();
            if (!enemy.getAlreadyHitPlayerStatus()
                    && collision(player, enemy, player.getRadius() + enemy.getRadius())) {

                /*when the player collected Invincibility Power, any attack from enemy
                 * will not cause harm to the player.
                 */
                if (player.getActiveInvincibilityFrames() == 0) {
                    player.getHurt(enemy.getDAMAGE_SIZE());
                    enemy.hadHitPlayer();
                }

            }
            enemy.draw();
        }
    }

    /**
     * This method draws all flying platform and applied logic for the player to land on the flying platform
     */
    public void drawAllFlyingPlatforms(){
        //draw all flyingPlatforms
        for (FlyingPlatform flyingPlatform: allFlyingPlatforms){
            flyingPlatform.moveHorizontalRandomly();
            //detect whether the player has landed on platform
            if (abs(player.getPositionX() - flyingPlatform.getPositionX()) < flyingPlatform.getHalfLength()
                    && abs(player.getPositionY() - flyingPlatform.getPositionY()) <= flyingPlatform.getHalfHeight()
                    && abs(player.getPositionY() - flyingPlatform.getPositionY()) >= flyingPlatform.getHalfHeight() - 1){

                /*player can only jump on a platform if it is jumping from the normal platform(ground)
                or if the player is jumping from a lower platform to a higher one
                */
                if (player.getCurrOnFlyingPlatform() == null
                        || player.getCurrOnFlyingPlatform().getPositionY() > flyingPlatform.getPositionY() ){

                    //when player land on platform, store its height (playerY) so that latter it can
                    //jump normally on a flying platform
                    player.setInitialPlayerYOnPlatform(player.getPositionY());
                    player.hadLandOnFlyingPlatform();
                    player.setCurrOnFlyingPlatform(flyingPlatform);
                    player.setVerticalSpeed(0);
                }

            }
            flyingPlatform.draw();
        }
    }

    /**
     * This method draws all double score powers and applied logic that once collected by player, player can have the
     * gift of doubling score for a period of time.
     */
    public void drawAllDoubleScorePowers(){
        for (DoubleScorePower doubleScorePower: allDoubleScorePowers){
            doubleScorePower.draw();
            if (!doubleScorePower.isCollected()
                    && collision(player, doubleScorePower, player.getRadius() + doubleScorePower.getRadius())) {
                //if the power has been collected, its effect will last for a certain period of time
                doubleScorePower.hasBeenCollected();
                player.setActiveDoubleScoreFrames(doubleScorePower.getMAXFRAMES());
            }
            if (doubleScorePower.isCollected()) {
                //if power has been collected, move it up
                doubleScorePower.disappearOffScreen();
            }
        }
        if (player.getActiveDoubleScoreFrames() > 0) {
            player.setActiveDoubleScoreFrames(player.getActiveDoubleScoreFrames() - 1);
        }
    }

    /**
     * This method draws all invincible powers and applied logic that once collected by player, player can have the
     * gift of being invincible (receive no damage) for a period of time.
     */
    public void drawAllInvinciblePowers(){
        //draw all invincible Powers and doubleScorePowers
        for (InvinciblePower invinciblePower: allInvinciblePowers){
            invinciblePower.draw();
            if (!invinciblePower.isCollected()
                    && collision(player, invinciblePower, player.getRadius() + invinciblePower.getRadius())) {
                //if the power has been collected, its effect will last for a certain period of time
                invinciblePower.hasBeenCollected();
                player.setActiveInvincibilityFrames(invinciblePower.getMAXFRAMES());
            }
            if (invinciblePower.isCollected()) {
                //if power has been collected, move it up
                invinciblePower.disappearOffScreen();
            }
        }
        if (player.getActiveInvincibilityFrames() > 0) {
            player.setActiveInvincibilityFrames(player.getActiveDoubleScoreFrames() - 1);
        }
    }

    /**
     * This method applied logic for the player and enemyBoss to shoot fireball to each other
     * @param input This is user's keyboard input
     */
    public void shootFireballLogic(Input input){
        //fireball can be shot by player and enemyBoss when they are within a certain distance
        //player can only shoot fireball if enemyBoss still alive
        if (!enemyBoss.isDefeatedStatus()) {

            if ((enemyBoss.getPositionX() - player.getPositionX()) <= FIREBALL_ATTACK_RANGE
                    && (enemyBoss.getPositionX() - player.getPositionX()) >= 0) {
                if(input.wasPressed(Keys.S)){
                    //player is on the left side of the enemyBoss, player should shoot to right
                    player.shootFireball(PROPS, "right");
                }

                enemyBoss.shootFireballRandomly (PROPS, "left");

            } else if ((enemyBoss.getPositionX() - player.getPositionX()) >= -1 * FIREBALL_ATTACK_RANGE
                    && (enemyBoss.getPositionX() - player.getPositionX()) < 0) {
                if(input.wasPressed(Keys.S)){
                    //player is on the right side of the enemyBoss, player should shoot to left
                    player.shootFireball(PROPS, "left");
                }

                enemyBoss.shootFireballRandomly (PROPS, "right");
            }

        }
    }

    /**
     * This method draws all fireballs shot by the enemyboss, and applied logic that if its fireballs hit the player,
     * player will get hurt by the damagesize of the fireball.
     */
    public void drawAllEnemybossFireballs(){
        //draw all fireballs that the enemyBoss had shot
        for (Fireball enemyFireball: enemyBoss.getAllEnemyFireballs()){
            if (enemyFireball.isActive()){
                enemyFireball.draw();

                if (collision(enemyFireball, player, enemyFireball.getRadius() + player.getRadius())) {
                    enemyFireball.setToInactive();

                    player.getHurt(enemyFireball.getDAMAGE_SIZE());

                }
            }

            // According to Ed #451, Once the fireball has left the window, the fireball will simply disappear and
            // there is no longer a need to continue rendering it.
            if (enemyFireball.getPositionX() > Window.getWidth() || enemyFireball.getPositionX() < 0){
                enemyFireball.setToInactive();
            }

            enemyFireball.shoot();
        }
    }

    /**
     * This method draws all fireballs shot by the player, and applied logic that if its fireballs hit the enemyBoss,
     * enemyBoss will get hurt by the damagesize of the fireball.
     */
    public void drawAllPlayerFireballs(){
        //draw all fireballs that the player had shot
        for (Fireball playerFireball: player.getAllPlayerFireballs()){
            if (playerFireball.isActive()){
                playerFireball.draw();

                if (collision(playerFireball, enemyBoss, playerFireball.getRadius() + enemyBoss.getRadius())) {
                    playerFireball.setToInactive();
                    enemyBoss.getHurt(playerFireball.getDAMAGE_SIZE());
                }
            }

            // According to Ed #451, Once the fireball has left the window, the fireball will simply disappear and
            // there is no longer a need to continue rendering it.
            if (playerFireball.getPositionX() > Window.getWidth() || playerFireball.getPositionX() < 0){
                playerFireball.setToInactive();
            }

            playerFireball.shoot();
        }
    }

    /**
     * This method applied the logic that if player reached the endflag, it is considered won the game.
     */
    public void winLogicOfEndflag(){
        //player has reached endflag, player won
        if (collision(player, endflag, player.getRadius() + endflag.getRadius())){
            gameEnd = true;
            gameWon = true;
        }
    }
    /**
     * This method applied the logic that if player reached the endflag and had killed the enemyBoss, it is considered
     * won the game.
     */
    public void winLogicOfEndflageAndEnemyboss(){
        //if enemyBoss has lost all health Point, it is considered defeated, and should "disappear"
        if (enemyBoss.getHealthPercentage() <= 0) {
            enemyBoss.disappearOffScreen();
        }
        if (enemyBoss.getPositionY() > Window.getHeight()) {
            enemyBoss.hadBeenDefeated();
        }
        //if player has reached endflag and defeated the enemyBoss, player won the game
        if (enemyBoss.isDefeatedStatus()
                && collision(player, endflag, player.getRadius() + endflag.getRadius())){
            gameEnd = true;
            gameWon = true;

        }
    }

    /**
     * This method applied the logic that if the player had lost all health point, it is considered lost the game.
     */
    public void lostLogic(){
        //if player has lost all health points, player lost, game over
        if (player.getHealthPercentage() <= 0) {
            player.disappearOffScreen();
        }
        if (player.getPositionY() > Window.getHeight()){
            //recognise the end of game after the player had disappeared
            gameEnd = true;
            gameWon = false;
        }
    }


    /**
     * This is a method that calculates the distance between two game entities, and determine whether they are considered
     * collided.
     * @param entity1 This is gameEntity one
     * @param entity2 This is gameEntity two
     * @param range This is the collision, usually is the sum of the radius of the image of the two Game entities
     * @return This returns true if the distance between two Game Entities is less than their collision range. Else,
     *          return false
     */
    private boolean collision(GameEntity entity1, GameEntity entity2, double range){
        if (entity1.getPosition().distance(entity2.getPosition()) <= range){
            //if distance between two gameEntity <= collision range, they are collided
            return true;
        }
        return false;
    }

    /**
     * This is a method that compare the time used for the player to win with the fastest time used record,
     * hence update the fastest time used record if needed.
     * @return double This returns the fastest time used in seconds
     */
    public Double getFastestGameWonTime(){
        this.endTime = System.nanoTime();
        this.elapsedTime = endTime - startTime;

        if (this.fastestGameWonTime == 0){
            this.fastestGameWonTime = this.elapsedTime;
        }
        if (this.elapsedTime < this.fastestGameWonTime){
            //player breaks the record
            this.fastestGameWonTime = this.elapsedTime;
            this.recordBreak = true;
        }

        //convert elapsedTime to milliseconds, then to seconds
        double elapsedTimeInSeconds = fastestGameWonTime / 1000000.0 / 1000.0;
        return elapsedTimeInSeconds;
    }

    /**
     * This is a method that calculates the time had used by the player to win the game
     * @return double This returns the time used in seconds
     */
    public Double getCurrGameWonTime(){
        this.endTime = System.nanoTime();
        this.elapsedTime = endTime - startTime;

        //convert elapsedTime to milliseconds, then to seconds
        double elapsedTimeInSeconds = elapsedTime / 1000000.0 / 1000.0;
        return elapsedTimeInSeconds;
    }

    /**
     * This a getter method
     * @return boolean This indicates whether the player had broke the record in the game that just played
     */
    public boolean isRecordBreak() {
        return recordBreak;
    }
}
