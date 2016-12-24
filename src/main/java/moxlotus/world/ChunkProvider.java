package moxlotus.world;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class ChunkProvider implements IChunkGenerator{
	
	protected final World world;
	
	public ChunkProvider(World world){
		super();
		this.world = world;
	}
	@Override
	public Chunk provideChunk(int x, int z){
		ChunkPrimer primer = new ChunkPrimer();
		IBlockState iblockstate = Blocks.BEDROCK.getDefaultState();
		
		for (int i = 0; i < 1; ++i){
            for (int j = 0; j < 16; ++j){
                for (int k = 0; k < 16; ++k){
                    primer.setBlockState(j, i, k, iblockstate);
                }
            }
        }
		
		Chunk chunk = new Chunk(world, primer, x, z);
		byte b = (byte)Biome.getIdForBiome(Biomes.PLAINS);
		byte[] abyte = chunk.getBiomeArray();
		for (int n = 0; n < abyte.length; n++) abyte[n] = b;
		
		chunk.generateSkylightMap();
		return chunk;
	}
	@Override
	public void populate(int x, int z){
		;
	}
	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z){
		return false;
	}
	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos){
		return new ArrayList<SpawnListEntry>();
	}
	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_){
		return null;
	}
	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z){
		;
	}
}
