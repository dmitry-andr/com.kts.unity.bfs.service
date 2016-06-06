package com.kts.unity.config.web.backend.utils.player;

import com.kts.unity.config.web.utils.ConfigParams;

public class PlayerLevelsProgressUtility {

    private String playerName;
    private String levelsData;

    public PlayerLevelsProgressUtility(String playerName, int levelCompleted, int progressCompletedPercents) {
        this.playerName = playerName;
        this.levelsData = "L" + levelCompleted + "_" + progressCompletedPercents;
    }

    public PlayerLevelsProgressUtility(String playerName, String levelsData) {
        this.playerName = playerName;
        this.levelsData = levelsData;
    }

    public String updateLevelInfo(int level, int progressPercent) {
        String[] levelEntries = levelsData.split(";");

        boolean isLevelUpdated = false;
        for (int k = 0; k < levelEntries.length; k++) {
            String levelLabel = "L" + level;
            if (levelEntries[k].contains(levelLabel)) {
                String[] levelData = levelEntries[k].split("_");
                int currentProgress = Integer.parseInt(levelData[1]);
                if (currentProgress <= progressPercent) {
                    currentProgress = progressPercent;
                }
                levelEntries[k] = levelData[0] + "_" + currentProgress;
                isLevelUpdated = true;
                StringBuilder updatedLevelData = new StringBuilder();
                for (int i = 0; i < levelEntries.length; i++) {
                    updatedLevelData.append(levelEntries[i]);
                    if (i < (levelEntries.length - 1)) {
                        updatedLevelData.append(";");
                    }
                }
                this.levelsData = updatedLevelData.toString();
                break;
            }
        }

        if (!isLevelUpdated) {
            if ((this.levelsData != null) && !"".equals(this.levelsData)) {
                this.levelsData = this.levelsData + ";L" + level + "_" + progressPercent;
            } else {
                this.levelsData = "L" + level + "_" + progressPercent;
            }
        }

        return levelsData;
    }
    
    public boolean isLevelsCompletedWithReqPercent(int numOfLevels, int requiredProgressPercents){
        String[] levelElems = this.levelsData.split(";");
        if(levelElems.length < numOfLevels){
            return false;
        }
        int counterOf100PercentCompletedLevels = 0;
        for (int k = 0; k < levelElems.length; k++) {
            int progressPercents = Integer.parseInt(levelElems[k].trim().split("_")[1]);
            if(progressPercents == requiredProgressPercents){
                counterOf100PercentCompletedLevels++;
                if(counterOf100PercentCompletedLevels == numOfLevels){
                    return true;
                }
            }else{//there is at least one level below 100 percents no need to continue iteration
                break;
            }
        }
        return false;
    }

    public int getProgressPoinsInSinglePlayerMode() {

        String[] levelElems = this.levelsData.split(";");
        int points = 0;
        for (int k = 0; k < levelElems.length; k++) {
            int progressPercents = Integer.parseInt(levelElems[k].trim().split("_")[1]);
            points += (k + 1) * progressPercents / 10;
        }

        return points;
    }

    public String getXMLOutput() {
        if ((this.levelsData == null) || ("".equals(this.levelsData))) {
            return ConfigParams.NO_DATA_AVAILABLE_WS;
        }

        StringBuilder xmlOutput = new StringBuilder();
        xmlOutput.append("<playerlevelstat>");

        xmlOutput.append("<playername>");
        xmlOutput.append(this.playerName);
        xmlOutput.append("</playername>");

        xmlOutput.append("<points>");
        xmlOutput.append(this.getProgressPoinsInSinglePlayerMode());
        xmlOutput.append("</points>");

        String[] levelEntries = levelsData.split(";");
        for (int l = 0; l < levelEntries.length; l++) {
            String[] entryElems = levelEntries[l].split("_");
            String levelTagLabel = entryElems[0];

            xmlOutput.append("<" + levelTagLabel + ">");
            xmlOutput.append(entryElems[1]);
            xmlOutput.append("</" + levelTagLabel + ">");
        }


        xmlOutput.append("</playerlevelstat>");

        return xmlOutput.toString();
    }

    public String getLevelsData() {
        return levelsData;
    }
}
