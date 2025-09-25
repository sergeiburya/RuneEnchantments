//package sb.ua.rune
//
//import be.seeseemelk.mockbukkit.MockBukkit
//import be.seeseemelk.mockbukkit.ServerMock
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import kotlin.test.assertNotNull
//import kotlin.test.assertTrue
//
//class RuneEnchantmentsTest {
//    private lateinit var server: ServerMock
//    private lateinit var plugin: RuneEnchantments
//
//    @BeforeEach
//    fun setUp() {
//        server = MockBukkit.mock()
//        plugin = MockBukkit.load(RuneEnchantments::class.java)
//    }
//
//    @AfterEach
//    fun tearDown() {
//        MockBukkit.unmock()
//    }
//
//    @Test
//    fun `plugin should enable correctly`() {
//        assertTrue(plugin.isEnabled)
//    }
//
//    @Test
//    fun `config should be loaded`() {
//        assertNotNull(plugin.config)
//        assertTrue(plugin.config.contains("veinsmelt.autoSmeltEnabled"))
//    }
//}