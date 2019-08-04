package com.tcaulk.cof.entity;

import com.badlogic.gdx.math.Vector2;
import com.tcaulk.cof.TextureStore;
import com.tcaulk.cof.animation.Animation;
import com.tcaulk.cof.input.InputController;

import java.util.Arrays;
import java.util.List;

public class Player extends AnimatedEntity {

    private static final float MOVEMENT_SPEED = 150f;
    private static final float VELOCITY_SMOOTH = 0.52f;

    public static enum PlayerAnimation {
        IDLE(0),
        RUN(1);

        private int animationIndex;
        PlayerAnimation(int animationIndex) {
            this.animationIndex = animationIndex;
        }

        public int getAnimationIndex() {
            return animationIndex;
        }
    }

    private static List<Animation> animations = Arrays.asList(
            new Animation(TextureStore.SPRITESHEET, Animation.getFrameRange(368, 172, 16, 20, 4)),
            new Animation(TextureStore.SPRITESHEET, Animation.getFrameRange(432, 172, 16, 20, 4))
    );

    private InputController inputController;

    public Player(int x, int y, InputController inputController) {
        super(x, y, 48, 60, true, MOVEMENT_SPEED, animations, PlayerAnimation.IDLE.getAnimationIndex(), EntityDirection.Right);

        this.inputController = inputController;
    }

    @Override
    public void update(float delta) {
        Vector2 acceleration = new Vector2(0, 0);
        boolean moving = false;
        EntityDirection nextEntityDirection = entityDirection;

        if(inputController.isDownPressed()) {
            acceleration.y += -movementSpeed * delta;
            moving = true;
        } else if(inputController.isUpPressed()) {
            acceleration.y += +movementSpeed * delta;
            moving = true;
        }

        if(inputController.isLeftPressed()) {
            acceleration.x += -movementSpeed * delta;
            moving = true;
            nextEntityDirection = EntityDirection.Left;
        } else if(inputController.isRightPressed()) {
            acceleration.x += movementSpeed * delta;
            moving = true;
            nextEntityDirection = EntityDirection.Right;
        }

        velocity.add(acceleration);
        position.add(velocity);

        velocity.x *= VELOCITY_SMOOTH;
        velocity.y *= VELOCITY_SMOOTH;

        if(moving) {
            setAnimationIndex(PlayerAnimation.RUN.getAnimationIndex());
        } else {
            setAnimationIndex(PlayerAnimation.IDLE.getAnimationIndex());
        }

        entityDirection = nextEntityDirection;
    }
}