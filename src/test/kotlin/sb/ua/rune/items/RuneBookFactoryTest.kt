//package sb.ua.rune.items
//
//import be.seeseemelk.mockbukkit.MockBukkit
//import be.seeseemelk.mockbukkit.ServerMock
//import org.junit.jupiter.api.AfterEach
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import sb.ua.rune.RuneEnchantments
//import sb.ua.rune.items.RuneBookFactory
//import kotlin.test.assertTrue
//
//class RuneBookFactoryTest {
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
//    fun `createVeinSmeltBook should return enchanted book`() {
//        val book = RuneBookFactory.createVeinSmeltBook()
//        val meta = book.itemMeta
//
//        assertTrue(meta.hasDisplayName())
//        assertTrue(meta.lore()?.isNotEmpty() == true)
//        assertTrue(meta.enchants.isNotEmpty())
//    }
//}
