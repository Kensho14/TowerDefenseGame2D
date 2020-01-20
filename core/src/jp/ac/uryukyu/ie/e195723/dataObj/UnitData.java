package jp.ac.uryukyu.ie.e195723.dataObj;

public class UnitData {
    public String id;
    public int spawnCost;
    public String displayName;
    public String iconPath;

    public UnitData(String id, int spawnCost, String displayName, String iconPath){
        this.id = id;
        this.spawnCost = spawnCost;
        this.displayName = displayName;
        this.iconPath = iconPath;
    }
}
