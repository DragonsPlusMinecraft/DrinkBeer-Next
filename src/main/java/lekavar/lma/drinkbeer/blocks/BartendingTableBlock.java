package lekavar.lma.drinkbeer.blocks;

import com.mojang.serialization.MapCodec;
import lekavar.lma.drinkbeer.blockentities.BartendingTableBlockEntity;
import lekavar.lma.drinkbeer.items.BeerMugItem;
import lekavar.lma.drinkbeer.items.MixedBeerBlockItem;
import lekavar.lma.drinkbeer.items.SpiceBlockItem;
import lekavar.lma.drinkbeer.registries.SoundEventRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class BartendingTableBlock extends BaseEntityBlock {
    public static final MapCodec<BartendingTableBlock> CODEC = simpleCodec(pro->new BartendingTableBlock());
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPENED = BooleanProperty.create("opened");
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 1, 2);

    public final static VoxelShape SHAPE = Block.box(0, 0.01, 0, 16, 16, 16);

    public BartendingTableBlock() {
        super(BlockBehaviour.Properties.of().ignitedByLava().mapColor(MapColor.WOOD).strength(2.0f).noOcclusion());
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPENED, true)
                .setValue(TYPE, 1));
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(OPENED).add(TYPE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BartendingTableBlockEntity(blockPos, blockState);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!world.isClientSide()) {
            BlockEntity blockentity = world.getBlockEntity(pos);
            if (blockentity instanceof BartendingTableBlockEntity bartendingTableBlockEntity) {
                if (player.isShiftKeyDown()) {
                    boolean currentOpenedState = state.getValue(OPENED);
                    world.playSound(null, pos, currentOpenedState ? SoundEventRegistry.BARTENDING_TABLE_CLOSE.get() : SoundEventRegistry.BARTENDING_TABLE_OPEN.get(), SoundSource.BLOCKS, 1f, 1f);
                    world.setBlockAndUpdate(pos, state.setValue(OPENED, !currentOpenedState));
                    return InteractionResult.CONSUME;
                } else {
                    var tryTake = bartendingTableBlockEntity.takeBeer(false);
                    if (!tryTake.isEmpty()) {
                        boolean flag = player.getInventory().add(tryTake);
                        if (!flag) {
                            ItemEntity itementity = player.drop(tryTake, false);
                            if (itementity != null) {
                                itementity.setNoPickUpDelay();
                                itementity.setThrower(player);
                            }
                        }
                    }
                }
            }
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!world.isClientSide()) {
            ItemStack itemStack = player.getItemInHand(hand);
            BlockEntity blockentity = world.getBlockEntity(pos);
            if (blockentity instanceof BartendingTableBlockEntity bartendingTableBlockEntity) {
                if (itemStack.getItem() instanceof MixedBeerBlockItem || itemStack.getItem() instanceof BeerMugItem) {
                    var placeIn = itemStack.copy();
                    placeIn.setCount(1);
                    var result = bartendingTableBlockEntity.placeBeer(placeIn);
                    if (result) {
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1f, 1f);
                        if (!player.getAbilities().instabuild)
                            itemStack.shrink(1);
                    }
                    return ItemInteractionResult.CONSUME;
                } else if (itemStack.getItem() instanceof SpiceBlockItem) {
                    var placeIn = itemStack.copy();
                    placeIn.setCount(1);
                    var result = bartendingTableBlockEntity.putSpice(placeIn);
                    if (result) {
                        world.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1f, 1f);
                        if (!player.getAbilities().instabuild)
                            itemStack.shrink(1);
                    }
                }
                return ItemInteractionResult.CONSUME;
            }
        }
        return ItemInteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }
}
