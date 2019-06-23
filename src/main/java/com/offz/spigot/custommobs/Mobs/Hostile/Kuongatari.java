package com.offz.spigot.custommobs.Mobs.Hostile;

import com.offz.spigot.custommobs.Builders.MobBuilder;
import com.offz.spigot.custommobs.Mobs.Behaviours.HitBehaviour;
import com.offz.spigot.custommobs.Mobs.MobDrop;
import com.offz.spigot.custommobs.Pathfinders.PathfinderGoalWalkingAnimation;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Material;

public class Kuongatari extends HostileMob implements HitBehaviour {
    static MobBuilder builder = new MobBuilder("Kuongatari", 35)
            .setDrops(new MobDrop(Material.LIME_DYE, 1, 2));

    public Kuongatari(World world) {
        super(world, builder);
        this.setSize(0.6F, 0.6F);
    }

    @Override
    protected void createPathfinders() {
        super.createPathfinders();
        this.goalSelector.a(0, new PathfinderGoalWalkingAnimation(this, builder.getModelID()));
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(7.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.3D);
        this.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(0.2);
    }
}