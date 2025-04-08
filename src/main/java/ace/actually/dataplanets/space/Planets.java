package ace.actually.dataplanets.space;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class Planets {



    //https://github.com/zzzzBov/Voxel-Sphere-Generator/blob/master/assets/js/script.js
    public static void sphere(Level level, BlockPos pos, int radius)
    {
        boolean eq = false;
        radius = +radius;
        radius |= 0;
        if (radius < 1) {
            radius = 1;
        }
        int r2 = radius * radius;
        for (int i = 0; i < radius+1; i++) {
           int i2 = i * i;
           for (int j = 0; j < radius + 1; j++) {
                int j2 = j * j;

                if (i2 + j2 > r2 || i2 + j2 == r2 && !eq) {
                    break;
                }

                for (int k = 0; k < radius + 1; k++) {
                    int k2 = k * k;

                    if (i2 + j2 + k2 > r2 || i2 + j2 + k2 == r2 && !eq) {
                        break;
                    }

                    BlockState block = Blocks.GLASS.defaultBlockState();
                    level.setBlockAndUpdate(pos.offset(i,j,k),block);
                    level.setBlockAndUpdate(pos.offset(-i,j,k),block);
                    level.setBlockAndUpdate(pos.offset(-i,-j,k),block);
                    level.setBlockAndUpdate(pos.offset(-i,-j,-k),block);
                    level.setBlockAndUpdate(pos.offset(-i,j,-k),block);
                    level.setBlockAndUpdate(pos.offset(i,-j,-k),block);
                    level.setBlockAndUpdate(pos.offset(i,-j,k),block);
                    level.setBlockAndUpdate(pos.offset(i,j,-k),block);
                }
            }
        }
    }


    public static String fancyName(String name)
    {
        StringBuilder builder = new StringBuilder();
        int lp = 0;
        char[] charArray = name.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (Character.isAlphabetic(c)) {
                builder.append(Character.toUpperCase(c));
            } else {
                builder.append('-');
                lp=i;
                break;
            }
        }
        while (charArray.length>lp && Character.isDigit(charArray[lp]))
        {
            builder.append(charArray[lp]);
            lp++;
        }
        for (int i = lp; i < charArray.length; i++) {
            char c = charArray[i];
            if (Character.isAlphabetic(c)) {
                builder.append(Character.toLowerCase(c));
            }
        }
        System.out.println(builder);
        return builder.toString();
    }
}
