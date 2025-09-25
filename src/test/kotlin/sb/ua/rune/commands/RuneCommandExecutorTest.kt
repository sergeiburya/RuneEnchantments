//package sb.ua.rune.commands
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
//class RuneCommandExecutorTest {
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
//    fun `player receives book on rune give`() {
//        val player = server.addPlayer("TestPlayer")
//        val executor = RuneCommandExecutor()
//
//        val result = executor.onCommand(
//            player,
//            server.commandMap.getCommand("rune")!!,
//            "rune",
//            arrayOf("give", "TestPlayer", "veinsmelt")
//        )
//
//        assertTrue(result)
//        assertTrue(player.inventory.containsAtLeast(RuneBookFactory.createVeinSmeltBook(), 1))
//    }
//}