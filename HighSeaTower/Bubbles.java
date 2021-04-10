/*
* Dimanche 5 avril, TP2 IFT1025 - HighSeaTower - JavaFX project
* Jean-Marc Prud'homme (20137035) jm.prudhomme@icloud.com
* Hugo Scherer  (957841) hugo.sch.42@gmail.com
*
* Objet pour créer les bulles qui montent en arrière plan.
*
* Nous avons gardé le mouvement en Y des bulles séparé
* de la vitesse du tableau, car nous ne voulions pas que les bulles
* défilent plus rapidement lorsque le tableau le faisait.
*/


import javafx.scene.paint.Color;


public final class Bubbles extends Entity{

    private final double hautInit;


    /**
     * Constructeur.
     * La proportion permet de créer des bulles systématiquement plus grosses
     * ou plus petites dans un level.
     * @param x             position de la base en X
     * @param proportion    proportion de la grosseur d'une bulle
     * @param color         couleur désirée
     * @param heightLevel   haut du tableau pour partir du bas
     */
    public Bubbles(double x, double proportion, Color color, double heightLevel){
        this.vy = (Math.random() * 100) + 350;
        this.hautInit = heightLevel + 40;
        this.haut = ((Math.random() * 30) + 10) * proportion;
        this.larg = this.haut ;
        this.x = (Math.random() * 40) -20 + x ;
        this.color = color;
    }



    /**
     * Update de la position des bulles.
     * Ici la convention utilisée est que (0,0) est en haut à gauche de l'écran.
     * @param dt    deltatime entre updates
     */
    public void update(double dt){
        this.tempsTotal += dt;
        this.y = this.hautInit - (this.tempsTotal * this.vy);
    }
}
