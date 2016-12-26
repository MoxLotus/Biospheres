package moxlotus.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import moxlotus.world.biosphere.Biosphere;
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
    private final long seed;
    protected Random rand;

	public ChunkProvider(World world){
        super();
        this.world = world;
        seed = world.getSeed();
        rand = new Random(seed);
    }

    private void setSeed(int x, int z){
        rand.setSeed(seed + x*7477l + z*8501l);
    }
    @Override
    public Chunk provideChunk(int x, int z){
        this.setSeed(x, z);
        Biome[] biomesForGeneration = this.world.getBiomeProvider().getBiomes(null, x, z, 16, 16);
        ChunkPrimer primer = provideChunkPrimer(x, z, biomesForGeneration);
        Chunk chunk = new Chunk(world, primer, x, z);

        byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) abyte[i] = (byte)Biome.getIdForBiome(biomesForGeneration[i]);

        chunk.generateSkylightMap();
        return chunk;
    }
    
    protected ChunkPrimer provideChunkPrimer(int x, int z, Biome[] biomesForGeneration){
    	ChunkPrimer primer = new ChunkPrimer();
    	Biosphere b = Biosphere.getBiosphere(x, z);
    	b.getChunk(primer);

        for (int i = 0; i < 16; ++i) for (int j = 0; j < 16; ++j){
            Biome biome = biomesForGeneration[j + i*16];
            biome.genTerrainBlocks(this.world, this.rand, primer, x * 16 + i, z * 16 + j, 0/*this.depthBuffer[j + i * 16]*/);
        }
        return primer;
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
