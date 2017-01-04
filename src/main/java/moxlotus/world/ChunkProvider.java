package moxlotus.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import moxlotus.world.biosphere.Biosphere;
import net.minecraft.block.Block;
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
        rand.setSeed(seed + x*4513l + z*3529l);
    }
    @Override
    public Chunk provideChunk(int X, int Z){
        this.setSeed(X, Z);
        Biosphere biosphere = Biosphere.getBiosphere(X, Z);
        Biome biome = Biosphere.getBiosphere(X, Z).getBiome();
        ChunkPrimer primer = provideChunkPrimer(X, Z, biosphere);
        Chunk chunk = new Chunk(world, primer, X, Z);

        byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) abyte[i] = (byte)Biome.getIdForBiome(biome);

        chunk.generateSkylightMap();
        return chunk;
    }
    
    protected ChunkPrimer provideChunkPrimer(int X, int Z, Biosphere biosphere){
        ChunkPrimer primer = new ChunkPrimer();
        Biome biome = biosphere.getBiome();
        biosphere.getChunk(primer, X, Z);
        biosphere.createStructures(X, Z, primer);


        IBlockState state;
        Block block;
        final IBlockState AIR = Blocks.AIR.getDefaultState();
        final IBlockState STONE = Blocks.STONE.getDefaultState();
        for (int i = 0; i < 16; ++i) for (int k = 0; k < 16; ++k)
            biome.genTerrainBlocks(this.world, this.rand, primer, i, k, 0);
        for (int i = 0; i < 16; ++i) for (int k = 0; k < 16; ++k) for (int j = 1; j < 256; j++){
            state = primer.getBlockState(i, j, k);
            block = state.getBlock();
            if (block == Blocks.BEDROCK || block == Blocks.COAL_ORE || block == Blocks.BONE_BLOCK) primer.setBlockState(i, j, k, AIR);
            else if (block == Blocks.GRAVEL){
                primer.setBlockState(i, j-1, k, STONE);
                break;
            }else if (state.isFullBlock()) break;
        }
        return primer;
    }
    @Override
    public void populate(int X, int Z){
        setSeed(X, Z);
        BlockPos pos = new BlockPos(X*16, 0, Z*16);
        Biosphere biosphere = Biosphere.getBiosphere(X, Z);
        biosphere.populate(world, rand, pos);
    }
    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z){
        return false;
    }
    @Override
    public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos){
        return Biosphere.getBiosphere(pos.getX()/16, pos.getZ()/16).getBiome().getSpawnableList(creatureType);
    }
    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_){
        return null;
    }
    @Override
    public void recreateStructures(Chunk chunkIn, int X, int Z){
        Biosphere.getBiosphere(X, Z).createStructures(X, Z, (ChunkPrimer)null);
    }
}
