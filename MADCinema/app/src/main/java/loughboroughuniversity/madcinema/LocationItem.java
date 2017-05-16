package loughboroughuniversity.madcinema;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Adam on 16/05/2017.
 */



public class LocationItem {
    private String ID = "";
    private String Name = "";
    private String Address = "";

    public LocationItem (String ID, String Name, String Address){
        this.ID = ID;
        this.Name = Name;
        this.Address = Address;
    }

    public String getID() {
        return this.ID;
    }

    public String getName(){
        return this.Name;
    }

    public String getAddress(){
        return this.Address;
    }
}

