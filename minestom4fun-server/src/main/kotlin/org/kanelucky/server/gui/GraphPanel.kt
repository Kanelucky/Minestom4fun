/*
 * Copyright (c) 2019-2022 GeyserMC. http://geysermc.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author GeyserMC
 * @link https://github.com/GeyserMC/Geyser
 */

package org.kanelucky.server.gui

import java.awt.*
import javax.swing.JPanel

/**
 * Ported from AllayMC Dashboard (GeyserMC)
 * @author Kanelucky
 */
class GraphPanel : JPanel() {

    companion object {
        private const val PADDING = 10
        private const val LABEL_PADDING = 25
        private const val POINT_WIDTH = 4
        private const val NUMBER_Y_DIVISIONS = 20
        private const val MAX_SCORE_THRESHOLD = 100
        private const val MIN_SCORE_THRESHOLD = 0

        private val TEXT_COLOR = Color.WHITE
        private val BACKGROUND_COLOR = Color(40, 40, 40)
        private val GRID_COLOR = Color.GRAY
        private val GRAPH_STROKE: Stroke = BasicStroke(2f)
    }

    private val lineColor = Color(44, 102, 230, 200)
    private val pointColor = Color(44, 102, 230, 200)
    private val values = mutableListOf<Int>()
    private var minScore = MIN_SCORE_THRESHOLD
    private var maxScore = MAX_SCORE_THRESHOLD
    var xLabel = ""

    init {
        preferredSize = Dimension(700 - PADDING * 2, 300)
    }

    fun setValues(newValues: Collection<Int>) {
        values.clear()
        values.addAll(newValues)
        calculateExtremum()
        updateUI()
    }

    private fun calculateExtremum() {
        if (values.isEmpty()) return
        minScore = minOf(MIN_SCORE_THRESHOLD, values.min())
        maxScore = maxOf(MAX_SCORE_THRESHOLD, (values.max() * 1.2).toInt())
    }

    override fun paintComponent(graphics: Graphics) {
        super.paintComponent(graphics)
        val g = graphics as? Graphics2D ?: return

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val length = values.size
        val w = width
        val h = height
        val scoreRange = maxScore - minScore

        g.color = BACKGROUND_COLOR
        g.fillRect(PADDING + LABEL_PADDING, PADDING, w - 2 * PADDING - LABEL_PADDING, h - 2 * PADDING - LABEL_PADDING)

        val fontMetrics = g.fontMetrics
        val fontHeight = fontMetrics.height

        // Y axis grid
        for (i in 0..NUMBER_Y_DIVISIONS) {
            val x1 = PADDING + LABEL_PADDING
            val x2 = POINT_WIDTH + PADDING + LABEL_PADDING
            val y = h - (i * (h - PADDING * 2 - LABEL_PADDING) / NUMBER_Y_DIVISIONS + PADDING + LABEL_PADDING)
            if (length > 0) {
                g.color = GRID_COLOR
                g.drawLine(x1 + 1 + POINT_WIDTH, y, w - PADDING, y)
                g.color = TEXT_COLOR
                val tickValue = minScore + scoreRange * i / NUMBER_Y_DIVISIONS
                val yLabel = tickValue.toString()
                g.drawString(yLabel, x1 - fontMetrics.stringWidth(yLabel) - 5, y + fontHeight / 2 - 3)
            }
            g.color = TEXT_COLOR
            g.drawLine(x1, y, x2, y)
        }

        // X axis grid
        if (length > 1) {
            for (i in 0 until length) {
                val x = i * (w - PADDING * 2 - LABEL_PADDING) / (length - 1) + PADDING + LABEL_PADDING
                val y1 = h - PADDING - LABEL_PADDING
                val y2 = y1 - POINT_WIDTH
                if (i % (length / 20 + 1) == 0) {
                    g.color = GRID_COLOR
                    g.drawLine(x, y1 - 1 - POINT_WIDTH, x, PADDING)
                    g.color = Color.BLACK
                }
                g.drawLine(x, y1, x, y2)
            }
        }

        // Axes
        g.color = TEXT_COLOR
        g.drawLine(PADDING + LABEL_PADDING, h - PADDING - LABEL_PADDING, PADDING + LABEL_PADDING, PADDING)
        g.drawLine(PADDING + LABEL_PADDING, h - PADDING - LABEL_PADDING, w - PADDING, h - PADDING - LABEL_PADDING)

        val labelWidth = fontMetrics.stringWidth(xLabel)
        val labelX = (PADDING + LABEL_PADDING + w - PADDING) / 2
        val labelY = h - PADDING - LABEL_PADDING
        g.drawString(xLabel, labelX - labelWidth / 2, labelY + fontHeight + 3)

        if (length <= 1 || scoreRange == 0) return

        val xScale = (w - 2.0 * PADDING - LABEL_PADDING) / (length - 1)
        val yScale = (h - 2.0 * PADDING - LABEL_PADDING) / scoreRange

        val oldStroke = g.stroke
        g.color = lineColor
        g.stroke = GRAPH_STROKE

        val graphPoints = (0 until length).map { i ->
            Point(
                (i * xScale + PADDING + LABEL_PADDING).toInt(),
                ((maxScore - values[i]) * yScale + PADDING).toInt()
            )
        }

        for (i in 0 until graphPoints.size - 1) {
            g.drawLine(graphPoints[i].x, graphPoints[i].y, graphPoints[i + 1].x, graphPoints[i + 1].y)
        }

        g.stroke = oldStroke
        g.color = pointColor
        if (w > length * POINT_WIDTH) {
            for (p in graphPoints) {
                g.fillOval(p.x - POINT_WIDTH / 2, p.y - POINT_WIDTH / 2, POINT_WIDTH, POINT_WIDTH)
            }
        }
    }
}




































