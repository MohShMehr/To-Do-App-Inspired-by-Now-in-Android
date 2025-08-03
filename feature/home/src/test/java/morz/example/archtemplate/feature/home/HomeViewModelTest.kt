package morz.example.archtemplate.feature.home

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import morz.example.archtemplate.core.domain.ToDoRepository
import morz.example.archtemplate.core.network.model.toDo.NetworkToDo
import morz.example.archtemplate.core.network.model.toDo.NetworkToDoResponse
import morz.example.archtemplate.core.network.util.ApiResult
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for HomeViewModel.
 * Tests state management, data flow, and business logic.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mockToDoRepository: ToDoRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockToDoRepository = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test successful data loading updates state to Success`() = runTest {
        // Given: Mock repository returns successful response
        val mockTodos = listOf(
            NetworkToDo(id = 1, todo = "Test Todo 1", completed = false, userId = 13),
            NetworkToDo(id = 2, todo = "Test Todo 2", completed = true, userId = 13)
        )
        val mockResponse = NetworkToDoResponse(
            todos = mockTodos,
            total = 2,
            skip = 0,
            limit = 10
        )
        val successResult = ApiResult.Success(mockResponse)
        val successFlow = MutableStateFlow(successResult)

        whenever(mockToDoRepository.getToDos()).thenReturn(successFlow)

        // When: HomeViewModel is initialized
        homeViewModel = HomeViewModel(mockToDoRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: State should be Success with correct data
        val currentState = homeViewModel.homeUiState.first()
        assertTrue(currentState is HomeUiState.Success)

        val successState = currentState as HomeUiState.Success
        assertEquals(2, successState.homeData.data.size)
        assertEquals("Test Todo 1", successState.homeData.data[0].todo)
        assertEquals("Test Todo 2", successState.homeData.data[1].todo)
        assertFalse(successState.homeData.data[0].completed!!)
        assertTrue(successState.homeData.data[1].completed!!)
    }

    @Test
    fun `test error response updates state to Error`() = runTest {
        // Given: Mock repository returns error response
        val errorMessage = "Network error occurred"
        val errorResult = ApiResult.Error(Exception(errorMessage))
        val errorFlow = MutableStateFlow(errorResult)

        whenever(mockToDoRepository.getToDos()).thenReturn(errorFlow)

        // When: HomeViewModel is initialized
        homeViewModel = HomeViewModel(mockToDoRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: State should be Error with correct message
        val currentState = homeViewModel.homeUiState.first()
        assertTrue(currentState is HomeUiState.Error)

        val errorState = currentState as HomeUiState.Error
        assertEquals(errorMessage, errorState.errorMessage)
    }

    @Test
    fun `test delete operation removes item from UI state when in Success state`() = runTest {
        // Given: Initial state is Success with some todos
        val mockTodos = listOf(
            NetworkToDo(id = 1, todo = "Todo 1", completed = false, userId = 13),
            NetworkToDo(id = 2, todo = "Todo 2", completed = false, userId = 13),
            NetworkToDo(id = 3, todo = "Todo 3", completed = false, userId = 13)
        )
        val mockResponse = NetworkToDoResponse(
            todos = mockTodos,
            total = 3,
            skip = 0,
            limit = 10
        )
        val successResult = ApiResult.Success(mockResponse)
        val successFlow = MutableStateFlow(successResult)

        whenever(mockToDoRepository.getToDos()).thenReturn(successFlow)
        whenever(mockToDoRepository.deleteToDo("2")).thenReturn(
            ApiResult.Success(NetworkToDo(id = 2, todo = "Todo 2", completed = false, userId = 13))
        )

        // When: HomeViewModel is initialized and delete is called
        homeViewModel = HomeViewModel(mockToDoRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        homeViewModel.deleteTodoItem("2")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: The deleted item should be removed from the UI state
        val currentState = homeViewModel.homeUiState.first()
        assertTrue(currentState is HomeUiState.Success)

        val successState = currentState as HomeUiState.Success
        assertEquals(2, successState.homeData.data.size)
        assertEquals("Todo 1", successState.homeData.data[0].todo)
        assertEquals("Todo 3", successState.homeData.data[1].todo)
        // Todo 2 should be removed
        assertFalse(successState.homeData.data.any { it.id == 2 })
    }

    @Test
    fun `test delete operation does not affect UI state when in non-Success state`() = runTest {
        // Given: Initial state is Error
        val errorResult = ApiResult.Error(Exception("Network error"))
        val errorFlow = MutableStateFlow(errorResult)

        whenever(mockToDoRepository.getToDos()).thenReturn(errorFlow)
        whenever(mockToDoRepository.deleteToDo("1")).thenReturn(
            ApiResult.Success(NetworkToDo(id = 1, todo = "Todo", completed = false, userId = 13))
        )

        // When: HomeViewModel is initialized and delete is called
        homeViewModel = HomeViewModel(mockToDoRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        homeViewModel.deleteTodoItem("1")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: State should remain Error (not affected by delete)
        val currentState = homeViewModel.homeUiState.first()
        assertTrue(currentState is HomeUiState.Error)
    }

    @Test
    fun `test repository is called with correct parameters`() = runTest {
        // Given: Mock repository setup
        val successFlow =
            MutableStateFlow(ApiResult.Success(NetworkToDoResponse(emptyList(), 0, 0, 10)))
        whenever(mockToDoRepository.getToDos()).thenReturn(successFlow)
        whenever(mockToDoRepository.deleteToDo("123")).thenReturn(
            ApiResult.Success(NetworkToDo(id = 123, todo = "Test", completed = false, userId = 13))
        )

        // When: HomeViewModel is initialized and delete is called
        homeViewModel = HomeViewModel(mockToDoRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        homeViewModel.deleteTodoItem("123")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Repository methods should be called with correct parameters
        verify(mockToDoRepository).getToDos()
        verify(mockToDoRepository).deleteToDo("123")
    }

    @Test
    fun `test data mapping from NetworkToDoResponse to ToDo list`() = runTest {
        // Given: Mock repository returns response with todos
        val mockTodos = listOf(
            NetworkToDo(id = 1, todo = "First Todo", completed = false, userId = 13),
            NetworkToDo(id = 2, todo = "Second Todo", completed = true, userId = 13)
        )
        val mockResponse = NetworkToDoResponse(
            todos = mockTodos,
            total = 2,
            skip = 0,
            limit = 10
        )
        val successResult = ApiResult.Success(mockResponse)
        val successFlow = MutableStateFlow(successResult)

        whenever(mockToDoRepository.getToDos()).thenReturn(successFlow)

        // When: HomeViewModel is initialized
        homeViewModel = HomeViewModel(mockToDoRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: Data should be correctly mapped
        val currentState = homeViewModel.homeUiState.first()
        assertTrue(currentState is HomeUiState.Success)

        val successState = currentState as HomeUiState.Success
        val mappedTodos = successState.homeData.data

        assertEquals(2, mappedTodos.size)
        assertEquals(1, mappedTodos[0].id)
        assertEquals("First Todo", mappedTodos[0].todo)
        assertEquals(false, mappedTodos[0].completed)
        assertEquals(13, mappedTodos[0].userId)

        assertEquals(2, mappedTodos[1].id)
        assertEquals("Second Todo", mappedTodos[1].todo)
        assertEquals(true, mappedTodos[1].completed)
        assertEquals(13, mappedTodos[1].userId)
    }

    @Test
    fun `test empty todo list handling`() = runTest {
        // Given: Mock repository returns empty response
        val mockResponse = NetworkToDoResponse(
            todos = emptyList(),
            total = 0,
            skip = 0,
            limit = 10
        )
        val successResult = ApiResult.Success(mockResponse)
        val successFlow = MutableStateFlow(successResult)

        whenever(mockToDoRepository.getToDos()).thenReturn(successFlow)

        // When: HomeViewModel is initialized
        homeViewModel = HomeViewModel(mockToDoRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: State should be Success with empty list
        val currentState = homeViewModel.homeUiState.first()
        assertTrue(currentState is HomeUiState.Success)

        val successState = currentState as HomeUiState.Success
        assertTrue(successState.homeData.data.isEmpty())
    }
}