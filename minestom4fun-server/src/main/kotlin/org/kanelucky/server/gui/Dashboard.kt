package org.kanelucky.server.gui

import com.formdev.flatlaf.themes.FlatMacDarkLaf
import net.minestom.server.MinecraftServer
import net.minestom.server.utils.time.Tick
import java.awt.*
import java.io.OutputStream
import java.io.PrintStream
import java.util.*
import javax.swing.*
import javax.swing.table.DefaultTableModel

/**
 * Ported from AllayMC Dashboard (GeyserMC)
 * @author Kanelucky
 */
class Dashboard private constructor() {

    companion object {
        private const val RAM_VALUE_COUNT = 100
        private const val MEGABYTE = 1024L * 1024L
        private var INSTANCE: Dashboard? = null

        fun getInstance(): Dashboard {
            if (INSTANCE == null) {
                FlatMacDarkLaf.setup()
                INSTANCE = Dashboard()
            }
            return INSTANCE!!
        }
    }

    private val frame = JFrame("Minestom4fun")
    private val consolePane = ConsolePanel()
    private val cmdInput = JTextField()
    private val ramGraph = GraphPanel()
    private val tpsGraph = GraphPanel()
    private val playerTable = JTable()
    private val ramValues = ArrayDeque(Collections.nCopies(RAM_VALUE_COUNT, 0))
    private val tpsValues = ArrayDeque(Collections.nCopies(RAM_VALUE_COUNT, 0))

    init {
        setupUI()
        wrapSystemOutputStreams()
    }

    private fun setupUI() {
        frame.defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        frame.addWindowListener(object : java.awt.event.WindowAdapter() {
            override fun windowClosing(e: java.awt.event.WindowEvent) {
                MinecraftServer.stopCleanly()
            }
        })
        frame.setSize(800, 600)
        frame.setLocationRelativeTo(null)

        // Icon
        val icon = Dashboard::class.java.classLoader.getResource("icon.png")
        if (icon != null) frame.iconImage = ImageIcon(icon).image

        val tabbedPane = JTabbedPane()
        tabbedPane.addTab("Console", createConsoleTab())
        tabbedPane.addTab("Performance", createPerformanceTab())
        tabbedPane.addTab("Players", createPlayersTab())

        frame.contentPane = tabbedPane
        frame.isVisible = true
    }

    private fun createConsoleTab(): JPanel {
        val panel = JPanel(BorderLayout(0, 4))
        panel.border = BorderFactory.createEmptyBorder(4, 4, 4, 4)

        consolePane.background = Color(0x131313)
        consolePane.isEditable = false
        consolePane.font = Font(Font.MONOSPACED, Font.PLAIN, 12)

        val scrollPane = JScrollPane(consolePane)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS

        cmdInput.isEnabled = false
        cmdInput.addActionListener {
            val cmd = cmdInput.text.trim()
            cmdInput.text = ""
            if (cmd.isEmpty()) return@addActionListener
            appendTextToConsole("> $cmd\n")
            MinecraftServer.getCommandManager().executeServerCommand(cmd)
        }

        panel.add(scrollPane, BorderLayout.CENTER)
        panel.add(cmdInput, BorderLayout.SOUTH)
        return panel
    }

    private fun createPerformanceTab(): JPanel {
        val panel = JPanel(BorderLayout())
        val innerTabs = JTabbedPane()

        // RAM graph
        val ramPanel = JPanel(BorderLayout())
        ramGraph.setValues(ramValues)
        ramPanel.add(ramGraph, BorderLayout.CENTER)
        innerTabs.addTab("Memory", ramPanel)

        // TPS graph
        val tpsPanel = JPanel(BorderLayout())
        tpsGraph.setValues(tpsValues)
        tpsPanel.add(tpsGraph, BorderLayout.CENTER)
        innerTabs.addTab("TPS", tpsPanel)

        panel.add(innerTabs, BorderLayout.CENTER)
        return panel
    }

    private fun createPlayersTab(): JPanel {
        val panel = JPanel(BorderLayout())
        val countLabel = JLabel("Online: 0")
        countLabel.border = BorderFactory.createEmptyBorder(4, 8, 4, 8)

        playerTable.autoCreateRowSorter = true
        val scrollPane = JScrollPane(playerTable)

        panel.add(countLabel, BorderLayout.NORTH)
        panel.add(scrollPane, BorderLayout.CENTER)
        return panel
    }

    private fun wrapSystemOutputStreams() {
        val originalOut = System.out
        val proxy = object : OutputStream() {
            override fun write(b: Int) = write(byteArrayOf(b.toByte()), 0, 1)
            override fun write(b: ByteArray, off: Int, len: Int) {
                originalOut.write(b, off, len)
                appendTextToConsole(String(b, off, len))
            }
        }
        System.setOut(PrintStream(proxy, true))
        System.setErr(PrintStream(proxy, true))
    }

    fun afterServerStarted() {
        cmdInput.isEnabled = true

        MinecraftServer.getSchedulerManager().buildTask {
            SwingUtilities.invokeLater {
                updateRamGraph()
                updateTpsGraph()
                updatePlayerTable()
            }
        }.repeat(java.time.Duration.ofSeconds(1)).schedule()
    }

    private fun updateRamGraph() {
        val runtime = Runtime.getRuntime()
        val used = runtime.totalMemory() - runtime.freeMemory()
        val total = runtime.maxMemory()
        val percent = (used * 100 / total).toInt()

        if (ramValues.size >= RAM_VALUE_COUNT) ramValues.poll()
        ramValues.add(percent)
        ramGraph.xLabel = "${used / MEGABYTE}MB / ${total / MEGABYTE}MB"
        ramGraph.setValues(ramValues)
    }

    private fun updateTpsGraph() {
        val tps = Tick.SERVER_TICKS.ticksPerSecond.toInt()
        if (tpsValues.size >= RAM_VALUE_COUNT) tpsValues.poll()
        tpsValues.add(tps)
        tpsGraph.xLabel = "TPS: $tps"
        tpsGraph.setValues(tpsValues)
    }

    private fun updatePlayerTable() {
        val players = MinecraftServer.getConnectionManager().onlinePlayers
        val data = players.map { arrayOf(it.username, it.uuid.toString()) }.toTypedArray()
        val model = object : DefaultTableModel(data, arrayOf("Name", "UUID")) {
            override fun isCellEditable(row: Int, column: Int) = false
        }
        playerTable.model = model
        playerTable.tableHeader.reorderingAllowed = false
    }

    fun appendTextToConsole(text: String) {
        SwingUtilities.invokeLater {
            consolePane.appendText(text)
        }
    }
}