package play.mickedplay.ctf.shop;

/**
 * Created by mickedplay on 27.04.2016 at 14:40 UTC+1.
 * You are not allowed to remove this comment.
 */
public enum LevelType {
    PLAYER_KILL(1, "Spieler-Kill"),
    FLAG_STEAL(1, "Flagge gestohlen"),
    FLAG_CAPTURE(3, "Flagge erobert"),
    MINE_EXPLOSION(1, "Minenexplosion"),
    RANDOM_ENEMY_KILL(6, "Zufälliger Gegner-Kill"),
    RANDOM_ENEMY_KILL_CAPTURER(9, "Spieler mit Flagge getötet");

    private int level;
    private String reason;

    LevelType(int level, String reason) {
        this.level = level;
        this.reason = reason;
    }

    public int getLevel() {
        return level;
    }

    public String getReason() {
        return reason;
    }
}