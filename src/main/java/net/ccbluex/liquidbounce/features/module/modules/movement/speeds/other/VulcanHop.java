/*
 * LiquidBounce++ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/TheMosKau/LiquidBouncePlusPlus/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventState;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode;
import net.ccbluex.liquidbounce.utils.MovementUtils;

public class VulcanHop extends SpeedMode {

    public VulcanHop() {
        super("VulcanHop");
    }

    private int groundTick = 0;

    @Override
    public void onMotion(MotionEvent eventMotion) {
        final Speed speed = (Speed) LiquidBounce.moduleManager.getModule(Speed.class);

        if(speed == null || eventMotion.getEventState() != EventState.PRE)
            return;
        
        if (MovementUtils.isMoving()) {
            mc.timer.timerSpeed = (mc.thePlayer.motionY > 0) ? 1.65f : 0.73f;

            if (mc.thePlayer.onGround) {
                if (groundTick >= 0) {
                        MovementUtils.strafe(0.483f);
                    }
                    if (0.42 != 0) {
                        mc.thePlayer.motionY = 0.42;
                    }
                }
                groundTick++;
            } else {
                groundTick = 0;
                mc.thePlayer.motionY += 0.0f * 0.03;
            }
        } 
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMotion() {
        
    }

    @Override
    public void onMove(MoveEvent event) {
    }
}
