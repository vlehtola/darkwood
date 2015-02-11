/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood;

/**
 *
 * @author Ville
 *
 * Description: An object that is visible through movementview. Usable.
 */
public abstract class UsableThing extends Thing {

    protected boolean used;
    public int level;

    public UsableThing(String name, String sImage) {
        super(name, sImage);
        this.used = false;
    }
    public UsableThing(String name, String sImage, int frameWidth) {
        super(name, sImage, frameWidth);
        this.used = false;
    }
    public abstract void tick();
    public abstract String queryUsedText();
    public abstract String queryUnusedText();

    public void setLevel(int level) {
        this.level = level;
    }
    public boolean isUsed() {
        return used;
    }

    public abstract boolean useThing();
 
}
