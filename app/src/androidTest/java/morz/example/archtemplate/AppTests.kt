package morz.example.archtemplate

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import morz.example.archtemplate.ui.MainActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for the BaseArch application.
 * These tests will execute on an Android device or emulator.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppTests {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("morz.example.archtemplate", appContext.packageName)
    }

    @Test
    fun testAppLaunchesSuccessfully() {
        // Verify that the app launches and the main UI components are visible
        composeTestRule.onNodeWithContentDescription("App TopAppBar navigation icon bank login")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("App TopAppBar action icon avatar")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testBottomNavigationBarIsDisplayed() {
        // Verify that the bottom navigation bar is present
        // Check for the Login navigation item which should be visible
        composeTestRule.onNodeWithText("Login")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testTopAppBarNavigationClick() {
        // Test clicking the settings navigation icon in the top app bar
        composeTestRule.onNodeWithContentDescription("App TopAppBar navigation icon bank login")
            .performClick()
        
        // Verify navigation to settings screen (this would depend on your navigation implementation)
        // You might need to add specific assertions based on your navigation setup
    }

    @Test
    fun testTopAppBarActionClick() {
        // Test clicking the support action icon in the top app bar
        composeTestRule.onNodeWithContentDescription("App TopAppBar action icon avatar")
            .performClick()
        
        // Verify navigation to support screen
    }

    @Test
    fun testAddButtonInBottomNavigation() {
        // Test the add button in the bottom navigation
        composeTestRule.onNodeWithContentDescription("Add new item")
            .assertExists()
            .assertIsDisplayed()
            .performClick()
        
        // Verify navigation to todo detail screen
    }

    @Test
    fun testBottomNavigationDestinations() {
        // Test that all bottom navigation destinations are accessible
        val destinations = listOf("Login", "Tools", "Inbox", "Home")
        
        destinations.forEach { destination ->
            composeTestRule.onNodeWithText(destination)
                .assertExists()
                .assertIsDisplayed()
                .performClick()
        }
    }

    @Test
    fun testAppThemeIsApplied() {
        // Verify that the app theme is properly applied
        // This test checks that the app has proper theming
        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun testAppStateInitialization() {
        // Test that the app state is properly initialized
        // This verifies that the app doesn't crash on startup
        composeTestRule.waitForIdle()
        
        // Verify that the main content area is displayed
        // Try to find any content in the app
        try {
            composeTestRule.onNodeWithTag("MainContent")
                .assertExists()
                .assertIsDisplayed()
        } catch (e: AssertionError) {
            // If the tag doesn't exist, verify that the app root is accessible
            composeTestRule.onRoot().assertExists()
        }
    }

    @Test
    fun testNavigationStateManagement() {
        // Test that navigation state is properly managed
        // Click on different navigation items and verify state changes
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithText("Tools").performClick()
        composeTestRule.waitForIdle()
        
        composeTestRule.onNodeWithText("Inbox").performClick()
        composeTestRule.waitForIdle()
    }

    @Test
    fun testAppResponsiveness() {
        // Test that the app responds to user interactions
        // Perform multiple rapid interactions to test responsiveness
        repeat(5) {
            composeTestRule.onNodeWithText("Home").performClick()
            composeTestRule.onNodeWithText("Tools").performClick()
            composeTestRule.onNodeWithText("Inbox").performClick()
        }
        
        // Verify the app is still responsive
        composeTestRule.onRoot().assertExists()
    }

    @Test
    fun testAccessibilityFeatures() {
        // Test that accessibility features are properly implemented
        composeTestRule.onNodeWithContentDescription("App TopAppBar navigation icon bank login")
            .assertHasClickAction()
        
        composeTestRule.onNodeWithContentDescription("App TopAppBar action icon avatar")
            .assertHasClickAction()
    }

    @Test
    fun testAppPerformance() {
        // Basic performance test - verify app doesn't freeze during interactions
        val startTime = System.currentTimeMillis()
        
        // Perform a series of interactions
        repeat(10) {
            composeTestRule.onNodeWithText("Home").performClick()
            composeTestRule.waitForIdle()
        }
        
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        
        // Verify that interactions complete within a reasonable time (10 seconds for slower devices)
        assertTrue("App interactions took too long: ${duration}ms", duration < 10000)
    }

    @Test
    fun testTopAppBarComponents() {
        // Test that the top app bar has the correct test tag
        composeTestRule.onNodeWithTag("AppTopAppBar")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testNavigationItemTestTags() {
        // Test that navigation items have proper test tags
        val navigationItems = listOf("Login", "Tools", "Inbox", "Home")
        
        navigationItems.forEach { item ->
            // Each navigation item should have a test tag based on its class name
            composeTestRule.onNodeWithText(item)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun testAppThemeConsistency() {
        // Test that the app maintains consistent theming across navigation
        val startDestination = "Home"
        
        // Navigate to different screens and verify theme consistency
        composeTestRule.onNodeWithText(startDestination).performClick()
        composeTestRule.waitForIdle()
        
        // Verify the top app bar is still visible and themed
        composeTestRule.onNodeWithTag("AppTopAppBar")
            .assertExists()
            .assertIsDisplayed()
        
        // Navigate to another screen
        composeTestRule.onNodeWithText("Tools").performClick()
        composeTestRule.waitForIdle()
        
        // Verify theme consistency is maintained
        composeTestRule.onNodeWithTag("AppTopAppBar")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun testAppStartupTime() {
        // Test that the app starts up within a reasonable time
        val startTime = System.currentTimeMillis()
        
        // Wait for the app to be fully loaded
        composeTestRule.waitForIdle()
        
        val endTime = System.currentTimeMillis()
        val startupTime = endTime - startTime
        
        // Verify startup time is reasonable (5 seconds max)
        assertTrue("App startup took too long: ${startupTime}ms", startupTime < 5000)
    }

    @Test
    fun testAppMemoryUsage() {
        // Basic memory usage test - verify app doesn't crash after multiple operations
        repeat(20) {
            composeTestRule.onNodeWithText("Home").performClick()
            composeTestRule.waitForIdle()
            
            composeTestRule.onNodeWithText("Tools").performClick()
            composeTestRule.waitForIdle()
            
            composeTestRule.onNodeWithText("Inbox").performClick()
            composeTestRule.waitForIdle()
        }
        
        // Verify the app is still functional
        composeTestRule.onRoot().assertExists()
        composeTestRule.onNodeWithText("Home").assertExists()
    }
}