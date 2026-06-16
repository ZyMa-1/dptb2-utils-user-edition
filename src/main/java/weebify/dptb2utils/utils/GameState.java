package weebify.dptb2utils.utils;

public class GameState {

    public enum MapType {
        CITY,
        WILD_WEST,
        UNKNOWN
    }

    private int claimedJackpotValue = -1;
    private int currentJackpotValue = -1;
    private MapType currentMap = MapType.UNKNOWN;

    public int getCurrentJackpotValue() {
        return currentJackpotValue;
    }

    public void setCurrentJackpotValue(int currentJackpotValue) {
        this.currentJackpotValue = currentJackpotValue;
    }
    public int getClaimedJackpotValue() {
        return claimedJackpotValue;
    }

    public void setClaimedJackpotValue(int claimedJackpotValue) {
        this.claimedJackpotValue = claimedJackpotValue;
    }

    public void setCurrentMap(MapType currentMap) {
        this.currentMap = currentMap;
    }

    public MapType getCurrentMap() {
        return this.currentMap;
    }

    public boolean isCity() {
        return currentMap == MapType.CITY;
    }

    public boolean isWildWest() {
        return currentMap == MapType.WILD_WEST;
    }
}