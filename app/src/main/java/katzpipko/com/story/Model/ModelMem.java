package katzpipko.com.story.Model;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 2017-07-31.
 */



public class ModelMem {
    private List<Story> data = new LinkedList<Story>();
    ModelMem() {
    }

    public  List<Story> GetAllStories(){
        return data;
    }



    void SetNewStoryList(List<Story> lst,int limit)
    {

        Collections.sort(lst,new StoryComparator());

        int i=0;
        data.clear();;
        for (Story s: lst) {
            if (i++<limit)
            addStory(s);
            else return;
        }
    }


    void addStory(Story st){
        data.add(st);
    }

    Story getStory(String storyID) {
        for (Story s : data) {
            if (s.storyID.equals(storyID)) {
                return s;
            }
        }
        return null;
    }


    class StoryComparator implements Comparator<Story>
    {

        @Override
        public int compare(Story o1, Story o2) {
            if (o1.timestamp<o2.timestamp) return 1;
            else if (o1.timestamp==o2.timestamp) return 0;
            else return  -1;
        }
    }


}
