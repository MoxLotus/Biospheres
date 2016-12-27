package moxlotus.world;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import moxlotus.world.biosphere.Biosphere;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;

public class BiomeProviderMox extends BiomeProvider{
    @Override
    public Biome[] getBiomes(@Nullable Biome[] listToReuse, int X, int Z, int width, int length, boolean cacheFlag){
        if (listToReuse == null || listToReuse.length < width * length) listToReuse = new Biome[width * length];
        if (cacheFlag && width == 16 && length == 16 && (X & 15) == 0 && (Z & 15) == 0){
            return super.getBiomes(listToReuse, X, Z, width, length, cacheFlag);
        }else{
            for (int i = 0; i < width; i++) for (int j = 0; j < length; j++)
                listToReuse[i+j*width] = Biosphere.getBiosphere(X, Z).getBiome();
            return listToReuse;
        }
    }
    @Override
    public boolean areBiomesViable(int X, int Z, int radius, List<Biome> allowed){
        Biome biome = Biosphere.getBiosphere(X, Z).getBiome();
        for (Biome b : getBiomes(null, X, Z, radius, radius)) if (allowed.contains(b)) return true;
        return false;
    }
    @Override
    @Nullable
    public BlockPos findBiomePosition(int X, int Z, int range, List<Biome> biomes, Random random){
        return new BlockPos(0, 0, 0);
    }
}
