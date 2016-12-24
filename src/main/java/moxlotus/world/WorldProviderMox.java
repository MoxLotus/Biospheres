package moxlotus.world;

import moxlotus.Biospheres;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderMox extends WorldProvider{
	protected float cloudHeight = 8.0f;

	@Override
	public DimensionType getDimensionType() {
		return Biospheres.DIM_TYPE;
	}
	@Override
	public IChunkGenerator createChunkGenerator(){
		return new ChunkProvider(this.worldObj);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight(){
		return cloudHeight;
	}
}
