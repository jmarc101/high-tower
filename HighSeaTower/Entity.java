/*
 * Dimanche 5 avril, TP2 IFT1025 - HighSeaTower - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Classe abstraite qui consiste en la base pour les éléments qui
 * font partie du level et qui peuvent être dessinés sur le canevas.
 */


import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public abstract class Entity {

    // Couleur ou image pour dessiner l'entité selon le cas
    protected Color color;
    protected Image image;

    protected double larg,haut;
    protected double x,y;
    protected double vx, vy;
    protected double ax, ay;
    protected double tempsTotal = 0;



    /**
     * Getters
     */

    public Color getColor() {
        return this.color;
    }

    public Image getImage(){
        return this.image;
    }

    public double getLarg() {
        return this.larg;
    }

    public double getHaut() {
        return this.haut;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getVx() {
        return this.vx;
    }

    public double getVy() {
        return this.vy;
    }

    public double getAx() {
        return ax;
    }

    public double getAy() {
        return ay;
    }

}
