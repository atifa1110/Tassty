package com.example.tassty.screen.cart

import com.example.core.domain.usecase.CreateOrderUseCase
import com.example.core.domain.usecase.GetCartsUseCase
import com.example.core.domain.usecase.GetUserAddressUseCase
import com.example.core.domain.usecase.RemoveAllCartMenuUseCase
import com.example.core.domain.usecase.RemoveCartMenuUseCase
import com.example.core.domain.usecase.UpdateCartHiddenUseCase
import com.example.core.domain.usecase.UpdateCartNotesUseCase
import com.example.core.domain.usecase.UpdateCartQuantityUseCase
import com.example.tassty.dispatcher.MainDispatcherRule
import com.example.tassty.utils.DataDummy
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.usecase.GetUserAvailableVoucherUseCase
import com.example.tassty.util.emptyRestaurant
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

class CartViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCartsUseCase: GetCartsUseCase = mockk()
    private val getUserAvailableVoucherUseCase: GetUserAvailableVoucherUseCase = mockk()
    private val getUserAddressUseCase: GetUserAddressUseCase = mockk()
    private val removeCartMenuUseCase: RemoveCartMenuUseCase = mockk()
    private val removeAllCartMenuUseCase: RemoveAllCartMenuUseCase = mockk()
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase = mockk()
    private val createOrderUseCase: CreateOrderUseCase = mockk()
    private val updateCartHiddenUseCase: UpdateCartHiddenUseCase = mockk()
    private val updateCartNotesUseCase: UpdateCartNotesUseCase = mockk()

    private lateinit var viewModel: CartViewModel

    private val cartFlow = MutableStateFlow(DataDummy.cartDomain)

    @Before
    fun setUp(){
        cartFlow.value = DataDummy.cartDomain

        try {
            if (org.threeten.bp.zone.ZoneRulesProvider.getAvailableZoneIds().isEmpty()) {
                org.threeten.bp.zone.ZoneRulesProvider.registerProvider(
                    org.threeten.bp.zone.TzdbZoneRulesProvider()
                )
            }
        } catch (e: Exception) {

        }

        every { getCartsUseCase() } returns cartFlow
        every { getUserAddressUseCase() } returns flowOf(DataDummy.addressResponseSuccess)
        every { getUserAvailableVoucherUseCase() } returns flowOf(DataDummy.voucherResponseSuccess)

        viewModel = CartViewModel(
            getCartsUseCase = getCartsUseCase,
            getUserAvailableVoucherUseCase = getUserAvailableVoucherUseCase,
            getUserAddressUseCase = getUserAddressUseCase,
            removeCartMenuUseCase = removeCartMenuUseCase,
            removeAllCartMenuUseCase = removeAllCartMenuUseCase,
            updateCartNotesUseCase = updateCartNotesUseCase,
            updateCartHiddenUseCase = updateCartHiddenUseCase,
            updateCartQuantityUseCase = updateCartQuantityUseCase,
            createOrderUseCase = createOrderUseCase
        )
    }


    @Test
    fun `initial state should display carts correctly when data is available`() = runTest {
        viewModel.uiState.test {
            val state = expectMostRecentItem()

            assertEquals("Indah Cafe", state.carts?.restaurant?.name)
            assertEquals(2, state.carts?.menus?.size)
            assertEquals("Shabu Premium Set", state.carts?.menus[0]?.name)
            assertFalse(state.carts?.menus[0]?.isSelected == true)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial state should display empty view when no carts are available`() = runTest {
        val emptyCart = DataDummy.cartDomain.copy(
            menus = emptyList(),
            restaurant = emptyRestaurant
        )

        every { getCartsUseCase() } returns flowOf(emptyCart)

        val viewModelEmpty = CartViewModel(
            getCartsUseCase = getCartsUseCase,
            getUserAvailableVoucherUseCase = getUserAvailableVoucherUseCase,
            getUserAddressUseCase = getUserAddressUseCase,
            removeCartMenuUseCase = removeCartMenuUseCase,
            removeAllCartMenuUseCase = removeAllCartMenuUseCase,
            updateCartNotesUseCase = updateCartNotesUseCase,
            updateCartHiddenUseCase = updateCartHiddenUseCase,
            updateCartQuantityUseCase = updateCartQuantityUseCase,
            createOrderUseCase = createOrderUseCase
        )

        viewModelEmpty.uiState.test {
            val state = awaitItem()
            assertEquals("",state.carts?.restaurant?.name)
            assertEquals(0,state.carts?.menus?.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCartSelectionChange should update subtotal and total order correctly`() = runTest {
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(0, initialState.subtotal)

            val targetCartId = DataDummy.cartDomain.menus[0].cartId
            viewModel.onEvent(CartUiEvent.OnCartSelectionChange(targetCartId))

            val finalState = expectMostRecentItem()
            val selectedMenu = finalState.carts?.menus?.find { it.cartId == targetCartId }

            assertTrue(selectedMenu?.isSelected == true)
            assertTrue(finalState.subtotal > 0)

            assertEquals(150000, finalState.subtotal)
            assertEquals(10000, finalState.deliveryFee)
            assertEquals(160000, finalState.totalOrder)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onNoteClicked then update note should work correctly and update the UI state`() = runTest {
        val targetCartId = DataDummy.cartDomain.menus[0].cartId
        val newNote = "Tolong sambalnya dipisah dan banyakin bawang goreng ya"

        coEvery { updateCartNotesUseCase(targetCartId, newNote) } coAnswers {
            val currentCart = cartFlow.value
            val updatedMenus = currentCart.menus.map {
                if (it.cartId == targetCartId) it.copy(notes = newNote) else it
            }
            cartFlow.value = currentCart.copy(menus = updatedMenus)
        }

        viewModel.uiState.test {
            expectMostRecentItem()

            viewModel.onEvent(CartUiEvent.OnNoteClicked(targetCartId))

            val stateWithSheet = awaitItem()
            assertTrue(stateWithSheet.isNoteSheetVisible)
            assertEquals(targetCartId, stateWithSheet.selectedCart?.cartId)

            viewModel.onEvent(CartUiEvent.OnUpdateNoteItem(targetCartId, newNote))
            advanceUntilIdle()
            viewModel.onEvent(CartUiEvent.OnDismissNoteSheet)

            val finalState = expectMostRecentItem()
            val updatedMenu = finalState.carts?.menus?.find { it.cartId == targetCartId }

            assertNotNull(updatedMenu)
            assertEquals(newNote, updatedMenu?.notes)
            assertFalse(finalState.isNoteSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onNoteClicked should emit NavigateToDetail effect when menu is customizable`() = runTest {
        val targetCartId = DataDummy.cartDomain.menus[1].cartId
        val expectedMenuId = DataDummy.cartDomain.menus[1].menuId

        viewModel.uiEffect.test {
            viewModel.onEvent(CartUiEvent.OnNoteClicked(targetCartId))

            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is CartUiEffect.NavigateDetailMenu)
            assertEquals(expectedMenuId, (effect as CartUiEffect.NavigateDetailMenu).id)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onIncrementQuantity should update total and subtotal when menu is selected`() = runTest {
        val targetCartId = cartFlow.value.menus[0].cartId

        coEvery { updateCartQuantityUseCase(targetCartId, true) } coAnswers {
            val currentCart = cartFlow.value
            val updatedMenus = currentCart.menus.map {
                if (it.cartId == targetCartId) it.copy(quantity = 2) else it
            }
            cartFlow.value = currentCart.copy(menus = updatedMenus)
        }

        viewModel.uiState.test {
            awaitItem()
            viewModel.onEvent(CartUiEvent.OnCartSelectionChange(targetCartId))
            viewModel.onEvent(CartUiEvent.OnIncrementQuantity(targetCartId))

            advanceUntilIdle()

            val finalState = expectMostRecentItem()
            val selectedMenu = finalState.carts?.menus?.find { it.cartId == targetCartId }

            assertEquals(2, selectedMenu?.quantity)
            assertEquals(300000, finalState.subtotal)
            assertEquals(10000, finalState.deliveryFee)
            assertEquals(310000, finalState.totalOrder)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDecrementQuantity should update total correctly`() = runTest {
        val initialCart = cartFlow.value
        val targetMenu = initialCart.menus[0]
        val targetCartId = targetMenu.cartId

        val cartWithTwoItems = initialCart.copy(
            menus = initialCart.menus.map {
                if (it.cartId == targetCartId) it.copy(quantity = 2) else it
            }
        )
        cartFlow.value = cartWithTwoItems

        coEvery { updateCartQuantityUseCase(targetCartId, false) } coAnswers {
            val currentCart = cartFlow.value
            val updatedMenus = currentCart.menus.map {
                if (it.cartId == targetCartId) it.copy(quantity = it.quantity - 1) else it
            }
            cartFlow.value = currentCart.copy(menus = updatedMenus)
        }

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(CartUiEvent.OnCartSelectionChange(targetCartId))
            viewModel.onEvent(CartUiEvent.OnDecrementQuantity(targetCartId))

            val finalState = expectMostRecentItem()
            val updatedMenu = finalState.carts?.menus?.find { it.cartId == targetCartId }

            assertEquals(1, updatedMenu?.quantity)
            assertEquals(targetMenu.price, finalState.subtotal) // Kembali ke harga 1 item

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onIncrementQuantity should not update subtotal when menu is not selected`() = runTest {
        val targetCartId = cartFlow.value.menus[0].cartId

        coEvery { updateCartQuantityUseCase(targetCartId, true) } coAnswers {
            val currentCart = cartFlow.value
            val updatedMenus = currentCart.menus.map {
                if (it.cartId == targetCartId) it.copy(quantity = 2) else it
            }
            cartFlow.value = currentCart.copy(menus = updatedMenus)
        }

        viewModel.uiState.test {
            awaitItem()
            viewModel.onEvent(CartUiEvent.OnIncrementQuantity(targetCartId))

            advanceUntilIdle()

            val finalState = expectMostRecentItem()
            val selectedMenu = finalState.carts?.menus?.find { it.cartId == targetCartId }

            assertEquals(2, selectedMenu?.quantity)
            assertEquals(0, finalState.subtotal)
            assertEquals(0, finalState.deliveryFee)
            assertEquals(0, finalState.totalOrder)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onIncrementQuantity with wrong ID should not change subtotal`() = runTest {
        val wrongId = "NOT-ID"

        coEvery { updateCartQuantityUseCase(any(), any()) } returns Unit
        viewModel.onEvent(CartUiEvent.OnIncrementQuantity(wrongId))
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(0, state.subtotal)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onSelectAllClicked should select all menus and update subtotal correctly`() = runTest {
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertEquals(0, initialState.subtotal)

            viewModel.onEvent(CartUiEvent.OnSelectAllClicked)
            advanceUntilIdle()

            val stateAfterSelect = expectMostRecentItem()
            val allMenus = stateAfterSelect.carts?.menus ?: emptyList()

            assertTrue("Semua menu harusnya terpilih", allMenus.all { it.isSelected })

            val expectedSubtotal = allMenus.sumOf { it.price * it.quantity }
            assertEquals(expectedSubtotal, stateAfterSelect.subtotal)
            assertTrue(stateAfterSelect.totalOrder > 0)

            viewModel.onEvent(CartUiEvent.OnSelectAllClicked)
            advanceUntilIdle()

            val stateAfterUnselect = expectMostRecentItem()
            val menusAfterUnselect = stateAfterUnselect.carts?.menus ?: emptyList()

            assertTrue("Semua menu harusnya batal terpilih", menusAfterUnselect.all { !it.isSelected })
            assertEquals(0, stateAfterUnselect.subtotal)
            assertEquals(0, stateAfterUnselect.totalOrder)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onShowLocationSheet should make sheet visible and load address data`() = runTest {
        viewModel.onEvent(CartUiEvent.OnShowLocationSheet)

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isLocationSheetVisible)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.addressState.test {
            val addressResult = awaitItem()
            assertNotNull(addressResult.data)
            assertEquals(2, addressResult.data?.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAddressSelected should update the selected address and close the sheet`() = runTest {
        val targetAddress = DataDummy.addressResponseSuccess.data?.get(0)
        val addressId = targetAddress?.id?:""
        assertNotNull(addressId)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(CartUiEvent.OnShowLocationSheet)
            assertEquals(true, awaitItem().isLocationSheetVisible)

            viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(addressId))
            viewModel.onEvent(CartUiEvent.OnSetLocationClicked)
            viewModel.onEvent(CartUiEvent.OnDismissVoucherSheet)

            val finalState = expectMostRecentItem()

            assertFalse(finalState.isLocationSheetVisible)
            assertEquals(addressId, finalState.selectedAddress?.id)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDismissLocationSheet should persist previous address if available`() = runTest {
        val initialAddress = DataDummy.addressResponseSuccess.data?.get(0)
        val initialAddressId = initialAddress?.id?:""

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(CartUiEvent.OnShowLocationSheet)
            assertEquals(true, awaitItem().isLocationSheetVisible)

            viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(initialAddressId))
            viewModel.onEvent(CartUiEvent.OnSetLocationClicked)

            val stateWithAddress = expectMostRecentItem()
            assertEquals(initialAddressId, stateWithAddress.selectedAddress?.id)

            viewModel.onEvent(CartUiEvent.OnShowLocationSheet)
            assertTrue(awaitItem().isLocationSheetVisible)

            viewModel.onEvent(CartUiEvent.OnDismissLocationSheet)

            val finalState = expectMostRecentItem()
            assertFalse(finalState.isLocationSheetVisible)
            assertEquals(null, finalState.selectedAddress?.id)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onShowVoucherSheet should make voucher sheet visible and load vouchers`() = runTest {
        viewModel.uiState.test {
            val stateWithCarts = awaitItem()
            assertNotNull(stateWithCarts.carts?.restaurant?.id)

            viewModel.onEvent(CartUiEvent.OnShowVoucherSheet)

            advanceUntilIdle()

            val finalUiState = expectMostRecentItem()
            assertTrue(finalUiState.isVoucherSheetVisible)
        }

        viewModel.voucherState.test {
            val voucherResult = expectMostRecentItem()
            assertNotNull(voucherResult.data)
            assertEquals(3, voucherResult.data?.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onVoucherSelected should update the selected voucher and close the sheet`() = runTest {
        val targetVoucher = DataDummy.voucherResponseSuccess.data?.get(0)
        val voucherId = targetVoucher?.id ?: ""

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(CartUiEvent.OnShowVoucherSheet)
            advanceUntilIdle()

            assertTrue(expectMostRecentItem().isVoucherSheetVisible)

            viewModel.onEvent(CartUiEvent.OnVoucherSelectionChanged(voucherId))
            viewModel.onEvent(CartUiEvent.OnApplyVoucherClicked)

            val finalState = expectMostRecentItem()

            assertEquals(voucherId, finalState.selectedVoucher?.id)
            assertFalse(finalState.isVoucherSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }


        viewModel.voucherState.test {
            val state = expectMostRecentItem()
            assertEquals(3, state.data?.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `totalOrder should remain zero when address and voucher are selected but no menu is checked`() = runTest {
        val targetAddress = DataDummy.addressResponseSuccess.data?.get(0)
        val targetVoucher = DataDummy.voucherResponseSuccess.data?.get(0)

        viewModel.uiState.test {
            val stateCarts = expectMostRecentItem()
            if (stateCarts.carts == null) {
                advanceUntilIdle()
            }

            viewModel.onEvent(CartUiEvent.OnShowLocationSheet)
            viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(targetAddress?.id ?: ""))
            viewModel.onEvent(CartUiEvent.OnSetLocationClicked)

            viewModel.onEvent(CartUiEvent.OnShowVoucherSheet)
            viewModel.onEvent(CartUiEvent.OnVoucherSelectionChanged(targetVoucher?.id ?: ""))
            viewModel.onEvent(CartUiEvent.OnApplyVoucherClicked)

            val finalState = expectMostRecentItem()

            assertNotNull(finalState.selectedAddress)
            assertNotNull(finalState.selectedVoucher)
            assertEquals(0, finalState.totalOrder)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `totalOrder should update correctly when address, voucher, and menu are all selected`() = runTest {
        val targetAddress = DataDummy.addressResponseSuccess.data?.get(0)
        val targetVoucher = DataDummy.voucherResponseSuccess.data?.get(0)
        val targetCard = DataDummy.cartDomain.menus[0].cartId

        viewModel.uiState.test {
            val stateCarts = expectMostRecentItem()
            if (stateCarts.carts == null) {
                advanceUntilIdle()
            }

            viewModel.onEvent(CartUiEvent.OnShowLocationSheet)
            advanceUntilIdle()
            viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(targetAddress?.id ?: ""))
            viewModel.onEvent(CartUiEvent.OnSetLocationClicked)

            viewModel.onEvent(CartUiEvent.OnShowVoucherSheet)
            advanceUntilIdle()
            viewModel.onEvent(CartUiEvent.OnVoucherSelectionChanged(targetVoucher?.id ?: ""))
            viewModel.onEvent(CartUiEvent.OnApplyVoucherClicked)

            viewModel.onEvent(CartUiEvent.OnCartSelectionChange(targetCard))

            val finalState = expectMostRecentItem()

            assertNotNull(finalState.selectedAddress)
            assertNotNull(finalState.selectedVoucher)
            val isAnyMenuSelected = finalState.carts?.menus?.any { it.isSelected } ?: false
            assertTrue(isAnyMenuSelected)
            assertEquals(30000, finalState.voucherDiscount)
            assertEquals(150000, finalState.subtotal)
            assertEquals(10000, finalState.deliveryFee)
            assertEquals(130000, finalState.totalOrder)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `checkout button should only be enabled when menu is selected and address exists`() = runTest {
        val targetAddress = DataDummy.addressResponseSuccess.data?.get(0)
        val targetCartId = DataDummy.cartDomain.menus[0].cartId

        viewModel.uiState.test {
            val state1 = expectMostRecentItem()
            assertFalse(state1.isCheckoutButtonEnabled)

            viewModel.onEvent(CartUiEvent.OnShowLocationSheet)
            advanceUntilIdle()
            viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(targetAddress?.id ?: ""))
            viewModel.onEvent(CartUiEvent.OnSetLocationClicked)
            assertFalse(expectMostRecentItem().isCheckoutButtonEnabled)

            viewModel.onEvent(CartUiEvent.OnCartSelectionChange(targetCartId))
            assertTrue(expectMostRecentItem().isCheckoutButtonEnabled)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updating a note should persist in the specific menu in the carts state`() = runTest {
        val targetCartId = DataDummy.cartDomain.menus[0].cartId
        val newNote = "Jangan pakai pedas ya, tolong dipisah sambalnya"

        coEvery { updateCartNotesUseCase(targetCartId, newNote) } coAnswers {
            val currentCart = cartFlow.value
            val updatedMenus = currentCart.menus.map {
                if (it.cartId == targetCartId) it.copy(notes = newNote) else it
            }
            cartFlow.value = currentCart.copy(menus = updatedMenus)
        }

        viewModel.uiState.test {
            expectMostRecentItem()

            viewModel.onEvent(CartUiEvent.OnUpdateNoteItem(targetCartId, newNote))

            advanceUntilIdle()

            val finalState = expectMostRecentItem()

            val updatedMenu = finalState.carts?.menus?.find { it.cartId == targetCartId }

            assertNotNull(updatedMenu)
            assertEquals(newNote, updatedMenu?.notes)
            assertFalse(finalState.isNoteSheetVisible)

            coVerify(exactly = 1) { updateCartNotesUseCase(targetCartId, newNote) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onRemoveCartItem should remove specific menu item and then dismiss sheet`() = runTest {
        val targetCartId = DataDummy.cartDomain.menus[0].cartId
        val remainingMenuCount = DataDummy.cartDomain.menus.size - 1

        coEvery { removeCartMenuUseCase(targetCartId) } coAnswers {
            val currentCart = cartFlow.value
            val updatedMenus = currentCart.menus.filterNot { it.cartId == targetCartId }
            cartFlow.value = currentCart.copy(menus = updatedMenus)
        }

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(CartUiEvent.OnShowRemoveItemSheet(targetCartId))

            val sheetState = awaitItem()
            assertTrue( sheetState.isRemoveItemSheetVisible)

            viewModel.onEvent(CartUiEvent.OnRemoveCartItem(targetCartId))
            advanceUntilIdle()
            viewModel.onEvent(CartUiEvent.OnDismissRemoveItemSheet)

            val finalState = expectMostRecentItem()
            val currentMenus = finalState.carts?.menus ?: emptyList()

            assertEquals(remainingMenuCount, currentMenus.size)
            assertFalse(currentMenus.any { it.cartId == targetCartId })
            assertFalse(finalState.isRemoveItemSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `removeAllCart should clear all menu items in the current UI state`() = runTest {
        coEvery { removeAllCartMenuUseCase(any()) } coAnswers {
            cartFlow.value = DataDummy.cartDomain.copy(menus = emptyList())
        }

        viewModel.uiState.test {
            expectMostRecentItem()

            viewModel.onEvent(CartUiEvent.OnShowDeleteAllSheet)

            val stateWithSheet = awaitItem()
            assertTrue(stateWithSheet.isDeleteAllSheetVisible)

            viewModel.onEvent(CartUiEvent.OnDeleteAll)

            advanceUntilIdle()

            viewModel.onEvent(CartUiEvent.OnDismissDeleteAllSheet)
            val finalState = expectMostRecentItem()

            val menuCount = finalState.carts?.menus?.size ?: 0
            assertEquals(0, menuCount)
            assertFalse(finalState.isDeleteAllSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `checkout success should emit success effect and update sheet visibility`() = runTest {
        val targetAddress = DataDummy.addressResponseSuccess.data?.get(0)
        val targetMenu = DataDummy.cartDomain.menus[0]
        val targetAddressId = targetAddress?.id ?: "addr-123"

        coEvery {
            createOrderUseCase(any(), any(), any(), any(), any())
        } returns flowOf(DataDummy.orderResponseSuccess)

        coEvery { updateCartHiddenUseCase(any(), any()) } returns Unit

        turbineScope {
            val stateTurbine = viewModel.uiState.testIn(backgroundScope)
            val effectTurbine = viewModel.uiEffect.testIn(backgroundScope)

            stateTurbine.awaitItem()

            viewModel.onEvent(CartUiEvent.OnShowLocationSheet)
            viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(targetAddressId))
            viewModel.onEvent(CartUiEvent.OnSetLocationClicked)

            viewModel.onEvent(CartUiEvent.OnCartSelectionChange(targetMenu.cartId))
            stateTurbine.expectMostRecentItem()

            viewModel.onEvent(CartUiEvent.OnShowDoubleCheckSheet)

            val stateWithDoubleCheck = stateTurbine.awaitItem()
            assertTrue(stateWithDoubleCheck.isDoubleCheckSheetVisible)

            viewModel.onEvent(CartUiEvent.OnCheckoutClicked)
            viewModel.onEvent(CartUiEvent.OnDismissDoubleCheckSheet)

            val effect = effectTurbine.awaitItem()
            assertTrue(effect is CartUiEffect.CheckoutSuccess)
            assertEquals("ORDER-123", (effect as CartUiEffect.CheckoutSuccess).id)

            val finalState = stateTurbine.expectMostRecentItem()
            assertFalse(finalState.isDoubleCheckSheetVisible)
            assertFalse(finalState.isLoading)

            stateTurbine.cancelAndIgnoreRemainingEvents()
            effectTurbine.cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `checkout failed should emit error effect and stop loading state`() = runTest {
        val targetAddress = DataDummy.addressResponseSuccess.data?.get(0)
        val targetMenu = DataDummy.cartDomain.menus[0]
        val errorMessage = "Connection Error"

        coEvery { createOrderUseCase(any(), any(), any(), any(), any()) } returns
                flowOf(TasstyResponse.Error(meta = Meta(
                    message = errorMessage,
                    status = "error",
                    code = 400
                ))
                )

        viewModel.onEvent(CartUiEvent.OnShowLocationSheet)
        viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(targetAddress?.id ?: ""))
        viewModel.onEvent(CartUiEvent.OnSetLocationClicked)
        viewModel.onEvent(CartUiEvent.OnCartSelectionChange(targetMenu.cartId))

        viewModel.uiEffect.test {
            viewModel.onEvent(CartUiEvent.OnCheckoutClicked)

            val effect = awaitItem()

            assertTrue(effect is CartUiEffect.ShowError)
            assertEquals(errorMessage, (effect as CartUiEffect.ShowError).message)

            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertFalse(state.isDoubleCheckSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
