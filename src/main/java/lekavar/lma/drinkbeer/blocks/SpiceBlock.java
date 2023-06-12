package lekavar.lma.drinkbeer.blocks;

import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.utils.mixedbeer.Spices;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class SpiceBlock extends HalfTransparentBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public final static VoxelShape DEFAULT_SHAPE = box(5.5, 0, 5.5, 10.5, 2, 10.5);
    public final static VoxelShape SPICE_FROZEN_PERSIMMON_SHAPE = box(5.5, 0, 5.5, 10.5, 3.5, 10.5);
    public final static VoxelShape SPICE_DRIED_SELAGINELLA = box(5.5, 0, 5.5, 10.5, 4.5, 10.5);

    public SpiceBlock() {
        super(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.WOOD).strength(1.0f).pushReaction(PushReaction.DESTROY));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
        if (this.equals(BlockRegistry.SPICE_FROZEN_PERSIMMON.get())) {
            return SPICE_FROZEN_PERSIMMON_SHAPE;
        }
        if (this.equals(BlockRegistry.SPICE_DRIED_SELAGINELLA.get())) {
            return SPICE_DRIED_SELAGINELLA;
        }
        return DEFAULT_SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return p_196271_2_ == Direction.DOWN && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        if (p_196260_2_.getBlockState(p_196260_3_.below()).getBlock() == Blocks.AIR) return false;
        return Block.canSupportCenter(p_196260_2_, p_196260_3_.below(), Direction.UP);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            SimpleParticleType particle = Spices.byItem(this.asItem()).getFlavor().getParticle();
            double x = (double) pos.getX() + 0.5D;
            double y = (double) pos.getY() + 0.3D + new Random().nextDouble() / 4;
            double z = (double) pos.getZ() + 0.5D;
            if (particle != null) {
                world.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
