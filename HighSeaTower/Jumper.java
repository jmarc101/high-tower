/*
 * Dimanche 5 avril, TP2 IFT1025 - HighSeaTower - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * La classe qui code le personnage qui saute de plateforme en plateforme.
 *
 * Nous avons décidé de mettre dans cette classe la gestion des collisions
 * avec les plateformes et de procéder par héritage car si nous décidions
 * de bonifier le jeu, des personnages différents pourraient avoir des interactions
 * différentes avec le Level du jeu (en faisant de l'override sur les fonctions
 * de collision).
 */


import javafx.scene.image.Image;


public abstract class Jumper extends Entity{

    protected Image[] frames;
    protected Image[] framesLeft;
    private final Level level;

    protected double frameRate = 1;

    private boolean parterre = true;
    protected boolean looksLeft = false;



    /**
     * Constructeur du Jumper par default
     * @param x     PosX depart
     * @param y     posY depart
     */
    public Jumper(double x, double y, Level level){
        this.x = x;
        this.y = y;
        this.ay = -1200;
        this.level = level;
    }



    /**
     * Méthodes pour contrôler le personnage
     */

    // Saut
    public void jump() {
        if (parterre || y == 0 ) {
            vy = 600;
        }
    }

    // Mouvement à droite
    public void right(){
        looksLeft = false;
        ax = 1200;
    }

    // Mouvement à gauche
    public void left(){
        looksLeft = true;
        ax = -1200;
    }

    // Arrêt
    public void stop(){
        ax = 0;
        vx = 0;
    }

    // Rebondir sur une plateforme
    // Minimum de 100 tel que dit dans l'énoncé
    public void rebond(){
        vy = Math.max(-vy*1.5, 100);
    }



    /**
     * Update la position et le changement d'image.
     * La physique du personnage est codée ici.
     * @param dt        delta time entre updates
     * @param width     largeur de l'écran (largeur maximale)
     */
    public void update(double dt, int width) {

        vx += dt * ax;

        // si dans les air, la gravité le pousse vers le bas.
        if (!parterre) {
            vy += dt * ay;
        }

        // Réinitialise, sera re-testé plus tard avec les collisions
        parterre = false;

        x += dt * vx;
        y += dt * vy;


        // Force à rester dans les bornes de l'écran
        if (x + larg > width || x < 0) {
            vx *= -1;
        }

        x = Math.min(x, width - larg);

        // Empêche que le personnage meurt au début du jeu
        x = Math.max(x, 0);
        y = Math.max(y, 0);


        // Mise à jour de l'image à afficher
        tempsTotal += dt;

        int frame = (int) (tempsTotal * frameRate);

        if (looksLeft){
            image = framesLeft[frame % framesLeft.length];
        } else {
            image = frames[frame % frames.length];
        }
    }



    /**
     * Teste la collision avec une plateforme, qui a lieu seulement si :
     * - Il y a une intersection entre la plateforme et le personnage
     * - La collision a lieu entre la plateforme et le *bas du personnage*
     * seulement
     * - La vitesse va vers le bas (le personnage est en train de tomber,
     * pas en train de sauter)
     *
     * Gère aussi l'effet de la collision.
     *
     * @return un boolean selon s'il y a collision ou non
     */
    public boolean testCollision(Plateforme other) {

        if (intersects(other) && Math.abs(this.y - other.getY()) < 10) {

            // Plateformes sans rebond
            if (other.isOrange() || other.isYellow() || other.isRed()) {

                // Plateforme jaune accélère le level
                if (other.isYellow()){
                    this.level.setAccelerate(true);
                }

                this.vy = 0;
                pushOut(other);
            }

            // Plateform avec rebond
            else if (other.isGreen()){
                this.rebond();
            }

            this.parterre = true;

            return  true;
        }

        return false;
    }



    /**
     * Test si collision par le haut avec une plateforme
     * @param other     palteforme pour tester collission
     * @return          Boolean selon s'il y a collision ou non
     */
    public boolean testUpCollision(Plateforme other) {

        // Plateforme rouge = on bloque le saut
        if (other.isRed() && upIntersects(other) &&
                Math.abs((other.getY() - other.getHaut())
                        - (this.y + this.haut)) < 10) {
            this.vy = 0;
            return true;
        }

        return false;
    }



    /**
     * Pour tester si le jumper atterrit
     * @param other     plateforme avec qui tester la collision
     * @return          boolean selon si collision ou non
     */
    private boolean intersects(Plateforme other) {
        return ( (this.x + this.larg > other.getX())
                 && (other.getX() + other.getLarg() > this.x)
                 && (this.vy <= 0));
    }



    /**
     * Pour tester si le jumper arrive par le bas
     * @param other     plateforme avec qui tester la collision
     * @return          boolean selon si collision ou non
     */
    private boolean upIntersects(Plateforme other) {
        return ( (this.x + this.larg > other.getX())
                && (other.getX() + other.getLarg() > this.x)
                && (this.vy >= 0));
    }



    /**
     * Repousse le personnage vers le haut (sans déplacer la
     * plateforme).
     */
    private void pushOut(Plateforme other) {
        double deltaY = this.y - other.getY();
        this.y -= deltaY;
    }



    /**
     * Getter et setter pour le boolean parterre
     */

    public boolean getParterre(){
        return this.parterre;
    }

}
