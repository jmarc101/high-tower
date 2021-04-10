/*
 * Dimanche 5 avril, TP2 IFT1025 - HighSeaTower - JavaFX project
 * Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
 * Hugo Scherer  (957841) hugo.sch.42@gmail.com
 *
 * Personnage créé à partir de la classe abstraite Jumper
 * avec images et grandeurs appropriées.
 *
 * Accélération droite et gauche plus rapides pour ce personnage.
 */


import javafx.scene.image.Image;


public final class NyanCat extends Jumper {

    /**
     * Constructeur
     * @param x position x initiale du jumper
     * @param y position y initiale du jumper
     * @param level le niveau dans lequel le personnage est
     */
    public NyanCat(double x, double y, Level level){

        super(x,y, level);

        this.larg = 60;
        this.haut = 30;

        this.frames = new Image[]{
                new Image("/img/catR.png")
        };

        this.framesLeft = new Image[]{
                new Image("/img/catL.png")
        };

        this.image = this.frames[0];
    }



    /**
     * Accélération à droite et à gauche plus rapides.
     */

    @Override
    public void right() {
        looksLeft = false;
        ax = 1400;
    }

    @Override
    public void left() {
        looksLeft = true;
        ax = -1400;
    }



    /**
     * Rebond minimal plus grand dans le vide de l'espace
     */
    @Override
    public void rebond(){
        vy = Math.max(-vy*1.5, 600);
    }

}
