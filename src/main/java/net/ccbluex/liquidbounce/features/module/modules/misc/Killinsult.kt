package net.ccbluex.liquidbounce.features.module.modules.misc

import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.file.FileManager
import net.ccbluex.liquidbounce.utils.FileUtils
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.entity.player.EntityPlayer
import java.io.File

@ModuleInfo(name = "KillInsults", spacedName = "Kill Insults", description = "Automatically insult the player you killed", category = ModuleCategory.MISC)
object KillInsults : Module() {

    val modeValue = ListValue(
        "Mode", arrayOf(
            "UwU",
            "UwUWithWords",
            "OnlyWords"
        ), "RawWords"
    )
    private val waterMarkValue = BoolValue("WaterMark", true)

    private val insultFile = File(LiquidBounce.fileManager.dir, "killinsult.json")
    var insultWords = mutableListOf<String>()

    private var target: EntityPlayer? = null

    val kaTarget = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target

    val actualTarget = if (kaTarget != null && kaTarget is EntityPlayer) kaTarget
                      else null

    init {
        loadFile()
    }

    fun loadFile() {
        fun convertJson() {
            insultWords.clear()
            insultWords.addAll(insultFile.readLines(Charsets.UTF_8).filter { it.isNotBlank() })

            val json = JsonArray()
            insultWords.map { JsonPrimitive(it) }.forEach(json::add)
            insultFile.writeText(FileManager.PRETTY_GSON.toJson(json), Charsets.UTF_8)
        }

        try {
            // check file exists
            if (!insultFile.exists()) {
                FileUtils.unpackFile(insultFile, "assets/minecraft/liquidbounce+/killinsult.json")
            }
            // read it
            val json = JsonParser().parse(insultFile.readText(Charsets.UTF_8))
            if (json.isJsonArray) {
                insultWords.clear()
                json.asJsonArray.forEach {
                    insultWords.add(it.asString)
                }
            } else {
                // not jsonArray convert it to jsonArray
                convertJson()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            convertJson()
        }
    }

    fun getRandomOne(): String {
        return insultWords[RandomUtils.nextInt(0, insultWords.size - 1)]
    }

    @EventTarget
    fun onKilled() {
        val target = actualTarget

        if (target !is EntityPlayer) {
            return
        return target.isDead();
        }

        when (modeValue.get().toLowerCase()) {
            "uwu" -> {
                sendInsultWords("UwU ${target.name}", target.name)
            }
            "uwuwithwords" -> {
                sendInsultWords("UwU ${target.name} " + getRandomOne(), target.name)
            }
            "onlywords" -> {
                sendInsultWords(getRandomOne(), target.name)
            }
        }
    }

    private fun sendInsultWords(msg: String, name: String) {
        var message = msg.replace("%name%", name)
        if (waterMarkValue.get()) {
            message = "[UwU] $message"
        }
        mc.thePlayer.sendChatMessage(message)
    }

    override val tag: String
        get() = modeValue.get()
}
