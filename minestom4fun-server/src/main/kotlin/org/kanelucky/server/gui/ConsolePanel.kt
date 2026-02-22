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

import java.awt.Color
import javax.swing.JTextPane
import javax.swing.SwingUtilities
import javax.swing.text.BadLocationException
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

/**
 * Ported from AllayMC Dashboard (GeyserMC)
 * @author Kanelucky
 */
class ConsolePanel : JTextPane() {

    companion object {
        private const val MAX_DOCUMENT_CHARS = 200_000
    }

    private var colorCurrent: Color = ANSIColor.RESET.color
    private var remaining = ""

    private fun append(color: Color, text: String) {
        val attribute = SimpleAttributeSet()
        StyleConstants.setForeground(attribute, color)
        StyleConstants.setBold(attribute, ANSIColor.isBoldColor(color))

        val len = document.length

        if (text.contains("\r")) {
            if (text.contains("\n")) {
                try {
                    document.insertString(len, text, attribute)
                    trimDocument()
                } catch (e: BadLocationException) {
                    e.printStackTrace()
                }
                return
            }
            try {
                document.remove(len - text.length, text.length)
                document.insertString(len - text.length, text, attribute)
                trimDocument()
            } catch (e: BadLocationException) {
                e.printStackTrace()
            }
            return
        }

        try {
            document.insertString(len, text, attribute)
            trimDocument()
        } catch (e: BadLocationException) {
            e.printStackTrace()
        }
    }

    private fun trimDocument() {
        val excess = document.length - MAX_DOCUMENT_CHARS
        if (excess <= 0) return
        try {
            document.remove(0, excess)
        } catch (e: BadLocationException) {
            e.printStackTrace()
        }
    }

    fun appendANSI(text: String) {
        var aPos = 0
        var stillSearching = true
        val addString = remaining + text
        remaining = ""

        if (addString.isEmpty()) return

        var aIndex = addString.indexOf("\u001B")
        if (aIndex == -1) {
            append(colorCurrent, addString)
            return
        }

        if (aIndex > 0) {
            append(colorCurrent, addString.substring(0, aIndex))
            aPos = aIndex
        }

        while (stillSearching) {
            val mIndex = addString.indexOf("m", aPos)
            if (mIndex < 0) {
                remaining = addString.substring(aPos)
                stillSearching = false
                continue
            }

            val tmpString = addString.substring(aPos, mIndex + 1)
            colorCurrent = ANSIColor.fromANSI(tmpString).color
            aPos = mIndex + 1

            aIndex = addString.indexOf("\u001B", aPos)
            if (aIndex == -1) {
                append(colorCurrent, addString.substring(aPos))
                stillSearching = false
                continue
            }

            append(colorCurrent, addString.substring(aPos, aIndex))
            aPos = aIndex
        }
    }

    fun appendText(text: String) {
        SwingUtilities.invokeLater {
            appendANSI(text)
            caretPosition = document.length
        }
    }
}