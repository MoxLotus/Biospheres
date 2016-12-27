package moxlotus;

import moxlotus.world.WorldProviderMox;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="biospheres", version="0.0")
public class Biospheres{
    public static final int DIM_ID = 0;//DimensionManager.getNextFreeDimId();
    public static final String DIM_NAME = "Biospheres";
    public static final DimensionType DIM_TYPE = DimensionType.register(DIM_NAME, "_" + DIM_NAME.toLowerCase(), DIM_ID, WorldProviderMox.class, true);
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        DimensionManager.unregisterDimension(0);
        DimensionManager.registerDimension(0, DIM_TYPE);
    }
}
