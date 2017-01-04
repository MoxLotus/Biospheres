package moxlotus.world;

import moxlotus.Biospheres;
import moxlotus.world.biosphere.Biosphere;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderMox extends WorldProvider{

    private float cloudHeight = 8.0f;
    @Override
    public DimensionType getDimensionType(){
        return Biospheres.DIM_TYPE;
    }
    @Override
    public IChunkGenerator createChunkGenerator(){
        return new ChunkProvider(worldObj);
    }
    public void setCloudHeight(float height){
        cloudHeight = height;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public float getCloudHeight(){
        return cloudHeight;
    }
    /**
     * This method is only called from {@link WorldProvider#registerWorld(World)}, so we will use it as an extension of that method
     */
    @Override
    protected void createBiomeProvider(){
        this.worldObj.setSeaLevel(Biosphere.HEIGHT);
        this.field_191067_f = true;
        this.biomeProvider = new BiomeProviderMox();
    }
}
