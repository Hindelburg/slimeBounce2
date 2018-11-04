package com.company;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Timer;
import java.util.TimerTask;

public class Entity extends Visual{
    private static final int PLACES = 2;

    //Speeds
    private double currentXSpeed = 0;
    private double currentYSpeed = 0;

    public boolean active = false;

    //Vertical movement related variables (except speed)
    private final double maxJumpTime;
    private double gravity;
    private double jumpSpeed;
    private double jumpTime = 0;

    private boolean onGround = false;
    protected boolean jumping = false;

    //Controls
    protected boolean right = false;
    protected boolean left = false;
    protected boolean tryJumping = false;

    //Speed related things.
    private double maxSpeed;
    private double acc;


    public Entity(String pSprite, double posX, double posY, double scale, double maxJumpTime, double gravity, double jumpSpeed, double maxSpeed, double acc) {
        super(pSprite, posX, posY, scale);
        this.maxJumpTime = maxJumpTime;
        this.gravity = gravity;
        this.jumpSpeed = jumpSpeed;
        this.maxSpeed = maxSpeed;
        this.acc = acc;
    }

    private class State{
        public double posX;
        public double posY;
        public double currentXSpeed;
        public double currentYSpeed;
        public int count = 0;
        public double jumpTime;///
        public boolean jumping;///
        public boolean onGround;///

        public State(double posX, double posY, Entity entity){
            this.posX = posX;
            this.posY = posY;
            currentXSpeed = entity.currentXSpeed;
            currentYSpeed = entity.currentYSpeed;
            jumpTime = entity.jumpTime;
            jumping = entity.jumping;
            onGround = entity.onGround;
        }
    }

    private void adoptState(State state){
        setPosX(state.posX);
        setPosY(state.posY);
        currentXSpeed = state.currentXSpeed;
        currentYSpeed = state.currentYSpeed;
        jumpTime = state.jumpTime;
        jumping = state.jumping;
        onGround = state.onGround;
    }

    public void run(){
        if (active) {
            ySpeed();
            xSpeed();
            safeMove();
        }
    }

    private static double round(double value) {
        if (PLACES < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(PLACES, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void xSpeed() {
        if (right && left || (!right && !left)) {
            if (currentXSpeed > 0) {
                currentXSpeed = currentXSpeed - acc;
            } else if (currentXSpeed < 0) {
                currentXSpeed = currentXSpeed + acc;
            }
        } else if (right) {
            if (currentXSpeed < maxSpeed) {
                currentXSpeed = currentXSpeed + acc;
            }
        } else{
            if (currentXSpeed > -maxSpeed) {
                currentXSpeed = currentXSpeed - acc;
            }
        }
        currentXSpeed = round(currentXSpeed);
    }

    private void ySpeed() {
        currentYSpeed = currentYSpeed + gravity;
        if (onGround && tryJumping) {
            jumping = true;
            jumpTime = 0;
        }
        if (jumpTime > maxJumpTime) {
            jumping = false;
        }
        if (jumping) {
            currentYSpeed = -jumpSpeed;
        }
        if(!onGround) {
            jumpTime++;
        }
    }

    private boolean overlaps(double x, double y, Obj o){
        Rectangle r = new Rectangle((int)Math.round(x), (int)Math.round(y), getHitbox().getWidth(), getHitbox().getHeight());
        return (r.intersects(o.getPosX(), o.getPosY(), o.getHitbox().getWidth(), o.getHitbox().getHeight()));
    }

    private void safeMove(){
        double pPosX = getPosX() + currentXSpeed;
        double pPosY = getPosY() + currentYSpeed;
        inAir();

        State state = safeMovePrimary(pPosX, pPosY);
        if(state.count > 1) {
            state = safeMoveSecondary(pPosX, pPosY);
        }

        adoptState(state);
    }

    private void overlaps(Obj o, State state){
        if (overlaps(state.posX, state.posY, o)) {
            double above = o.getPosY() - (getPosY() + getHitbox().getHeight());
            double below = getPosY() - (o.getPosY() + o.getHitbox().getHeight());
            double left = o.getPosX() - (getPosX() + getHitbox().getWidth());
            double right = getPosX() - (o.getPosX() + o.getHitbox().getWidth());

            if ((above >= below) && (above >= left) && (above >= right)) {
                onGroundCollision(o, state);
            } else if ((below >= left) && (below >= right) && (below >= above)) {
                hitHeadCollision(o, state);
            } else if ((left >= below) && (left >= above) && (left >= right)) {
                hitRight(o, state);
            } else if ((right >= below) && (right >= above) && (right >= left)) {
                hitLeft(o, state);
            }
            state.count++;
        }
    }


    private State safeMovePrimary(double pPosX, double pPosY){
        State state = new State(pPosX, pPosY, this);
        for (Obj o : Level.objects) {
            overlaps(o, state);
        }
        return state;
    }

    private State safeMoveSecondary(double pPosX, double pPosY){
        State state = new State(pPosX, pPosY,this);
        //for (int i = Level.objects.length-1; i != 0; i--) {
        for (int i = Level.objects.size()-1; i != 0; i--) {
            //overlaps(Level.objects[i], state);
            overlaps(Level.objects.get(i), state);
        }
        return state;
    }

    public void hitRight(Obj o, State state){
        if(o.getCollision() == 0) {
            state.currentXSpeed = 0;
            state.posX = ((int)(o.getPosX() - getHitbox().getWidth()));
        }
        if(o instanceof DamageObj){
            death();
        }
    }

    public void hitLeft(Obj o, State state){
        if(o.getCollision() == 0) {
            state.currentXSpeed = 0;
            state.posX = ((int)(o.getPosX() + o.getHitbox().getWidth()));
        }
        if(o instanceof DamageObj){
            death();
        }
    }

    public void onGroundCollision(Obj o, State state) {
        if(o.getCollision() == 0) {
            state.currentYSpeed = 0;
            state.jumping = false;
            state.jumpTime = 0;
            state.onGround = true;
            state.posY = ((int)(o.getPosY() - getHitbox().getHeight()));
        }
        if(o instanceof DamageObj){
            death();
        }
    }

    public void hitHeadCollision(Obj o, State state) {
        if(o.getCollision() == 0) {
            state.currentYSpeed = 0;
            state.jumping = false;
            state.posY = ((int)(o.getPosY() + o.getHitbox().getHeight()));
        }
        if(o instanceof DamageObj){
            death();
        }
    }

    public void inAir() {
        onGround = false;
    }

    public void death(){
        active = false;
    }

    public void reset() {
        currentXSpeed = 0;

        currentYSpeed = 0;

        onGround = false;
        active = false;

        jumping = false;
        right = false;
        left = false;
        tryJumping = false;

        jumpTime = 0;

        setPosY(getDefaultPosY());
        setPosX(getDefaultPosX());
    }
}
