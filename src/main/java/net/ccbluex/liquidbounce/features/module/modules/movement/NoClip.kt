/*
 * LiquidBounce+ Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/WYSI-Foundation/LiquidBouncePlus/
 */

package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils

@ModuleInfo(name = "NoClip", description = "Allows you to freely move through walls (A sandblock has to fall on your head).", category = ModuleCategory.MOVEMENT)
class NoClip: Module() {

    private var speed = FloatValue("Speed", 1 F, 0 F, 10 F)

    override fun onDisable() {
        mc.thePlayer.noClip = false
        mc.thePlayer.motionX = 0.0
        mc.thePlayer.motionY = 0.0
        mc.thePlayer.motionZ = 0.0
    }

    @EventTarget
    public void onMove(final MoveEvent event) {
        mc.thePlayer.noClip = true
        val speed = speed.get()
        // Get the direction yaw needed to set the speed using LiquidBounce MathUtils
        val yaw = MovementUtils.getDirectionRotation(mc.thePlayer!!.rotationYaw, mc.thePlayer!!.moveStrafing, mc.thePlayer!!.moveForward).toDouble()
        mc.thePlayer.onGround = false
        // Check if the player is moving, if not then don't make the player move horizontally
        if (mc.thePlayer!!.movementInput.moveForward != 0 F || mc.thePlayer!!.movementInput.moveStrafe != 0 F) {
            event.setX(-sin(Math.toRadians(yaw)) * speed)
            event.setZ(cos(Math.toRadians(yaw)) * speed)
        } else {
            event.zeroXZ()
        }
        // Check whether or not the player wants to go up or down, if both or none then don't make the player move vertically
        if (mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
            event.setY(-speed)
        } else if (!mc.gameSettings.keyBindSneak.isKeyDown() && mc.gameSettings.keyBindJump.isKeyDown()) {
            event.setY(speed)
        } else {
            event.setY(0)
        }
    }
}
