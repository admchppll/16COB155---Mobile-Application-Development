package loughboroughuniversity.madcinema;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Adam on 16/05/2017.
 */



public class LocationItem {
    private String ID = "";
    private static String Name = "";
    private String Address = "";
    private Boolean Preferred = false;

    public LocationItem (String ID, String Name, String Address){
        this.ID = ID;
        this.Name = Name;
        this.Address = Address;
    }

    public String getID() {
        return this.ID;
    }

    public static String getName(){
        return Name;
    }

    public String getAddress(){
        return this.Address;
    }

    public boolean getPreferred(){
        return this.Preferred;
    }

    public void setPreferred(){
        this.Preferred = true;
    }

    public void setNotPreferred(){
        this.Preferred = false;
    }
}

