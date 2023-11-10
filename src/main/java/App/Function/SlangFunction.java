package App.Function;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class SlangFunction {
    ArrayList<String[]> data = new ArrayList<String[]>(); //used to store the data
    ArrayList<String[]> history = new ArrayList<String[]>();
    HashMap<String, Integer> slangHashMap = new HashMap<String, Integer>();
        HashMap<String, Integer> meaningHashMap = new HashMap<String, Integer>();
    String filePath;
    public SlangFunction(String filePath) {
        BufferedReader br;
        String line;
        String[] meaningList;

        this.filePath = filePath;
        try {
            int currentIndex = 0;
            br = new BufferedReader(new FileReader(filePath));

            while ((line = br.readLine()) != null) {
                data.add(line.split("`"));
                slangHashMap.put(data.get(currentIndex)[0], currentIndex);//put slang into slang hashmap for fast searching
                if (!data.get(currentIndex)[1].contains("|")) { // if slang has 1 meaning
                    meaningHashMap.put(data.get(currentIndex)[1].trim(), currentIndex);
                }
                else { // if slang has 2 or more meanings
                    meaningList = data.get(currentIndex)[1].split("\\|");
                    for (int i = 0; i < meaningList.length; i++) {
                        meaningHashMap.put(meaningList[i].trim(), currentIndex);
                    }
                }
                currentIndex++;
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // return the meaning of an input keyword, if not found return ""
    public String[] findBySlang(String keyword, boolean saveToHistory) {
        keyword = keyword.trim();
        if (slangHashMap.get(keyword) == null) {
            return null;
        }
        if (saveToHistory)
            history.add(data.get(slangHashMap.get(keyword))); // add keyword to history ArrayList for history function
        return data.get(slangHashMap.get(keyword));
    }

    public ArrayList<String[]> findByMeaning(String keyword, boolean saveToHistory) {
        keyword = keyword.trim();
        HashSet<Integer> result = new HashSet<Integer>();
        for (String key : meaningHashMap.keySet())
            if (key.contains(keyword))
                result.add(meaningHashMap.get(key));
        ArrayList<String[]> slangList = new ArrayList<String[]>();
        for (int i : result) {
            slangList.add(data.get(i));
            if (saveToHistory)
                history.add(data.get(i)); // add keyword to history ArrayList for history function
        }
        return slangList;
    }

    public void addSlang(String slang, String meaning, boolean duplicate) {
        slang = slang.trim();
        meaning = meaning.trim();
        String[] newSlang = new String[2];
        newSlang[0] = slang;
        newSlang[1] = meaning;
        if (findBySlang(slang, false) == null) { // new slang
            slangHashMap.put(slang, data.size());
            meaningHashMap.put(meaning, data.size());
            data.add(newSlang);
            return;
        }
        String duplicateMeaning = data.get(slangHashMap.get(slang))[1];
        if (duplicate) { // duplicate meaning if slang exist
            duplicateMeaning = duplicateMeaning + " | " + meaning;
            newSlang[1] = duplicateMeaning;
            data.set(slangHashMap.get(slang), newSlang);
            return;
        }
        data.set(slangHashMap.get(slang), newSlang);
    }

    public void editSlang(String[] oldWord, String[] newWord) {
        String oldSlang = oldWord[0].trim();
        String newSlang = newWord[0].trim();

        boolean existed = false;
        //Check if the user changes a slang word to an existing slang word
        if (slangHashMap.get(oldSlang) != slangHashMap.get(newSlang) && slangHashMap.get(newSlang) != null) {
            int index = slangHashMap.get(oldSlang);
            slangHashMap.remove(oldSlang);
            for (String j : oldWord[1].split("\\|"))
                meaningHashMap.remove(j.trim());
            data.remove(index);

            int existedWordIndex = slangHashMap.get(newSlang);
            String[] newMeaningList = newWord[1].split("\\|");
            for (int i = 0; i < newMeaningList.length; i++) {
                if (data.get(existedWordIndex)[1].contains(newMeaningList[i].trim())) {
                    continue;
                }
                meaningHashMap.put(newMeaningList[i].trim(), slangHashMap.get(newSlang));
                String[] newMeaning = data.get(existedWordIndex);
                if (!newMeaning[1].isEmpty())
                    newMeaning[1] = newMeaning[1] + " | " + newMeaningList[i].trim();
                else
                    newMeaning[1] = newMeaningList[i].trim();
                data.set(existedWordIndex, newMeaning);
            }
            return;
        }

        int index = slangHashMap.get(oldSlang);
        slangHashMap.remove(oldSlang);
        for (String j : oldWord[1].split("\\|"))
            meaningHashMap.remove(j.trim());
        data.remove(index);

        slangHashMap.put(newSlang, index);
        for (String i : newWord[1].split("\\|"))
            meaningHashMap.put(i.trim(), index);
        data.add(index, newWord);
        return;

    }

    public void deleteSlang(String slang) {
        slang = slang.trim();
        int index = slangHashMap.get(slang);
        String[] meaningList = data.get(index)[1].split("\\|");

        String[] nullSlang = {"", "slang is deleted"};
        data.set(index, nullSlang);
        slangHashMap.remove(slang);
        for (String meaning : meaningList) {
            meaning = meaning.trim();
            meaningHashMap.remove(meaning);
        }
    }

    public String[] ramdomSlang() {
        int random = (int) (Math.random() * data.size());
        return data.get(random);
    }

    public ArrayList<String[]> getHistory() {
        return history;
    }

    public ArrayList<String[]> slangQuizz() {
        ArrayList<String[]> randomList = new ArrayList<String[]>();
        int[] randomIndexes = new int[4];
        int random = (int) (Math.random() * data.size());

        randomList.add(data.get(random));
        randomIndexes[0] = random;
        for (int i = 0; i < 3; i++) {
            do {
                random = (int) (Math.random() * data.size());
            }
            while (Arrays.asList(randomIndexes).contains(random));
            randomList.add(data.get(random));
            randomIndexes[i + 1] = random;
        }

        return randomList;
    }

    public void save() {
        BufferedWriter bw;
        try {
            String line;
            bw = new BufferedWriter(new FileWriter(filePath));

            for (int i = 0; i < data.size(); i++) {
                if (data.get(i)[0].isEmpty())
                    continue;
                line = String.join("`", data.get(i));
                bw.write(line);
                if (i != data.size() - 1) {
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        data.clear();
        slangHashMap.clear();
        meaningHashMap.clear();

        BufferedReader br;
        String line;
        String[] meaningList;

        try {
            int currentIndex = 0;
            String twoMeaningSlang;
            br = new BufferedReader(new FileReader(filePath));

            //remove the first line - instruction line(Slag`Meaning)
            br.readLine();
            while ((line = br.readLine()) != null) {
                data.add(line.split("`"));
                slangHashMap.put(data.get(currentIndex)[0], currentIndex);//put slang into slang hashmap for fast searching
                if (!data.get(currentIndex)[1].contains("|")) { // if slang has 1 meaning
                    meaningHashMap.put(data.get(currentIndex)[1].trim(), currentIndex);
                }
                else { // if slang has 2 or more meanings
                    meaningList = data.get(currentIndex)[1].split("\\|");
                    for (int i = 0; i < meaningList.length; i++)
                        meaningHashMap.put(meaningList[i].trim(), currentIndex);
                }
                currentIndex++;
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void print() {
        for (int i = 0; i< data.size(); i++)
            System.out.println(data.get(i)[0] + " : " + data.get(i)[1]);
    }

    String findByIndex (int index) {
        if (index > data.size())
            return "wrong index";
        return data.get(index)[0];
    }
}
