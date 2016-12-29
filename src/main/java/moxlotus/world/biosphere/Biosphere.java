package moxlotus.world.biosphere;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class Biosphere{
    public static final int CHUNKS = 8;
    public static final int BLOCKS_PER_CHUNK = 16;
    public static final int MAX_RADIUS = 60;
    public static final int MIN_RADIUS = 30;

    public long seed = Minecraft.getMinecraft().getIntegratedServer().getEntityWorld().getSeed();
    protected NoiseGeneratorOctaves octaves = new NoiseGeneratorOctaves(new Random(seed), 4);

    protected Biome biome;
    protected int radius;

    public static Biosphere getBiosphere(int X, int Z){
        return new Biosphere(Math.floorDiv(X, CHUNKS), Math.floorDiv(Z, CHUNKS));
    }

    public Biosphere(int X, int Z){
        Random rand = new Random(seed + X*4513l + Z*3529l);
        biome = Biome.REGISTRY.getRandomObject(rand);
        radius = rand.nextInt(1+MAX_RADIUS-MIN_RADIUS)+MIN_RADIUS;
    }

    public Biome getBiome(){
        return biome;
    }

    public void getChunk(ChunkPrimer primer, int X, int Z){
        IBlockState state = Blocks.STONE.getDefaultState();
        int c = CHUNKS*BLOCKS_PER_CHUNK/2 - 1;
        int x = c - modulus(X, CHUNKS)*BLOCKS_PER_CHUNK;
        int y = 127;
        int yMin = y-radius;
        int yMax = y+8;
        int z = c - modulus(Z, CHUNKS)*BLOCKS_PER_CHUNK;
        int radiusPow2 = radius*radius;
        double threshold = -8d;
        
        if (biome == Biomes.HELL){
            state = Blocks.NETHERRACK.getDefaultState();
            yMax = y+radius;
            threshold = 0;
        }else if (biome == Biomes.SKY){
            state = Blocks.END_STONE.getDefaultState();
            yMin = y-(radius/2);
            threshold = 4;
        }
        
        int ySize = yMax-yMin;
        double[] array = new double[BLOCKS_PER_CHUNK*ySize*BLOCKS_PER_CHUNK];
        array = octaves.generateNoiseOctaves(array, (X*BLOCKS_PER_CHUNK), yMin, (Z*BLOCKS_PER_CHUNK), BLOCKS_PER_CHUNK, ySize, BLOCKS_PER_CHUNK, 0.5, 0.5, 0.5);
        
        for (int i = 0; i < BLOCKS_PER_CHUNK; i++) for (int j = yMin; j < yMax; j++) for (int k = 0; k < BLOCKS_PER_CHUNK; k++)
            if(calcDisPow2(x, y, z, i, j, k) < radiusPow2 && array[(i*BLOCKS_PER_CHUNK+ k)*ySize + j-yMin] > threshold)
                primer.setBlockState(i, j, k, state);
        generate(primer, X, Z);
    }

    private static final int modulus(int a, int b){
        return (a%b + b)%b;
    }

    private static final int calcDisPow2(int x1, int y1, int z1, int x2, int y2, int z2){
        return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) + (z2-z1)*(z2-z1);
    }

    protected void generate(ChunkPrimer primer, int X, int Z){
        ;
    }

    public void populate(int x, int z) {
        ;
    }
}
