package org.vyperlang.plugin

import com.intellij.lexer.FlexAdapter
import org.vyperlang.plugin.grammar._VyperLexer
import java.io.Reader

class VyperLexerAdapter : FlexAdapter(_VyperLexer(null as Reader?))
