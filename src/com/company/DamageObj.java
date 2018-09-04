package com.company;

public class DamageObj extends Obj {
    public int damage;
    public double knockback;

    public DamageObj(String sprite, double x, double y, double scale, int collision, int damage, double knockback){
        super(sprite, x, y, scale, collision);
        this.damage = damage;
        this.knockback = knockback;
    }
}
