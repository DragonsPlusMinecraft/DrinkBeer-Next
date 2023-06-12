package lekavar.lma.drinkbeer.blocks;

import lekavar.lma.drinkbeer.blockentities.MixedBeerBlockEntity;
import lekavar.lma.drinkbeer.managers.SpiceAndFlavorManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class MixedBeerBlock extends BaseEntityBlock {
    public final static VoxelShape ONE_MUG_SHAPE = Block.box(4, 0, 4, 12, 6, 12);

    public MixedBeerBlock() {
        super(Properties.of().ignitedByLava().mapColor(MapColor.WOOD).strength(1.0f).noOcclusion().pushReaction(PushReaction.DESTROY));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return ONE_MUG_SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return p_196271_2_ == Direction.DOWN && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    @Override
    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        if (p_196260_2_.getBlockState(p_196260_3_.below()).getBlock() instanceof BeerMugBlock) return false;
        return Block.canSupportCenter(p_196260_2_, p_196260_3_.below(), Direction.UP);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.isEmpty()) {
            if (!world.isClientSide()) {
                world.removeBlock(pos, false);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        MixedBeerBlockEntity te = (MixedBeerBlockEntity) world.getBlockEntity(pos);
        if (te != null) {
            ItemStack mixedBeerItemStack = te.getPickStack();
            Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), mixedBeerItemStack);
        } else {
            System.out.println("Somthing wrong with dropping mixed beer item stack!");
        }
        super.onRemove(state, world, pos, newState, moved);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MixedBeerBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (world.isClientSide()) {
            super.animateTick(state, world, pos, random);
            if (random.nextInt(5) == 0) {
                MixedBeerBlockEntity entity = (MixedBeerBlockEntity) world.getBlockEntity(pos);
                SimpleParticleType particle = SpiceAndFlavorManager.getLastSpiceFlavorParticle(entity.getSpiceList());
                if (random.nextInt(5) == 0) {
                    double x = (double) pos.getX() + 0.5D;
                    double y = (double) pos.getY() + 0.5D + random.nextDouble() / 4;
                    double z = (double) pos.getZ() + 0.5D;
                    world.addParticle(particle, x, y, z, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

}
