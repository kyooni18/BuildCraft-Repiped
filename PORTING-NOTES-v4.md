# BuildCraft 1.21.1 port - v4 notes

This revision is based on fixed-v3 and focuses on persistence compatibility and the shared laser/range renderer.

## Fixed

- BlockPos NBT compatibility on Minecraft 1.21.1.
  - Added a keyed `NBTUtilBC.readBlockPos(CompoundTag, String)` dispatcher.
  - It accepts both the current int-array representation and legacy BuildCraft compound representations.
  - Migrated Miner, Snapshot/SnapshotBuilder, hanging-entity schematics, VolumeBox and volume locks away from `getCompound()` assumptions.
  - Centralized these writes through `NBTUtilBC.writeBlockPos`.
- Dynamic laser vertex format/state consistency.
  - Preserves and emits the real per-vertex normal instead of the hard-coded `(1,1,1)` normal.
  - Added a single block-atlas `entityCutoutNoCull` RenderType for dynamic BuildCraft lasers/range outlines.
  - Quarry, Builder, Filler, Construction Marker, Architect Table, VolumeBox, Tube and Laser render paths use the matching dynamic RenderType.
  - Snapshot rendering now separates model geometry and laser geometry buffers so each is emitted into the correct RenderType.
  - Dynamic/static laser caches are invalidated on resource-model reload to avoid stale atlas UV data.

## Regression tests added

- `NBTUtilBCBlockPosTest`
  - current int-array format
  - legacy compound format
  - missing-key behavior
- `LaserCompiledBufferTest`
  - verifies that vertex normals survive compilation

## Validation in the build environment

- All changed production sources compiled against Minecraft 1.21.1 / Forge 52.1.14 cached dependencies with Java 21.
- 4 focused JUnit tests passed.
- Final JAR ZIP integrity check passed and contains no duplicate ZIP entries.
- Bytecode inspection confirms TileMiner now calls the typed BlockPos dispatcher and Quarry uses the shared dynamic laser RenderType.

## Still requires in-game visual validation

The build environment cannot render the actual macOS client frame, so Quarry/marker/filler range outlines should be visually checked in-game. If their geometry is still offset after this RenderType/normal fix, the next target is the server/client `frameBox` values rather than the laser pipeline.
