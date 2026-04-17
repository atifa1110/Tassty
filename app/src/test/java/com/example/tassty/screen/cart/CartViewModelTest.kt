package com.example.tassty.screen.cart

import com.example.core.domain.usecase.CreateOrderUseCase
import com.example.core.domain.usecase.GetCartsUseCase
import com.example.core.domain.usecase.GetRestaurantVouchersUseCase
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
import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
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
    private val getRestaurantVouchersUseCase: GetRestaurantVouchersUseCase = mockk()
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
        every { getRestaurantVouchersUseCase(any()) } returns flowOf(DataDummy.voucherResponseSuccess)

        viewModel = CartViewModel(
            getCartsUseCase = getCartsUseCase,
            getRestaurantVouchersUseCase = getRestaurantVouchersUseCase,
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
    fun cartViewModel_init_cartsDisplayedCorrectly () = runTest {
        viewModel.uiState.test {
            val state = awaitItem()

            assertEquals("Indah Cafe", state.carts?.restaurant?.name)
            assertEquals(1, state.carts?.menus?.size)
            assertEquals("Shabu Premium Set", state.carts?.menus[0]?.name)
            assertFalse(state.carts?.menus[0]?.isSelected == true)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun cartViewModel_init_emptyCartsDisplay () = runTest {
        val emptyCart = DataDummy.cartDomain.copy(
            menus = emptyList(),
            restaurant = emptyRestaurant
        )

        every { getCartsUseCase() } returns flowOf(emptyCart)

        val viewModelEmpty = CartViewModel(
            getCartsUseCase = getCartsUseCase,
            getRestaurantVouchersUseCase = getRestaurantVouchersUseCase,
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
    fun cartViewModel_onMenuSelected_updatesTotalAndSubtotal() = runTest {
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
    fun cartViewModel_onIncrementQuantity_whenMenuIsSelected_updatesTotalAndSubtotal() = runTest {
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun  cartViewModel_onIncrementQuantity_whenMenuIsNotSelected_totalRemainsZero() = runTest {
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
    fun cartViewModel_onIncrementQuantity_withWrongId_doesNothing() = runTest {
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

    @Test
    fun cartViewModel_onShowLocationSheet_locationSheetVisibleAndAddressesLoaded() = runTest {
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
    fun cartViewModel_onAddressSelected_updatesSelectedAddressAndClosesSheet() = runTest {
        val targetAddress = DataDummy.addressResponseSuccess.data?.get(0)
        val addressId = targetAddress?.id?:""
        assertNotNull(addressId)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onEvent(CartUiEvent.OnShowLocationSheet)
            assertEquals(true, awaitItem().isLocationSheetVisible)

            viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(addressId))
            viewModel.onEvent(CartUiEvent.OnSetLocationClicked)

            val finalState = expectMostRecentItem()

            assertFalse(finalState.isLocationSheetVisible)
            assertEquals(addressId, finalState.selectedAddress?.id)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun cartViewModel_onDismissLocationSheet_previousAddressPersists() = runTest {
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
    fun cartViewModel_onShowVoucherSheet_promoVoucherVisibleAndVoucherLoaded() = runTest {
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

    @Test
    fun cartViewModel_onShowVoucherSheet_whenRestaurantIdEmpty_showsErrorMessage() = runTest {
        val emptyRestaurantCart = DataDummy.cartDomain.copy(
            restaurant = DataDummy.cartDomain.restaurant.copy(id = "")
        )
        every { getCartsUseCase() } returns flowOf(emptyRestaurantCart)

        viewModel = CartViewModel(
            getCartsUseCase = getCartsUseCase,
            getRestaurantVouchersUseCase = getRestaurantVouchersUseCase,
            getUserAddressUseCase = getUserAddressUseCase,
            removeCartMenuUseCase = removeCartMenuUseCase,
            removeAllCartMenuUseCase = removeAllCartMenuUseCase,
            updateCartNotesUseCase = updateCartNotesUseCase,
            updateCartHiddenUseCase = updateCartHiddenUseCase,
            updateCartQuantityUseCase = updateCartQuantityUseCase,
            createOrderUseCase = createOrderUseCase
        )

        viewModel.uiEffect.test {
            viewModel.onEvent(CartUiEvent.OnShowVoucherSheet)

            val effect = awaitItem()

            assertTrue(effect is CartUiEffect.ShowError)
            assertEquals("Restaurant Id is not exist", (effect as CartUiEffect.ShowError).message)

            cancelAndIgnoreRemainingEvents()
        }

        viewModel.voucherState.test {
            val state = awaitItem()
            assertEquals(null,state.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun cartViewModel_onVoucherSelected_updatesSelectedVoucherAndClosesSheet() = runTest {
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
            assertFalse("Sheet voucher harusnya tertutup setelah Apply", finalState.isVoucherSheetVisible)

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
    fun totalOrder_remainsZero_whenAddressAndVoucherSelectedButNoMenuChecked() = runTest {
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
    fun totalOrder_updatesCorrectly_whenAddressAndVoucherSelectedMenuChecked() = runTest {
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
    fun checkoutButton_enabled_onlyWhenMenuSelectedAndAddressExists() = runTest {
        val targetAddress = DataDummy.addressResponseSuccess.data?.get(0)
        val targetCartId = DataDummy.cartDomain.menus[0].cartId

        viewModel.uiState.test {
            val state1 = expectMostRecentItem()
            assertFalse("Harusnya mati karena belum ada apa-apa", state1.isCheckoutButtonEnabled)

            viewModel.onEvent(CartUiEvent.OnShowLocationSheet)
            advanceUntilIdle()
            viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(targetAddress?.id ?: ""))
            viewModel.onEvent(CartUiEvent.OnSetLocationClicked)
            assertFalse("Harusnya masih mati karena menu belum dipilih", expectMostRecentItem().isCheckoutButtonEnabled)

            viewModel.onEvent(CartUiEvent.OnCartSelectionChange(targetCartId))
            assertTrue("Harusnya nyala karena alamat ada dan menu dipilih", expectMostRecentItem().isCheckoutButtonEnabled)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updateNote_persistsInSpecificMenuInCarts() = runTest {
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

            assertNotNull("Menu harusnya ada", updatedMenu)
            assertEquals("Catatan di menu spesifik harus sesuai", newNote, updatedMenu?.notes)
            assertFalse("Sheet note harusnya sudah tertutup", finalState.isNoteSheetVisible)

            coVerify(exactly = 1) { updateCartNotesUseCase(targetCartId, newNote) }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun removeAllCart_clearsAllMenusInState() = runTest {
        coEvery { removeAllCartMenuUseCase(any()) } coAnswers {
            cartFlow.value = DataDummy.cartDomain.copy(menus = emptyList())
        }

        viewModel.uiState.test {
            expectMostRecentItem()

            viewModel.onEvent(CartUiEvent.OnDeleteAll)

            advanceUntilIdle()

            val finalState = expectMostRecentItem()

            val menuCount = finalState.carts?.menus?.size ?: 0
            assertEquals("Harusnya sudah tidak ada menu", 0, menuCount)

            assertFalse(finalState.isDeleteAllSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun checkout_success_emitsEffectAndUpdatesState() = runTest {
        val targetAddress = DataDummy.addressResponseSuccess.data?.get(0)
        val targetMenu = DataDummy.cartDomain.menus[0]

        coEvery { createOrderUseCase(any(), any(), any(), any(), any(), any(), any(), any()) } returns
                flowOf(DataDummy.orderResponseSuccess)
        coEvery { updateCartHiddenUseCase(any(), any()) } returns Unit

        viewModel.onEvent(CartUiEvent.OnShowLocationSheet)
        viewModel.onEvent(CartUiEvent.OnAddressSelectionChanged(targetAddress?.id ?: ""))
        viewModel.onEvent(CartUiEvent.OnSetLocationClicked)
        viewModel.onEvent(CartUiEvent.OnCartSelectionChange(targetMenu.cartId))

        viewModel.uiEffect.test {
            viewModel.onEvent(CartUiEvent.OnCheckoutClicked)

            val effect = awaitItem()
            assertTrue("Effect harusnya CheckoutSuccess", effect is CartUiEffect.CheckoutSuccess)
            assertEquals("ORDER-123", (effect as CartUiEffect.CheckoutSuccess).id)
        }

        viewModel.uiState.test {
            val finalState = expectMostRecentItem()
            assertFalse("Sheet double check harusnya sudah tutup", finalState.isDoubleCheckSheetVisible)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun checkout_failed_emitsErrorEffectAndStopsLoading() = runTest {
        val targetAddress = DataDummy.addressResponseSuccess.data?.get(0)
        val targetMenu = DataDummy.cartDomain.menus[0]
        val errorMessage = "Koneksi internet bermasalah atau stok habis"

        // 2. Mocking: Pakai TasstyResponse.Error
        coEvery { createOrderUseCase(any(), any(), any(), any(), any(), any(), any(), any()) } returns
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

            assertTrue("Effect harusnya ShowError saat gagal", effect is CartUiEffect.ShowError)
            assertEquals(errorMessage, (effect as CartUiEffect.ShowError).message)

            cancelAndIgnoreRemainingEvents()
        }

        viewModel.uiState.test {
            val state = expectMostRecentItem()
            assertFalse("Sheet double check tetap terbuka atau sesuai logic UI", state.isDoubleCheckSheetVisible)

            cancelAndIgnoreRemainingEvents()
        }
    }
}