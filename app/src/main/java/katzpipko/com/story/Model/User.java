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


    public String getFullName()
    {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object obj) {

        if(getClass() != obj.getClass()) return false;
        User u = (User)obj;

       if ( u.uid.equals(uid) && u.email.equals(email) && u.firstName.equals(firstName) && u.lastName.equals(lastName) && u.profileImage.equals(profileImage) && u.createdTimeStamp==createdTimeStamp) return  true;
        return  false;

    }

    public User (User user)
    {
        this.setUid(user.getUid());
        this.setEmail(user.getEmail());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setProfileImage(user.getProfileImage());
        this.setCreatedTimeStamp(user.getCreatedTimeStamp());
        this.setUid(user.getUid());


    }


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
