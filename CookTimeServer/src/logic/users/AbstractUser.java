package logic.users;

import com.google.gson.Gson;
import logic.ServerManager;
import logic.structures.simplelist.SimpleList;
import logic.structures.stack.Stack;
import logic.utilities.Encrypter;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPOutputStream;

import static logic.ServerManager.NOTIFICATION_ADDED_RECIPE;
import static logic.ServerManager.RECIPE_TYPE;


public class AbstractUser implements Comparable<AbstractUser>, Serializable {
    //User personal data and credentials
    protected String name;
    protected String email;
    protected String password;

    protected   Stack<Recipe> newsFeed;
    //For notifications logic, UI and logical work
    protected   SimpleList<AbstractUser> followers = new SimpleList<>();
    protected   SimpleList<AbstractUser> following = new SimpleList<>();
    //user info for UI representation

    protected  MyMenu myMenu = new MyMenu();

    protected   Stack<String> notifications;


    //methods for the logic of followers
    public void addFollower(String jsonUser) {
        AbstractUser user = new Gson().fromJson(jsonUser, AbstractUser.class);
        user.followUser(new Gson().toJson(this, this.getClass()));
        this.followers.append(user);
    }

    public void followUser(String jsonUser) {
        AbstractUser user = new Gson().fromJson(jsonUser, AbstractUser.class);
        this.followers.append(user);
        this.following.append(user);
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public int compareTo(AbstractUser user) {
        return this.email.compareTo(user.email);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "AbstractUser{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getPass() {
        return this.password;
    }

    public void encryptPassword() throws NoSuchAlgorithmException {
        this.password = Encrypter.encryptPassword(this.password);
    }

    public void addNotification(String notification) {
        this.notifications.push(notification);
    }

    public void addRecipe(String jsonRecipe) {
        System.out.println(this + "added a recipe: " + jsonRecipe);
        this.myMenu.addRecipe(jsonRecipe);
        Recipe newRecipe = new Gson().fromJson(jsonRecipe, RECIPE_TYPE);
        //add the new notification and the recipe to the feed of the followers
        for (int i = 0; i < this.followers.len(); i++) {
            this.followers.indexElement(i).updateFeed(newRecipe);
        }
            ServerManager.getInstance().saveInfo();
    }

    private void updateFeed(Recipe newRecipe) {
        this.newsFeed.push(newRecipe);
        this.addNotification(this.name + NOTIFICATION_ADDED_RECIPE);

    }

    public String getSerializedFollowers() {
        return
                new Gson().toJson(this.followers, SimpleList.class);
    }

    public String getSerializedFollowing() {
        return
                new Gson().toJson(this.following, SimpleList.class);
    }
    public String getSerializedNotifications(){
        return new Gson().toJson(this.notifications,Stack.class);
    }
    public String getSerializedNewsFeed(){
        return new Gson().toJson(this.newsFeed,Stack.class);
    }
    public String getSerializedMyMenu(){
        return new Gson().toJson(this.myMenu,MyMenu.class);
    }
}
