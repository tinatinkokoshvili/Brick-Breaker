import java.io.*;


public class Writer {
    
    public void writeToFile(int score, String name, String pathToFile) {  
        Reader rd = new Reader();

        if (!rd.getScores().contains(score)) {
       //         (rd.getPlayerScore(name) != null && rd.getPlayerScore(name) < score)) {
            try {
                FileWriter wr = new FileWriter(pathToFile, true);
                BufferedWriter bw = new BufferedWriter(wr);
                if (name == null) {
                    bw.write(Integer.toString(score) + ";Player");
                } else {
                    bw.write(Integer.toString(score) + ";" + name);
                }
                bw.newLine();
                bw.flush();
                bw.close();
            } catch (IOException e) {
                System.out.println("Invalid File");
            }
        }   
    }
    
}
