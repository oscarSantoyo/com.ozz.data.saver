package com.ozz.web.storage.saver;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

class SaverApplicationTests {

    @Test
    public void contextLoads() {
    String template = getFile("template.xml");
        print("File contents: "+template);
        ArrayList<String> groups = getGroups(template);
        groups.forEach(System.out::println);
    }

    private ArrayList<String> getGroups(String template) {
        int groupInitIndex = -1;
        int groupEndIndex = -1;
        ArrayList<String> groups = new ArrayList<>();
        while( (groupInitIndex = template.indexOf("{{", groupEndIndex+1) ) != -1){
            groupEndIndex = getGroupEndIndex(groupInitIndex, template);
            if(groupEndIndex != -1){
                groups.add(template.substring(groupInitIndex, groupEndIndex + 1));
            } else {
                print("no more groups found from index: "+groupEndIndex);
                break;
            }
        }
        return groups;
    }

	private int getGroupEndIndex(int groupInitIndex, String template) {
        ArrayList<String> key = new ArrayList<>();
        ArrayList<String> groupKeys = new ArrayList<>();
        int groupEndIndex = -1;
        for(int i = groupInitIndex; i < template.length() -1; i++ ){
            char actual = template.charAt(i);
            char next = template.charAt(i + 1);
            // print("actual: " + actual + " next: "+next+ " groupSize: "+groupKeys.size()+ " keysSize: "+key.size()); 
            if(isGroupOpenning(actual, next)) {
                groupKeys.add("{{");
                i++;
                continue;
            }
            if(isGroupClosing(actual, next)) {
                groupKeys.remove(groupKeys.size()-1);
                i++;
                //Evaluate if group is closed
                if(groupKeys.size() == 0 && key.size() == 0) {
                    // print("----------------Group finish at: "+ i);
                    groupEndIndex = i;
                    break;
                }
            }
            if(isKeyOpenning(actual, next)) {
                key.add("{");
            }
            if(isKeyClosing(actual, next)) {
                key.remove(key.size()-1);
            }
        }

        // print("groupKeys size: " + groupKeys.size());
        // print("keys size: " + key.size());
        // print("Group init at: " + groupInitIndex +" ends at: "+ groupEndIndex);
        // print("SubString found: " + template.substring(groupInitIndex));
        return groupEndIndex;
	}

	// public String getGroup(String template) {
  //       int groupInitIndex = template.indexOf("{{");
  //       int groupEndIndex = 0;
  //       ArrayList<String> key = new ArrayList<>();
  //       ArrayList<String> groupKeys = new ArrayList<>();

  //       for(int i = groupInitIndex; i < template.length() -1; i++ ){
  //           char actual = template.charAt(i);
  //           char next = template.charAt(i + 1);
  //           print("actual: " + actual + " next: "+next+ " groupSize: "+groupKeys.size()+ " keysSize: "+key.size()); 
  //           if(isGroupOpenning(actual, next)) {
  //               groupKeys.add("{{");
  //               i++;
  //               continue;
  //           }
  //           if(isGroupClosing(actual, next)) {
  //               groupKeys.remove(groupKeys.size()-1);
  //               i++;
  //               //Evaluate if group is closed
  //               if(groupKeys.size() == 0 && key.size() == 0) {
  //                   print("----------------Group finish at: "+ i);
  //                   groupEndIndex = i;
  //               }
  //               break;
  //           }
  //           if(isKeyOpenning(actual, next)) {
  //               key.add("{");
  //           }
  //           if(isKeyClosing(actual, next)) {
  //               key.remove(key.size()-1);
  //           }
  //       }

  //       print("groupKeys size: " + groupKeys.size());
  //       print("keys size: " + key.size());
  //       System.out.println("Group init at: " + groupInitIndex);
  //       return template.substring(groupInitIndex, groupEndIndex+1);
  //   }

    private void print(String string) {
        System.out.println(string);
    }

    private boolean isGroupOpenning(char actual, char next) {
        return actual == '{' && next == '{';
    }

    private boolean isGroupClosing(char actual, char next) {
        return actual == '}' && next == '}';
    }
    private boolean isKeyOpenning(char actual, char next) {
        return actual == '{' && next != '{';
    }
    private boolean isKeyClosing(char actual, char next) {
        return actual == '}' && next != '}';
    }

	public String getFile(String name) {
        try {
            File file = new File(this.getClass().getClassLoader().getResource(name).getFile());
            return new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
