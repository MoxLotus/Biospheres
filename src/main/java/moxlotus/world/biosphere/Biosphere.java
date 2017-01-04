package moxlotus.world.biosphere;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class Biosphere{
    public static final int CHUNKS = 8;
    public static final int BLOCKS_PER_CHUNK = 16;
    public static final int MAX_RADIUS = 60;
    public static final int MIN_RADIUS = 30;
    public static final int HEIGHT = 127;

    public static long seed = Minecraft.getMinecraft().getIntegratedServer().getEntityWorld().getSeed();
    protected static NoiseGeneratorOctaves octaves = new NoiseGeneratorOctaves(new Random(seed), 4);
    protected static List<Biome> biomes;
    {
        biomes = new ArrayList<Biome>(20);
        biomes.add(Biomes.BIRCH_FOREST);
        biomes.add(Biomes.DESERT);
        biomes.add(Biomes.EXTREME_HILLS);
        biomes.add(Biomes.FOREST);
        biomes.add(Biomes.HELL);
        biomes.add(Biomes.JUNGLE);
        biomes.add(Biomes.MESA_ROCK);
        biomes.add(Biomes.MUSHROOM_ISLAND);
        biomes.add(Biomes.MUTATED_FOREST);
        biomes.add(Biomes.MUTATED_ICE_FLATS);
        biomes.add(Biomes.MUTATED_PLAINS);
        biomes.add(Biomes.PLAINS);
        biomes.add(Biomes.REDWOOD_TAIGA);
        biomes.add(Biomes.ROOFED_FOREST);
        biomes.add(Biomes.SAVANNA);
        biomes.add(Biomes.SKY);
        biomes.add(Biomes.SWAMPLAND);
    }

    protected Biome biome;
    protected int radius;

    public static Biosphere getBiosphere(int X, int Z){
        return new Biosphere(Math.floorDiv(X, CHUNKS), Math.floorDiv(Z, CHUNKS));
    }

    public Biosphere(int X, int Z){
        Random rand = new Random(seed + X*36847l + Z*45389l);
        biome = biomes.get(rand.nextInt(biomes.size()));
        radius = rand.nextInt(1+MAX_RADIUS-MIN_RADIUS)+MIN_RADIUS;
    }

    public Biome getBiome(){
        return biome;
    }

    public void getChunk(ChunkPrimer primer, int X, int Z){
        Biome biome = getBiome();
        IBlockState state = Blocks.STONE.getDefaultState();
        int c = CHUNKS*BLOCKS_PER_CHUNK/2 - 1;
        int x = c - modulus(X, CHUNKS)*BLOCKS_PER_CHUNK;
        int y = HEIGHT;
        int yStart = y-radius;
        int yMin = y;
        int yMax = (int) (biome.getHeightVariation()*20);
        int yEnd = yMin + yMax;
        int z = c - modulus(Z, CHUNKS)*BLOCKS_PER_CHUNK;
        int radiusPow2 = radius*radius;
        double threshold = -8d;
        
        if (biome == Biomes.HELL){
            state = Blocks.NETHERRACK.getDefaultState();
            yEnd = y+radius;
            threshold = 0;
        }else if (biome == Biomes.SKY){
            state = Blocks.END_STONE.getDefaultState();
            yStart = y-(radius/2);
            threshold = 4;
            radiusPow2 =c*c;
        }
        
        int ySize = yEnd-yStart;
        double[] array = new double[BLOCKS_PER_CHUNK*ySize*BLOCKS_PER_CHUNK];
        array = octaves.generateNoiseOctaves(array, (X*BLOCKS_PER_CHUNK), yStart, (Z*BLOCKS_PER_CHUNK), BLOCKS_PER_CHUNK, ySize, BLOCKS_PER_CHUNK, 0.5, 0.5, 0.5);
        
        for (int i = 0; i < BLOCKS_PER_CHUNK; i++) for (int k = 0; k < BLOCKS_PER_CHUNK; k++){
            int height = yMin + norm(-10f, 10f, (float)array[(i*BLOCKS_PER_CHUNK+ k)*ySize], 0, yMax);
            if (biome == Biomes.HELL) height = y+radius;
            for (int j = yStart; j < height; j++){
                if(calcDisPow2(x, y, z, i, j, k) < radiusPow2 && array[(i*BLOCKS_PER_CHUNK+ k)*ySize + j-yStart] > threshold)
                    primer.setBlockState(i, j, k, state);
            }
        }
    }
    
    private static final int norm(float minIn, float maxIn, float value, int minOut, int maxOut){
        float y = value*(maxOut-minOut) + maxIn*minOut - minIn*maxOut;
        y /= maxIn - minIn;
        return (int)y;
    }

    private static final int modulus(int a, int b){
        return (a%b + b)%b;
    }

    private static final int calcDisPow2(int x1, int y1, int z1, int x2, int y2, int z2){
        return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) + (z2-z1)*(z2-z1);
    }

    public void populate(World world, Random rand, BlockPos pos){
        Biome biome = getBiome();
        biome.decorate(world, rand, pos);
        if (biome.theBiomeDecorator.dirtGen == null) return;
        biome.theBiomeDecorator.dirtGen.generate(world, rand, pos.add(rand.nextInt(16), rand.nextInt(256), rand.nextInt(16)) );
        biome.theBiomeDecorator.dioriteGen.generate(world, rand, pos.add(rand.nextInt(16), rand.nextInt(256), rand.nextInt(16)) );
        biome.theBiomeDecorator.graniteGen.generate(world, rand, pos.add(rand.nextInt(16), rand.nextInt(256), rand.nextInt(16)) );
        biome.theBiomeDecorator.andesiteGen.generate(world, rand, pos.add(rand.nextInt(16), rand.nextInt(256), rand.nextInt(16)) );
    }

    public void createStructures(int x, int z, ChunkPrimer primer) {
        ;
    }
}
