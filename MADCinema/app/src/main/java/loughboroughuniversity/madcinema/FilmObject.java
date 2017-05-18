package loughboroughuniversity.madcinema;

/**
 * Created by Jithin on 17/05/2017.
 */

public class FilmObject {
    private int id;
    private String name;
    private String description;
    private int rating;
    private int age;
    private String trailer;
    private String img;
    private String added;

    public FilmObject(int idIn, String nameIn, String descriptionIn,int ratingIn, int ageIn, String trailerIn, String imgIn){
        id = idIn;
        name = nameIn;
        description= descriptionIn;
        rating=ratingIn;
        age=ageIn;
        trailer=trailerIn;
        img=imgIn;


    }
    public int getId(){ return id;}
    public String getName(){return name;}
    public String getDescription(){return description;}
    public int getRating(){return rating;}
    public int getAge(){return age;}
    public String getTrailer(){return trailer;}
    public String getImg(){return img;}
    public String getAdded(){return added;}

}
