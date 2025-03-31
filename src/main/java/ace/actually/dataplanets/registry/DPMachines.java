package ace.actually.dataplanets.registry;

import ace.actually.dataplanets.machine.ObservatoryMachine;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterialBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;


import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.ITEM_EXPORT_BUS;
import static com.gregtechceu.gtceu.common.data.GTMachines.ITEM_IMPORT_BUS;

public class DPMachines {




    public static final MultiblockMachineDefinition OBSERVATORY = Reg.REGISTRATE.multiblock("observatory", ObservatoryMachine::new)
            .langValue("Observatory")
            .rotationState(RotationState.NON_Y_AXIS)
            .appearanceBlock(GTBlocks.CASING_STAINLESS_CLEAN)
            .tier(GTValues.EV)
            .pattern((definition) -> FactoryBlockPattern.start()
                    .aisle("BBBKBBB", "KKKKKKK", "   K   ", "   K   ", "   K   ", " KKKKK "," VVCVV "," VVCVV ")
                    .aisle("BBBBBBB", "K     K", "       ", "       ", "       ", "K     K","VVVCVVV","V     V")
                    .aisle("BBBBBBB", "K     K", "       ", "       ", "       ", "K     K","VVVCVVV","V     V")
                    .aisle("KBBCBBK", "K     K", "K     K", "K     K", "K     K", "K     K","CCC CCC","C     C")
                    .aisle("BBBBBBB", "K     K", "       ", "       ", "       ", "K     K","VVVCVVV","V     V")
                    .aisle("BBBBBBB", "K     K", "       ", "       ", "       ", "K     K","VVVCVVV","V     V")
                    .aisle("TBIKOBT", "KKKSKKK", "   K   ", "   K   ", "   K   ", " KKKKK "," VVCVV "," VVCVV ")
                    .where('S', controller(blocks(definition.getBlock())))
                    .where('V',blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                    .where('B', blocks(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get()))
                    .where('K', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                    .where('C',blocks(GTBlocks.MATERIALS_TO_CASINGS.get(GTMaterials.SterlingSilver).get()))
                    .where(' ', any())
                    .where('T', abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1))
                    .where('I', blocks(ITEM_IMPORT_BUS[GTValues.EV].getBlock()))
                    .where('O', blocks(ITEM_EXPORT_BUS[GTValues.EV].getBlock()))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"),
                    GTCEu.id("block/multiblock/data_bank"), false)
            .recipeType(DPRecipeTypes.OBSERVATORY_RECIPE_TYPE)
            .register();

    public static final MultiblockMachineDefinition RADIO = Reg.REGISTRATE.multiblock("radio_telescope", ObservatoryMachine::new)
            .langValue("Radio Telescope")
            .rotationState(RotationState.NON_Y_AXIS)
            .appearanceBlock(GTBlocks.CASING_STAINLESS_CLEAN)
            .tier(GTValues.EV)
            .alwaysTryModifyRecipe(true)
            .pattern((definition) -> FactoryBlockPattern.start()
                    .aisle("B         B","B         B","B         B","B         B","B         B","B         B","KKKKKKKKKKK","KKKKKKKKKKK","B         B","B         B","B         B")
                    .aisle("           ","           ","           ","           "," KKKKKKKKK "," KKKKKKKKK ","K         K","KZZZZZZZZZK","           ","           ","           ")
                    .aisle("           ","           ","  KKKKKKK  ","  KKKKKKK  "," K       K "," KZZZZZZZK ","K         K","KZ       ZK","           ","           ","           ")
                    .aisle("           ","   KKKKK   ","  K     K  ","  K     K  "," K       K "," KZ     ZK ","K         K","KZ       ZK","           ","           ","           ")
                    .aisle("    KKK    ","   K   K   ","  K     K  ","  K     K  "," K       K "," KZ     ZK ","K         K","KZ       ZK","           ","           ","           ")
                    .aisle("    KKK    ","   K V K   ","  K  V  K  ","  K  V  K  "," K   V   K "," KZ  V  ZK ","K    V    K","KZ   V   ZK","     V     ","           ","           ")
                    .aisle("    KKK    ","   K   K   ","  K     K  ","  K     K  "," K       K "," KZ     ZK ","K         K","KZ       ZK","           ","           ","           ")
                    .aisle("     K     ","   KKKKK   ","  K     K  ","  K     K  "," K       K "," KZ     ZK ","K         K","KZ       ZK","           ","           ","           ")
                    .aisle("     K     ","           ","  KKKKKKK  ","  KKKKKKK  "," K       K "," KZZZZZZZK ","K         K","KZ       ZK","           ","           ","           ")
                    .aisle("     K     ","           ","           ","           "," KKKKKKKKK "," KKKKKKKKK ","K         K","KZZZZZZZZZK","           ","           ","           ")
                    .aisle("B  TIKOT  B","B    S    B","B         B","B         B","B         B","B         B","KKKKKKKKKKK","KKKKKKKKKKK","B         B","B         B","B         B")
                    .where('S', controller(blocks(definition.getBlock())))
                    .where('V',blocks(GCYMBlocks.CASING_LASER_SAFE_ENGRAVING.get()))
                    .where('B', blocks(GTMaterialBlocks.MATERIAL_BLOCKS.get(TagPrefix.frameGt,GTMaterials.StainlessSteel).get()))
                    .where('K', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                    .where('Z', blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                    .where(' ', any())
                    .where('T', abilities(PartAbility.INPUT_ENERGY).setMinGlobalLimited(1))
                    .where('I', blocks(ITEM_IMPORT_BUS[GTValues.EV].getBlock()))
                    .where('O', blocks(ITEM_EXPORT_BUS[GTValues.EV].getBlock()))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
                    GTCEu.id("block/multiblock/data_bank"), false)
            .recipeType(DPRecipeTypes.RADIO_RECIPE_TYPE)
            .register();


    public static void init() {
    }


}
