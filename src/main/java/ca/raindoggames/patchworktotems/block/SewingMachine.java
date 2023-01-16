package ca.raindoggames.patchworktotems.block;

import javax.annotation.Nullable;

import ca.raindoggames.patchworktotems.blockentity.TileSewingMachineEntity;
import ca.raindoggames.patchworktotems.menu.SewingMenu;
import ca.raindoggames.patchworktotems.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class SewingMachine extends Block implements EntityBlock {
	public final static DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	private static final Component CONTAINER_TITLE = new TranslatableComponent("container.sewingmachine_title");

	public SewingMachine(Properties properties) {
		super(properties.noOcclusion());
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}
	
	@Override
    public BlockState getStateForPlacement(BlockPlaceContext  pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
    
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    	builder.add(FACING);
     }

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TileSewingMachineEntity(pos, state);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
	    if (!level.isClientSide()) {
	    	BlockEntity entity = level.getBlockEntity(pos);
	    	if (entity instanceof TileSewingMachineEntity) {
	    		NetworkHooks.openGui((ServerPlayer)player, (TileSewingMachineEntity)entity, pos);
	    	} else {
	    		throw new IllegalStateException("Our Container provider is missing!");
	    	}
	    }
		return InteractionResult.sidedSuccess(level.isClientSide());
	}
	
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return type == ModBlockEntities.SEWING_MACHINE.get() ? TileSewingMachineEntity::tick : null;
	}
}
