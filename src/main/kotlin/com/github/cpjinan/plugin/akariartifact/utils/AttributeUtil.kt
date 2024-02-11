package com.github.cpjinan.plugin.akariartifact.utils

import github.saukiya.sxattribute.SXAttribute
import org.bukkit.entity.Player
import org.serverct.ersha.AttributePlus

object AttributeUtil {
    fun getAttributeValue(player: Player, plugin: String, name: String): Number {
        return when (plugin) {
            "AttributePlus" -> AttributePlus.attributeManager.getAttributeData(player)
                .getAttributeValue(name)[0]

            "SX-Attribute" -> SXAttribute.getApi().getEntityData(player).getValues(name)[0]

            else -> throw IllegalArgumentException("Unsupported attribute plugin $plugin.")
        }
    }
}