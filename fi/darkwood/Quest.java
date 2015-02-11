/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.darkwood;

import fi.darkwood.ui.component.MessageLog;
import fi.mirake.Local;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Administrator
 */
public class Quest {

    public Hashtable requirements = new Hashtable();
    public Hashtable requirementsDone = new Hashtable();
    private Equipment reward;
    private boolean questDone;
    private String questText;
    private String completedText;
    int level;
    private Quest nextQuest;

    public void setNextQuest(Quest q) {
        nextQuest = q;
    }

    public String getCompletedText() {
        return completedText;
    }

    public void setCompletedText(String completedText) {
        this.completedText = completedText;
    }

    public Quest(String questText, int level) {
        this.questText = questText;
        this.level = level;
    }

    public void setReward(Equipment eq) {
        reward = eq;
    }

    public String getQuestText() {
        return questText;
    }

    /**
     * Add a kill requirement for this quest
     * @param monsterType Give an instance of the monster required
     * @param number Number of kills required 
     */
    public void addKillRequirement(Monster monsterType, int number) {
        //System.out.println("Adding Kill requirement to quest: " + monsterType.name + ", " + number);
        addRequirement(monsterType.name, number);
    }

    public void addRequirement(String str, int number) {
        requirements.put(str, new Integer(number));
        requirementsDone.put(str, new Integer(0));

    }

    public void setCompletedKills(String name, int number) {
        requirementsDone.put(name, new Integer(number));
    }

    public void awardKillRequirement(Creature creature) {
        awardRequirement(creature.name);
    }

    public void awardRequirement(String str) {
        // if quest is already done, no need to add
        if (checkQuestDone()) {
            return;
        }

        Enumeration enumeration = requirements.keys();
        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            if (name.equals(str)) {
                Integer done = (Integer) requirementsDone.get(str);
                int doneInt = done.intValue();

                Integer req = (Integer) requirements.get(str);
                int reqInt = req.intValue();

                // check if quest kill requirement is not already done
                if (doneInt < reqInt) {
                    doneInt += 1;
                    requirementsDone.remove(str);
                    requirementsDone.put(str, new Integer(doneInt));

                    MessageLog.getInstance().addMessage(Local.get("questkill")+ ": " + str + " " + requirementsDone.get(str) + "/" + requirements.get(str));

                }
//                System.out.println("" + this.toString() + "  " + creature.name + " " + requirementsDone.get(creature.name) + "/" + requirements.get(creature.name));

            }
        }

        // this check if this was the last kill of the quest, and then prints "Quest complete."
        if (checkQuestDone()) {
            MessageLog.getInstance().addMessage(Local.get("questcomplete"));
        }
    }

    /**
     * Check if quest is done. Returns true if all requirements are fulfilled,
     * otherwise false.
     * 
     * @return
     */
    public boolean checkQuestDone() {
        // if quest is already marked done, return true
        if (questDone) {
            return true;
        }
        Enumeration enum1 = requirements.keys();
        // loop all requirements
        while (enum1.hasMoreElements()) {
            String key = (String) enum1.nextElement();

            Integer killsRequired = (Integer) requirements.get(key);
            Integer killsDone = (Integer) requirementsDone.get(key);

            // if requirement is not fullfilled, return false
            if (killsRequired.intValue() > killsDone.intValue()) {
                return false;
            }

        }
        // all requirements were fulfilled, set questDone to true and return true
        questDone = true;

        return true;
    }

    public String printKillRequirements() {
        String str = "";

        Enumeration enumeration = requirements.keys();
        String key;
        while (enumeration.hasMoreElements()) {
            key = (String) enumeration.nextElement();
            str = str + key + " " + requirementsDone.get(key) + "/" + requirements.get(key) + "\n";

        //   System.out.println("" + this.toString() + "  " + key + " " + requirementsDone.get(key) + "/" + requirements.get(key));

        }

        return str;
    }

    /**
     * Add this quest to the list of completed quests of this player
     * @param player
     */
    public void completeQuest(Player player) {
        if (checkQuestDone() == false) {
            return; // was not completed
        }
        player.currentQuest = null;
        player.completedQuests.addElement(this);
        player.awardExp(getExpReward());
        player.addMoney(getMoneyReward());
    }

    private int getTotalKills() {
        Enumeration enumeration = requirements.keys();
        String key;
        int sum = 0;
        while (enumeration.hasMoreElements()) {
            key = (String) enumeration.nextElement();
            Integer number = (Integer) requirementsDone.get(key);
            sum += number.intValue();
        }
        if (sum < 10) {
            sum = 10; // typical amount, also for non-kill quests
        }
        return sum;
    }

    // Quest rewards are now in line with the monster kill exp/money ++Ville
    // Quest level is defined by the level of the killed mobs or otherwise by the set value
    public int getExpReward() {
        int kills = getTotalKills();
        return kills * this.level * GameConstants.monsterEXPConstant;
    }

    public int getMoneyReward() {
        int kills = getTotalKills();
        return kills * GameConstants.getMonsterMoney(this.level);
    }

    public boolean isQuestDone() {
        return questDone;
    }

    public Quest getNextIncompleteQuest(Player player) {
        if (player.completedQuests.contains(this)) {
            if (nextQuest == null) {
                return null;
            } else {
                return nextQuest.getNextIncompleteQuest(player);
            }
        }
        return this;
    }

    /**
     * Two quest objects are the same if classname is same
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if ((o instanceof Quest) == false) {
            return false;
        }
        if (o.getClass().equals(this.getClass())) {
            //  System.out.println("Quest the same");
            return true;
        } else {
            //   System.out.println("Quest not same " + o.getClass().getName() + " / " + this.getClass().getName());
            return false;
        }
    }
}
