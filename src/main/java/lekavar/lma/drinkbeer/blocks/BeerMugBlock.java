package lekavar.lma.drinkbeer.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class BeerMugBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty AMOUNT = IntegerProperty.create("amount", 1, 3);

    protected static final VoxelShape[] SHAPE_BY_AMOUNT = new VoxelShape[]{
            Block.box(0, 0, 0, 16, 16, 16),
            Block.box(4, 0, 4, 12, 6, 12),
            Block.box(2, 0, 2, 14, 6, 14),
            Block.box(1, 0, 1, 15, 6, 15)
    };

    public BeerMugBlock() {
        super(Properties.of().ignitedByLava().mapColor(MapColor.WOOD).strength(1.0f).noOcclusion().pushReaction(PushReaction.DESTROY));
        this.registerDefaultState(
                this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(AMOUNT, 1)
        );
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return SHAPE_BY_AMOUNT[p_220053_1_.getValue(AMOUNT)];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AMOUNT, FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        return p_196271_2_ == Direction.DOWN && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        // Placing Beer
        if (itemStack.getItem().asItem() == state.getBlock().asItem()) {
            if (world != null && world.isClientSide()) {
                return InteractionResult.SUCCESS;
            } else if (world != null) {
                int amount = state.getValue(AMOUNT);
                int mugInHandCount = player.getItemInHand(hand).getCount();
                boolean isCreative = player.isCreative();
                switch (amount) {
                    case 1: {
                        world.setBlockAndUpdate(pos, state.getBlock().defaultBlockState().setValue(AMOUNT, 2).setValue(FACING, state.getValue(FACING)));
                        if (!isCreative) {
                            player.getItemInHand(hand).setCount(mugInHandCount - 1);
                        }
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1f, 1f);
                        return InteractionResult.CONSUME;
                    }
                    case 2: {
                        world.setBlockAndUpdate(pos, state.getBlock().defaultBlockState().setValue(AMOUNT, 3).setValue(FACING, state.getValue(FACING)));
                        if (!isCreative) {
                            player.getItemInHand(hand).setCount(mugInHandCount - 1);
                        }
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1f, 1f);
                        return InteractionResult.CONSUME;
                    }
                    default: {
                        return InteractionResult.FAIL;
                    }
                }
            } else {
                return InteractionResult.FAIL;
            }
        }
        // Retrieve Beer
        else if (itemStack.isEmpty()) {
            if (world.isClientSide()) {
                return InteractionResult.SUCCESS;
            } else {
                ItemStack takeBackBeer = state.getBlock().asItem().getDefaultInstance();
                ItemHandlerHelper.giveItemToPlayer(player, takeBackBeer);
                int amount = state.getValue(AMOUNT);
                switch (amount) {
                    case 3:
                    case 2:
                        world.setBlockAndUpdate(pos, state.getBlock().defaultBlockState().setValue(AMOUNT, amount - 1).setValue(FACING, state.getValue(FACING)));
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.AMBIENT, 0.5f, 0.5f);
                        return InteractionResult.CONSUME;
                    case 1:
                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.AMBIENT, 0.5f, 0.5f);
                        return InteractionResult.CONSUME;
                    default: {
                        return InteractionResult.FAIL;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        if (p_196260_2_.getBlockState(p_196260_3_.below()).getBlock() instanceof BeerMugBlock) return false;
        return Block.canSupportCenter(p_196260_2_, p_196260_3_.below(), Direction.UP);
    }
}
