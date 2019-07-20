package play.mickedplay.ctf.player;

import me.winterguardian.easyscoreboards.ScoreboardUtil;
import play.mickedplay.ctf.CaptureTheFlag;
import play.mickedplay.ctf.game.stages.DecisionPhase;
import play.mickedplay.ctf.game.stages.End;
import play.mickedplay.ctf.game.stages.Ingame;
import play.mickedplay.ctf.team.Team;
import play.mickedplay.gameapi.game.GameStage;
import play.mickedplay.gameapi.game.stages.Lobby;
import play.mickedplay.gameapi.utilities.Utilities;
import play.mickedplay.gameapi.utilities.game.GameUtilities;
import play.mickedplay.gameapi.utilities.game.TimeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mickedplay on 17.04.2016 at 10:37 UTC+1.
 * You are not allowed to remove this comment.
 */
public class ScoreboardManager {

    private CaptureTheFlag ctf;

    public ScoreboardManager(CaptureTheFlag ctf) {
        this.ctf = ctf;
    }

    public void setFor(CTFPlayer ctfPlayer) {
        List<String> lines = new ArrayList<>();
        lines.add("§e§l" + this.ctf.getName());
        lines.add("");
        GameStage gameStage = this.ctf.getGameStage();

        if (gameStage instanceof Lobby) {
            if (this.ctf.getMapManager().canVote()) {
                lines.addAll(this.ctf.getMapManager().getGameMaps().values().stream().map(gameMap -> "§f" + gameMap.getWorldName() + ": §a" + gameMap.getVotes() + " Votes").collect(Collectors.toList()));
            } else {
                lines.add("§f" + this.ctf.getGameMap().getWorldName() + ": §a" + this.ctf.getGameMap().getVotes() + " Votes");
            }
            lines.add(" ");
            lines.add("Highscore: " + "§a" + ctfPlayer.getGameStats().getHighscore());
        } else if (gameStage instanceof DecisionPhase) {
            lines.addAll(this.ctf.getTeamManager().getTeams().stream().map(team -> team.getDisplayName() + " §f\u00BB §7" + team.getSize() + "/" + this.ctf.getTeamManager().getMaxTeamSize()).collect(Collectors.toList()));
        } else if (gameStage instanceof Ingame) {
            lines.addAll(this.ctf.getTeamManager().getTeams().stream().map(team -> team.getChatColor() + "\u2691 " + team.getCaptures() + " §7\u00BB " + team.getDisplayName()).collect(Collectors.toList()));
            lines.addAll(Arrays.asList(" ", "§4Zeit: §6" + GameUtilities.formatTime(this.ctf.getGameTimer().getTime(), TimeType.MINUTES_SECONDS)));
        } else if (gameStage instanceof End) {
            Team winningTeam = this.ctf.getActionManager().getWinningTeam();
            lines.add("§7Sieger: " + (winningTeam == null ? "§7Niemand" : winningTeam.getDisplayName()));
        } else {
            Team winningTeam = this.ctf.getActionManager().getWinningTeam();
            lines.add("§7Sieger: " + (winningTeam == null ? "§7Niemand" : winningTeam.getDisplayName()));
            lines.add("§cServer startet neu...");
        }
        ScoreboardUtil.unrankedSidebarDisplay(ctfPlayer.getPlayer(), Utilities.toArray(lines));
    }

    public void setForAll() {
        this.ctf.getCTFPlayers().values().forEach(this::setFor);
    }
}