package com.mineinabyss.mobzy.mobs.types

import com.mineinabyss.geary.ecs.Engine
import com.mineinabyss.mobzy.api.nms.aliases.NMSDataContainer
import com.mineinabyss.mobzy.api.nms.aliases.NMSEntityInsentient
import com.mineinabyss.mobzy.api.nms.aliases.toNMS
import com.mineinabyss.mobzy.ecs.components.minecraft.deathLoot
import com.mineinabyss.mobzy.ecs.components.minecraft.expToDrop
import com.mineinabyss.mobzy.mobs.CustomMob
import com.mineinabyss.mobzy.mobs.MobType
import com.mineinabyss.mobzy.registration.MobTypes
import net.minecraft.server.v1_16_R1.*
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_16_R1.event.CraftEventFactory
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity

abstract class MobBase : NMSEntityInsentient(error(""), error("")), CustomMob {
    override val mobzyId: Int = Engine.getNextId()
    override val type: MobType = MobTypes[this as CustomMob]

    //implementation of properties from CustomMob
    override var dead: Boolean
        get() = killed
        set(value) {
            killed = value
        }
    override val nmsEntity: EntityInsentient get() = this

    override fun lastDamageByPlayerTime(): Int = lastDamageByPlayerTime
    override val killScore: Int = aV

    override fun dropExp() = dropExperience()

    //overriding NMS methods
    override fun initPathfinder() = createPathfinders()

    override fun saveData(nbttagcompound: NMSDataContainer) = super.saveData(nbttagcompound).also { loadMobNBT(nbttagcompound) }
    override fun loadData(nbttagcompound: NMSDataContainer) = super.loadData(nbttagcompound).also { saveMobNBT(nbttagcompound) }

    override fun die(damagesource: DamageSource) = (this as CustomMob).die(damagesource)
    override fun getScoreboardDisplayName() = scoreboardDisplayNameMZ
    override fun getExpValue(entityhuman: EntityHuman): Int = expToDrop()

    override fun getSoundAmbient(): SoundEffect? = null.also { makeSound(soundAmbient) }
    override fun getSoundHurt(damagesource: DamageSource): SoundEffect? = null.also { makeSound(soundHurt) }
    override fun getSoundDeath(): SoundEffect? = null.also { makeSound(soundDeath) }
    override fun a(blockposition: BlockPosition, iblockdata: IBlockData) = makeSound(soundStep)
}

fun CustomMob.die(damageSource: DamageSource?) {
    val nmsWorld: World = entity.world.toNMS()
    if (!dead) {
        dead = true
        val killer = killer
        if (killScore >= 0 && killer != null) killer.a(nmsEntity, killScore, damageSource)
        // this line causes the entity to send a statistics update on death (we don't want this as it causes a NPE exception and crash)
//            killer?.a_(nmsEntity);

        if (entity.isSleeping) nmsEntity.entityWakeup()

        if (!nmsEntity.world.isClientSide) {
            if (nmsWorld.gameRules.getBoolean(GameRules.DO_MOB_LOOT) && killer != null) {
                dropItems(killer.bukkitEntity)
            } else CraftEventFactory.callEntityDeathEvent(nmsEntity)
        }
        nmsEntity.combatTracker.g() //resets combat tracker

        nmsWorld.broadcastEntityEffect(nmsEntity, 3.toByte())
        nmsEntity.pose = EntityPose.DYING
        //TODO add PlaceHolderAPI support
        deathLoot?.deathCommands?.forEach { Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), it) }
    }
}

fun CustomMob.dropItems(killer: HumanEntity) {
    val heldItem = killer.inventory.itemInMainHand
    val looting = heldItem.enchantments[Enchantment.LOOT_BONUS_MOBS] ?: 0
    val fire = heldItem.enchantments[Enchantment.FIRE_ASPECT] ?: 0 > 0
    CraftEventFactory.callEntityDeathEvent(nmsEntity, deathLoot?.drops?.toList()?.map { it.chooseDrop(looting, fire) }
            ?: listOf())
    nmsEntity.expToDrop = expToDrop()
    dropExp()
}