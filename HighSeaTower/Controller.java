/*
 * Dimanche 5 avril, TP2 IFT1025 - HighSeaTower - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Notre controlleur fait le pont entre HighSeaTower (la vue) et la logique de
 * notre jeu. La plupart des méthodes de cette classe servent à faire le lien
 * entre les classes Level et HighSeaTower.
 *
 * Nous avons mis un boolean space car le background pour l'espace est une image
 * et celui pour le mode normal est un rectangle (ce qui alourdi un peu notre
 * code mais permet les deux modes).
 */


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class Controller {

    private final int width;
    private final int height;

    private boolean space = false;  //Mode espace
    private boolean debug = false;  //Debug Mode
    private boolean dead = false;   //Jumper mort
    private boolean inPlay = false; //Jeu en cours ou pas

    private Level level;     // Niveau actuel du jeu
    private Jumper jumper;   // info sur les Coordonnées du Jumper



    /**
     * Constructeur
     * @param highSeaTower la vue, qui se passe elle-même en paramètre
     *                     lors de la construction du controller
     */
    public Controller(HighSeaTower highSeaTower){
        this.width = highSeaTower.getWIDTH();
        this.height = highSeaTower.getHEIGHT();

        this.restartGame();
    }



    /**
     * Réinitialise les valeurs de base.
     */
    public void reInit(){
        this.inPlay = false;
        this.debug = false;
        this.dead = false;
    }



    /**
     * Recommence la partie.
     */
    public void restartGame(){

        this.reInit();

        if (space){
            this.level = new SpaceLevel(width, height);
        } else {
            this.level = new SeaLevel(width, height);
        }

        this.inPlay = true;
    }



    /**
     * Met le level en action.
     */
    public void startGame(){
        if (!debug) {
            this.level.startGame();
        }
    }



    /**
     * Update des différents éléments du jeu
     * @param dt        delta time entre chaque update
     * @param context   canevas pour dessiner dessus
     */
    public void updateLevel(double dt, GraphicsContext context){
        if (inPlay) {

            // Mise à jour du level et du jumper
            this.level.update(dt);

            //Update les coordonnées du Jumper au Controller
            this.jumper = level.getJumper();


            // Dessine le tableau
            this.drawLevel(context);

            //Si on est mort
            this.dead = this.level.getDead();
            if (dead) {
                this.inPlay = false;
            }


        }
    }



    /**
     * Commandes pour le contrôle du personnage.
     */

    public void jump(){
        this.level.jump();
    }

    public void right(){
        this.level.right();
    }

    public void stop(){
        this.level.stop();
    }

    public void left(){
        this.level.left();
    }



    /**
     * Setters pour différents modes de jeu  Space / Normal / Debug
     */

    public void setDebugMode(){
        this.debug = !this.debug;
        this.level.debugMode();
    }

    public void setSpaceMode(){
        this.space = true;
        this.restartGame();
    }

    public void setNormalMode(){
        this.space = false;
        this.restartGame();
    }



    /**
     * Getters divers
     */

    public boolean getDebug(){
        return this.debug;
    }

    public boolean getDead(){
        return this.dead;
    }

    public int getHighScore(){
        return this.level.getFenetreY();
    }



    /**
     * Getter pour les informations à être affichées pour le mode debug.
     * @return L'info en debug fournie sous forme de tableau.
     * L'info selon la position dans le tableau est:
     *     0: position en x, 1: position en y,
     *     2: vitesse en x, 3: vitesse en y,
     *     4: accélération en x, 5: accélération en y,
     *     6: vaut 0 si ne touche pas le sol, 1 si touche le sol.
     */
    public int[] getDebugInfo(){

        int[] debugInfo = new int[7];

        debugInfo[0] = (int) Math.floor(this.jumper.getX());
        debugInfo[1] = (int) Math.floor(this.jumper.getY());
        debugInfo[2] = (int) Math.floor(this.jumper.getVx());
        debugInfo[3] = (int) Math.floor(this.jumper.getVy());
        debugInfo[4] = (int) Math.floor(this.jumper.getAx());
        debugInfo[5] = (int) Math.floor(this.jumper.getAy());

        if (this.jumper.getParterre()){
            debugInfo[6] = 1;
        } else {
            debugInfo[6] = 0;
        }

        //Si y<=0 vy = 0
        if (this.jumper.getY() <= 0){
            debugInfo[3] = 0;
        }

        return debugInfo.clone();
    }



    /**
     * Dessine le level et tous ses éléments.
     * Nous avons mis cette méthode dans le controller puisqu'elle fait appel
     * à des éléments graphiques et à des éléments de logique du jeu à la fois,
     * mais nous avons voulu éviter que les classes du jeu se dessinent elles-même.
     * @param context   canevas sur lequel on veux dessiner
     */
    private void drawLevel(GraphicsContext context){

        // Position où le bas de la fenêtre est rendue
        double fenetreY = level.getFenetreY();


        // *** Background ***

        // image pour space, mais un rectangle pour mode normal
        if (space){
            context.drawImage(level.getImage(), 0,
                    0, level.getWidth(), level.getHeight());
        } else {
            context.setFill(level.getColor());
            context.fillRect(0, 0, level.getWidth(), level.getHeight());
        }


        // *** Bulles ***
        Bubbles[] bubbles = level.getBubbles();

        for (Bubbles b : bubbles){
            context.setFill(b.getColor());
            context.fillOval(b.getX(),b.getY(),b.getLarg(),b.getHaut());
        }


        // *** Plateformes ***
        double pltY;

        for (Plateforme plt : level.getPlateformes()){

            //calcul de la position y pour l'affichage
            pltY = this.height - (plt.getY() - fenetreY);

            //images pour mode space, rectangles pour mode normal
            if (space){
            context.drawImage(plt.getImage(), plt.getX(),
                    pltY, plt.getLarg(), plt.getHaut());
            }

            else if (debug && plt.getJumperOn()){
                context.setFill(Color.YELLOW);
                context.fillRect(plt.getX(), pltY, plt.getLarg(), plt.getHaut());
            }

            else {
                context.setFill(plt.getColor());
                context.fillRect(plt.getX(), pltY, plt.getLarg(), plt.getHaut());
            }
        }


        // *** Jumper ***

        //calcul de la position y pour l'affichage
        double jumperY = this.height - (this.jumper.getY() - fenetreY);

        // Image, X, Y, Larg, Haut
        context.drawImage(this.jumper.getImage(),
                this.jumper.getX(),
                jumperY - this.jumper.getHaut(),
                this.jumper.getLarg(),
                this.jumper.getHaut());

        // Carré rouge lors du mode DEBUG
        if (debug){
            context.setFill(Color.rgb(255,0,0, 0.4));
            context.fillRect(this.jumper.getX(),
                    jumperY - this.jumper.getHaut(),
                    this.jumper.getLarg(),
                    this.jumper.getHaut());
        }
    }
}
