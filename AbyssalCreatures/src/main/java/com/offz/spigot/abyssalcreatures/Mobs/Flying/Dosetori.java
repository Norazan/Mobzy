package com.offz.spigot.abyssalcreatures.Mobs.Flying;

import com.offz.spigot.mobzy.mobs.behaviours.HitBehaviour;
import com.offz.spigot.mobzy.mobs.types.FlyingMob;
import com.offz.spigot.mobzy.pathfinders.flying.PathfinderGoalDiveOnTargetAttack;
import net.minecraft.server.v1_13_R2.World;

public class Dosetori extends FlyingMob implements HitBehaviour {
    public Dosetori(World world) {
        super(world, "Dosetori");
        setSize(3F, 2F);
    }

    @Override
    public void createPathfinders() {
        super.createPathfinders();
        goalSelector.a(2, new PathfinderGoalDiveOnTargetAttack(this, -0.3, 6, 10, 8, 2, 0.6, 30));
    }
}