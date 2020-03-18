package com.sarihunter.localstores.classes;

import java.util.ArrayList;
import java.util.List;

public class Tags {

    String tag;

    public Tags() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Tags(String tag) {
        this.tag = tag;
    }

    public List<Tags> fillTagsList() {

        List<Tags> tags = new ArrayList<Tags>();
        tags.add(new Tags("Computers and Electronics"));
        tags.add(new Tags("Home and Garden"));
        tags.add(new Tags("Vehicales"));
        tags.add(new Tags("Hobbies"));
        tags.add(new Tags("Entertainment"));
        tags.add(new Tags("Classifieds"));
        tags.add(new Tags("Family"));


        return tags;

    }

    public List<Tags> fillWith(String s){
        List<Tags> tagss = new ArrayList<Tags>();
        tagss.add(new Tags(s));
        return tagss;

    }


}
