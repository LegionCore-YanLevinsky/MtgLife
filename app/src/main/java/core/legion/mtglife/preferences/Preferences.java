package core.legion.mtglife.preferences;

import java.util.List;

import core.legion.mtglife.pojo.Player;


public interface Preferences {
    boolean getIsRated();
    void setIsRated(boolean value);

    List<Player> getPlayers();
    void setPlayers(List<Player> players);
}
