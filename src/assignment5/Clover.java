/*
 * Do not change or submit this file.
 */

package assignment5;

import assignment5.Critter.TestCritter;

public class Clover extends TestCritter {

    public String toString() {
        return "@";
    }

    public boolean fight(String opponent) {
        return true;
    }

    public void doTimeStep() {
        setEnergy(getEnergy() + Params.PHOTOSYNTHESIS_ENERGY_AMOUNT);
    }

    public CritterShape viewShape() {
        return CritterShape.CIRCLE;
    }

    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.GREEN;
    }
}
