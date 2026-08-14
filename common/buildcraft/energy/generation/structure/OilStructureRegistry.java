package buildcraft.energy.generation.structure;

import buildcraft.energy.BCEnergy;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class OilStructureRegistry {
    public static String STRUCTURE_OIL_SPOUT = "oil_spout";
    public static final ResourceLocation STRUCTURE_ID = ResourceLocation.fromNamespaceAndPath(BCEnergy.MODID, STRUCTURE_OIL_SPOUT);

    public static final StructureType<OilStructureFeature> STRUCTURE_TYPE = Registry.register(BuiltInRegistries.STRUCTURE_TYPE, STRUCTURE_ID, () -> OilStructureFeature.CODEC);

    public static final StructurePieceType STRUCTURE_PIECE_TYPE = Registry.register(BuiltInRegistries.STRUCTURE_PIECE, ResourceLocation.fromNamespaceAndPath(BCEnergy.MODID, "oil_structure"), (StructurePieceType.ContextlessType) OilStructure::deserialize);

    public static void clinit() {
    }
}
