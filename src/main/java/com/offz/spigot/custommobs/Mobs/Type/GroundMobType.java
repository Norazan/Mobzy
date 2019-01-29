package com.offz.spigot.custommobs.Mobs.Type;


import com.offz.spigot.custommobs.Mobs.Behaviours.LivingMobBehaviour;
import com.offz.spigot.custommobs.Mobs.Behaviours.MobBehaviour;
import com.offz.spigot.custommobs.Mobs.Behaviours.SingleEntityMobBehaviour;
import com.offz.spigot.custommobs.Mobs.Fuwagi;
import com.offz.spigot.custommobs.Mobs.Inbyo;
import com.offz.spigot.custommobs.Mobs.Neritantan;
import com.offz.spigot.custommobs.Mobs.Rohana;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.World;
import org.bukkit.Material;

import java.util.function.Function;

public enum GroundMobType implements MobType {
    NERITANTAN("Neritantan", (short) 2,
            new LivingMobBehaviour(), Neritantan.class, Neritantan::new
    ),FUWAGI("Fuwagi", (short) 5,
            new LivingMobBehaviour(), Fuwagi.class, Fuwagi::new
    ),INBYO("Inbyo", (short) 8,
            new LivingMobBehaviour(), Inbyo.class, Inbyo::new
    ),ROHANA("Rohana", (short) 14,
            new SingleEntityMobBehaviour(), Rohana.class, Rohana::new
    );

    private final String name;
    private final short modelID;
    private final MobBehaviour behaviour;
    private final Material material;
    private final Class entityClass;
    private final Function<? super World, ? extends Entity> entityFromClass;

    GroundMobType(String name, short modelID, MobBehaviour behaviour, Class entityClass, Function<? super World, ? extends Entity> entityFromClass) {
        this.name = name;
        this.modelID = modelID;
        this.behaviour = behaviour;
        this.entityClass = entityClass;
        this.entityFromClass = entityFromClass;
        this.material = Material.DIAMOND_AXE;
        behaviour.setMobType(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public short getModelID() {
        return modelID;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Class getEntityClass() {
        return entityClass;
    }

    @Override
    public Function<? super World, ? extends Entity> getEntityFromClass() {
        return entityFromClass;
    }

    @Override
    public MobBehaviour getBehaviour() {
        return behaviour;
    }
}