/*
 * Dimanche 5 avril, TP2 IFT1025 - HighSeaTower - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Classe abstraite qui donne le squelette de la façon dont nous voulons que le
 * jeu se déroule. Contient la physique de base et éléments "essentiels dans
 * un tableau".
 *
 * Voici notre logique pour le tableau:
 * Le y = 0 est tout en bas du level.
 * Le fenetreY indique la position y du BAS de ce qui est affiché.
 * Il y a une vitesse de défilement de ce qu'on voit;
 * les pateformes ont une position fixe n'ont pas de vitesse.
 * On crée un nombre de plateformes fixe (que l'on a calculé à 6 selon la hauteur de l'affichage).
 * Les plateformes sont donc stoquées dans un tableau de taille fixe;
 * quand une plateforme est trop basse, on change ses paramètres (couleur,
 * position, etc.), mais c'est le même objet.
 * lastPltY et isLastPltRed permettent de bien générer la prochaine plateforme.
 * (NB: plateforme trop basse = 50 px sous fenetreY, re: prendre en compte hauteur du jumper)
 */


import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public abstract class Level {

    protected Bubbles[] bubbles = new Bubbles[0];
    protected Image image; //image de background
    protected Color color; //couleur de background
    protected Jumper jumper;
    private final Plateforme[] plateformes;

    protected final double width;
    protected final double height;
    private double lastPltY = 0; //position y de la dernière plateforme générée
    private double fenetreY = 0; //position Y du bas de l'affichage
    private double vitDefilBase = 50;
    private double tempsTotal= 0;

    private boolean moves = false; //sert pour mode debug ou le level ne bouge pas
    private boolean started = false;
    private boolean accelerate = false; //sert pour les plateformes accélérantes
    private boolean timer = true;
    private boolean isLastPltRed = false; //pour éviter deux plateformes rouges d'affilé
    private boolean dead = false;



    /**
     * Constructeur de Level
     * @param w     Largeur du tableau
     * @param h     Hauteur du tableau
     */
    public Level(double w, double h) {
        this.width = w;
        this.height = h;

        // 6 plateformes suffisantes pour la hauteur 480 px
        this.plateformes = new Plateforme[6];

        for (int i = 0; i<this.plateformes.length; i++){
            this.plateformes[i] = new Plateforme(this.lastPltY + 100, nextPlatFormeType());
            this.lastPltY += 100;
        }

    }



    /**
     * Début du jeu
     */
    public void startGame(){
        this.started = true;
        this.moves = true;
    }



    /**
     * Update pour tout le level - chaque level prend en charge ses propres
     * plateformes, ses bubbles, et le jumper.
     * @param dt        delta time entre les updates
     */
    public void update(double dt) {

        if (!started || dead)
            return;

        tempsTotal += dt;

        // Remise à la valeur par défaut
        this.accelerate = false;

        // Update de chaque élément du level avec méthodes privées
        this.jumperUpdate(dt);
        this.plateformesUpdate();
        this.defilUpdate(dt);
        this.bullesUpdate(dt);

    }



    /**
     * Updates reliées au jumper
     * @param dt delta time entre les updates
     */
    private void jumperUpdate(double dt) {
        jumper.update(dt, (int)width);

        // Test s'il tombe trop bas
        if (jumper.getY() + jumper.getHaut() < this.fenetreY){
            this.dead = true;
        }
    }



    /**
     * Updates reliées aux plateformes
     */
    private void plateformesUpdate() {

        for (Plateforme p : plateformes) {

            // Test collision du jumper avec les platformes
            boolean collision = jumper.testCollision(p);
            boolean upCollision = jumper.testUpCollision(p);

            // Si collision, informe la plateforme que le jumper la touche
            p.setJumperOn(collision || upCollision);

            // Lorsque la platforme est trop basse, on change ses paramètres
            // Voir la logique dans le premier commentaire de la classe
            if (p.getY() < fenetreY - jumper.getHaut()){
                p.makePlatForme(this.lastPltY + 100, nextPlatFormeType());
                this.lastPltY += 100;
            }
        }

    }



    /**
     * Updates reliées au défilement de la fenêtre
     * @param dt delta time entre les updates
     */
    private void defilUpdate(double dt){

        // Valeur de base de la vitesse de défilement avec accélération.
        // Pas d'accélération pendant que moves = false (i.e. en mode debug).
        if (this.moves) {
            this.vitDefilBase += dt * 2;
        }

        //vitesse de défilement de l'affichage
        double vitDefil = vitDefilBase;

        // Si le jumper est sur une plateforme accélérante
        if (this.accelerate) {
            vitDefil *= 3;
        }

        // Si le jumper arrive a 75% on defile le tableau
        if ((jumper.getVy()) > 0 &&
                (jumper.getY() > this.fenetreY + this.height*0.75)) {
            vitDefil = jumper.getVy();
            this.fenetreY += vitDefil * dt;
        }

        else {
            // défilement normal du tableau
            if (this.moves) {
                this.fenetreY += vitDefil * dt;
            }
        }

    }



    /**
     * Updates reliées aux bulles
     * @param dt delta time entre les updates
     */
    private void bullesUpdate(double dt){

        // Le timer est pour la gestion des bulles
        if ( ((int)tempsTotal %3  == 0) && timer){
            makeBubbles(15);
            timer = false;
        }

        if ((int)tempsTotal % 3 != 0){
            timer = true;
        }

        if (bubbles.length != 0) {
            for (Bubbles b : bubbles) {
                b.update(dt);
            }
        }

    }



    /**
     * Détermine le type de la prochaine plateforme selon les probabilité,
     * et empêche d'avoir deux rouge de suite.
     * Modifications seraient possibles pour des levels differents
     * avec un override.
     * Types des plateformes: [0]orange, [1]vert, [2]jaune, [3]rouge
     * @return  return type de plateforme à construire
     */
    private int nextPlatFormeType(){

        double prob = (Math.random() * 100);
        int typePlt;

        if (prob <= 65){
            typePlt = 0; // orange
            isLastPltRed = false;
        }

        else if (prob <=85){
            typePlt = 1; // verte
            isLastPltRed = false;
        }

        else if (prob <= 95){
            typePlt = 2; // jaune
            isLastPltRed = false;
        }

        // empêche deux rouges de suite
        else if (prob <= 100 && !isLastPltRed){
            typePlt = 3; // rouge
            isLastPltRed = true;
        }

        // si ça tombe sur rouge mais que la dernière était rouge
        else {
            typePlt = (int)Math.floor(Math.random() * 3);
            isLastPltRed = false;
        }

        return typePlt;
    }



    /**
     * Creation de bulles en 3 groupes
     * modification possible pour level different
     * @param numBubbles    nombre de bulles voulu
     */
    public void makeBubbles(int numBubbles){

        // Position en x de la base des groupes de bulles
        double[] baseX = {
                Math.random() * width,
                Math.random() * width,
                Math.random() * width
        };

        this.bubbles = new Bubbles[numBubbles];

        for (int i = 0; i < this.bubbles.length; i++) {

            if (i % 3 == 0) {
                this.bubbles[i] = new Bubbles(baseX[2],
                        1, Color.rgb(0,0,255,0.4), height);
            } else if (i % 2 == 0) {
                this.bubbles[i] = new Bubbles(baseX[1],
                        1 , Color.rgb(0,0,255,0.4), height);
            } else {
                this.bubbles[i] = new Bubbles(baseX[0],
                        1 ,Color.rgb(0,0,255,0.4), height);
            }
        }

    }



    /**
     * Commandes pour le contrôle du personnage.
     */

    public void jump(){
        jumper.jump();
    }

    public void right(){
        jumper.right();
    }

    public void stop(){
        jumper.stop();
    }

    public void left(){
        jumper.left();
    }



    /**
     * Setters
     */

    public void debugMode(){
        this.moves = !this.moves;
    }

    public void setAccelerate(boolean value){
        this.accelerate = value;
    }



    /**
     * Getters
     */

    public Plateforme[] getPlateformes() {
        return this.plateformes;
    }

    public Bubbles[] getBubbles(){
        return this.bubbles;
    }

    public Image getImage() {
        return this.image;
    }

    public Color getColor(){
        return this.color;
    }

    public Jumper getJumper() {
        return this.jumper;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public int getFenetreY(){
        return (int) this.fenetreY;
    }

    public boolean getDead(){
        return this.dead;
    }


}
