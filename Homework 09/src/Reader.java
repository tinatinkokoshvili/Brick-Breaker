import java.io.*;
import java.util.*;


public class Reader {
    
    
    private FileReader frd;
    private BufferedReader brd;
    private ArrayList<String> lines = new ArrayList<String>();
    private int lineNum = 0;
    private int topScoreNum = 3;
    private ArrayList<Integer> topScores = new ArrayList<Integer>();
    private ArrayList<Integer> scores = new ArrayList<Integer>();
    private ArrayList<String> playersNames = new ArrayList<String>();
    TreeMap<Integer, String> scoreMap = new TreeMap<Integer, String>();
    
    private String usersAndScores;

    
    
    public Reader() {
        readLines();
        mapScores();
        getTopThreeScore();  
        usersAndScores();        
       
    }
    
    
    private void readLines() {   
        try {
            frd = new FileReader("files/HighScores.txt");
            brd = new BufferedReader(frd);
            String line = brd.readLine();
            while (line != null) {             
                lines.add(line);
                lineNum++;
                line = brd.readLine();
            }
            brd.close();
        } catch (FileNotFoundException ioe) {                 
            lines = null; 
            throw new IllegalArgumentException();
        }  catch (IOException io) {
            lines = null;
        }
    }    
    
    private void mapScores() {
        for (int i = 0; i < lineNum; i++) {
            String[] userScore = lines.get(i).split(";");
            scoreMap.put(Integer.valueOf(userScore[0]), userScore[1]);
            playersNames.add(userScore[1]);                       
            scores.add(Integer.parseInt(userScore[0]));
        }
    }
    
    public ArrayList<Integer> getTopThreeScore() {
        Collections.sort(scores, Collections.reverseOrder());
        for (int i = 0; i < topScoreNum; i++) {
            if ((i + 1) <= scores.size()) {
                topScores.add(scores.get(i));
            }
        }
        return topScores;                             
    }
    
    
    public void usersAndScores() {
        usersAndScores = "";
        for (int i = 0; i < topScores.size(); i++) {
            if (scoreMap.containsKey(topScores.get(i))) {
                usersAndScores += (i + 1) + "." + 
                      scoreMap.get(topScores.get(i)) + " " + topScores.get(i) + " ";
            }
        } 
        if (usersAndScores.isEmpty()) {
            usersAndScores = "There are currently no scores to display. Be First to add one!";
        }
    }
    
    public String getUsersAndScores() {
        return usersAndScores;
    }
    
    public ArrayList<Integer> getScores() {
        ArrayList<Integer> list2 = new ArrayList<Integer>(scores);
        return list2;
    }
    
    
    public TreeMap<Integer, String> getScoreMap() {
        TreeMap<Integer, String> list2 = new TreeMap<Integer, String>(scoreMap);
        return list2;
    }
    
    public ArrayList<String> getPlayers() {
        ArrayList<String> list2 = new ArrayList<String>(playersNames);
        return list2;
    }
    
    public Integer getPlayerScore(String name) {
        for (Map.Entry<Integer, String> ent : scoreMap.entrySet()) {
            if (name.equals(ent.getValue())) {
                return ent.getKey();                
            }
        }
        return null;
    }
    
}
