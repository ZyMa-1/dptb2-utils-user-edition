package weebify.dptb2utils.utils;

public class GameState {

    public enum MapType {
        CITY,
        WILD_WEST,
        UNKNOWN
    }

    private int claimedJackpotValue = -1;
    private MapType currentMap = MapType.UNKNOWN;

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