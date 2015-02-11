/*
 * Copyright Mirake Ltd. All Rights reserved.
 */
package fi.darkwood.ability;

/**
 *
 * @author Teemu
 */
public class AbilityTables {

    public static final int[][] ABILITY_LEVELS = {
        {1, 0, 0, 0}, // 1
        {1, 0, 0, 0}, // 2
        {1, 1, 0, 0}, // 3
        {1, 1, 0, 0}, // 4
        {1, 1, 0, 0}, // 5
        {2, 1, 0, 0}, // 6
        {2, 1, 0, 0}, // 7
        {2, 1, 0, 0}, // 8
        {2, 1, 0, 0}, // 9
        {2, 2, 0, 0}, // 10
        {2, 2, 0, 0}, // 11
        {2, 2, 0, 0}, // 12
        {2, 2, 0, 0}, // 13
        {2, 2, 1, 0}, // 14
        {2, 2, 1, 0}, // 15
        {3, 2, 1, 0}, // 16
        {3, 2, 1, 0}, // 17
        {3, 2, 1, 0}, // 18
        {3, 2, 1, 0}, // 19
        {3, 3, 1, 0}, // 20
        {3, 3, 1, 0}, // 21
        {3, 3, 2, 0}, // 22
        {3, 3, 2, 0}, // 23
        {3, 3, 2, 0}, // 24
        {3, 3, 2, 1}, // 25
        {4, 3, 2, 1}, // 26
        {4, 4, 2, 1}, // 27
        {4, 4, 2, 1}, // 28
        {4, 4, 3, 1}, // 29
        {4, 4, 3, 2},  // 30
    };

    /**
     * Check which ability has advanced this level up. Return ability slot number or -1 if none.
     * @param previouslevel
     * @param currentlevel
     * @return
     */
    public static int checkAbilityChange(int currentLevel) {
        if (currentLevel == 1) { return -1; }
        
        for (int i = 0; i < 4; i++) {
            if (ABILITY_LEVELS[currentLevel - 2][i] != ABILITY_LEVELS[currentLevel - 1][i]) {
                return i;
            }
        }
        return -1;
    }
}
