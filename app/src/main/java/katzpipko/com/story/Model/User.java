package katzpipko.com.story.Model;

/**
 * Created by User on 2017-07-28.
 */

public class User {

    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private String profileImage;
    private long createdTimeStamp;

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setCreatedTimeStamp(long createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public long getCreatedTimeStamp() {
        return createdTimeStamp;
    }


    public User()
    {
         createdTimeStamp = System.currentTimeMillis()/1000;
    }

}
