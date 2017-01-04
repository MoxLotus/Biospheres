package moxlotus;

import java.util.LinkedList;
import java.util.List;

import moxlotus.world.WorldProviderMox;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.world.DimensionType;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.OreGenEvent.GenerateMinable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid="biospheres", version="0.0")
public class Biospheres{
    public static final int DIM_ID = 0;//DimensionManager.getNextFreeDimId();
    public static final String DIM_NAME = "Biospheres";
    public static final DimensionType DIM_TYPE = DimensionType.register(DIM_NAME, "_" + DIM_NAME.toLowerCase(), DIM_ID, WorldProviderMox.class, true);
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        DimensionManager.unregisterDimension(0);
        DimensionManager.registerDimension(0, DIM_TYPE);
        MinecraftForge.ORE_GEN_BUS.register(this);
    }
    @SubscribeEvent
    public void onOreGenEvent(GenerateMinable e){
        e.setResult(Event.Result.DENY);
    }
}
