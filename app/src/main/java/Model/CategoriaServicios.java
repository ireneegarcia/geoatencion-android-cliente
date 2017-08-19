package Model;

import java.util.ArrayList;

/**
 * Created by Irene on 19/8/2017.
 */

public class CategoriaServicios {
    private String Id;
    private String Name;
    private String Icon;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public static ArrayList<CategoriaServicios> get(){
        ArrayList<CategoriaServicios> items = new ArrayList<CategoriaServicios>();
        CategoriaServicios item = new CategoriaServicios();
        item.setId("1");
        item.setName("BOD");
        items.add(item);

        /*item = new Bank();
        item.setCode("0002");
        item.setName("BNC");
        items.add(item);

        item = new Bank();
        item.setCode("003");
        item.setName("BANESCO");
        items.add(item);

        item = new Bank();
        item.setCode("0004");
        item.setName("BVBA");
        items.add(item);

        item = new Bank();
        item.setCode("0005");
        item.setName("DEL SUR");
        items.add(item);*/

        return items;
    }
}
