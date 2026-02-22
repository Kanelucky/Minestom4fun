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
import java.util.regex.Pattern

/**
 * Ported from AllayMC Dashboard (GeyserMC)
 * @author Kanelucky
 */
enum class ANSIColor(val ansiCode: String, val color: Color) {
    BLACK("(0;)?30(0;)?m", Color.BLACK),
    RED("(0;)?31(0;)?m", Color(0xfff0524f.toInt())),
    GREEN("(0;)?32(0;)?m", Color(0xff5c962c.toInt())),
    YELLOW("(0;)?33(0;)?m", Color(0xffa68a0d.toInt())),
    BLUE("(0;)?34(0;)?m", Color(0xff6cb6ff.toInt())),
    MAGENTA("(0;)?35(0;)?m", Color(0xffa771bf.toInt())),
    CYAN("(0;)?36(0;)?m", Color(0xff96d0ff.toInt())),
    WHITE("(0;)?37(0;)?m", Color(0xffbcbec4.toInt())),
    B_BLACK("(0;)?(1;30|30;1)m", Color.BLACK),
    B_RED("(0;)?(1;31|31;1)m", Color(0xfff0524f.toInt())),
    B_GREEN("(0;)?(1;32|32;1)m", Color(0xff5c962c.toInt())),
    B_YELLOW("(0;)?(1;33|33;1)m", Color(0xffa68a0d.toInt())),
    B_BLUE("(0;)?(1;34|34;1)m", Color(0xff3993d4.toInt())),
    B_MAGENTA("(0;)?(1;35|35;1)m", Color(0xffa771bf.toInt())),
    B_CYAN("(0;)?(1;36|36;1)m", Color(0xff00a3a3.toInt())),
    B_WHITE("(0;)?(1;37|37;1)m", Color(0xff808080.toInt())),
    RESET("0m", Color(0xffbcbec4.toInt()));

    companion object {
        private val PREFIX = Pattern.quote("\u001B[")
        private val PATTERN_MAP = entries.associateWith { Pattern.compile(PREFIX + it.ansiCode) }

        fun fromANSI(code: String): ANSIColor =
            entries.firstOrNull { PATTERN_MAP[it]?.matcher(code)?.matches() == true } ?: RESET

        fun isBoldColor(color: Color): Boolean =
            color == B_BLACK.color || color == B_RED.color || color == B_GREEN.color ||
                    color == B_YELLOW.color || color == B_BLUE.color || color == B_MAGENTA.color ||
                    color == B_CYAN.color || color == B_WHITE.color
    }
}