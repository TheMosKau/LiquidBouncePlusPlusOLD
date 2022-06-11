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
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.event.StrafeEvent
import kotlin.math.*

@ModuleInfo(name = "NoClip", description = "Allows you to freely move through walls (A sandblock has to fall on your head).", category = ModuleCategory.MOVEMENT)
class NoClip : Module() {


    private val speed = FloatValue("Speed", 0.32F, 0F, 10F)
	
    override fun onDisable() {
        mc.thePlayer.noClip = false
		mc.thePlayer.motionX = 0.0
        mc.thePlayer.motionY = 0.0
        mc.thePlayer.motionZ = 0.0
    }
	
    @EventTarget
    fun onStrafe(event: StrafeEvent) {
		mc.thePlayer.noClip = true
		mc.thePlayer.jumpMovementFactor = 0.0f
        val speed = speed.get()
        val yaw = getMoveYaw().toDouble()
		mc.thePlayer.onGround = false
		if (mc.thePlayer!!.movementInput.moveForward != 0F || mc.thePlayer!!.movementInput.moveStrafe != 0F) {
			mc.thePlayer!!.motionX = -Math.sin(Math.toRadians(yaw)) * speed.toDouble()
			mc.thePlayer!!.motionZ = Math.cos(Math.toRadians(yaw)) * speed.toDouble()
		} else {
			mc.thePlayer!!.motionX = 0.0
			mc.thePlayer!!.motionZ = 0.0
		}
		if (mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.thePlayer!!.motionY = -speed.toDouble()
		}
		else if (!mc.gameSettings.keyBindSneak.isKeyDown() && mc.gameSettings.keyBindJump.isKeyDown()) {
			mc.thePlayer!!.motionY = speed.toDouble()
		} else {
			mc.thePlayer!!.motionY = 0.0
		}
    }
    private fun getMoveYaw(): Float {
        var moveYaw = mc.thePlayer!!.rotationYaw
        if (mc.thePlayer!!.moveForward != 0F && mc.thePlayer!!.moveStrafing == 0F) {
            moveYaw += if(mc.thePlayer!!.moveForward > 0) 0 else 180
        } else if (mc.thePlayer!!.moveForward != 0F && mc.thePlayer!!.moveStrafing != 0F) {
            if (mc.thePlayer!!.moveForward > 0) {
                moveYaw += if (mc.thePlayer!!.moveStrafing > 0) -45 else 45
            } else {
                moveYaw -= if (mc.thePlayer!!.moveStrafing > 0) -45 else 45
            }
            moveYaw += if(mc.thePlayer!!.moveForward > 0) 0 else 180
        } else if (mc.thePlayer!!.moveStrafing != 0F && mc.thePlayer!!.moveForward == 0F) {
            moveYaw += if(mc.thePlayer!!.moveStrafing > 0) -90 else 90
        }
        return moveYaw
    }
}
