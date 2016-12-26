package moxlotus.world.biosphere;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

public class Biosphere{

    public static Biosphere getBiosphere(int chunkX, int chunkZ){
        return new Biosphere(Math.floorDiv(chunkX, 8), Math.floorDiv(chunkZ, 8));
    }
    
    public Biosphere(int x, int z){
        ;
    }
    
    public Biome getBiome(int x, int z){
        return Biomes.PLAINS;
    }

    public void getChunk(ChunkPrimer primer){
    	IBlockState state = Blocks.STONE.getDefaultState();
        for (int i = 0; i < 16; i++) for (int k = 0; k < 16; k++)
            for (int j = 0; j < 10; j++) primer.setBlockState(i, j, k, state);
    }
}
