package fr.cnam.nfe204.webserver;

public class Occurence {
    private String word;
    private int occurence;

    public Occurence(String word, int occurence) {
        this.word = word;
        this.occurence = occurence;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getOccurence() {
        return occurence;
    }

    public void setOccurence(int occurence) {
        this.occurence = occurence;
    }

    @Override
    public String toString() {
        return "Occurence{" +
                "word='" + word + '\'' +
                ", occurence=" + occurence +
                '}';
    }
}
