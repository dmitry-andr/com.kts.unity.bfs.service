package com.kts.unity.config.web.backend.utils;

import com.kts.unity.config.web.utils.AppContext;
import com.kts.unity.config.web.utils.ConfigParams;
import java.io.File;
import java.io.IOException;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.core.io.Resource;

public class ScoringRules {

    private int[][] scores;
    private RankMinMaxScores[] rankScores;

    public int init() {
        int status = 0;

        //Excel file processing temp area

        Workbook w;
        try {
            Resource scoresRulesFileResource = AppContext.getContext().getResource(ConfigParams.APP_SCORES_RULES_FILE_PATH_NAME);
            //InputStream fileInputStream = new FileInputStream(new File(scoresRulesFileResource.getFile().getPath()));

            File inputWorkbook = new File(scoresRulesFileResource.getFile().getPath());

            
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            
            this.scores = new int[ConfigParams.NUMBER_OF_RANKS][ConfigParams.NUMBER_OF_RANKS];

            for (int j = 1; j < ConfigParams.NUMBER_OF_RANKS +1; j++) {//indices start from 0 - start from 1 as 0 - cell with header/text
                for (int i = 1; i < ConfigParams.NUMBER_OF_RANKS +1; i++) {
                    Cell cell = sheet.getCell(j, i);
                    if ((cell.getType() == CellType.NUMBER) || (cell.getType() == CellType.NUMBER_FORMULA)) {
                        //System.out.println("Column : " + j + " Row : " + i + " -> I got a number " + cell.getContents());// - Log this output
                        this.scores[i-1][j-1] = Integer.parseInt(cell.getContents());
                    }
                }
            }
            
         
            
            //Ranking scores implementation initialization using sheet2 data
            sheet = w.getSheet(1);
            this.rankScores = new RankMinMaxScores[sheet.getRows() - 2]; //Log info about number of rows at the sheet !!!!!!!!!!
                        
            //System.out.println("Num of rows at the sheet  : " + sheet.getRows());
            
            //System.out.println("Arr size before init  : " + this.rankScores.length);
            
            for(int k = 1; k < (sheet.getRows() - 1); k++){
                int[] vals = new int[2];
                for(int l = 1; l < sheet.getColumns(); l++){
                    Cell cell = sheet.getCell(l, k);//read values line by line
                    if ((cell.getType() == CellType.NUMBER) || (cell.getType() == CellType.NUMBER_FORMULA)) {
                        vals[l - 1] = Integer.parseInt(cell.getContents());
                    }
                }
                
                //System.out.println("Ranking values  : " + k + " - " + vals[0]+ "<->"+ vals[1]); //- Log this output
                this.rankScores[ k - 1] = new RankMinMaxScores(vals[0], vals[1]);
            }
            
            //System.out.println("Arr size after init  : " + this.rankScores.length);
            
        } catch (BiffException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        
        return status;
    }


    public int getScores(int killerRank, int killedRank){
        if(((killerRank - 1) > this.scores.length) || ((killedRank - 1) > this.scores[0].length)){
            return -1;
        }
        
        return this.scores[killerRank - 1][killedRank - 1];        
    }
    
    public int getRankForPoints(int scores){
        
        int rank = -1;
        
        for(int k = 0; k < this.rankScores.length; k++){
            int minScore = this.rankScores[k].minValue;
            int maxScore = this.rankScores[k].maxValue;
            if((scores >= minScore) && (scores <= maxScore)){
                rank = k + 1;
                break;
            }
        }
        
        return rank;
    }
    
    public int getMinScoresForRank(int rank){
        return this.rankScores[rank - 1].minValue;
    }

    public String rankingRulesXml(){
        
        StringBuilder xml = new StringBuilder();
        
        xml.append("<scores>");        
        for(int k = 0; k < this.rankScores.length; k++){            
            String rankTag = "rank" + (k + 1);
            xml.append("<" + rankTag + ">");
            
            xml.append("<min>");
            xml.append(this.rankScores[k].minValue);
            xml.append("</min>");

            xml.append("<max>");
            xml.append(this.rankScores[k].maxValue);
            xml.append("</max>");            
            
            xml.append("</" + rankTag + ">");
            
        }        
        xml.append("</scores>");
        
        xml.append("\n");
        xml.append("<awardingpoints>");
        xml.append("\n");
        for(int i = 0; i < ConfigParams.NUMBER_OF_RANKS; i++){
            for(int j = 0; j < ConfigParams.NUMBER_OF_RANKS; j++){
                xml.append("<pl_rank_" + (i+1) + "_won_rank_" + (j+1) + ">");
                xml.append(this.scores[i][j]);
                xml.append("</pl_rank_" + (i+1) + "_won_rank_" + (j+1) + ">");
                xml.append("\n");
            }
        }        
        xml.append("</awardingpoints>");
                
        return xml.toString();
    }
    
    
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        
        out.append("Scoring rules:\n");
        if(this.scores != null){
           for(int k = 0; k < (this.scores.length - 1); k++){
               for(int l = 0; l < (this.scores[k].length - 1); l++){
                   out.append(this.scores[k][l]);
                   out.append(" ; ");
               }
               out.append("\n");
           }
        }
        
        out.append("Ranking rules:\n");
        if(this.rankScores != null){
            for(int j = 0; j < this.rankScores.length; j++){
                out.append(this.rankScores[j]);
                out.append("; ");
            }
        }
        
        out.append("*****************");
        
        return out.toString();
    }

    private class RankMinMaxScores
    {
        private int minValue;
        private int maxValue;
        
        public RankMinMaxScores(int minVal, int maxVal){
            this.minValue = minVal;
            this.maxValue = maxVal;
        }
        
        public int getMaxValue() {
            return maxValue;
        }
        public void setMaxValue(int maxValue) {
            this.maxValue = maxValue;
        }
        public int getMinValue() {
            return minValue;
        }
        public void setMinValue(int minValue) {
            this.minValue = minValue;
        }

        @Override
        public String toString() {
            return "Min : " + this.minValue + " ; Max : " + maxValue;
        }
    }    
    
}
