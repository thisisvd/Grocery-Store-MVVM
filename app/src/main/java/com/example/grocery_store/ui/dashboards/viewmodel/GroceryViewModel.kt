package com.example.grocery_store.ui.dashboards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocery_store.core.Resource
import com.example.grocery_store.data.repository.GroceryRepository
import com.example.grocery_store.domain.CartCountAndSum
import com.example.grocery_store.domain.GroceryCartItem
import com.example.grocery_store.domain.GroceryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryViewModel @Inject constructor(
    private val repository: GroceryRepository,
) : ViewModel() {

    private val _groceryItems = MutableStateFlow<Resource<List<GroceryItem>>?>(null)
    val groceryItems: StateFlow<Resource<List<GroceryItem>>?> = _groceryItems

    fun groceryItems() = viewModelScope.launch {
        _groceryItems.emit(Resource.Loading())
        repository.groceryItems().collect { groceryList ->
            if (groceryList.isNotEmpty()) {
                _groceryItems.emit(Resource.Success(groceryList))
            } else {
                _groceryItems.emit(Resource.Error("No local grocery item found!"))
            }
        }
    }

    fun searchInGroceryItems(searchQuery: String) = viewModelScope.launch {
        repository.searchInGroceryItems(searchQuery).collect { groceryList ->
            _groceryItems.emit(Resource.Success(groceryList))
        }
    }

    fun updateItemInCart(
        item: GroceryCartItem,
        isIncrement: Boolean,
        itemStatus: (Boolean) -> Unit
    ) =
        viewModelScope.launch {
            var cartItem = item
            CoroutineScope(Dispatchers.IO).launch {
                val presentItemList = repository.getCartItem(item.userId, item.itemId)
                if (presentItemList.isNotEmpty()) {
                    val presentItem = presentItemList[0]
                    val newCount =
                        if (isIncrement) presentItem.itemCount + 1 else presentItem.itemCount - 1

                    if (newCount <= 0) {
                        deleteItemFromCart(presentItem.userId, presentItem.itemId)
                        itemStatus(true)
                        return@launch
                    }

                    cartItem = GroceryCartItem(
                        cartItemId = presentItem.cartItemId,
                        itemId = presentItem.itemId,
                        itemName = presentItem.itemName,
                        itemDescription = presentItem.itemDescription,
                        itemPrice = presentItem.itemPrice,
                        itemCount = newCount,
                        itemImage = presentItem.itemImage,
                        userId = presentItem.userId
                    )
                }
                val status = repository.insertItemInCart(cartItem)
                itemStatus(status >= 0)
            }
        }

    fun deleteItemFromCart(userId: Int, itemId: Int) = viewModelScope.launch {
        repository.deleteItemFromCart(userId, itemId)
    }

    fun updateStatusAllItemFromCart(userId: Int, status: (Boolean) -> Unit) =
        viewModelScope.launch {
            val deleteStatus = repository.updateStatusAllItemFromCart(userId)
            status(deleteStatus >= 0)
        }

    fun getTotalItemCountAndSum(userId: Int, itemDetails: (CartCountAndSum) -> Unit) =
        viewModelScope.launch {
            repository.getTotalCountAndSum(userId).collect { it ->
                itemDetails(it)
            }
        }

    private val _myCartItems = MutableStateFlow<List<GroceryCartItem>>(emptyList())
    val myCartItems: StateFlow<List<GroceryCartItem>> = _myCartItems

    fun myCartItems(userId: Int) = viewModelScope.launch {
        repository.getCartItems(userId).collect { cartItems ->
            _myCartItems.emit(cartItems)
        }
    }
}