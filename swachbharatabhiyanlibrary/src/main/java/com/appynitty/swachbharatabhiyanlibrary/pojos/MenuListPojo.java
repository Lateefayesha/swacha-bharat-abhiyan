package com.appynitty.swachbharatabhiyanlibrary.pojos;

/**
 * Created by Ayan Dey on 25/10/18.
 */
public class MenuListPojo {

    private String menuName;
    private Integer image;

    public MenuListPojo(String menuName, Integer image) {
        this.menuName = menuName;
        this.image = image;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
