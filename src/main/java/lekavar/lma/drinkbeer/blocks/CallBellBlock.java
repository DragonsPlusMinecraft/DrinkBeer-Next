package lekavar.lma.drinkbeer.blocks;

import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.registries.ParticleTypeRegistry;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;


public class CallBellBlock extends Block {

    public final static VoxelShape SHAPE = Block.box(5.5f, 0, 5.5f, 10.5f, 4, 10.5f);

    public CallBellBlock() {
        super(Properties.of().mapColor(MapColor.METAL).strength(1.0f).pushReaction(PushReaction.DESTROY));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return p_196271_2_ == Direction.DOWN && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide()) {
            if (state.getBlock() == BlockRegistry.IRON_CALL_BELL.get()) {
                world.playSound(null, pos, SoundEventRegistry.IRON_CALL_BELL_TINKLING.get(), SoundSource.BLOCKS, 1.5f, 1f);
            } else if (state.getBlock() == BlockRegistry.GOLDEN_CALL_BELL.get()) {
                world.playSound(null, pos, SoundEventRegistry.GOLDEN_CALL_BELL_TINKLING.get(), SoundSource.BLOCKS, 1.8f, 1f);
            } else if (state.getBlock() == BlockRegistry.LEKAS_CALL_BELL.get()) {
                world.playSound(null, pos, SoundEventRegistry.LEKAS_CALL_BELL_TINKLE.get(), SoundSource.BLOCKS, 0.9f, 1f);
            }
        } else {
            double x = (double) pos.getX() + 0.5D;
            double y = (double) pos.getY() + 0.2D + new Random().nextDouble() / 4;
            double z = (double) pos.getZ() + 0.5D;
            if (state.getBlock() == BlockRegistry.IRON_CALL_BELL.get()) {
                world.addParticle(ParticleTypes.NOTE, x, y, z, 0.0D, 0.0D, 0.0D);
            } else if (state.getBlock() == BlockRegistry.GOLDEN_CALL_BELL.get()) {
                world.addParticle(ParticleTypes.NOTE, x, y, z, 0.0D, 0.0D, 0.0D);
            } else if (state.getBlock() == BlockRegistry.LEKAS_CALL_BELL.get()) {
                world.addParticle((SimpleParticleType) ParticleTypeRegistry.CALL_BELL_TINKLE_PAW.get(), x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        if (p_196260_2_.getBlockState(p_196260_3_.below()).getBlock() == Blocks.AIR) return false;
        return Block.canSupportCenter(p_196260_2_, p_196260_3_.below(), Direction.UP);
    }
}
